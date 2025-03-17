import React from 'react';
import styled from 'styled-components';
import logo from '../images/logo-removebg-preview.png';

const LoginLogo = () => {
  return (
    <StyledWrapper>
   
    <section className='logoArea1'>
    <img src={logo} alt="logo" width='500px' />
    <p>Smart, Easy & Quick Recruit</p>
  </section>
 
  </StyledWrapper>
  )
}

const StyledWrapper = styled.div`

.logoArea1{
 background: #0A0F24;
 display: flex;
 flex-direction: column;
 gap: 20px;
  height: 100%;
 width: 50dvw;
  align-items: center;
   justify-content: center;
}
 .logoArea1>p{
 background: linear-gradient(90deg, #00AEEF 0%, #C84EF4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  font-size: 35px;
}

`

export default LoginLogo