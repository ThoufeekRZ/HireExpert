import React, { useEffect } from 'react';

const CandidatePipeline = ({recruit,resumes}) => {
  // Calculate percentages for progress bars
  const totalCandidates = resumes.length;
  const confirmedResumes = resumes.filter(resume=>resume.isEmailSentStatus === "InterviewConfirmed");
  const rejectedResumes = resumes.filter(resume=>resume.isEmailSentStatus === "Rejected");
  const inPendingResumes = resumes.filter(resume=>resume.isEmailSentStatus === "Interview");


  const interviewPercentage = totalCandidates > 0 ? (recruit.mailSented / totalCandidates) * 100 : 0;


  useEffect(()=>{
    console.log(recruit);
    console.log(resumes);
    
    
  },[])


  return (
    <div className="interv-card">
      <div className="interv-card-title">
        <i className="interv-card-icon">📊</i> Candidate Pipeline
      </div>
      <div className="interv-pipeline-container">
        <div className="interv-pipeline-stage">
          <div className="interv-pipeline-label">
            <span>Resume Filtered</span>
            <span className="interv-pipeline-count">{recruit.accepted}</span>
          </div>
          <div className="interv-progress-bar">
            <div 
              className="interv-progress-value interv-stage-1"
              style={{ width: '100%' }}
            ></div>
          </div>
        </div>
        <div className="interv-pipeline-stage">
          <div className="interv-pipeline-label">
            <span>Number of mails sent</span>
            <span className="interv-pipeline-count">{recruit.mailSented}</span>
          </div>
          <div className="interv-progress-bar">
            <div 
              className="interv-progress-value interv-stage-2"
              style={{ width: `${interviewPercentage}%` }}
            ></div>
          </div>
        </div>
        <div className="interv-pipeline-stage">
          <div className="interv-pipeline-label">
            <span>Responded candidates</span>
            <span className="interv-pipeline-count">{confirmedResumes.length}</span>
          </div>
          <div className="interv-progress-bar">
            <div 
              className="interv-progress-value interv-stage-3"
              style={{ width: `${(confirmedResumes.length/recruit.mailSented)*100}%` }}
            ></div>
          </div>
        </div>
        <div className="interv-final-status">
          <div className="interv-status-item interv-accepted">
            <div className="interv-status-count">{confirmedResumes.length}</div>
            <div className="interv-status-label">Accepted</div>
          </div>
          <div className="interv-status-item interv-rejected">
            <div className="interv-status-count">{rejectedResumes.length}</div>
            <div className="interv-status-label">Rejected</div>
          </div>
          <div className="interv-status-item interv-no-show">
            <div className="interv-status-count">{inPendingResumes.length}</div>
            <div className="interv-status-label">No-Show</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CandidatePipeline;