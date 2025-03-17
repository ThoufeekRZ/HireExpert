import React, { useState } from 'react';
import styled from 'styled-components';
import api from "../api/post";
import { Link, useNavigate } from 'react-router-dom';
import { AppleIcon } from 'lucide-react';
import axios from 'axios';

const Form = ({ onSignupSuccess, onSwitchToLogin }) => {


  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [emailWarning, setEmailWarning] = useState("");
  const [passwordWarning, setPasswordWarning] = useState("");
  const [usernameWarning, setUsernameWarning] = useState("");
  const [showLoginPopup,setShowLoginPopup]=useState(false);

  const patterns = {
    email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!])[A-Za-z\d@#$%^&+=!]{8,}$/,
    username: /^[a-zA-Z0-9._]{1,30}$/
  };

  const navigate = useNavigate();

  const handleSignUp = (e) => {


    if (!patterns.email.test(email)) {
      setEmailWarning("Please enter a valid email address")
      return;
    }
    else if (!patterns.username.test(password) && username === "") {

      setUsernameWarning("Username must be 1-30 characters long, allowing letters, numbers, dots, and underscores, but no spaces or special characters like @, #, $, %.")
      return;
    }
    else if (!patterns.password.test(password)) {
      setPasswordWarning("Please enter valid password. It should contain at least 8 characters, including uppercase and lowercase letters, numbers, and special characters")
      return;
    }

    const response = axios.post("http://localhost:8080/newResumeAnalyser_war_exploded/Signup", { email, username, password }, {
      headers: {
        "Content-Type": "application/json"
      }
      // withCredentials:true
    }).then((response) => {
      if (response.data) {
        console.log(response.data);

        const valid = response.data.isSignup;
        console.log(valid);

        if (valid) {
          // navigate('/login');
          setShowLoginPopup(true);
          onSignupSuccess();
        }
        else {
          setEmailWarning('Email already existing')
        }
      }
    }).catch((error) => {
      console.log("error:" + error.message);

    })

  }

  return (
    <StyledWrapper>
      <section className='logoArea'>
        <div className="card">
          <p className="singup">Sign Up</p>
          <div className="inputBox1">
            <input type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              onFocus={() => setEmailWarning("")}
              required="required" />
            <span className="user">Email</span>
            {(emailWarning.length > 0) ? <p className='warning'>{emailWarning}</p> : ""}
          </div>
          <div className="inputBox">
            <input type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              onFocus={() => setUsernameWarning("")}
              required="required" />
            <span>Username</span>
            {(usernameWarning.length > 0) ? <p className='warning'>{usernameWarning}</p> : ""}
          </div>
          <div className="inputBox">
            <input type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onFocus={() => setPasswordWarning("")}
              required="required" />
            <span>Password</span>
            {(passwordWarning.length > 0) ? <p className='warning'>{passwordWarning}</p> : ""}
          </div>
          <div className="singInConditions">
            <span className='checked-sigIn'></span>
            <p></p>
          </div>

          {/* <p><Link to="/login">Already have an account?</Link></p> */}
          {/* <p>
            Already have an account?{" "}
            <button className="switch-login" onClick={onSwitchToLogin}>Log in</button>
          </p> */}
          <p>
            <Link to="#" onClick={onSwitchToLogin}>Already have an account?</Link>
          </p>
          {/* setShowLoginPopup(true); */}
          <button type='submit' className="enter" onClick={() => handleSignUp()}>Sign Up</button>
        </div>

      </section>
    </StyledWrapper >
  );
}

const StyledWrapper = styled.div`

 .logoArea{
   height: 100%;
   width: 50dvw;
  //  display: flex;
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

  .singup {
    color: #000;
    text-transform: uppercase;
    letter-spacing: 2px;
    display: block;
    font-weight: bold;
    font-size: x-large;
    margin-top: 1.5em;
  }

  .card {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 420px;
    width: 350px;
    flex-direction: column;
    gap: 35px;
    border-radius: 15px;
    background: #e3e3e3;
    box-shadow: 16px 16px 32px #c8c8c8,
          -16px -16px 32px #fefefe;
    border-radius: 8px;
  }

  .inputBox,
  .inputBox1 {
    position: relative;
    width: 250px;
  }

  .inputBox input,
  .inputBox1 input {
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

  .inputBox span,
  .inputBox1 span {
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
    transform: translateX(117px) translateY(-15px);
    font-size: 0.8em;
    padding: 5px 10px;
    background: #000;
    letter-spacing: 0.2em;
    color: #fff;
    border: 2px;
  }

  .inputBox1 input:valid~span,
  .inputBox1 input:focus~span {
    transform: translateX(156px) translateY(-15px);
    font-size: 0.8em;
    padding: 5px 10px;
    background: #000;
    letter-spacing: 0.2em;
    color: #fff;
    border: 2px;
  }

  .inputBox input:valid,
  .inputBox input:focus,
  .inputBox1 input:valid,
  .inputBox1 input:focus {
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
    color:black;
    // text-transform: uppercase;
    font-size: 12px;
    // letter-spacing: 2px;
    margin-bottom: 3em;
  }

  .enter:hover {
    background-color: rgb(0, 0, 0);
    color: white;
  }`;

export default Form;