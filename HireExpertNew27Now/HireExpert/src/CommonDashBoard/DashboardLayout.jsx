import React from "react";
import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom";
import "./DashboardLayout.css";
import HRHelpBot from "../hrbot/HRHelpBot";


const DashboardLayout = () => {
    console.log("dl")
  return (
    <div className="dashboard-container">
      <Sidebar />

      <div className="dashboard-content">
        <HRHelpBot/>
        <Outlet />  
      </div>
    </div>
  );
};

export default DashboardLayout;
