import { useContext } from 'react';
import FeedbackForm from './Feedback';
import Login from './Login/Login';
import DataContext, { DataProvider } from "./context/DataContext";
import { Route, Routes } from "react-router-dom";
import RecruitmentForm from './HiringDashBoard/RecruitmentForm';
import HiringBoard from './HiringDashBoard/HiringBoard';
import Form from './Login/SignUp';
import ProfilePage from './Profile/ProfilePage';
import HRDashboard from './CommonDashBoard/HRDashboard';
import HRDashboardFinal from './HRDashboardFinal';
import Home from './HomePage/Home';
import ResumeManager from './Upload.js/ResumeManager';


function App() {

  const {loggedIn} = useContext(DataContext);

  return (
    <div className="App">      
    <HRDashboardFinal/>
    </div>
  );
}

export default App;
