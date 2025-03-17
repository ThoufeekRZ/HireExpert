import React from 'react';

const PendingActionsTracker = ({Hired,confirmed,rejectedResumes}) => {
  return (
    <div className="interv-card">
      <div className="interv-card-title">
        <i className="interv-card-icon">🕒</i> Pending Actions Tracker
      </div>
      <div className="interv-pending-container">
        <div className="interv-pending-item">
          <div className="interv-pending-label">
            <i className="interv-pending-icon">⏳</i>
            <span>Hired</span>
          </div>
          <div className="interv-pending-count">{Hired}</div>
        </div>
        
        <div className="interv-pending-item interv-manual">
          <div className="interv-pending-label">
            <i className="interv-pending-icon">📅</i>
            <span>Interview Confirmed</span>
          </div>
          <div className="interv-pending-count">{confirmed}</div>
        </div>
        
        <div className="interv-pending-item interv-reminder">
          <div className="interv-pending-label">
            <i className="interv-pending-icon">🔔</i>
            <span>Interview-rejected-by-candidate</span>
          </div>
          <div className="interv-pending-count">{rejectedResumes}</div>
        </div>
      </div>
    </div>
  );
};

export default PendingActionsTracker;