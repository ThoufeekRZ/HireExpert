// InterviewEmailTemplate.jsx
import React, { useContext, useEffect, useState } from 'react';
import './Email.css';
import { useNavigate } from "react-router-dom";
import DataContext from '../context/DataContext';
import axios from 'axios';
import EmailSendAnimation from './EmailSendAnimation';

const InterviewEmailTemplate = () => {


    const { setResumes, Recruit, setRecruit, sendEmailTo } = useContext(DataContext);

    const sessionId = localStorage.getItem('sessionId');







    const [emails, setEmails] = useState([]);
    const [loadingComplete, setLoadingComplete] = useState(false);


    


    // State to store all form data
    const [formData, setFormData] = useState({
      
        position: '',
        interviewDate: '',
        interviewTime: '',
        interviewType: 'Video Call',
        interviewLocation: '',
        interviewerName: '',
        interviewerTitle: '',
        additionalInfo: '',
        companyName: 'Rizh',
        hrName: '',
        hrTitle: 'HR Specialist',
        phoneNumber: '',
        emailAddress: ''
    });

    const fetchProfileData = async () => {
        try {
            console.log("hi...");
            const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/GetUserExtraInfo", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ sessionId })
            });
    
            const data = await response.json();
            console.log(data);
    
            if (data) {
                setFormData((prev) => ({
                    ...prev,
                    hrName: data.userName,
                    phoneNumber: data.phoneNumber,
                    emailAddress: data.email
                }));
            }
        } catch (error) {
            console.error('Error fetching profile data:', error);
        }
    };

    const getPosition = async()=>{
        try {
            
            const response = await axios.post("http://localhost:8080/newResumeAnalyser_war_exploded/GetRecruit",{
                recruitId: Recruit.value
            },{
                headers: { 'Content-Type': 'application/json' }
            }) 

            if(response.data){
                setFormData(prevData => ({
                   ...prevData,
                    position: response.data.recruitName
                }));
            }

        } catch (error) {
            console.error("error fetching profile")
        }
    }

    useEffect(() => {
        fetchProfileData();
        getPosition();
    }, []);


    const navigate = useNavigate();
    // Handle input changes for most fields
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    // Special handler for interview type to update placeholder text
    const handleInterviewTypeChange = (e) => {
        const type = e.target.value;
        setFormData(prevData => ({
            ...prevData,
            interviewType: type
        }));
    };

    // Generate email subject dynamically
    const generateEmailSubject = () => {
        return `Interview Invitation: ${formData.position || "[Position]"} Position at ${formData.companyName}`;
    };

    // Function to copy email content to clipboard
    const copyEmailToClipboard = () => {
        // Create a div to hold the email content
        const tempDiv = document.createElement('div');

        // Get the email content from the preview
        const emailPreview = document.querySelector('.email-preview');
        if (emailPreview) {
            // Clone the content
            tempDiv.innerHTML = emailPreview.innerHTML;

            // Remove the copy button if it exists in the cloned content
            const copyButton = tempDiv.querySelector('.copy-button');
            if (copyButton) {
                copyButton.remove();
            }

            // Select and copy the content
            document.body.appendChild(tempDiv);
            const range = document.createRange();
            range.selectNode(tempDiv);
            window.getSelection().removeAllRanges();
            window.getSelection().addRange(range);
            document.execCommand('copy');
            window.getSelection().removeAllRanges();
            document.body.removeChild(tempDiv);

            // Show feedback
            alert('Email content copied to clipboard!');
        }
    };


    const emailLoadingOn=()=>{

        setLoadingComplete(true)
        setTimeout(()=>{
            setFormData(false);
        },3500)

    }

    const sendEmail = async () => {
        


        if(formData.interviewDate === "" || formData.interviewTime === "" ){
            alert("Please select an interview date and time");
            return;
        }

        emailLoadingOn()

        const inlineEmailTemplate = `
        <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; border: 1px solid #ddd; background-color: #ffffff;">
            <div style="text-align: center;">
                <h1 style="color: #0073e6; margin-bottom: 5px;">${formData.companyName || 'Company Name'}</h1>
                <h2 style="color: #333; font-size: 18px;">Interview Invitation</h2>
            </div>

            <p style="color: #333;">Dear ${formData.candidateName || 'Candidate'},</p>
            <p style="color: #333;">Thank you for applying for the <strong>${formData.position || '[Position]'}</strong> position at ${formData.companyName}. We are impressed with your qualifications and would like to invite you for an interview.</p>

            <div style="border-left: 4px solid #3498db; background-color: #f8f9fa; padding: 10px; margin: 20px 0;">
                <h3 style="color: #2c3e50; font-size: 16px; margin-top: 0;">Interview Details:</h3>
                <ul style="padding-left: 20px; color: #333;">
                    <li><strong>Date:</strong> ${formData.interviewDate ? new Date(formData.interviewDate).toLocaleDateString('en-US') : '[Date]'}</li>
                    <li><strong>Time:</strong> ${formData.interviewTime ? new Date(`2000-01-01T${formData.interviewTime}`).toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true }) : '[Time]'}</li>
                    <li><strong>Format:</strong> ${formData.interviewType || '[Interview Type]'}</li>
                    <li><strong>Location/Link:</strong> ${formData.interviewLocation || '[Location/Link]'}</li>
                    <li><strong>Interviewer:</strong> ${formData.interviewerName || '[Interviewer Name]'}, ${formData.interviewerTitle || '[Interviewer Title]'}</li>
                </ul>
            </div>

            ${formData.additionalInfo ? `
            <div style="border-left: 4px solid #2ecc71; background-color: #f8f9fa; padding: 10px; margin: 20px 0;">
                <h3 style="color: #2c3e50; font-size: 16px;">Additional Information:</h3>
                <p style="color: #333;">${formData.additionalInfo}</p>
            </div>` : ''}

            <p style="color: #333;">Please confirm your attendance by replying to this email. If you have any questions, feel free to reach out.</p>

            <div style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 15px; color: #555;">
                <p>Best regards,</p>
                <p style="font-weight: bold;">${formData.hrName || '[Your Name]'}</p>
                <p>${formData.hrTitle || '[HR Title]'}</p>
                <p>${formData.companyName || '[Company Name]'}</p>
                ${formData.phoneNumber ? `<p>${formData.phoneNumber}</p>` : ''}
                ${formData.emailAddress ? `<p>${formData.emailAddress}</p>` : ''}
            </div>

            <p style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 15px; font-size: 12px; color: #777; text-align: center;">
                This email contains confidential information and is intended only for the named recipient. &copy; ${new Date().getFullYear()} ${formData.companyName}.
            </p>
        </div>`;
        const sendMail = localStorage.getItem("sendEmailTo");
        const riD = localStorage.getItem("recruitId");
        // const 
        const emailData = {
            recruitId: riD,
            sendEmailTo: sendEmailTo,
            subject: "Interview Invitation",
            body: inlineEmailTemplate,
            // body: document.querySelector(".email-body").outerHTML,  // Convert HTML template to string
        };

        const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/SendEmails", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(emailData),
        });

        const result = await response.json();
        console.log(result.emails);

        console.log(result.message);
        if (result.success) {
            // Store the emails in a state variable
            setEmails(result.emails);
            setRecruit(null)
            localStorage.setItem("uploadedResumes",JSON.stringify([]))
            setResumes([])
            sendEmailsOneByOne(result.emails);
            
        } else {
            console.error("Error:", result.message);
        }
    };


    const goToUploadPage = () => {
        navigate("/dashboard/upload");  // Redirect to /upload
    };

    const updateStatus = async (recruitId, email, status) => {
        try {
            const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/ChangeStatus", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ recruitId, email, status }),
            });

            const result = await response.json();
            if (result.success) {
                console.log(`Status updated successfully for ${email}: ${status}`);
                alert(`Status updated successfully: ${status}`);
            } else {
                console.error(`Failed to update status for ${email}`);
                alert(`Failed to update status`);
            }
        } catch (error) {
            console.error("Error updating status:", error);
        }
    };

    const sendEmailsOneByOne = async (emailList) => {

        console.log(emailList);


        for (const email of emailList) {

            const inlineEmailTemplate = `
        <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; border: 1px solid #ddd; background-color: #ffffff;">
            <div style="text-align: center;">
                <h1 style="color: #0073e6; margin-bottom: 5px;">${formData.companyName || 'Company Name'}</h1>
                <h2 style="color: #333; font-size: 18px;">Interview Invitation</h2>
            </div>

            <p style="color: #333;">Dear ${formData.candidateName || 'Candidate'},</p>
            <p style="color: #333;">Thank you for applying for the <strong>${formData.position || '[Position]'}</strong> position at ${formData.companyName}. We are impressed with your qualifications and would like to invite you for an interview.</p>

            <div style="border-left: 4px solid #3498db; background-color: #f8f9fa; padding: 10px; margin: 20px 0;">
                <h3 style="color: #2c3e50; font-size: 16px; margin-top: 0;">Interview Details:</h3>
                <ul style="padding-left: 20px; color: #333;">
                    <li><strong>Date:</strong> ${formData.interviewDate ? new Date(formData.interviewDate).toLocaleDateString('en-US') : '[Date]'}</li>
                    <li><strong>Time:</strong> ${formData.interviewTime ? new Date(`2000-01-01T${formData.interviewTime}`).toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true }) : '[Time]'}</li>
                    <li><strong>Format:</strong> ${formData.interviewType || '[Interview Type]'}</li>
                    <li><strong>Location/Link:</strong> ${formData.interviewLocation || '[Location/Link]'}</li>
                    <li><strong>Interviewer:</strong> ${formData.interviewerName || '[Interviewer Name]'}, ${formData.interviewerTitle || '[Interviewer Title]'}</li>
                </ul>
            </div>

            ${formData.additionalInfo ? `
            <div style="border-left: 4px solid #2ecc71; background-color: #f8f9fa; padding: 10px; margin: 20px 0;">
                <h3 style="color: #2c3e50; font-size: 16px;">Additional Information:</h3>
                <p style="color: #333;">${formData.additionalInfo}</p>
            </div>` : ''}

            <p style="color: #333;">Please confirm your attendance by replying to this email. If you have any questions, feel free to reach out.</p>

            <div style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 15px; color: #555;">
                <p>Best regards,</p>
                <p style="font-weight: bold;">${formData.hrName || '[Your Name]'}</p>
                <p>${formData.hrTitle || '[HR Title]'}</p>
                <p>${formData.companyName || '[Company Name]'}</p>
                ${formData.phoneNumber ? `<p>${formData.phoneNumber}</p>` : ''}
                ${formData.emailAddress ? `<p>${formData.emailAddress}</p>` : ''}
            </div>

           <div style="margin-top: 20px; text-align: center;">
    <a href="http://localhost:8080/newResumeAnalyser_war_exploded/ChangeStatus?recruitId=${Recruit ? Recruit.value : 0}&email=${encodeURIComponent(email)}&status=Accepted"
       style="background-color: #28a745; color: white; padding: 10px 20px; margin-right: 10px; border-radius: 4px; text-decoration: none;">
       Accept
    </a>
    <a href="http://localhost:8080/newResumeAnalyser_war_exploded/ChangeStatus?recruitId=${Recruit ? Recruit.value : 0}&email=${encodeURIComponent(email)}&status=Rejected"
       style="background-color: #dc3545; color: white; padding: 10px 20px; border-radius: 4px; text-decoration: none;">
       Reject
    </a>
</div>

            <p style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 15px; font-size: 12px; color: #777; text-align: center;">
                This email contains confidential information and is intended only for the named recipient. &copy; ${new Date().getFullYear()} ${formData.companyName}.
            </p>
        </div>`;
            const sendMail = localStorage.getItem("sendEmailTo");
            const riD = Recruit ? Recruit.value : 0;
            // const 
            const emailData = {
                recruitId: riD,
                sendEmailTo: sendMail,
                subject: "Interview Invitation",
                emailBody: inlineEmailTemplate,
                // body: document.querySelector(".email-body").outerHTML,  // Convert HTML template to string
            };
            const emailRequestData = {
                ...emailData,
                email: email
            };

            try {
                const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/SendEmailOneByOne", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(emailRequestData),
                });

                const result = await response.json();
                if (result.success) {
                    console.log(`Email sent successfully to ${email}`);
                } else {
                    console.error(`Failed to send email to ${email}`);
                }
            } catch (error) {
                console.error("Error sending email:", error);
               
            }
        }

        await addEmailTemplate();
        console.log("addEmailTemplate() was called");

        await deleteDeclinedResponse();


        navigate("/dashboard");

        
    };
    
    const deleteDeclinedResponse = async () => {
        try {
            await axios.post("http://localhost:8084/resumeAnalyser_war_exploded/DeleteDeclinedResume", {
                recruitId: Recruit?.value
            }, {
                headers: { 'Content-Type': 'application/json' }
            });
            console.log("Declined responses deleted");
        } catch (error) {
            console.error("Error deleting declined responses:", error);
        }
    };

    const addEmailTemplate = async () => {
        console.log("addEmailTemplate function called"); // Check if function is called
    
        const emailData = { ...formData };
        emailData.recruitId = Recruit?.value;
    
        try {
            await axios.post("http://localhost:8084/resumeAnalyser_war_exploded/addEmailTemplate", emailData, {
                headers: { 'Content-Type': 'application/json' },
            });
            console.log("Email template added successfully");
        } catch (error) {
            console.error("Error while adding email template:", error.message);
        }
    };
    
    


    return (
        <div className='email-big-container'>
            <div className="email-template-container">
                {/* FORM SECTION - Left side */}
                <div className="form-section">
                    <h2>Email Template Form</h2>
                    {/* <div className="form-group">
                    <label>Candidate Name:</label>
                    <input
                        type="text"
                        name="candidateName"
                        value={formData.candidateName}
                        onChange={handleChange}
                        placeholder="John Doe"
                    />
                </div> */}

                    <div className="form-group">
                        <label>Position:</label>
                        <input
                            type="text"
                            name="position"
                            value={formData.position}
                            onChange={handleChange}
                            placeholder="Software Developer"
                        />
                    </div>

                    <div className="form-group">
                        <label>Interview Date:</label>
                        <input
                            type="date"
                            name="interviewDate"
                            value={formData.interviewDate}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Interview Time:</label>
                        <input
                            type="time"
                            name="interviewTime"
                            value={formData.interviewTime}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Interview Type:</label>
                        <select
                            name="interviewType"
                            value={formData.interviewType}
                            onChange={handleInterviewTypeChange}
                        >
                            <option value="In-person">In-person</option>
                            <option value="Video Call">Video Call</option>
                            <option value="Phone Call">Phone Call</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Interview Location/Link:</label>
                        <input
                            type="text"
                            name="interviewLocation"
                            value={formData.interviewLocation}
                            onChange={handleChange}
                            placeholder={formData.interviewType === "In-person" ? "123 Office St, Suite 456" : "Zoom/Teams link or Phone number"}
                        />
                    </div>

                    <div className="form-group">
                        <label>Interviewer Name:</label>
                        <input
                            type="text"
                            name="interviewerName"
                            value={formData.interviewerName}
                            onChange={handleChange}
                            placeholder="Jane Smith"
                        />
                    </div>

                    <div className="form-group">
                        <label>Interviewer Title:</label>
                        <input
                            type="text"
                            name="interviewerTitle"
                            value={formData.interviewerTitle}
                            onChange={handleChange}
                            placeholder="Senior Developer"
                        />
                    </div>

                    <div className="form-group">
                        <label>Additional Information:</label>
                        <textarea
                            name="additionalInfo"
                            value={formData.additionalInfo}
                            onChange={handleChange}
                            placeholder="Please bring your portfolio, prepare for a technical assessment, etc."
                        />
                    </div>

                    <div className="form-group">
                        <label>Company Name:</label>
                        <input
                            type="text"
                            name="companyName"
                            value={formData.companyName}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>HR Name:</label>
                        <input
                            type="text"
                            name="hrName"
                            value={formData.hrName}
                            onChange={handleChange}
                            placeholder="Your Name"
                        />
                    </div>

                    <div className="form-group">
                        <label>HR Title:</label>
                        <input
                            type="text"
                            name="hrTitle"
                            value={formData.hrTitle}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Phone Number:</label>
                        <input
                            type="text"
                            name="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            placeholder="+1 (555) 123-4567"
                        />
                    </div>

                    <div className="form-group">
                        <label>Email Address:</label>
                        <input
                            type="email"
                            name="emailAddress"
                            value={formData.emailAddress}
                            onChange={handleChange}
                            placeholder="hr@company.com"
                        />
                    </div>
                    <button className='email-btn' onClick={sendEmail}>Send email</button>
                </div>

                {/* PREVIEW SECTION - Right side */}
                <div className="preview-section" style={{ padding: '20px', fontFamily: 'Arial, sans-serif', maxWidth: '600px', margin: 'auto', backgroundColor: '#f9f9f9', border: '1px solid #ddd', borderRadius: '8px' }}>
                    <div className='prview-back-btn'>
                        <button className='email-btn' onClick={goToUploadPage}>Back</button>
                    </div>

                    <h2 style={{ color: '#333', borderBottom: '1px solid #ddd', paddingBottom: '10px' }}>Email Preview</h2>
                    <button style={{ backgroundColor: '#3498db', color: 'white', border: 'none', padding: '10px 15px', borderRadius: '4px', cursor: 'pointer', fontSize: '14px', marginBottom: '15px', transition: 'background-color 0.3s' }}
                        onClick={copyEmailToClipboard}>
                        Copy Email Content
                    </button>
                    <div style={{ backgroundColor: '#fff', border: '1px solid #ddd', borderRadius: '4px', padding: '20px' }}>
                        <div style={{ backgroundColor: '#eee', padding: '10px', borderRadius: '4px', marginBottom: '15px', fontSize: '14px' }}>
                            <strong>Subject:</strong> {generateEmailSubject()}
                        </div>
                        <div style={{ textAlign: 'center', marginBottom: '30px' }}>
                            <h1 style={{ color: '#2c3e50', fontSize: '24px', marginBottom: '5px' }}>{formData.companyName}</h1>
                            <h2 style={{ color: '#3498db', fontSize: '18px', fontWeight: 'normal', marginTop: '0' }}>Interview Invitation</h2>
                        </div>
                        <p style={{ fontSize: '16px', color: '#555' }}>Dear {formData.candidateName || "Candidate"},</p>
                        <p style={{ fontSize: '16px', color: '#555' }}>Thank you for your application for the <strong>{formData.position || "[Position]"}</strong> position at {formData.companyName}. We are impressed with your qualifications and would like to invite you for an interview.</p>
                        <div style={{ backgroundColor: '#f8f9fa', borderLeft: '4px solid #3498db', padding: '15px', margin: '20px 0' }}>
                            <h3 style={{ color: '#2c3e50', marginTop: '0', fontSize: '16px' }}>Interview Details:</h3>
                            <ul style={{ paddingLeft: '20px', marginBottom: '0' }}>
                                <li><strong>Date:</strong> {formData.interviewDate || "[Date]"}</li>
                                <li><strong>Time:</strong> {formData.interviewTime || "[Time]"}</li>
                                <li><strong>Format:</strong> {formData.interviewType}</li>
                                <li><strong>Location/Link:</strong> {formData.interviewLocation || "[Location/Link]"}</li>
                                <li><strong>Interviewer:</strong> {formData.interviewerName || "[Interviewer Name]"}, {formData.interviewerTitle || "[Interviewer Title]"}</li>
                            </ul>
                        </div>
                        {formData.additionalInfo && (
                            <div style={{ backgroundColor: '#f8f9fa', borderLeft: '4px solid #2ecc71', padding: '15px', margin: '20px 0' }}>
                                <h3 style={{ color: '#2c3e50', marginTop: '0', fontSize: '16px' }}>Additional Information:</h3>
                                <p>{formData.additionalInfo}</p>
                            </div>
                        )}
                        <p style={{ fontSize: '16px', color: '#555' }}>Please confirm your attendance by replying to this email. If you need to reschedule or have any questions, please don't hesitate to contact me.</p>
                        <p style={{ fontSize: '16px', color: '#555' }}>We look forward to meeting you!</p>
                        <div style={{ marginTop: '30px', borderTop: '1px solid #eee', paddingTop: '20px', color: '#555' }}>
                            <p>Best regards,</p>
                            <p style={{ fontWeight: 'bold', marginBottom: '2px' }}>{formData.hrName || "[Your Name]"}</p>
                            <p style={{ margin: '2px 0', fontSize: '14px' }}>{formData.hrTitle}</p>
                            <p style={{ margin: '2px 0', fontSize: '14px' }}>{formData.companyName}</p>
                            {formData.phoneNumber && <p style={{ margin: '2px 0', fontSize: '14px' }}>{formData.phoneNumber}</p>}
                            {formData.emailAddress && <p style={{ margin: '2px 0', fontSize: '14px' }}>{formData.emailAddress}</p>}
                        </div>
                        <div className="unique-email-buttons">
                            <button className="accepted-btn">Accepted</button>
                            <button className="rejected-btn">Rejected</button>
                        </div>
                        <div style={{ marginTop: '30px', borderTop: '1px solid #eee', paddingTop: '15px', fontSize: '12px', color: '#777', textAlign: 'center' }}>
                            <p>This email contains confidential information and is intended only for the named recipient. If you have received this email in error, please notify the sender immediately.</p>
                            <p>&copy; {new Date().getFullYear()} {formData.companyName}. All rights reserved.</p>
                        </div>
                    </div>
                </div>
            </div>
            {loadingComplete && <EmailSendAnimation recipients={emails.length}/>}
        </div>
        
    );
};

export default InterviewEmailTemplate;