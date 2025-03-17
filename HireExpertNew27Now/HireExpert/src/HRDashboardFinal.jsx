import React from 'react';
import Sidebar from './CommonDashBoard/Sidebar';
import { Route, Routes } from 'react-router-dom';
import HRDashboard from './CommonDashBoard/HRDashboard';
import ProfilePage from './Profile/ProfilePage';
import HiringBoard from './HiringDashBoard/HiringBoard';
import Home from './HomePage/Home';
import ResumeManager from './Upload.js/ResumeManager';


const HRDashboardFinal = () => {
  return (
    <div className='container'>
    <Sidebar/>
    <Routes>
      <Route path='dashboard'>
       <Route index element={<Home/>}/>
       <Route path='profile' element={<ProfilePage/>}/>
       <Route path='upload' element={<ResumeManager/>}/>
       <Route path='hiringboard' element={<HiringBoard/>}/>
      </Route>
    </Routes>
    </div>
  )
}

export default HRDashboardFinal;