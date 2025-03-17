import React from 'react';
import { useState } from'react';
import Sidebar from './Sidebar';
import Header from './Header';
import KanbanBoard from './KanbanBoard';

const candidatesData = {
    sourced: [{ name: "Sonia Hoppe", email: "sonia94@gmail.com", image: "https://via.placeholder.com/50" }],
    inProgress: [],
    interview: [],
    hired: [],
    rejected: []
  };

const HRDashboard = () => {
    const [candidates, setCandidates] = useState(candidatesData);
  
    return (
 
        <main className="main1-content">
          <Header />
          <KanbanBoard candidates={candidates} />
        </main>

    );
}

export default HRDashboard;
    