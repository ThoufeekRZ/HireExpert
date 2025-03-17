import React, { useState, useEffect, useContext, useCallback } from 'react';
import './HiringBoard.css';
import RecruitmentForm from './RecruitmentForm';
import Search from './Search';
import Header from './Header';
import RecruitTable from './RecruitTable';
import axios from "axios";
import DataContext from '../context/DataContext';


const HiringBoard = () => {

    const [recruits, setRecruits] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filterStatus, setFilterStatus] = useState("all");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const sessionId = localStorage.getItem("sessionId");

    const {Recruit} = useContext(DataContext)

    useEffect(() => {
        fetchRecruits()
    }, []);

    const filteredRecruits = recruits.filter(recruit => {
        const matchesSearch = Object.values(recruit)
            .join(" ")
            .toLowerCase()
            .includes(searchTerm.toLowerCase());
            console.log("status : "+recruit.status);
        const matchesStatus = filterStatus === "all" || recruit.status === filterStatus;
        return matchesSearch && matchesStatus;
    });

    const fetchRecruits = useCallback(async () => {
        try {
            const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/AllRecruitsArray", {
                method: "POST",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ sessionId }),
            });
    
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Server error: ${text}`);
            }
    
            const data = await response.json();
            console.log("Fetched Recruits:", data);
    
            const formattedRecruits = data.map((recruit) => ({
                id: recruit.recruitId,
                name: recruit.recruitName,
                date: recruit.createdAt,
                description: recruit.description,
                maxHire: recruit.maxHire,
                recruitId: recruit.ID,
                experience: recruit.experience,
                status: recruit.status,
                createdAt: recruit.createdAt,
            }));
    
            setRecruits(formattedRecruits);
        } catch (error) {
            console.error("Error fetching recruits:", error);
        }
    }, [sessionId]);
    
    
    return (
        <div className="hiring-board-container">
            <div className="board-card">
                <Header setIsModalOpen={setIsModalOpen} />

                <Search searchTerm={searchTerm} setSearchTerm={setSearchTerm} filterStatus={filterStatus} setFilterStatus={setFilterStatus} />

                <RecruitTable filteredRecruits={filteredRecruits} fetchRecruits={fetchRecruits} />
            </div>

            {isModalOpen && (
                <RecruitmentForm onClose={() => setIsModalOpen(false)} fetchRecruits={fetchRecruits} />
            )}
        </div>
    );
};

export default HiringBoard;