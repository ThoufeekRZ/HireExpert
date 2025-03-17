// import React from 'react';
// import {
//     LineChart, Line, BarChart, Bar, PieChart, Pie,
//     XAxis, YAxis, CartesianGrid, Tooltip, Legend, Cell,
//     ResponsiveContainer
// } from 'recharts';
// import {
//     Users, BriefcaseIcon, CheckCircle, Clock,
//     TrendingUp, Calendar
// } from 'lucide-react';
// import './Home.css';

// const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

// const Dashboard = () => {
//     // Sample data - replace with actual data
//     const monthlyHires = [
//         { month: 'Jan', hires: 12 },
//         { month: 'Feb', hires: 19 },
//         { month: 'Mar', hires: 15 },
//         { month: 'Apr', hires: 25 },
//         { month: 'May', hires: 22 },
//         { month: 'Jun', hires: 30 }
//     ];

//     const applicationStatus = [
//         { name: 'Applied', value: 400 },
//         { name: 'Screening', value: 300 },
//         { name: 'Interview', value: 200 },
//         { name: 'Hired', value: 100 }
//     ];

//     const departmentHires = [
//         { department: 'Engineering', count: 45 },
//         { department: 'Sales', count: 30 },
//         { department: 'Marketing', count: 25 },
//         { department: 'HR', count: 15 },
//         { department: 'Design', count: 20 }
//     ];

//     const Header = () => {
//         return (

//             <header className="dashboard-header">
//                 <div className="header-content">
//                     <h1>Applicant Analytics</h1>
//                 </div>
//             </header>
//         )
//     }

//     const BoxInfo = () => {

//         return (
//             <div className="stats-grid">
//                 <div className="stat-card">
//                     <div className="stat-content">
//                         <Users className="stat-icon blue" />
//                         <div className="stat-info">
//                             <p className="stat-label">Total Candidates</p>
//                             <p className="stat-value">1,234</p>
//                         </div>
//                     </div>
//                 </div>

//                 <div className="stat-card">
//                     <div className="stat-content">
//                         <BriefcaseIcon className="stat-icon green" />
//                         <div className="stat-info">
//                             <p className="stat-label">Open Positions</p>
//                             <p className="stat-value">45</p>
//                         </div>
//                     </div>
//                 </div>

//                 <div className="stat-card">
//                     <div className="stat-content">
//                         <CheckCircle className="stat-icon yellow" />
//                         <div className="stat-info">
//                             <p className="stat-label">Hired This Month</p>
//                             <p className="stat-value">28</p>
//                         </div>
//                     </div>
//                 </div>

//                 <div className="stat-card">
//                     <div className="stat-content">
//                         <Clock className="stat-icon purple" />
//                         <div className="stat-info">
//                             <p className="stat-label">Time to Hire</p>
//                             <p className="stat-value">23 days</p>
//                         </div>
//                     </div>
//                 </div>
//             </div>
//         )
//     }

//     const MonthlyHiresChart = () => {
//         return (
//             <div className="chart-card">
//                 <h2>Monthly Hires Trend</h2>
//                 <div className="chart-container">
//                     <ResponsiveContainer width="100%" height="100%">
//                         <LineChart data={monthlyHires}>
//                             <CartesianGrid strokeDasharray="3 3" />
//                             <XAxis dataKey="month" />
//                             <YAxis />
//                             <Tooltip />
//                             <Legend />
//                             <Line type="monotone" dataKey="hires" stroke="#0088FE" strokeWidth={2} />
//                         </LineChart>
//                     </ResponsiveContainer>
//                 </div>
//             </div>
//         )
//     }

//     const ApplicationStatusChart = () => {
//         return (
//             <div className="chart-card">
//                 <h2>Application Status</h2>
//                 <div className="chart-container">
//                     <ResponsiveContainer width="100%" height="100%">
//                         <PieChart>
//                             <Pie
//                                 data={applicationStatus}
//                                 cx="50%"
//                                 cy="50%"
//                                 labelLine={false}
//                                 label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
//                                 outerRadius={80}
//                                 fill="#8884d8"
//                                 dataKey="value"
//                             >
//                                 {applicationStatus.map((entry, index) => (
//                                     <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
//                                 ))}
//                             </Pie>
//                             <Tooltip />
//                             <Legend />
//                         </PieChart>
//                     </ResponsiveContainer>
//                 </div>
//             </div>
//         )
//     }

//     const DepartmentHiresChart = () => {
//         return (
//             <div className="chart-card">
//                 <h2>Department-wise Hires</h2>
//                 <div className="chart-container">
//                     <ResponsiveContainer width="100%" height="100%">
//                         <BarChart data={departmentHires}>
//                             <CartesianGrid strokeDasharray="3 3" />
//                             <XAxis dataKey="department" />
//                             <YAxis />
//                             <Tooltip />
//                             <Legend />
//                             <Bar dataKey="count" fill="#0088FE" />
//                         </BarChart>
//                     </ResponsiveContainer>
//                 </div>
//             </div>
//         )
//     }

//     const Chart = () => {
//         return (
//             <div className="chart-card">
//                 <h2>Recent Activities</h2>
//                 <div className="activities-list">
//                     <div className="activity-item">
//                         <Calendar className="activity-icon" />
//                         <div className="activity-info">
//                             <p className="activity-title">New application received for Senior Developer</p>
//                             <p className="activity-time">2 hours ago</p>
//                         </div>
//                     </div>
//                     <div className="activity-item">
//                         <TrendingUp className="activity-icon" />
//                         <div className="activity-info">
//                             <p className="activity-title">Interview scheduled with John Doe</p>
//                             <p className="activity-time">5 hours ago</p>
//                         </div>
//                     </div>
//                     <div className="activity-item">
//                         <CheckCircle className="activity-icon" />
//                         <div className="activity-info">
//                             <p className="activity-title">Offer accepted by Jane Smith</p>
//                             <p className="activity-time">1 day ago</p>
//                         </div>
//                     </div>
//                 </div>
//             </div>
//         )
//     }

//     return (
//         <div className="dashboard">

//             <Header />

//             <main className="dashboard-main">
//                 {/* Stats Cards */}

//                 <BoxInfo />
//                 {/* Charts */}
//                 <div className="charts-grid">

//                     <MonthlyHiresChart />

//                     <ApplicationStatusChart />

//                     <DepartmentHiresChart />

//                     <Chart />

//                 </div>
//             </main>
//         </div>
//     );
// };

// export default Dashboard;
import React, { useEffect, useState } from 'react';
import ApplicationFunnelChart from './funnelChart';

import {
    LineChart, Line, BarChart, Bar, PieChart, Pie,
    XAxis, YAxis, CartesianGrid, Tooltip, Legend, Cell,
    ResponsiveContainer, FunnelChart, Funnel, LabelList
} from 'recharts';
import {
    Users, BriefcaseIcon, CheckCircle, Clock,
    TrendingUp, Calendar
} from 'lucide-react';
import './Home.css';
import axios from 'axios';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const data = [
    { name: 'Total Applications', value: 300, color: '#4F46E5' },
    { name: 'Shortlisted', value: 120, color: '#0EA5E9' },
    { name: 'Interviewed', value: 45, color: '#14B8A6' },
    { name: 'Hired', value: 10, color: '#22C55E' },
];



const Dashboard = () => {



    const sessionId = localStorage.getItem('sessionId');

    const [totalRecruits, setTotalRecruits] = useState(0);
    const [toalCandidates, setToalCandidates] = useState(0);
    const [totalMailSent, setTotalMailSent] = useState(0);
    const [totalCandidatesRejected, setTotalCandidatesRejected] = useState(0);
    const [recruitsCreatedDate, setRecruitsCreatedDates] = useState([]);




    useEffect(() => {
        const updateData = async () => {
        if (!sessionId) {
        console.warn("Session ID is missing!");
        return; // Stop execution if sessionId is not provided
         }
        
        
        console.log("Fetching data...");
        
        
        try {
        const response = await axios.post(
        "http://localhost:8084/resumeAnalyser_war_exploded/updateHomePage?sessionId=" + sessionId,
         { headers: { "Content-Type": "application/json" } }
         );
        
        
        if (response.data) {
        console.log("Response data:", response.data);
        
        
        // Safely access properties using optional chaining (?.)
        setTotalRecruits(response.data.total_recruits_created ?? 0);
        setToalCandidates(response.data.total_candidates ?? 0);
        setTotalMailSent(response.data.total_email_sent ?? 0);
        setTotalCandidatesRejected(response.data.total_candidates_rejected ?? 0);
        setRecruitsCreatedDates(response.data.createdDates || []);
         }
         } catch (error) {
        console.error("Error fetching data:", error);
         }
         };
        
        
        updateData();
         }, [sessionId]);
    const processRecruitData = (dates) => {
        const monthlyCounts = {};

        dates.forEach(date => {
            // Ensure correct parsing by handling "YYYY-MM-DD HH:MM:SS" format
            const month = new Date(date.replace(" ", "T")).toLocaleString('default', { month: 'short', year: 'numeric' });

            // Increment count for each month
            monthlyCounts[month] = (monthlyCounts[month] || 0) + 1;
        });

        // Convert object to array of { month, count }
        return Object.entries(monthlyCounts).map(([month, count]) => ({ month, count }));
    };

    const chartData = processRecruitData(recruitsCreatedDate);

    console.log(chartData)


    // Sample data - replace with actual data
    // const monthlyHires = [
    //     { month: 'Jan', hires: 12 },
    //     { month: 'Feb', hires: 19 },
    //     { month: 'Mar', hires: 15 },
    //     { month: 'Apr', hires: 25 },
    //     { month: 'May', hires: 22 },
    //     { month: 'Jun', hires: 30 }
    // ];

    const applicationStatus = [
        { name: 'Applied', value: toalCandidates },
        { name: 'Mails sent', value: totalMailSent },
        { name: 'Rejected', value: toalCandidates - totalMailSent}
    ];

    // const departmentHires = [
    //     { department: 'Engineering', count: 45 },
    //     { department: 'Sales', count: 30 },
    //     { department: 'Marketing', count: 25 },
    //     { department: 'HR', count: 15 },
    //     { department: 'Design', count: 20 }
    // ];

    const Header = () => {
        return (

            <header className="dashboard-header">
                <div className="header-content">
                    <h1>Recruitment Dashboard</h1>
                </div>
            </header>
        )
    }

    const BoxInfo = () => {

        return (
            <div className="stats-grid">
                <div className="stat-card">
                    <div className="stat-content">
                        <Users className="stat-icon blue" />
                        <div className="stat-info">
                            <p className="stat-label">Number of Recruits Created</p>
                            <p className="stat-value">{totalRecruits}</p>
                        </div>
                    </div>
                </div>

                <div className="stat-card">
                    <div className="stat-content">
                        <BriefcaseIcon className="stat-icon green" />
                        <div className="stat-info">
                            <p className="stat-label">Number of Resumes Uploaded</p>
                            <p className="stat-value">{toalCandidates}</p>
                        </div>
                    </div>
                </div>

                <div className="stat-card">
                    <div className="stat-content">
                        <CheckCircle className="stat-icon yellow" />
                        <div className="stat-info">
                            <p className="stat-label">Number of Emails Sent</p>
                            <p className="stat-value">{totalMailSent}</p>
                        </div>
                    </div>
                </div>

                <div className="stat-card">
                    <div className="stat-content">
                        <Clock className="stat-icon purple" />
                        <div className="stat-info">
                            <p className="stat-label">Number of Candidates Rejected</p>
                            <p className="stat-value">{toalCandidates - totalMailSent}</p>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

    const MonthlyHiresChart = () => {
        return (
            <div className="chart-card">
                <h2>Monthly Hires Trend</h2>
                <div className="chart-container">
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="month" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Line type="monotone" dataKey="hires" stroke="#0088FE" strokeWidth={2} />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>
        )
    }

    // const ApplicationStatusChart = () => {
    //     return (
    //         <div className="chart-card">
    //             <h2>Application Status</h2>
    //             <div className="chart-container">
    //                 <ResponsiveContainer width="100%" height="100%">
    //                     <PieChart>
    //                         <Pie
    //                             data={applicationStatus}
    //                             cx="50%"
    //                             cy="50%"
    //                             labelLine={false}
    //                             label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
    //                             outerRadius={80}
    //                             fill="#8884d8"
    //                             dataKey="value"
    //                         >
    //                             {applicationStatus.map((entry, index) => (
    //                                 <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
    //                             ))}
    //                         </Pie>
    //                         <Tooltip />
    //                         <Legend />
    //                     </PieChart>
    //                 </ResponsiveContainer>
    //             </div>
    const DepartmentHiresChart = () => {
        return (
            <div className="chart-card">
                <h2>Department-wise Hires</h2>
                <div className="chart-container">
                    <ResponsiveContainer width="100%" height={300}> {/* Set height explicitly */}
                        <BarChart data={applicationStatus}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="name" />  {/* Changed from "department" to "name" */}
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="value" fill="#0088FE" /> {/* Changed from "count" to "value" */}
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>
        );
    }

    // const Chart = ()=>{
    //     return (
    //         <div className="chart-card">
    //                     <h2>Recent Activities</h2>
    //                     <div className="activities-list">
    //                         <div className="activity-item">
    //                             <Calendar className="activity-icon" />
    //                             <div className="activity-info">
    //                                 <p className="activity-title">New application received for Senior Developer</p>
    //                                 <p className="activity-time">2 hours ago</p>
    //                             </div>
    //                         </div>
    //                         <div className="activity-item">
    //                             <TrendingUp className="activity-icon" />
    //                             <div className="activity-info">
    //                                 <p className="activity-title">Interview scheduled with John Doe</p>
    //                                 <p className="activity-time">5 hours ago</p>
    //                             </div>
    //                         </div>
    //                         <div className="activity-item">
    //                             <CheckCircle className="activity-icon" />
    //                             <div className="activity-info">
    //                                 <p className="activity-title">Offer accepted by Jane Smith</p>
    //                                 <p className="activity-time">1 day ago</p>
    //                             </div>
    //                         </div>
    //                     </div>
    //                 </div>
    //     )
    // }

    const FunnelChart = () => {
        return (
            <div style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
                <FunnelChart width={600} height={400}>
                    <Tooltip formatter={(value) => `${value} candidates`} />
                    <Funnel dataKey="value" data={data} isAnimationActive>
                        <LabelList
                            position="right"
                            fill="#fff"
                            stroke="none"
                            dataKey="name"
                        />
                    </Funnel>
                </FunnelChart>
            </div>
        )
    }

    return (
        <div className="dashboard">

            <Header />

            <main className="dashboard-main">
                {/* Stats Cards */}

                <BoxInfo />
                {/* Charts */}
                <div className="charts-grid">

                   <DepartmentHiresChart/>

                    <ApplicationFunnelChart applicationStatus={applicationStatus} totalCandidatesRejected={totalCandidatesRejected} totalCandidates={toalCandidates} totalMailSent={totalMailSent} />


                </div>
            </main>
        </div>
    );
};

export default Dashboard;