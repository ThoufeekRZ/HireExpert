import React from 'react'

const Search = ({searchTerm,setSearchTerm,filterStatus,setFilterStatus}) => {
    return (
        <div className="search-filter-container">
        <div className="search-container">
            <span className="search-icon">🔍</span>
            <input
                type="text"
                placeholder="Search recruits..."
                className="search-input"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
        </div>
        <select
            className="status-filter"
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
        >
            <option value="all">All Status</option>
            <option value="In Progress">In Round One</option>
            <option value="Open">Open</option>
            <option value="Closed">In Round Two</option>
        </select>
    </div>
       )
}

export default Search