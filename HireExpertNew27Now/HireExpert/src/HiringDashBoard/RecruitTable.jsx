import React, { useState } from 'react';
import { FaEllipsisV } from 'react-icons/fa';
import RecruitmentForm from './RecruitmentForm';
import RecruitTemplate from '../recruitTemplate/RecruitTemplate';
import noRecruitFound from '../images/nofile.jpg';
import axios from "axios";
// import { Edit } from "lucide-react";
import { Edit, Trash2 } from "lucide-react";

import RecruitmentDashboard from '../recruitTemplate/RecruitTemplate';

const RecruitTable = ({ filteredRecruits, fetchRecruits }) => {
    console.log(filteredRecruits)
    const [selectedRecruit, setSelectedRecruit] = useState(null);
    const [showRecruitModal, setShowRecruitModal] = useState(false);
    const [selectedRecruitId, setSelectedRecruitId] = useState(null);
    const [recruitData, setRecruitData] = useState(null);
    const sessionId = localStorage.getItem("sessionId");
    const [loading, setLoading] = useState(false);

    const handleEditClick = (recruit) => {
        setSelectedRecruit(recruit);
    };
    const handleDelete = async (recruitId) => {

        console.log("delete " + recruitId);
        try {
            const response = await axios.post(
                "http://localhost:8080/newResumeAnalyser_war_exploded/DeleteRecruit",
                { recruitId },
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            if (response.data.success) {
                alert(`Deleted recruit`);
                fetchRecruits();
            } else {
                alert(`Failed to delete recruit: ${recruitId}`);
            }
        } catch (error) {
            console.error("Error deleting recruit:", error);
            alert("An error occurred while deleting the recruit.");
        }
    }

    // const openRecruitTemplatePage = async (recruitId) => {
    //     console.log("openRecruit " + recruitId);
    //     setSelectedRecruitId(recruitId);
    //     // setShowRecruitModal(true);

    //     try {
    //         const response = await axios.post(
    //             "http://localhost:8080/ResumeAnalyser/filter/GetRecruit",
    //             { recruitId, sessionId },
    //             { headers: { "Content-Type": "application/json" } }
    //         );

    //         if (response.data) {
    //             setRecruitData(response.data);
    //             console.log("hwej " + response.data)
    //         }
    //         setLoading(true);
    //         setShowRecruitModal(true);
    //     } catch (error) {
    //         console.error("Error fetching recruit details:", error);
    //     }
    //     finally {
    //         setLoading(false); // Set loading to false after fetching
    //     }

    // }
    const openRecruitTemplatePage = async (recruitId) => {
        console.log("openRecruit " + recruitId);
        setSelectedRecruitId(recruitId);
        setLoading(true); // Set loading before API call

        try {
            const response = await axios.post(
                "http://localhost:8084/resumeAnalyser_war_exploded/getRecruit",
                { recruitId, sessionId },
                { headers: { "Content-Type": "application/json" } }
            );

            console.log("Backend Response:", response); // Debugging

            if (response.data) {
                setRecruitData(response.data);
                console.log("Recruit Data Set:", response.data);
            } else {
                console.warn("No data received from backend");
            }

            setShowRecruitModal(true); // Move this inside the try block
        } catch (error) {
            console.error("Error fetching recruit details:", error);
        } finally {
            setLoading(false); // Ensure this doesn't reset too early
        }
    };

    const closeRecruitTemplate = () => {
        setShowRecruitModal(false);
        setSelectedRecruitId(null);
        setRecruitData(null);
    };


    // return (
    //     <div className="table-container">

    //         <div className="recruits-container">
    //             {filteredRecruits.map(recruit => (
    //                 <div
    //                     key={recruit.recruitId}
    //                     className="hire-recruit-card"
    //                     onClick={() => openRecruitTemplatePage(recruit.recruitId)}
    //                     // onClick={() => setSelectedRecruit(recruit)}
    //                 >
    //                     <h3>{recruit.name}</h3>
    //                     <p className="hire-description">{recruit.description}</p>
    //                     <div className="hire-card-footer">
    //                         <span>Max-Hire: {recruit.maxHire}</span>
    //                         <span>Exp: {recruit.experience} years</span>
    //                         <div className="hire-actions">
    //                             <button className='recr-btn' onClick={(e) => {
    //                                 e.stopPropagation();
    //                                 handleEditClick(recruit);
    //                             }}>
    //                                 <Edit size={16} />
    //                             </button>
    //                             <button className='recr-btn' onClick={(e) => {
    //                                 e.stopPropagation();
    //                                 handleDelete(recruit.recruitId);
    //                             }}>
    //                                 <Trash2 size={16} />
    //                             </button>
    //                         </div>
    //                     </div>
    //                 </div>
    //             ))}
    //         </div>


    //         {selectedRecruit && (
    //             <RecruitmentForm
    //                 recruit={selectedRecruit}
    //                 onClose={() => setSelectedRecruit(null)}
    //                 fetchRecruits={fetchRecruits}
    //             />
    //         )}
    //         {showRecruitModal && (
    //             <div className="modal-overlay1">
    //                 <div className="modal-content1">
    //                     <button className="close-button1" onClick={closeRecruitTemplate}>X</button>
    //                     {/* <recruitTemplate recruitId={selectedRecruitId} /> */}

    //                     {loading ? (
    //                         <p>Loading...</p> // Show a loading message while data is being fetched
    //                     ) : recruitData ? (
    //                         <RecruitmentDashboard recruitId={selectedRecruitId} recruitData={recruitData} />
    //                     ) : (
    //                         <p>Error loading data</p> // Show error message if data is null
    //                     )}

    //                     {/* <RecruitmentDashboard recruitId={selectedRecruitId} recruitData={recruitData}/> */}
    //                 </div>
    //             </div>
    //         )}
    //     </div>
    // );
    const getStatusClass = (status) => {
        switch (status) {
            case 'Open':
                return 'status-open';
            case 'In progress':
                return 'status-in-progress';
            case 'Closed':
                return 'status-closed';
            default:
                return 'status-default';
        }
    };

    // return (
    //     <div className="table-container">
    //         <div className="recruits-container">
    //             {filteredRecruits.map(recruit => (
    //                 <div
    //                     key={recruit.recruitId}
    //                     className="hire-recruit-card"
    //                     onClick={() => openRecruitTemplatePage(recruit.recruitId)}
    //                 >
    //                     <h3>{recruit.name}</h3>
    //                     <p className="hire-description">{recruit.description}</p>
    //                     <div className="status-tag-container">
    //                         <span className={`status-tag ${getStatusClass(recruit.status)}`}>
    //                             {recruit.status}
    //                         </span>
    //                     </div>
    //                     <div className="hire-card-footer">
    //                         <span>Max-Hire: {recruit.maxHire}</span>
    //                         <span>Exp: {recruit.experience} years</span>
    //                         <div className="hire-actions">
    //                             <button className='recr-btn' onClick={(e) => {
    //                                 e.stopPropagation();
    //                                 handleEditClick(recruit);
    //                             }}>
    //                                 <Edit size={16} />
    //                             </button>
    //                             <button className='recr-btn' onClick={(e) => {
    //                                 e.stopPropagation();
    //                                 handleDelete(recruit.recruitId);
    //                             }}>
    //                                 <Trash2 size={16} />
    //                             </button>
    //                         </div>
    //                     </div>
    //                 </div>
    //             ))}
    //         </div>

    //         {selectedRecruit && (
    //             <RecruitmentForm
    //                 recruit={selectedRecruit}
    //                 onClose={() => setSelectedRecruit(null)}
    //                 fetchRecruits={fetchRecruits}
    //             />
    //         )}
    //         {showRecruitModal && (
    //             <div className="modal-overlay1">
    //                 <div className="modal-content1">
    //                     <button className="close-button1" onClick={closeRecruitTemplate}>X</button>
    //                     {loading ? (
    //                         <p>Loading...</p>
    //                     ) : recruitData ? (
    //                         <RecruitmentDashboard recruitId={selectedRecruitId} recruitData={recruitData} />
    //                     ) : (
    //                         <p>Error loading data</p>
    //                     )}
    //                 </div>
    //             </div>
    //         )}
    //     </div>
    // );
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    };
    return (
        <div className="table-container">
            {filteredRecruits === undefined || filteredRecruits === null ? (
                <div style={{ display: 'flex', justifyContent: "center", alignItems: "center", height: "100vh" }}>
                    <p>Loading...</p>
                </div>
            ) : filteredRecruits.length > 0 ? (
                filteredRecruits.map(recruit => (
                    <div
                        key={recruit.recruitId}
                        className="hire-recruit-card"
                        onClick={() => openRecruitTemplatePage(recruit.recruitId)}
                    >
                        <h3>{recruit.name}</h3>
                        <p className="hire-description">{recruit.description}</p>
                        <div className="status-tag-container">
                            <span className={`status-tag ${getStatusClass(recruit.status)}`}>
                                {recruit.status === "Closed" ? "In Round Two" : recruit.status === "In Progress" ? "In Round One" : recruit.status}
                            </span>
                        </div>
                        <div className="hire-card-footer">
                            <span>Max-Hire: {recruit.maxHire}</span>
                            <span>Exp: {recruit.experience} years</span>
                        </div>
                        <div className="hire-card-footer">
                            <span className="created-at">Created: {formatDate(recruit.date)}</span>
                        </div>
                        <div className="hire-actions">
                            <button className='recr-btn' onClick={(e) => {
                                e.stopPropagation();
                                handleEditClick(recruit);
                            }}>
                                <Edit size={16} />
                            </button>
                            <button className='recr-btn' onClick={(e) => {
                                e.stopPropagation();
                                handleDelete(recruit.recruitId);
                            }}>
                                <Trash2 size={16} />
                            </button>
                        </div>
                    </div>
                ))
            ) : (
                <div style={{ display: 'flex', justifyContent: "center", alignItems: "center", flexDirection: "column" }}>
                    <img width={'600px'} src={noRecruitFound} alt='No Recruits Found' />
                    <p>No Recruits found!</p>
                </div>
            )}


            {selectedRecruit && (
                <RecruitmentForm
                    recruit={selectedRecruit}
                    onClose={() => setSelectedRecruit(null)}
                    fetchRecruits={fetchRecruits}
                />
            )}
            {showRecruitModal && (
                <div className="modal-overlay1">
                    <div className="modal-content1">
                        <button className="close-button1" onClick={closeRecruitTemplate}>X</button>
                        {loading ? (
                            <p>Loading...</p>
                        ) : recruitData ? (
                            <RecruitmentDashboard recruitId={selectedRecruitId} recruitData={recruitData} />
                        ) : (
                            <p>Error loading data</p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default RecruitTable;
