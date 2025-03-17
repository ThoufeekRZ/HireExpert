
import React, { useState } from "react";

import { useNavigate } from "react-router-dom";
import "./ForgotPassword.css";
import Popup from "./Popup";


const ForgotPassword = ({ onSwitchToEnterOTP }) => {
  console.log("forgotpassword")
  const [email, setEmail] = useState("");
  const navigate = useNavigate(); 
  const [popupData, setPopupData] = useState({ title: "", message: "" });
  const [showPopup, setShowPopup] = useState(false);


  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };
  const handleSendOTP = async () => {
    if (!email) {
      setPopupData({ title: "Error", message: "Please enter an email!" });
      setShowPopup(true);
      return;
    }
  
    try {
      localStorage.setItem("email",email);
      const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/SendOTP", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email }),
      });
  
      const data = await response.json(); 
  
      if (response.ok && data.status === "success") {
        sessionStorage.setItem("otp", data.otp);
  
        setPopupData({ title: "Success", message: "OTP sent successfully!" });
        setShowPopup(true);

        setTimeout(() => {
          setShowPopup(false);
          // navigate("/enter-otp");
          onSwitchToEnterOTP();
        }, 2000);
      } else {
        setPopupData({ title: "Error", message: data.message || "Failed to send OTP. Please try again." });
        setShowPopup(true);
      }
    } catch (error) {
      console.error("Error:", error);
      setPopupData({ title: "Error", message: "Something went wrong!" });
      setShowPopup(true);
    }
  };
  
  return (
    <div className="containerr">
      <div className="card">
        <img className="forgotpasswordIcon" src="./src/images/reset.png" alt="Reset Password" />
        <a className="login">Enter Email</a>
        <p className="text">We'll send you an OTP to get back into your account.</p>
        <div className="inputBox">
          <input type="text" required value={email} onChange={handleEmailChange} />
          <span className="user">Email</span>
        </div>
        <button className="enter" onClick={handleSendOTP}>Send</button>
        {showPopup && <Popup title={popupData.title} message={popupData.message} onClose={() => setShowPopup(false)}/>}
      </div>
    </div>
  );
};

export default ForgotPassword;


