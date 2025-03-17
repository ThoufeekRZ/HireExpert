import React, { useContext, useEffect, useState } from "react";
import { data } from "react-router-dom";
import axios from "axios";
import Select from "react-select";
import DataContext from "../context/DataContext";

const RecruitSelect = ({ Recruit, setRecruit }) => {
    const [recruit, setSelectedRecruit] = useState([]);

    const {setAccepted,setReject,setMailSented, setResumeTotal,resumes} = useContext(DataContext)

    const sessionId = localStorage.getItem("sessionId");

    const recruitOptions = recruit.map((r) => ({
        value: r.ID,
        label: `${r.status} : ${r.recruitName} (Max Hires: ${r['maxHire']})`,
    }));
    



    useEffect(() => {
        const uploadResumes = async () => {
            try {
                // const response = await axios.post("http://localhost:8082/ResumeAnalyser/filter/AllRecruitsArray",sessionId);
                const response = await axios.post(
                    "http://localhost:8080/newResumeAnalyser_war_exploded/AllRecruitsArray",
                    { sessionId: sessionId }, // Send sessionId in the body as JSON
                    {
                        headers: {
                            "Content-Type": "application/json", // Tell the server it's JSON data
                        }
                    }
                );

                if (response) {
                    console.log(response);
                    const openRecruits = response.data.filter(r => r.status === "Open" || r.status === "In progress");

                    setSelectedRecruit(openRecruits);
                    // setSelectedRecruit(response.data)
                    console.log(response.data);
                    // const { accepted, reject, mailSented, totalResume } = response.data;
                    // setAccepted(response.data.accepted)

                    // Store values in localStorage
                    // localStorage.setItem("accepted", accepted);
                    // localStorage.setItem("reject", reject);
                    // localStorage.setItem("mailSented", mailSented);
                    // localStorage.setItem("totalResume", totalResume);
                }
            } catch (error) {
                console.error("Upload failed:", error.message);
            }
        };

        uploadResumes(); // Call the async function inside useEffect
    }, [resumes]);


    return (
        <Select
            className="upload-box"
            styles={{marginBottom:"30px"}}
            classNamePrefix="select"
            options={recruitOptions}
            value={Recruit}
            onChange={setRecruit}
            placeholder="Select the recruit"
        />
    );
};

export default RecruitSelect;
