import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

const ResponseRateAnalytics = ({ confirmed = 10, declined = 5, noResponse = 5 }) => {
    const total = confirmed + declined + noResponse;

    const getPercentage = (value) => ((value / total) * 100).toFixed(0);

    const confirmedPercentage = getPercentage(confirmed);
    const declinedPercentage = getPercentage(declined);
    const noResponsePercentage = getPercentage(noResponse);

    const rotationAngles = [
        (360 * confirmed) / total,
        (360 * declined) / total,
        (360 * noResponse) / total,
    ];

    const pieData = [
        { name: 'Confirmed', value: confirmed, color: '#4CAF50' },  // Soft Green (Success)
        { name: 'Declined', value: declined, color: '#F44336' },    // Bright Red (Error)
        { name: 'No Response', value: noResponse, color: '#FF9800' } // Warm Orange (Warning)
    ];
    

    return (
        <div className="interv-card">
            <div className="interv-card-title">
                <i className="interv-card-icon">📧</i> Response Rate Analytics
            </div>
            <div className="interv-analytics-container">
                {/* <div className="interv-analytics-chart">
                    <div className="interv-chart-circle">
                        <div
                            className="interv-circle-segment interv-segment-1"
                            style={{ transform: `rotate(${rotationAngles[0]}deg)` }}
                        ></div>
                    </div>
                    <div
                        className="interv-chart-circle"
                        style={{ transform: `rotate(${rotationAngles[1] + rotationAngles[0]}deg)` }}
                    >
                        <div className="interv-circle-segment interv-segment-2"></div>
                    </div>
                    <div
                        className="interv-chart-circle"
                        style={{ transform: `rotate(${rotationAngles[2] + rotationAngles[0] + rotationAngles[1]}deg)` }}
                    >
                        <div className="interv-circle-segment interv-segment-3"></div>
                    </div>
                    <div className="interv-chart-overlay">{total}</div>
                </div> */}

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

                <div className="interv-analytics-stats">
                    <div className="interv-stat-item">
                        <div className="interv-stat-color interv-confirmed"></div>
                        <span>Confirmed: {confirmed} ({confirmedPercentage}%)</span>
                    </div>
                    <div className="interv-stat-item">
                        <div className="interv-stat-color interv-declined"></div>
                        <span>Declined: {declined} ({declinedPercentage}%)</span>
                    </div>
                    <div className="interv-stat-item">
                        <div className="interv-stat-color interv-no-response"></div>
                        <span>No Response: {noResponse} ({noResponsePercentage}%)</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ResponseRateAnalytics;
