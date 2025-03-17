import { Link, useLocation } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import { PiSquaresFourFill } from "react-icons/pi";
import { IoCloudUpload } from "react-icons/io5";
import { CgProfile } from "react-icons/cg";
import React from "react";
import logo from "../images/logo-removebg-preview (3).png"; 
import "./sidebarcss.css"
import logoWhite from '../images/logoWhite4.png';

const Sidebar = () => {
  const location = useLocation();

  return (
    <aside className="sidebar">
      <div className="logo-container">
      <img className="logo-img" src={logoWhite} />
      </div>
      <nav>
        <ul className="nav-list">
          <SidebarLink to="/dashboard" icon={<FaHome />} label="Home" location={location} />
          <SidebarLink to="/dashboard/hiringboard" icon={<PiSquaresFourFill />} label="Hiring Board" location={location} />
          <SidebarLink to="/dashboard/upload" icon={<IoCloudUpload />} label="Upload Resumes" location={location} />
          <SidebarLink to="/dashboard/profile" icon={<CgProfile />} label="Profile" location={location} />
        </ul>
      </nav>
    </aside>
  );
};

const SidebarLink = ({ to, icon, label, location }) => (
  <li>
    <Link
      to={to}
      className={`nav-link ${location.pathname === to ? "active" : ""}`}
    >
      <span className="icon">{icon}</span>
      <span className="link-text">{label}</span>
    </Link>
  </li>
);

export default Sidebar;









