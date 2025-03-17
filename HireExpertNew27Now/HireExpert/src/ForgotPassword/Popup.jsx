import React from 'react';
import { useState, useEffect } from "react";
import "./Popup.css";

const Popup = ({ title, message,onClose }) => {
    const [visible, setVisible] = useState(true);

    useEffect(() => {
      const timer = setTimeout(() => {
        setVisible(false);
        if (onClose) onClose();
      }, 5000);
  
      return () => clearTimeout(timer); 
    }, [onClose]);
  
    if (!visible) return null;
    return (
      <div className="notification">
        <div className="notiglow" />
        <div className="notiborderglow" />
        <div className="notititle">{title}</div>
        <div className="notibody">{message}</div>
      </div>
    );
  };

export default Popup;
