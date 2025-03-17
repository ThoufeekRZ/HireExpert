
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Popup from "./Popup";

const EnterOTP = ({ onSwitchToConfirmPassword }) => {
  const [otp, setOtp] = useState("");
  const navigate = useNavigate();
  const [showPopup, setShowPopup] = useState(false);
  const [popupMessage, setPopupMessage] = useState({ title: "", message: "" });

  const handleOtpChange = (event) => {
    setOtp(event.target.value);
  };

const handleSubmitOTP = async () => {
    if (!otp) {
      setPopupMessage({ title: "Error", message: "Please enter the OTP!" });
      setShowPopup(true);
      return;
    }

    const storedOTP = sessionStorage.getItem("otp");
    

  
    try {
      const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/ReceiveOTP", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ enteredOtp: otp, storedOtp: storedOTP }),
      });
  
      const result = await response.json();
  
      if (response.ok && result.status === "success") {
        setPopupMessage({ title: "Success", message: "OTP verified successfully!" });
        setShowPopup(true);
        sessionStorage.removeItem("otp"); 
  
        // setTimeout(() => navigate("/reset-password"), 2000);
        setTimeout(() => onSwitchToConfirmPassword(), 2000);

      } else {
        setPopupMessage({ title: "Error", message: "Invalid OTP. Please try again." });
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
      <div className="card">
        <img src="./public/images/buss.png" alt="OTP Verification" />
        <a className="login">Enter OTP</a>
        <div className="inputBox">
          <input type="text" required value={otp} onChange={handleOtpChange} />
          <span className="user">OTP</span>
        </div>
        <button className="enter" onClick={handleSubmitOTP}>Enter</button>
        {showPopup && <Popup title={popupMessage.title} message={popupMessage.message} onClose={() => setShowPopup(false)} />}
      </div>
    </div>
  );
};

export default EnterOTP;
