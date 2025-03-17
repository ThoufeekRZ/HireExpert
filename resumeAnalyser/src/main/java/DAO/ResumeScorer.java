package DAO;

import DTO.Experience;
import DTO.Recruit;
import DTO.Resume;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResumeScorer {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static List<Resume> ScoreResume(int recruitId) throws SQLException, IOException {
        try (Connection connection = DBConnection.getConnection()) {

            // Collect resume IDs first to avoid ResultSet closure issues
            List<Integer> resumeIds = new ArrayList<>();
            String getAllResumeQuery = "SELECT resume.resumeId FROM resume " +
                    "JOIN recr_resu_relation ON resume.resumeId = recr_resu_relation.resumeId " +
                    "WHERE recr_resu_relation.recruitId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(getAllResumeQuery)) {
                preparedStatement.setInt(1, recruitId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    resumeIds.add(resultSet.getInt("resumeId"));
                }
            }

            // Fetch the recruit once
            Recruit recruit = RecruitDAO.getRecruitById(recruitId);
            if (recruit == null) {
                return new ArrayList<>();
            }

            // Fetch resume objects by IDs
            List<Resume> resumes = new ArrayList<>();
            for (int resumeId : resumeIds) {
                Resume resume = ResumeDAO.getResumeById(resumeId, recruitId);
                if (resume != null) {
                    resumes.add(resume);
                }
            }

            // Validate resumes (Experience and Education)
            resumes = ResumeValidation.experienceYearValidation(resumes, recruit);
            resumes = ResumeValidation.educationVerification(resumes, recruit);

            System.out.println("After validation: " + resumes);

            // Process scoring in parallel using CompletableFuture
            List<CompletableFuture<Resume>> futures = resumes.stream()
                    .map(resume -> CompletableFuture.supplyAsync(() -> processScoring(resume, recruit), executor))
                    .toList();

            // Wait for all scoring tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // Collect results and handle potential failures
            List<Resume> finalResumes = futures.stream()
                    .map(future -> {
                        try {
                            return future.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null; // Handle errors without breaking flow
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values
                    .collect(Collectors.toCollection(ArrayList::new));

            System.out.println("Processed resumes count: " + finalResumes.size());

            // Update scores in the database
            for (Resume resume : finalResumes) {
                ResumeDAO.updateResumeScore(resume.getResumeId(), resume.getResumeScore(), recruitId);
            }

            // Sort resumes by score (descending)
            finalResumes.sort(Comparator.comparing(Resume::getResumeScore).reversed());

            // Limit the number of resumes if necessary
            if (recruit.getMaximumNumber() < finalResumes.size()) {
                finalResumes = finalResumes.subList(0, recruit.getMaximumNumber());
            }

            return finalResumes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ensure errors are propagated
        }
    }


    private static Resume processScoring(Resume resume, Recruit recruit) {
        try {
            double score = calculateScore(resume, recruit);
            resume.setResumeScore(score);
            System.out.println("Scored Resume: " + resume.getResumeId() + ", Score: " + score);
            return resume;
        } catch (Exception e) {
            e.printStackTrace();
            return resume; // Return partially scored resume if error occurs
        }
    }

    private static double calculateScore(Resume resume, Recruit recruit) throws Exception {
        double score = 0;

        // LinkedIn score
        if (resume.getContact().getLinkedInId() != null
                && ResumeValidation.isValidLinkedInUrl(resume.getContact().getLinkedInId())) {
            score += 10;
        }

        // Skill matching
        score = SkillMatcher.raiseScoreBySkills(resume.getSkills(), recruit.getRequirement().getSkills(), score);

        // Experience and description analysis
        int totalExperience = DescriptionMatcher.getExperienceYears(resume.getExperiences());
        for (Experience experience : resume.getExperiences()) {
            score += DescriptionMatcher.scoreTitleDescriptionAndExperience(
                    experience.getTitle(),
                    recruit.getRecruitName(),
                    experience.getResponsibilities(),
                    recruit.getDescription(),
                    recruit.getRequirement().getExperienceYear(),
                    totalExperience);
        }

        // Python model analysis (summary and achievements)
        score += Python_model_analyser.analyseSummary(resume.getDescription());

        for (Experience experience : resume.getExperiences()) {
            score += Python_model_analyser.analyseFalseAchievement(experience.getResponsibilities());
        }

        // Ensure score is between 0 and 100
        return Math.max(((score/140) * 100),0);
    }

    public static void shutdownExecutor() {
        executor.shutdown();
    }
}
