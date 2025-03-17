import React from 'react';
import './CircularProgress.css';

const CircularProgress = ({ uploadProgress }) => {
  const strokeDashoffset = 440 - (440 * uploadProgress) / 100;

  return (
    <div className="pro-container">
      <svg className="pro-circle" viewBox="0 0 150 150">
        <circle className="pro-bg" cx="75" cy="75" r="70" />
        <circle
          className="pro-fg"
          cx="75"
          cy="75"
          r="70"
          style={{ strokeDashoffset }}
        />
      </svg>
      <div className="pro-text">{uploadProgress}%</div>
    </div>
  );
};

export default CircularProgress;
