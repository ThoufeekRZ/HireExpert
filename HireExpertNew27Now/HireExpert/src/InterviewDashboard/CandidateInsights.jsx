import React from 'react';
import axios from 'axios';

const CandidateInsights = ({selectedResumes,selectedRecruitInfo, setPopUpResume}) => {
  // Sample data - in a real app, this would come from props or state

  const handleResumeClick = async (id,recruidId) => {
    try {
      console.log("44");

      console.log(recruidId);
      

      const response = await axios.post("http://localhost:8084/resumeAnalyser_war_exploded//getResumeFor2?id=" + id + "&recruitId=" + recruidId, {
        headers: { "Content-Type": "application/json" }
      })

      if (response.data) {
        setPopUpResume(response.data);
        console.log(response.data);
      }


    } catch (error) {
      console.log(error.message);

    }
  }


  return (
    <div className="interv-card">
      <div className="interv-card-title">
        <i className="interv-card-icon">👥</i> Candidate Insights
      </div>
      <div className="interv-table-container">
        <table className="interv-table">
          <thead>
            <tr>
              <th>Candidate Name</th>
              <th>Resume Score</th>
              <th>Status</th>
              <th>Interview Date</th>
              <th>Interviewer</th>
              <th>Interviewer Title</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {selectedResumes.map((candidate, index) => (
              <tr key={index}>
                <td>{candidate.candidateName}</td>
                <td>{candidate.score}</td>
                <td>
                  <span className={`interv-badge ${candidate.isEmailSentStatus === "InterviewConfirmed" ? "interv-badge-success":candidate.isEmailSentStatus === "Rejected" ? "interv-badge-danger" : "interv-badge-pending"}`}>
                    {candidate.isEmailSentStatus === "Interview" ? "Pending" : candidate.isEmailSentStatus}
                  </span>
                </td>
                <td>{selectedRecruitInfo.interviewDetails.interviewDate}</td>
                <td>{selectedRecruitInfo.interviewDetails.interviewerName || "-"}</td>
                <td>{selectedRecruitInfo.interviewDetails.interviewerTitle || "-"}</td>
            
                <td>
                  <div className="interv-action-btn">
                    <button className="interv-btn interv-btn-outline" 
                    onClick={()=>handleResumeClick(candidate.id,selectedRecruitInfo.recruitId)}
                    >View</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CandidateInsights;
