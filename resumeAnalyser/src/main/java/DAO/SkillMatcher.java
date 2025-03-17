package DAO;

import DTO.Skill;

import java.util.*;
import java.util.stream.Collectors;

public class SkillMatcher {
    private static final Map<String, List<String>> isRelatedSkills = new HashMap<>();

    static {
        // 🔹 Programming Languages
        isRelatedSkills.put("JavaScript", Arrays.asList("TypeScript", "Node.js", "React.js", "Vue.js"));
        isRelatedSkills.put("Java", Arrays.asList("Spring Boot", "Hibernate", "Kotlin", "Scala"));
        isRelatedSkills.put("Python", Arrays.asList("Django", "Flask", "Machine Learning", "Data Science"));
        isRelatedSkills.put("C#", Arrays.asList(".NET", "ASP.NET", "Unity"));
        isRelatedSkills.put("C++", Arrays.asList("C", "Game Development", "Unreal Engine"));
        isRelatedSkills.put("Swift", Arrays.asList("iOS Development", "Objective-C"));
        isRelatedSkills.put("Kotlin", Arrays.asList("Android Development", "Java"));
        isRelatedSkills.put("PHP", Arrays.asList("Laravel", "CodeIgniter", "WordPress"));
        isRelatedSkills.put("Ruby", Arrays.asList("Ruby on Rails", "Sinatra"));
        isRelatedSkills.put("Go", Arrays.asList("Golang", "Microservices", "Cloud Computing"));
        isRelatedSkills.put("Rust", Arrays.asList("Systems Programming", "Blockchain"));

        // 🔹 Web Development
        isRelatedSkills.put("React.js", Arrays.asList("Next.js", "Redux", "React Native"));
        isRelatedSkills.put("Vue.js", Arrays.asList("Nuxt.js", "Quasar"));
        isRelatedSkills.put("Angular", Arrays.asList("TypeScript", "RxJS"));
        isRelatedSkills.put("Node.js", Arrays.asList("Express.js", "NestJS", "GraphQL"));
        isRelatedSkills.put("Django", Arrays.asList("Flask", "FastAPI", "REST API"));
        isRelatedSkills.put("Bootstrap", Arrays.asList("Tailwind CSS", "Material UI"));

        // 🔹 Databases
        isRelatedSkills.put("MySQL", Arrays.asList("MariaDB", "PostgreSQL"));
        isRelatedSkills.put("PostgreSQL", Arrays.asList("MySQL", "SQL"));
        isRelatedSkills.put("MongoDB", Arrays.asList("NoSQL", "Firebase", "Cassandra"));
        isRelatedSkills.put("Redis", Arrays.asList("Memcached", "Caching"));
        isRelatedSkills.put("Oracle DB", Arrays.asList("SQL", "PL/SQL"));

        // 🔹 Cloud & DevOps
        isRelatedSkills.put("AWS", Arrays.asList("Azure", "Google Cloud Platform (GCP)"));
        isRelatedSkills.put("Docker", Arrays.asList("Kubernetes", "Containerization"));
        isRelatedSkills.put("Terraform", Arrays.asList("Infrastructure as Code (IaC)", "AWS CloudFormation"));
        isRelatedSkills.put("Jenkins", Arrays.asList("CI/CD", "GitHub Actions"));

        // 🔹 Cybersecurity
        isRelatedSkills.put("Ethical Hacking", Arrays.asList("Penetration Testing", "Bug Bounty"));
        isRelatedSkills.put("Network Security", Arrays.asList("Firewall", "IDS/IPS"));
        isRelatedSkills.put("Cryptography", Arrays.asList("Blockchain", "Encryption"));

        // 🔹 AI & Data Science
        isRelatedSkills.put("Machine Learning", Arrays.asList("Deep Learning", "Neural Networks"));
        isRelatedSkills.put("Deep Learning", Arrays.asList("TensorFlow", "PyTorch", "Keras"));
        isRelatedSkills.put("Data Science", Arrays.asList("Pandas", "NumPy", "Matplotlib"));
        isRelatedSkills.put("Big Data", Arrays.asList("Apache Spark", "Hadoop", "Kafka"));
        isRelatedSkills.put("NLP (Natural Language Processing)", Arrays.asList("OpenAI", "Chatbots", "Text Analysis"));

        // 🔹 Testing & Automation
        isRelatedSkills.put("Selenium", Arrays.asList("Test Automation", "Playwright"));
        isRelatedSkills.put("JUnit", Arrays.asList("TestNG", "Unit Testing"));

        // 🔹 Miscellaneous
        isRelatedSkills.put("Blockchain", Arrays.asList("Smart Contracts", "Solidity"));
        isRelatedSkills.put("Game Development", Arrays.asList("Unity", "Unreal Engine", "C#"));
        isRelatedSkills.put("UI/UX", Arrays.asList("Figma", "Adobe XD", "Sketch"));
        isRelatedSkills.put("Project Management", Arrays.asList("Scrum", "Agile"));
    }

    public static double raiseScoreBySkills(List<Skill> candidateSkills, List<Skill> requiredSkills, double score) {
        Set<String> requiredSkillsSet = requiredSkills.stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toSet());

        Set<String> candidateSkillsSet = candidateSkills.stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toSet());

        double skillScore = 0;
        Set<String> matchedSkills = new HashSet<>();


        for (String skill : candidateSkillsSet) {
            if (requiredSkillsSet.contains(skill)) {
                skillScore += 5;
                matchedSkills.add(skill);
            }
        }


        for (String skill : candidateSkillsSet) {
            List<String> relatedSkills = isRelatedSkills.getOrDefault(skill, Collections.emptyList());

            for (String relatedSkill : relatedSkills) {
                if (requiredSkillsSet.contains(relatedSkill) && !matchedSkills.contains(relatedSkill)) {
                    skillScore += 3;
                    matchedSkills.add(relatedSkill);
                    break;
                }
            }
        }


        score += Math.min(skillScore, 40);

        return score;
    }


}
