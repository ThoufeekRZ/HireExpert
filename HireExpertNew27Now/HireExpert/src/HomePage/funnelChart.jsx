import React from 'react';
import { FunnelChart, Funnel, Cell, ResponsiveContainer, Tooltip, LabelList } from 'recharts';
import './funnelChart.css';

const ApplicationFunnelChart = ({ applicationStatus, totalCandidates, totalCandidatesRejected, totalMailSent }) => {
    // Sample data values (you can replace these with actual values)


    // Calculate success rate (non-rejected candidates who received mails)
    const successRate = Math.round(((totalMailSent - totalCandidatesRejected) / totalCandidates) * 100);

    // Professional color palette
    const COLORS = ['#4285F4', '#34A853', '#FBBC05'];

    const CustomTooltip = ({ active, payload }) => {
        if (active && payload && payload.length) {
            return (
                <div className="hire-tooltip">
                    <p className="hire-tooltip-title">{`${payload[0].name}: ${payload[0].value}`}</p>
                    {payload[0].name === 'Applied' && (
                        <p className="hire-tooltip-desc">Total applications received</p>
                    )}
                    {payload[0].name === 'Mails sent' && (
                        <p className="hire-tooltip-desc">{`${Math.round((payload[0].value / totalCandidates) * 100)}% of applicants contacted`}</p>
                    )}
                    {payload[0].name === 'Rejected' && (
                        <p className="hire-tooltip-desc">{`${Math.round((payload[0].value / totalCandidates) * 100)}% of applicants rejected`}</p>
                    )}
                </div>
            );
        }
        return null;
    };

    return (
        <div className="hire-funnel-container">
            <div className="hire-funnel-header">

                <div className="hire-funnel-subtitle-row">
                    <p className="hire-funnel-subtitle">Recruitment pipeline analytics</p>
                    <div className="hire-success-badge">
                        <span>{`${successRate}% Success Rate`}</span>
                    </div>
                </div>
            </div>

            <div className="hire-funnel-chart-container">
                <ResponsiveContainer width="100%" height="100%">
                    <FunnelChart>
                        <Tooltip content={<CustomTooltip />} />
                        <Funnel
                            dataKey="value"
                            data={applicationStatus}
                            isAnimationActive
                            width="80%"
                            paddingAngle={2}
                        >
                            <LabelList
                                position="right"
                                fill="#333"
                                stroke="none"
                                dataKey="name"
                                formatter={(name) => `${name}`}
                            />
                            <LabelList
                                position="left"
                                fill="#666"
                                stroke="none"
                                dataKey="value"
                                formatter={(value) => `${value}`}
                                offset={40}
                            />
                            {
                                applicationStatus.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                ))
                            }
                        </Funnel>
                    </FunnelChart>
                </ResponsiveContainer>
            </div>

            <div className="hire-funnel-legend">
                <div className="hire-legend-items">
                    {applicationStatus.map((entry, index) => (
                        <div key={`legend-${index}`} className="hire-legend-item">
                            <div
                                className="hire-legend-color"
                                style={{ backgroundColor: COLORS[index % COLORS.length] }}
                            />
                            <span className="hire-legend-label">{entry.name}</span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default ApplicationFunnelChart;