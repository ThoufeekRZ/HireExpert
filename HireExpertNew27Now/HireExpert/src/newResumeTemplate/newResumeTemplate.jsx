import React, { useContext } from 'react';
import { X } from 'lucide-react';
import './newResumeTemplate.css'
import DataContext from '../context/DataContext';

const NewResumeTemplate = () => {

  const { popUpResume, setPopUpResume, handleRejectResume, handleShortlistResume } = useContext(DataContext)

  const closePopup = () => {
    console.log(popUpResume.skills);
    console.log(popUpResume.experiences);
    
    setPopUpResume({});
  }

  return (
    <div className="temp-modal">
      <div className="temp-overlay" onClick={closePopup}></div>
      <div className="temp-modal-content">
        <div className="temp-modal-header">
          <div>
            <h2 className="temp-modal-title">{popUpResume.candidateName}</h2>
            <p className="temp-modal-subtitle">{popUpResume.tile} • {popUpResume.totalExperiences} years experience</p>
          </div>
          <button className="temp-close-btn"
            onClick={closePopup}
          >
            <X className="temp-close-icon" />
          </button>
        </div>

        <div className="temp-modal-body">
          <div className="temp-resume-grid">
            <div className="temp-contact-info">
              <h3 className="temp-section-title">Contact Information</h3>
              <div className="temp-info-item">
                <span className="temp-info-label">Email:</span>
                <span className="temp-info-value">{popUpResume.email}</span>
              </div>
              <div className="temp-info-item">
                <span className="temp-info-label">Phone:</span>
                <span className="temp-info-value">{popUpResume['mobile-number']}</span>
              </div>
              <div className="temp-info-item">
                <span className="temp-info-label">Applied on:</span>
                <span className="temp-info-value">{"02/02/2025"}</span>
              </div>
            </div>

            <div className="temp-match-details">
              <h3 className="temp-section-title">Match Details</h3>
              <div className="temp-match-score-details">
                <div className="temp-score-header">
                  <span>Overall Match</span>
                  <span className="temp-score-percent">{popUpResume.score}%</span>
                </div>
                <div className="temp-detailed-score-bar">
                  <div
                    className={`temp-detailed-score-fill ${popUpResume.score >= 85 ? 'temp-score-excellent' :
                        popUpResume.score >= 70 ? 'temp-score-good' :
                          popUpResume.score >= 60 ? 'temp-score-average' : 'temp-score-poor'
                      }`}
                    style={{ width: `${popUpResume.score}%` }}
                  />
                </div>
              </div>
              <div className="temp-current-status">
                <span className="temp-status-label">Current Status:</span>
                <span className={`temp-status-badge-small ${popUpResume.status === 'New' ? 'temp-status-new' :
                    popUpResume.status === 'Reviewed' ? 'temp-status-reviewed' :
                      popUpResume.status === 'Shortlisted' ? 'temp-status-shortlisted' :
                        popUpResume.status === 'Rejected' ? 'temp-status-rejected' :
                          'temp-status-interview'
                  }`}>
                  {popUpResume.status}
                </span>
              </div>
            </div>
          </div>

          <div className="temp-skills-section">
            <h3 className="temp-section-title">Skills</h3>
            <div className="temp-skills-container">
              {popUpResume.skills.map((skill, idx) => (
                <span key={idx} className="temp-skill-tag-lg">
                  {skill.skillName}
                </span>
              ))}
            </div>
          </div>

          <div className="temp-experience-section">
            <h3 className="temp-section-title">Professional Experience</h3>
            
            {popUpResume.experiences.map((experience, idx)=>(
               <div className="temp-experience-item">
               <div className="temp-job-title">{experience.title}</div>
               <div className="temp-company-name">{experience.company}</div>
               <div className="temp-job-period">
              {experience.experienceYear} years
               </div>
               <p className="temp-job-description">
                 {experience.responsibilities}
               </p>
             </div>
            ))}

          </div>

          <div className="temp-education-section">
            <h3 className="temp-section-title">Education</h3>
            <div className="temp-education-item">
              <div className="temp-degree">Education Qualification</div>
              <div className="temp-university">{popUpResume.education}</div>
            </div>
          </div>
        </div>

        <div className="temp-modal-footer">
          <div className="temp-action-buttons">
            <button className="temp-reject-btn" onClick={()=>{handleRejectResume(popUpResume.id)
              closePopup()
            }}>
              Reject
            </button>
            <button className="temp-shortlist-btn" onClick={()=>{handleShortlistResume(popUpResume.id)
              closePopup()
            }}>
              Shortlist
            </button>
          </div>
          {/* <button className="temp-schedule-btn">
            Schedule Interview
          </button> */}
        </div>
      </div>
    </div>
  )
}

export default NewResumeTemplate