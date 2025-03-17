import React, { useState } from 'react';
import styled from 'styled-components';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const LoginPage = ({ onClose, onSwitchToSignup,onForgotPassword }) => {

  const navigate = useNavigate();


  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isValid, setIsValid] = useState(true);



  const handleCorpLogin = ()=>{

    const url = "https://accounts.zoho.com/oauth/v2/auth?response_type=code&client_id=1000.KFNDCNGYB48MAD72J7HXP9ZY8HI4TI&scope=email&redirect_uri=http://localhost:8084/resumeAnalyser_war_exploded/corpLogin"
    window.location.href = url;
    
  }


  const handleLogin = async (e) => {

    if (email.trim() === "" || password.trim() === "") {
      console.log("Email and password are required");
      setIsValid(false); // Show error state if needed
      return;
    }

    try {
      // axios.defaults.withCredentials = true; 
      const response = await axios.post(
        "http://localhost:8080/newResumeAnalyser_war_exploded/Login",
        { email, password },
        {
          headers: { "Content-Type": "application/json" },
        },
        // {Credential:"include"}
        // { withCredentials: true }  
      );

      if (response.data) {
        const isValidUser = response.data.isValidUser;
        if (isValidUser) {
          console.log("Login successful:", response.data);

          // onClose();
          let sessionId= response.data.sessionId; 

          localStorage.setItem("sessionId", sessionId);
          localStorage.removeItem("accepted");
          localStorage.removeItem("mailSented");
          localStorage.removeItem("reject");
          localStorage.removeItem("totalResumes");
          localStorage.removeItem("total_Resume");
          localStorage.removeItem("uploadedResumes");

          navigate("/dashboard",{ replace: true }); 
        } else {
          setIsValid(false);
          console.log("Invalid credentials:", response.data);
        }
      }
    } catch (error) {
      console.error("Login request failed:", error);
    }
  };

  return (
    <StyledWrapper>
      <section className='logoArea'>
        <div className="card">
          <p className='login'>Welcome</p>
          <div className="inputBox inputBox11">
            <input type="text"
              value={email}
              onFocus={() => setIsValid(true)}
              onChange={(e) => setEmail(e.target.value)}
              required="required" />
            <span className="user">Email</span>
          </div>
          <div className="inputBox">
            <input type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onFocus={() => setIsValid(true)}
              required="required" />
            <span>Password</span>
          </div>
          <div className='queries'>
            {(!isValid) ? <p className='warning'>Invalid Credential, try again</p> : ""}
            {/* <p><Link to={''}>Forget Password?</Link></p> */}
            <p>
              <Link to="#" onClick={(e)=>onForgotPassword()}>Forgot Password?</Link>
            </p>
            {/* <p><Link to={'/'}>Don't you have an account?</Link></p> */}
            <p>
              <Link to="#" onClick={onSwitchToSignup}>New User?</Link>
            </p>
          </div>
          {/* <button onClick={()=>handleCorpLogin()}>signin with zoho</button> */}
          <button type='submit' onClick={() => handleLogin()} className="enter">Login</button>
        </div>
      </section>
    </StyledWrapper>
  )
}

const StyledWrapper = styled.div`
.logoArea{
 height: 100%;
 width: 50dvw;
  // display: flex;
  // align-items: center;
  //  justify-content: center;
}

.warning {
    // // opacity: 0;
    // // visibility: hidden;
    // background-color: ; /* Red warning background */
    color:#ff4d4d
    padding: 10px;
    border-radius: 5px;
    text-align: center;
    font-size: 14px;
    transition: opacity 0.4s ease-in-out, visibility 0.4s ease-in-out;
}

 .queries{
 display: flex;
 flex-direction: column;
 align-items: center;
 gap:7px;
 }


  .login {
    color: #000;
    text-transform: uppercase;
    letter-spacing: 2px;
    display: block;
    font-weight: bold;
    font-size: x-large;
  }

  .card {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 430px;
    width: 350px;
    flex-direction: column;
    gap: 35px;
    background: #e3e3e3;
    box-shadow: 16px 16px 32px #c8c8c8,
          -16px -16px 32px #fefefe;
    border-radius: 8px;
  }

  .inputBox {
    position: relative;
    width: 250px;
  }

  .inputBox input {
    width: 100%;
    padding: 10px;
    outline: none;
    border: none;
    color: #000;
    font-size: 1em;
    background: transparent;
    border-left: 2px solid #000;
    border-bottom: 2px solid #000;
    transition: 0.1s;
    border-bottom-left-radius: 8px;
  }

  .inputBox span {
    margin-top: 5px;
    position: absolute;
    left: 0;
    transform: translateY(-4px);
    margin-left: 10px;
    padding: 10px;
    pointer-events: none;
    font-size: 12px;
    color: #000;
    text-transform: uppercase;
    transition: 0.5s;
    letter-spacing: 3px;
    border-radius: 8px;
    z-index: 1;
  }

  .inputBox input:valid~span,
  .inputBox input:focus~span {
    transform: translateX(113px) translateY(-23px);
    font-size: 0.8em;
    padding: 5px 10px;
    background: #000;
    letter-spacing: 0.2em;
    color: #fff;
    border: 2px;
  }

   .inputBox11 input:valid~span,
  .inputBox11 input:focus~span{
  transform: translateX(155px) translateY(-23px) !important;
  }


  .inputBox input:valid,
  .inputBox input:focus {
    border: 2px solid #000;
    border-radius: 8px;
  }

  .enter {
    height: 45px;
    width: 100px;
    border-radius: 5px;
    border: 2px solid #000;
    cursor: pointer;
    background-color: transparent;
    transition: 0.5s;
    // text-transform: uppercase;
    font-size: 12px;
    color:black;
    // letter-spacing: 2px;
    margin-bottom: 1em;
  }

  .enter:hover {
    background-color: rgb(0, 0, 0);
    color: white;
  }`;


export default LoginPage