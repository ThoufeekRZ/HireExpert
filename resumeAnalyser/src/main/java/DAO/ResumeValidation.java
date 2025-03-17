package DAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;

import DTO.Recruit;
import DTO.Requirement;
//import DTO.Connect;
//import DTO.Recruit;
//import DTO.Requirement;
import DTO.Resume;
import DTO.Skill;

//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

public class ResumeValidation {



	
	public static boolean phoneNumberValidate(String phoneNumber) {
        String regex = "^[6-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
	}



	public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidLinkedInUrl(String url) {
        String linkedInUrlPattern = "^https?:\\/\\/(www\\.)?linkedin\\.com\\/in\\/[a-zA-Z0-9-]+$";
        String linkedInUsernamePattern = "^(?!-)[a-zA-Z0-9-]{3,100}(?<!-)$";
        return url.matches(linkedInUrlPattern) || url.matches(linkedInUsernamePattern);
    }
    
    public static boolean  isValidgithubId(String githubUrl) {
        String githubUrlPattern = "^https?:\\/\\/github\\.com\\/[a-zA-Z0-9-]{1,39}$";
        String githubUsernamePattern = "^(?!-)[a-zA-Z0-9-]{1,39}(?<!-)$";
        return githubUrl.matches(githubUrlPattern) || githubUrl.matches(githubUsernamePattern);
        
    }

    public static List<Resume> experienceYearValidation(List<Resume> list, Recruit recruit) {
        int jobRequiredYears = recruit.getRequirement().getExperienceYear();
        return list.stream()
                .filter(resume -> DescriptionMatcher.getExperienceYears(resume.getExperiences()) >= jobRequiredYears)
                .collect(Collectors.toList());
    }

    public static List<Resume> educationVerification(List<Resume> list, Recruit recruit) {
        String jobRequiredQualification = recruit.getRequirement().getQualification();
        Set<String> qualifications = new HashSet<>(Arrays.asList("HSC", "UG", "PG"));

        return list.stream()
                .filter(resume -> {
                    String candidateQualification = resume.getEducation();
                    if (candidateQualification == null) return false;

                    switch (jobRequiredQualification) {
                        case "HSC":
                            return qualifications.contains(candidateQualification);
                        case "UG":
                            return candidateQualification.equals("UG") || candidateQualification.equals("PG");
                        case "PG":
                            return candidateQualification.equals("PG");
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

}
