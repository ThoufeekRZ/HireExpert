import React, { useContext } from "react";
import { Filter, Search, ArrowUpDown } from "lucide-react";
import DataContext from "../context/DataContext";


const FilterSection = () => {

   const {setSearchResume,searchResume,filterStatus,setFilterStatus,sendEmaiTo,setSendEmailTo, handleSendEmailTo, emailSuccessState, navigate, resumes} = useContext(DataContext);


    return (
        <section className="filter-section">
           
                <div className="temp-search-wrapper">
                    <Search className="temp-search-icon" />
                    <input
                        type="text"
                        placeholder="Search by name, role, or skills..."
                        className="temp-search-input"
                    value={searchResume}
                    onChange={(e) => setSearchResume(e.target.value)}
                    />
                </div>

                <div className="temp-filter-wrapper">
          <div className="temp-filter">
            <Filter className="temp-filter-icon" />
            <select 
              className="temp-filter-select"
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
            >
              <option value="all">All Status</option>
              <option value="New">New</option>
              <option value="Reviewed">Reviewed</option>
              <option value="Shortlisted">Shortlisted</option>
              <option value="Rejected">Rejected</option>
              <option value="Interview Scheduled">Interview Scheduled</option>
            </select>
          </div>
          </div>
          
          <div className="temp-sort">
        <ArrowUpDown className="temp-sort-icon" />
        <button className="email-template-btn" onClick={() =>resumes.length >0 ? navigate("/email-template"): alert("upload resumes to select template")}>
          fill template to send email
        </button>
        <span> &nbsp; to :</span>
        <select
          className="temp-sort-select"
          value={sendEmaiTo}
          onChange={(e) => {
            const selectedValue = e.target.value;
            setSendEmailTo(selectedValue);
            localStorage.setItem("sendEmailTo", selectedValue); // Store in localStorage
          }}
        >
          <option value="All candidates">All candidates</option>
          <option value="Shortlisted candidates">Shortlisted candidates</option>

        </select>

       
      </div>
           
        </section>
    );
};

export default FilterSection;
