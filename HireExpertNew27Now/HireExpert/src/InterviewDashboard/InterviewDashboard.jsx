import React, { useContext, useState, useEffect } from 'react';
import './InterviewDashBoard.css';
import HeaderSection from './HeaderSection';
import CandidatePipeline from './CandidatePipeline';
import ResponseRateAnalytics from './ResponseRateAnalytics';
import CandidateInsights from './CandidateInsights';
import PendingActionsTracker from './PendingActionsTracker';
import NewResumeTemplateFor2 from '../newResumeTemplate/NewResumeTemplateFor2';
import DataContext from '../context/DataContext';

import axios from 'axios';

const InterviewDashboard = () => {
    const { selectAfterDetailsRecruit } = useContext(DataContext);
    const sessionId = localStorage.getItem("sessionId");

    const [selectedResumes, setSelectedResumes] = useState([]);
    const [selectedRecruitInfo, setSelectedRecruitInfo] = useState({});

    const [popUpResume, setPopUpResume] = useState(null);


    useEffect(() => {
        const getRelatedData = async () => {
            try {
                const response = await axios.post(
                    `http://localhost:8084/resumeAnalyser_war_exploded/getCorrespondingResumes?recruitId=${selectAfterDetailsRecruit}`,
                    {
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                );

                if (response.data) {
                    setSelectedResumes(response.data);
                }
            } catch (er) {
                console.error("Error getting related data: ", er.message);
            }
        };

        getRelatedData();
    }, []);

    useEffect(() => {
        const getRecruitDetails = async () => {
            try {
                const response = await axios.post(
                    "http://localhost:8084/resumeAnalyser_war_exploded/getRecruit",
                    { recruitId:selectAfterDetailsRecruit, sessionId },
                    {
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                );

                if (response.data) {
                    console.log("Recruit Details: ", response.data);
                    setSelectedRecruitInfo(response.data);
                }
            } catch (e) {
                console.error("Error getting recruit details: ", e.message);
            }
        };

        getRecruitDetails();
    }, [sessionId]);


    useEffect(()=>{
        console.log(popUpResume);
        
    },[popUpResume]);

    const confirmedResumes = selectedResumes.filter(resume => resume.isEmailSentStatus === "InterviewConfirmed");
    const rejected = selectedResumes.filter(resume => resume.isEmailSentStatus === "Rejected");
    const inPending = selectedResumes.filter(resume => resume.isEmailSentStatus === "Interview");
    const HiredResumes = selectedResumes.filter(resume => resume.isEmailSentStatus === "Hired");

    console.log(confirmedResumes);
    

    return (
        <div className="interv-container">
            <HeaderSection />

            <div className="interv-grid">
                <CandidatePipeline recruit={selectedRecruitInfo} resumes={selectedResumes} />
                <ResponseRateAnalytics
                    confirmed={confirmedResumes.length}
                    declined={rejected.length}
                    noResponse={inPending.length}
                />
            </div>

            <CandidateInsights
                selectedResumes={selectedResumes.filter(resume => resume.isEmailSentStatus !== "Declined")}
                selectedRecruitInfo={selectedRecruitInfo}
                setPopUpResume={setPopUpResume}
            />

            <PendingActionsTracker
                Hired={HiredResumes.length}
                confirmed={confirmedResumes.length}
                rejectedResumes={rejected.length}
            />
            {popUpResume && Object.keys(popUpResume).length > 0 && <NewResumeTemplateFor2 popUpResume={popUpResume} setPopUpResume={setPopUpResume} />}
        </div>
    );
};

export default InterviewDashboard;
