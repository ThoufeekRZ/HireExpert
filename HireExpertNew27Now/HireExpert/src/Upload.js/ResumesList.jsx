import React, { useContext } from "react";
import { File } from "lucide-react";
import StatusIcon from "./StatusIcon";
import ResumeShowCase from "./ResumeShowCase";
import DataContext from "../context/DataContext";

const ResumesList = () => {

  const {resumes, searchResume,filterStatus,setFilterStatus} = useContext(DataContext);
  
  const filteredResumes = resumes.filter((resume) =>
    resume.candidateName.toLowerCase().includes(searchResume.toLowerCase()) ||
    resume.title.toLowerCase().includes(searchResume.toLowerCase()) ||
    resume.skills.some((skill) => skill.skillName.toLowerCase().includes(searchResume.toLowerCase()))
  );

  const withFilterStatusResumes = filteredResumes.filter((resume) =>{
    if(filterStatus === "all"){
      return true;
    }
    return resume.status === filterStatus;
  })



  return (
    <section className="resumes-section">
 
    {withFilterStatusResumes.length <= 0 ? (
  <div className="no-resumes">
    <File className="no-resumes-icon" />
    <p>No resumes found</p>
  </div>
) : (
  withFilterStatusResumes.map((item, idx) => (
    <ResumeShowCase resume={item} key={item.id} />
  ))
)}
       
        
    </section>
  );
};

export default ResumesList;
