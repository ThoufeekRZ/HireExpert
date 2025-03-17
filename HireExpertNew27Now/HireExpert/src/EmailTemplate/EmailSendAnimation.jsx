import React, { useState, useEffect } from 'react';
import './Email.css';

const EmailSendAnimation = ({ 
    recipients = 15, 
    templateName = "Standard Interview Invite" 
}) => {
    const [isSending, setIsSending] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsSending(false);
        }, 3000);

        return () => clearTimeout(timer);
    }, []);

    return (
      <div className='sendem-bg'>
        <div className="sendem-popup">
            {isSending ? (
                <>
                    <div className="sendem-email-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path d="M22 12l-20-9 5 9-5 9z"/>
                        </svg>
                    </div>
                    <div className="sendem-loading-text">Sending Candidate Emails...</div>
                    <div className="sendem-progress-bar">
                        <div className="sendem-progress-bar-fill"></div>
                    </div>
                    <div className="sendem-email-details">
                        <p>Recipients: {recipients} Candidates</p>
                        <p>Template: {templateName}</p>
                    </div>
                </>
            ) : (
                <div className="sendem-success-state">
                    <div className="sendem-success-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
                        </svg>
                    </div>
                    <div className="sendem-loading-text sendem-success-text">
                        Emails Sent Successfully!
                    </div>
                    <div className="sendem-email-details">
                        <p>{recipients}/{recipients} Emails Delivered</p>
                        <p>Sent at: {new Date().toLocaleTimeString()}</p>
                    </div>
                </div>
            )}
        </div>
        </div>
    );
};

export default EmailSendAnimation;