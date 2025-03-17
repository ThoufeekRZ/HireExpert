import React, { useState } from "react";
import { BrowserRouter } from "react-router-dom";
import Login from './Login/Login';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./Login/LoginPage";
import SignIn from "./Login/SignUp";
import HRDashboardFinal from "./HRDashboardFinal";
import HiringBoard from "./HiringDashBoard/HiringBoard";
import ProfilePage from "./Profile/ProfilePage";
import ResumeManager from "./Upload.js/ResumeManager";
import Home from "./HomePage/Home";
import SignUp from './Login/SignUp';
import Landing from "./LandingPage/Landing";
import Sidebar from './CommonDashBoard/Sidebar';
import DashboardLayout from "./CommonDashBoard/DashboardLayout";
import About from "./LandingPage/About";
import { ThemeProvider } from "./ThemeContext";
import { DataProvider } from "./context/DataContext";
import HRHelpBot from "./hrbot/HRHelpBot";
import InterviewEmailTemplate from "./EmailTemplate/InterviewEmalTemplate";
import InterviewDashboard from "./InterviewDashboard/InterviewDashboard";
// import { ThemeProvider } from "styled-components";

function App() {
  const [showLoginPopup, setShowLoginPopup] = useState(false);
  return (
    // <ThemeProvider>
    <ThemeProvider>
    <BrowserRouter>
    <DataProvider>
      <div className="App">
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/dashboard/*" element={<DashboardLayout />}>
            <Route index element={<Home />} />
            <Route path='profile' element={<ProfilePage />} />
            <Route path='upload' element={<ResumeManager />} />
            <Route path='hiringboard' element={<HiringBoard />} /> 
            <Route path="track" element={<InterviewDashboard/>}/>
            {/* <Route index element={<InterviewDashboard/>}/> */}
             <Route path="*" element={HRHelpBot}/> 
          </Route>
          
          {showLoginPopup && <Login onClose={() => setShowLoginPopup(false)} />}
          <Route path="/about" element={<About />} />
          <Route path="/email-template" element={<InterviewEmailTemplate/>}/>
        </Routes>
      </div>
      </DataProvider>
    </BrowserRouter>
     </ThemeProvider>
  );
}

export default App;

