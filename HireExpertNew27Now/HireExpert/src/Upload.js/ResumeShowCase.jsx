import { Key } from 'lucide-react'
import React, { useContext } from 'react'
import DataContext from '../context/DataContext'

 const ResumeShowCase = ({resume}) => {

    // const resume = {'id':1,
    //     'status':'Reviewed',
    //     'role':'fronted',
    //     'experience':3,
    //     'skills':['React','Node','JavaScript'],
    //     'previousCompany':'XYZ Corp',
    //     'appliedDate':'2022-01-01',
    //     'score':95,
    //     'name':'John Doe',    
    // }

    const {handleResumeClick} = useContext(DataContext);

    return (
        <div 
        key={resume.id}
        className="temp-card"
        onClick={() => handleResumeClick(resume.id,resume.status)}
      >
        <div className="temp-card-header">
          <h3 className="temp-card-title">{resume.candidateName}</h3>
          <span className={`temp-status-badge ${
            resume.status === 'New' ? 'temp-status-new' : 
            resume.status === 'Reviewed' ? 'temp-status-reviewed' :
            resume.status === 'Shortlisted' ? 'temp-status-shortlisted' :
            resume.status === 'Rejected' ? 'temp-status-rejected' :
            'temp-status-interview'
          }`}>
            {resume.status}
          </span>
        </div>
        
        <div className="temp-card-body">
          <div className="temp-role-experience">
            <span className="temp-role">{resume.title}</span>
            <span className="temp-experience">{resume.totalExperiences} yr exp</span>
          </div>
          
          <div className="temp-skill-tags">
            {resume.skills.map((skill, idx) => (
              <span key={idx} className="temp-skill-tag">
                {skill.skillName}
              </span>
            ))}
          </div>
          
          <div className="temp-company-date">
            <span className="temp-company">{resume.previousCompany}</span>
            <span className="temp-date">02/05/2006</span>
          </div>
        </div>
        
        <div className="temp-card-footer">
          <div className="temp-match-score">
            <div className="temp-score-bar">
              <div 
                className={`temp-score-fill ${
                  resume.score >= 85 ? 'temp-score-excellent' : 
                  resume.score >= 70 ? 'temp-score-good' : 
                  resume.score >= 60 ? 'temp-score-average' : 'temp-score-poor'
                }`}
                style={{ width: `${Math.round(resume.score)}%` }}
              />
            </div>
            <span className="temp-score-value">{Math.round(resume.score)}%</span>
          </div>
          
          <button className="temp-view-btn" >
            View Details
          </button>
        </div>
      </div>
    )
}

 export default ResumeShowCase