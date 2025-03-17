import React, { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import "./recruitTemplate.css"
import { Briefcase, User, FileText, CheckCircle, XCircle, Mail, ChevronDown, ChevronUp } from 'lucide-react';
import DataContext from '../context/DataContext';

const RecruitmentDashboard = ({recruitId,recruitData}) => {
    const [expanded, setExpanded] = useState(false);
    const {setSelectAfterDetailsRecruit} = useContext(DataContext)
    console.log("box "+recruitData);

    const navigate = useNavigate()
    // Sample recruitment data
    // const recruitData = {
    //     title: "Senior Frontend Developer",
    //     maxHire: 5,
    //     description: "We are looking for an experienced Frontend Developer proficient in React.js to join our dynamic team. The ideal candidate will work on developing user-facing features, building reusable components, and optimizing application performance.",
    //     qualifications: [
    //         "Bachelor's degree in Computer Science or related field",
    //         "Strong knowledge of JavaScript, CSS, HTML and frontend frameworks",
    //         "Experience with responsive design and cross-browser compatibility",
    //         "Understanding of server-side rendering and its benefits"
    //     ],
    //     experience: "3+ years of professional experience with React.js",
    //     skillSet: ["React.js", "JavaScript (ES6+)", "Redux", "TypeScript", "HTML5/CSS3", "Responsive Design", "Git", "REST APIs"]
    // };
    

    // Sample application statistics
    // const [totalResume,accepted,reject,totalEmail] = [localStorage.getItem("total_Resume"),localStorage.getItem("accepted"),localStorage.getItem("reject"),localStorage.getItem("mailSented")];
    const applicationStats = {
        total:  recruitData.totalResume,
        rejected: recruitData.reject,
        approved: recruitData.accepted,
        emailSent: recruitData.mailSented
    };

    // Data for bar chart
    const barData = [
        { name: 'Total Resumes', value: applicationStats.total, color: '#4A6FF3' },
        { name: 'Rejected', value: applicationStats.emailSent >0 ? applicationStats.total - applicationStats.emailSent : 0 , color: '#F87171' },
        { name: 'Emails Sent', value: applicationStats.emailSent, color: '#FBBF24' }
    ];

    // Data for pie chart
    const pieData = [
        { name: 'Rejected', value: applicationStats.emailSent >0 ? applicationStats.total - applicationStats.emailSent : 0 , color: '#F87171' },
        { name: 'Emails Sent', value: applicationStats.emailSent, color: '#FBBF24' }
    ];

    // Toggle details section
    const toggleDetails = () => {
        setExpanded(!expanded);
    };

    const handleNavigate = () =>{
        setSelectAfterDetailsRecruit(recruitId)
        console.log(recruitId);
        
        console.log("navigating");
        
        navigate("/dashboard/track")
    }

    return (
        <div className="recruitment-dashboard">
            <div className="dashboard-header">
                <div className="header-main">
                    <div className="header-icon">
                        <Briefcase size={28} />
                    </div>
                    <div className="header-title">
                        <h1>{recruitData.recruitName}</h1>
                        <div className="header-meta">
                            <span className="meta-item">
                                <User size={16} />
                                <span>Max Hire: {recruitData.maximumNumber}</span>
                            </span>
                            <span className="meta-item">
                                <FileText size={16} />
                                <span>Applications: {applicationStats.total}</span>
                            </span>
                        </div>
                    </div>
                </div>
                <div className="header-stats">
                    <div className="stat-card">
                        <div className="stat-icon rejected">
                            <XCircle size={20} />
                        </div>
                        <div className="stat-content">
                            <p>Rejected</p>
                            <h3>{applicationStats.emailSent >0 ? applicationStats.total - applicationStats.emailSent : 0 }</h3>
                        </div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon approved">
                            <CheckCircle size={20} />
                        </div>
                        <div className="stat-content">
                            <p>filtered resumes</p>
                            <h3>{applicationStats.approved}</h3>
                        </div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon emails">
                            <Mail size={20} />
                        </div>
                        <div className="stat-content">
                            <p>Emails Sent</p>
                            <h3>{applicationStats.emailSent}</h3>
                        </div>
                      
                    </div>
                    {recruitData.status === "Closed" && <Link onClick={(e)=>{e.preventDefault();handleNavigate()}} style={{display:"flex", justifyContent:"center",alignItems:"center"}} to={"#"}>Click here to track selected candidates</Link>}
                </div>
            </div>

            <div className="dashboard-body">
                <div className="dashboard-charts">
                    <div className="chart-container bar-chart">
                        <h2>Application Overview</h2>
                        <ResponsiveContainer width="100%" height={250}>
                            <BarChart
                                data={barData}
                                margin={{ top: 20, right: 30, left: 20, bottom: 30 }}
                            >
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip
                                    cursor={{ fill: 'rgba(200, 200, 200, 0.2)' }}
                                    contentStyle={{
                                        backgroundColor: 'rgba(255, 255, 255, 0.9)',
                                        border: '1px solid #ccc',
                                        borderRadius: '8px',
                                        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
                                    }}
                                />
                                <Legend />
                                <Bar dataKey="value" radius={[4, 4, 0, 0]}>
                                    {barData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={entry.color} />
                                    ))}
                                </Bar>
                            </BarChart>
                        </ResponsiveContainer>
                    </div>

                    <div className="chart-container pie-chart">
                        <h2>Application Status</h2>
                        <ResponsiveContainer width="100%" height={250}>
                            <PieChart>
                                <Pie
                                    data={pieData}
                                    cx="50%"
                                    cy="50%"
                                    innerRadius={60}
                                    outerRadius={90}
                                    paddingAngle={2}
                                    dataKey="value"
                                    label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                                    labelLine={false}
                                    animationBegin={300}
                                    animationDuration={1500}
                                >
                                    {pieData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={entry.color} />
                                    ))}
                                </Pie>
                                <Tooltip
                                    formatter={(value, name) => [`${value} applications`, name]}
                                    contentStyle={{
                                        backgroundColor: 'rgba(255, 255, 255, 0.9)',
                                        border: '1px solid #ccc',
                                        borderRadius: '8px',
                                        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
                                    }}
                                />
                            </PieChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                <div className="job-details-section">
                    <div className="section-header" onClick={toggleDetails}>
                        <h2>Job Details</h2>
                        <button className="toggle-btn">
                            {expanded ? <ChevronUp /> : <ChevronDown />}
                        </button>
                    </div>

                    {expanded && (
                        <div className="job-details-content">
                            <div className="detail-block">
                                <h3>Job Description</h3>
                                <p>{recruitData.description}</p>
                            </div>

                            <div className="detail-block">
                                <h3>Qualifications</h3>
                                <ul>
                                    {/* {recruitData.qualifications.map((qual, index) => (
                                        <li key={index}>{qual}</li>
                                    ))} */}
                                    {recruitData.qualification}
                                </ul>
                            </div>

                            <div className="detail-block">
                                <h3>Experience Required</h3>
                                <p>{recruitData.experienceYear}</p>
                            </div>

                            <div className="detail-block">
                                <h3>Skill Set</h3>
                                <div className="skill-tags">
                                    {recruitData.skills.map((skill, index) => (
                                        <span key={index} className="skill-tag">{skill}</span>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default RecruitmentDashboard;