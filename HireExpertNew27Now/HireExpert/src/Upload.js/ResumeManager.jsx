import React, { useContext, useEffect, useRef, useState } from "react";
import { File } from "lucide-react";
import RecruitSelect from "./RecruitSelect";
import UploadSection from "./UploadSection";
import StatsSection from "./StatsSection";
import FilterSection from "./FilterSection";
import ResumesList from "./ResumesList";
import axios from "axios";
import "./ResumeManager.css";
import { data } from "react-router-dom";
import DataContext from "../context/DataContext";
import NewResumeTemplate from "../newResumeTemplate/newResumeTemplate";

const ResumeManager = () => {

  const { setResumes, setLoading, popUpResume, setTotalResumes, Recruit, setRecruit } = useContext(DataContext)


  const [recruitWarning, setRecruitWarning] = useState("");
  const [uploadWarning,setUploadWarning] = useState("");
  const [uploadProgress,setUploadProgress] = useState(0);

  useEffect(()=>{


    if(uploadWarning){
    const timeout = setTimeout(() => {
    setUploadWarning("");
     }, 4000);
    return () => clearTimeout(timeout); // Cleanup on re-renders
     }
    
    
     },[uploadWarning])



  const [allRecruits, setAllRecruits] = useState([]);
  const fileInputRef = useRef(null);




  // Update resume status
  // const updateStatus = (id, newStatus) => {
  //   setResumes((prevResumes) =>
  //     prevResumes.map((resume) =>
  //       resume.id === id ? { ...resume, status: newStatus } : resume
  //     )
  //   );
  // };

  // Filtered resumes list
  // const filteredResumes =
  //   filterStatus === "all"
  //     ? resumes
  //     : resumes.filter((resume) => resume.status === filterStatus);

  // // Get count by status
  // const getStatusCount = (status) => {
  //   return resumes.filter((resume) => resume.status === status).length;
  // };

  const handleSubmit = async (files) => {
    localStorage.removeItem("totalResumes");
    localStorage.removeItem("uploadedResumes");
    setTotalResumes(localStorage.getItem("totalResumes") || 0);
    if (!Recruit) {
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }
      setRecruitWarning("Please select the recruit first");
      return;
    }

    console.log(Recruit.value);
    
    localStorage.setItem("recruitId",Recruit.value);

    
    

    if (!files || files.length === 0) {
      console.error("No files selected.");
      return;
    }

    setTotalResumes(files.length);
    localStorage.setItem("totalResumes", files.length);

    const formData = new FormData();
    for (const file of files) {
      formData.append("files", file);
    }

    console.log("Recruit Object:", Recruit);
    formData.append("recruitId", Recruit.value);
    

    // Reset input field
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
      setResumes([]);
    }

    try {
      setLoading(true); // Set loading before sending the request
      localStorage.setItem("uploadedResumes", JSON.stringify([]));
      const response = await axios.post(
        "http://localhost:8084/resumeAnalyser_war_exploded/uploadResumes",
        formData,
        // { headers: { "Content-Type": "multipart/form-data" } }
        {
          headers: {
            "Content-Type": "multipart/form-data",
          }
        }
      );

      setTimeout(() => setUploadProgress(100), 500);

      console.log("Upload Response:", response.data);

      if (response.data) {
        localStorage.setItem("uploadedResumes", JSON.stringify(response.data));
        const newRecruit = Recruit;
        newRecruit.label.replace("Open","In progress")
        setRecruit(newRecruit);
        setResumes(response.data);
      }
    } catch (err) {
      console.error("Upload Error:", err);
      setUploadWarning("Error uploading resumes, try again");
      setRecruit(null);
      
    } finally {
      setLoading(false);
      setUploadProgress(100) // Ensure loading is reset after request completes
    }
  };

  useEffect(() => {
    if (recruitWarning) {
      const timeout = setTimeout(() => {
        setRecruitWarning("");
      }, 4000);

      return () => clearTimeout(timeout); // Cleanup on re-renders
    }

  }, [recruitWarning]);

  useEffect(() => {

  }, [])






  return (
    <div className="resume-manager">
      {/* <header className="header">
        <h1>Resume Management System</h1>
      </header> */}

      <main className="main-content">
        <RecruitSelect Recruit={Recruit} setRecruit={setRecruit} />
        <UploadSection uploadWarning={uploadWarning} handleSubmit={handleSubmit} setRecruitWarning={setRecruitWarning} recruitWarning={recruitWarning} fileInputRef={fileInputRef} uploadProgress={uploadProgress} />

        <StatsSection />
        <FilterSection />
        <ResumesList />
        {popUpResume && Object.keys(popUpResume).length > 0 && <NewResumeTemplate />}
      </main>
    </div>
  );
}

export default ResumeManager;
