import React, { useContext, useEffect, useState } from "react";
import DataContext from "../context/DataContext";
import axios from "axios";

const StatsSection = () => {

  const {resumes,totalResumes,loading} = useContext(DataContext)

  const allTotalResumes = resumes.length > 0 ? totalResumes : 0;
 
  const [shortlisted,setShortlisted] = useState(0);
  const [rejected, setRejected] = useState(0);
  const [reviewed, setReviewed] = useState(0);

  useEffect(()=>{
  
   const shortlisteds = resumes.filter(resume => resume.status === "Shortlisted").length;
   const rejecteds = resumes.filter(resume => resume.status === "Rejected").length;
   const revieweds = resumes.filter(resume => resume.status === "Reviewed").length;

   setShortlisted(shortlisteds);
   setRejected(rejecteds);
   setReviewed(revieweds);

  },[resumes])

  return (
    <section className="stats-section">
      <div className="stat1-card">
        <div className="stat-value">{resumes.length}</div>
        <div className="stat-label">Filtered Resumes</div>
      </div>

      <div className="stat1-card">
        <div className="stat-value">{shortlisted}</div>
        <div className="stat-label">Shortlisted</div>
      </div>
      <div className="stat1-card">
        <div className="stat-value">{rejected}</div>
        <div className="stat-label">Rejected</div>
      </div>
      <div className="stat1-card">
        <div className="stat-value">{reviewed}</div>
        <div className="stat-label">In Review</div>
      </div>
      <div className="stat1-card">
        <div className="stat-value">{resumes.length - shortlisted - rejected - reviewed}</div>
        <div className="stat-label">New</div>
      </div>
    </section>
  );
};

export default StatsSection;
