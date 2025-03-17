import { useEffect, useState } from "react";
import Select from "react-select";
import "./RecruitmentForm.css";
import axios from "axios";

const skillOptions = [
  { "value": "java", "label": "Java" },
  { "value": "python", "label": "Python" },
  { "value": "javascript", "label": "JavaScript" },
  { "value": "c++", "label": "C++" },
  { "value": "c#", "label": "C#" },
  { "value": "php", "label": "PHP" },
  { "value": "swift", "label": "Swift" },
  { "value": "kotlin", "label": "Kotlin" },
  { "value": "typescript", "label": "TypeScript" },
  { "value": "ruby", "label": "Ruby" },
  { "value": "html", "label": "HTML" },
  { "value": "css", "label": "CSS" },
  { "value": "react", "label": "React.js" },
  { "value": "angular", "label": "Angular" },
  { "value": "vue", "label": "Vue.js" },
  { "value": "node.js", "label": "Node.js" },
  { "value": "express", "label": "Express.js" },
  { "value": "nextjs", "label": "Next.js" },
  { "value": "tailwind", "label": "Tailwind CSS" },
  { "value": "bootstrap", "label": "Bootstrap" },
  { "value": "mysql", "label": "MySQL" },
  { "value": "postgresql", "label": "PostgreSQL" },
  { "value": "mongodb", "label": "MongoDB" },
  { "value": "sqlite", "label": "SQLite" },
  { "value": "oracle", "label": "Oracle DB" },
  { "value": "redis", "label": "Redis" },
  { "value": "firebase", "label": "Firebase" },
  { "value": "cassandra", "label": "Cassandra" },
  { "value": "nosql", "label": "NoSQL" },
  { "value": "dynamodb", "label": "DynamoDB" },
  { "value": "aws", "label": "AWS" },
  { "value": "azure", "label": "Azure" },
  { "value": "gcp", "label": "Google Cloud Platform (GCP)" },
  { "value": "docker", "label": "Docker" },
  { "value": "kubernetes", "label": "Kubernetes" },
  { "value": "terraform", "label": "Terraform" },
  { "value": "ci_cd", "label": "CI/CD" },
  { "value": "jenkins", "label": "Jenkins" },
  { "value": "ansible", "label": "Ansible" },
  { "value": "openshift", "label": "OpenShift" },
  { "value": "machine_learning", "label": "Machine Learning" },
  { "value": "deep_learning", "label": "Deep Learning" },
  { "value": "ai", "label": "Artificial Intelligence" },
  { "value": "tensorflow", "label": "TensorFlow" },
  { "value": "pytorch", "label": "PyTorch" },
  { "value": "data_analysis", "label": "Data Analysis" },
  { "value": "nlp", "label": "NLP (Natural Language Processing)" },
  { "value": "opencv", "label": "OpenCV" },
  { "value": "computer_vision", "label": "Computer Vision" },
  { "value": "big_data", "label": "Big Data" },
  { "value": "ethical_hacking", "label": "Ethical Hacking" },
  { "value": "pen_testing", "label": "Penetration Testing" },
  { "value": "network_security", "label": "Network Security" },
  { "value": "cryptography", "label": "Cryptography" },
  { "value": "siem", "label": "SIEM (Security Information and Event Management)" },
  { "value": "firewalls", "label": "Firewalls" },
  { "value": "ids_ips", "label": "IDS/IPS (Intrusion Detection/Prevention)" },
  { "value": "kali_linux", "label": "Kali Linux" },
  { "value": "cyber_threat", "label": "Cyber Threat Intelligence" },
  { "value": "incident_response", "label": "Incident Response" },
  { "value": "selenium", "label": "Selenium" },
  { "value": "junit", "label": "JUnit" },
  { "value": "testng", "label": "TestNG" },
  { "value": "load_testing", "label": "Load Testing" },
  { "value": "api_testing", "label": "API Testing" },
  { "value": "performance_testing", "label": "Performance Testing" },
  { "value": "manual_testing", "label": "Manual Testing" },
  { "value": "automated_testing", "label": "Automated Testing" },
  { "value": "bug_tracking", "label": "Bug Tracking" },
  { "value": "agile", "label": "Agile Methodology" },
  { "value": "scrum", "label": "Scrum" },
  { "value": "project_management", "label": "Project Management" },
  { "value": "critical_thinking", "label": "Critical Thinking" },
  { "value": "decision_making", "label": "Decision Making" },
  { "value": "linux_admin", "label": "Linux Administration" },
  { "value": "windows_server", "label": "Windows Server" },
  { "value": "network_protocols", "label": "Network Protocols" },
  { "value": "tcp_ip", "label": "TCP/IP" },
  { "value": "cloud_networking", "label": "Cloud Networking" },
  { "value": "vpn", "label": "VPN" },
  { "value": "load_balancing", "label": "Load Balancing" },
  { "value": "serverless_computing", "label": "Serverless Computing" },
  { "value": "network_automation", "label": "Network Automation" },
  { "value": "hybrid_cloud", "label": "Hybrid Cloud" },
  { "value": "blockchain", "label": "Blockchain" },
  { "value": "iot", "label": "IoT (Internet of Things)" },
  { "value": "robotics", "label": "Robotics" },
  { "value": "ui_ux_design", "label": "UI/UX Design" },
  { "value": "figma", "label": "Figma" },
  { "value": "adobe_xd", "label": "Adobe XD" },
  { "value": "saas", "label": "SaaS Development" },
  { "value": "ar_vr", "label": "AR/VR Development" },
  { "value": "game_dev", "label": "Game Development" },
  { "value": "ethical_ai", "label": "Ethical AI" }
];

// const fetchRecruits = async () => {
//   try {
//     const response = await axios.get("http://localhost:8080/newResumeAnalyser_war_exploded/filter/GetAllRecruits");
//     setRecruits(response.data);
//   } catch (error) {
//     console.error("Error fetching recruits:", error);
//   }
// };

const RecruitmentForm = ({ recruit, onClose, fetchRecruits }) => {
  const [title, setTitle] = useState("");
  const [maximumRecruit, setMaximumRecruit] = useState(0);
  const [qualification, setQualification] = useState("Undergraduate (UG)");
  const [experience, setExperience] = useState(0);
  const [jobDescription, setJobDescription] = useState("");
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [status, setStatus] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [isEditable, setIsEditable] = useState(true);
  const sessionId = localStorage.getItem("sessionId");
  console.log(sessionId)

  useEffect(() => {
    if (recruit?.recruitId) {
      fetchRecruitDetails(recruit.recruitId);
    }
  }, [recruit]);

  const fetchRecruitDetails = async (recruitId) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/newResumeAnalyser_war_exploded/GetRecruit",
        { recruitId, sessionId },
        { headers: { "Content-Type": "application/json" } },

      );

      const data = response.data;
      console.log(data);

      if (data) {
        setTitle(data.recruitName || "");
        setMaximumRecruit(data.maximumNumber || 0);
        setQualification(
          data.qualification === "UG"
            ? "Undergraduate (UG)"
            : data.qualification === "HSC"
              ? "HSC"
              : "Postgraduate (PG)"
        );
        setExperience(data.experienceYear || 0);
        setJobDescription(data.description || "No Description");

        const formattedSkills = data.skills?.map((skill) => ({
          value: skill,
          label: skill,
        }));
        setSelectedSkills(formattedSkills || []);
        if (data.status !== "Open") {
          setIsEditable(false);
          setStatus("This recruit is not editable as it is not 'Open'.");
        } else {
          setIsEditable(true);
        }
      }
    } catch (error) {
      console.error("Error fetching recruit details:", error);
      setStatus("Failed to load recruit details.");
    }
  };

  const handleSubmit = async () => {
    if (isSubmitting || !isEditable) return;
    setIsSubmitting(true);
    console.log("in insert")

    if (title.length > 0 && maximumRecruit > 0 && selectedSkills.length > 0) {
      const newQualification =
        qualification === "Undergraduate (UG)"
          ? "UG"
          : qualification === "HSC"
            ? "HSC"
            : "PG";

            console.log(sessionId)
      const payload = {
        recruitId: recruit?.recruitId || null,
        title,
        maximumRecruit,
        newQualification,
        experience,
        selectedSkills: selectedSkills.map((skill) => skill.value),
        jobDescription: jobDescription.trim() || "No Description",
        sessionId
      };

      console.log("Object: " + payload);
      

      try {
        const url = recruit?.recruitId
          ? "http://localhost:8080/newResumeAnalyser_war_exploded/UpdateRecruit"
          : "http://localhost:8080/newResumeAnalyser_war_exploded/InsertRecruit";
        
          console.log(payload)
        const response = await axios.post(url, payload, {
        
          headers: { "Content-Type": "application/json" },
          // responseType: "json"
        });
        console.log("response ", response.data);
        console.log("response "+response.data.success)
        if (response.data.success) {
          setStatus(recruit?.recruitId ? "Recruit updated successfully" : "Recruit created successfully");

          await fetchRecruits();

          onClose();

          window.location.reload();
        } else {
          setStatus("Failed to process recruit.");
        }
      } catch (error) {
        console.error("Error:", error);
        setStatus("Error processing recruit.");
      } finally {
        setIsSubmitting(false);
      }
    } else {
      setStatus("Please fill all required fields.");
      setIsSubmitting(false);
    }
  };


  // return (
  //   <div className="form-background" onClick={onClose}>
  //     <div className="filter-container" onClick={(e) => e.stopPropagation()}>
  //       <h2 className="title">{recruit?.recruitId ? "Edit Recruit" : "Recruitment Form"}</h2>

  //       <label>Recruit Title</label>
  //       <input
  //         type="text"
  //         value={title}
  //         onChange={(e) => setTitle(e.target.value)}
  //         className="input-field"
  //         placeholder="Enter recruit title"
  //       />

  //       <label>Job Description</label>
  //       <textarea
  //         style={{ resize: "none" }}
  //         className="input-field"
  //         value={jobDescription}
  //         onChange={(e) => setJobDescription(e.target.value)}
  //         placeholder="Enter job description"
  //       />

  //       <label>Maximum Recruit</label>
  //       <input
  //         type="number"
  //         value={maximumRecruit}
  //         onChange={(e) => setMaximumRecruit(e.target.value)}
  //         className="input-field"
  //         min={1}
  //         placeholder="Enter maximum recruit"
  //       />

  //       <label>Skill</label>
  //       <Select
  //         isMulti
  //         options={skillOptions}
  //         className="multi-select"
  //         classNamePrefix="select"
  //         onChange={setSelectedSkills}
  //         value={selectedSkills}
  //         placeholder="Select skills"
  //       />

  //       <label>Qualification</label>
  //       <select
  //         value={qualification}
  //         onChange={(e) => setQualification(e.target.value)}
  //         className="input-field"
  //       >
  //         <option>Undergraduate (UG)</option>
  //         <option>Postgraduate (PG)</option>
  //         <option>HSC</option>
  //       </select>

  //       <label>Experience Years</label>
  //       <input
  //         type="number"
  //         value={experience}
  //         onChange={(e) => setExperience(e.target.value)}
  //         className="input-field"
  //         placeholder="Enter years of experience"
  //       />

  //       {status && <p className="status-message">{status}</p>}

  //       <button className="apply-btn" onClick={handleSubmit} disabled={isSubmitting}>
  //         {isSubmitting ? "Submitting..." : recruit?.recruitId ? "Update Recruit" : "Create Recruit"}
  //       </button>
  //     </div>
  //   </div>
  // );
  return (
    <div className="form-background" onClick={onClose}>
      <div className="filter-container" onClick={(e) => e.stopPropagation()}>
        <h2 className="title">{recruit?.recruitId ? "Edit Recruit" : "Recruitment Form"}</h2>
  
        <label>Recruit Title</label>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="input-field"
          placeholder="Enter recruit title"
          disabled={!isEditable} 
        />
  
        <label>Job Description</label>
        <textarea
          style={{ resize: "none" }}
          className="input-field"
          value={jobDescription}
          onChange={(e) => setJobDescription(e.target.value)}
          placeholder="Enter job description"
          disabled={!isEditable}
        />
  
        <label>Maximum Recruit</label>
        <input
          type="number"
          value={maximumRecruit}
          onChange={(e) => setMaximumRecruit(e.target.value)}
          className="input-field"
          min={1}
          placeholder="Enter maximum recruit"
          disabled={!isEditable} 
        />
  
        <label>Skill</label>
        <Select
          isMulti
          options={skillOptions}
          className="multi-select"
          classNamePrefix="select"
          onChange={setSelectedSkills}
          value={selectedSkills}
          placeholder="Select skills"
          isDisabled={!isEditable} 
        />
  
        <label>Qualification</label>
        <select
          value={qualification}
          onChange={(e) => setQualification(e.target.value)}
          className="input-field"
          disabled={!isEditable} 
        >
          <option>Undergraduate (UG)</option>
          <option>Postgraduate (PG)</option>
          <option>HSC</option>
        </select>
  
        <label>Experience in Years</label>
        <input
          type="number"
          value={experience}
          onChange={(e) => setExperience(e.target.value)}
          className="input-field"
          placeholder="Enter years of experience"
          disabled={!isEditable}
        />
  
        {status && <p className="status-message">{status}</p>}
  
        <button 
          className="apply-btn" 
          onClick={handleSubmit} 
          disabled={!isEditable || isSubmitting} 
        >
          {isSubmitting ? "Submitting..." : recruit?.recruitId ? "Update Recruit" : "Create Recruit"}
        </button>
      </div>
    </div>
  );
  
};

export default RecruitmentForm;
