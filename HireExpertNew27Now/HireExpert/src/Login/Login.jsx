import React from 'react';
import { Route, Routes, useNavigate } from "react-router-dom";
import styled from 'styled-components';
import LoginLogo from './LoginLogo';
import logo from '../images/logo-removebg-preview.png';
import LoginPage from './LoginPage';
import Form from './SignUp';



const Login = () => {
  return (
    
    <StyledWrapper>
    <div className="container">
      <LoginLogo />
      
      <Routes>
        <Route path='/login' element={<LoginPage/>}/>
        <Route path="/" element={<Form />} />
      </Routes>
    </div>
  </StyledWrapper>
  );
}

const StyledWrapper = styled.div`
.container{
  background-color: #e8e8e8;
  height: 100dvh;
  width: 100dvw;
  display: flex;
  font-family: "Orbitron", sans-serif;
  font-weight: bold;
 
  }
  `;



export default Login;