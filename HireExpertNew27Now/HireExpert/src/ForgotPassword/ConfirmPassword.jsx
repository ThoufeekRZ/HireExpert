import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Popup from "./Popup";

const ConfirmPassword = ({ onSwitchToLogin }) => {
    const sessionId=localStorage.getItem("sessionId");
    const enterEmail=localStorage.getItem("email");

    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [showPopup, setShowPopup] = useState(false);
    const [popupMessage, setPopupMessage] = useState({ title: "", message: "" });

    const navigate = useNavigate();

    const handleNewPasswordChange = (event) => {
        setNewPassword(event.target.value);
    };

    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    };
    
    const handleResetPassword = async () => {
        if (!newPassword || !confirmPassword) {
          setPopupMessage({ title: "Error", message: "Please fill in both fields!" });
          setShowPopup(true);
          return;
        }
      
        if (newPassword !== confirmPassword) {
          setPopupMessage({ title: "Error", message: "Passwords do not match!" });
          setShowPopup(true);
          return;
        }
      
        try {
          console.log(JSON.stringify({ newPassword: newPassword }));
      
          const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/ChangePassword", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ newPassword: newPassword,sessionId, enterEmail }),
          });
      
          const result = await response.json();
      
          if (response.ok && result.status === "success") {
            setPopupMessage({ title: "Success", message: "Password changed successfully! Please log in." });
            setShowPopup(true);
            setTimeout(() => onSwitchToLogin(), 2000);
          } else {
            setPopupMessage({ title: "Error", message: result.message || "Failed to reset password." });
            setShowPopup(true);
          }
        } catch (error) {
          console.error("Error:", error);
          setPopupMessage({ title: "Error", message: "Something went wrong!" });
          setShowPopup(true);
        }
      };

    return (
        <div className="containerr">
            <div className="card1">
                <a className="login">RESET PASSWORD</a>
                <div className="inputBox">
                    <input type="password" required value={newPassword} onChange={handleNewPasswordChange} />
                    <span className="user">NEW PASSWORD</span>
                </div>
                <div className="inputBox">
                    <input type="password" required value={confirmPassword} onChange={handleConfirmPasswordChange} />
                    <span>CONFIRM PASSWORD</span>
                </div>
                <button className="enter" onClick={handleResetPassword}>Reset</button>
                {showPopup && <Popup title={popupMessage.title} message={popupMessage.message} onClose={() => setShowPopup(false)} />}
            </div>
        </div>
    );
};

export default ConfirmPassword;
