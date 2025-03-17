import React from 'react'

const Header = () => {
   return (
    <header>
      <div className="header-top">
        <button className="back-button">← Back to Job List</button>
        <h1>UI/UX Designer</h1>
        <div className="search-container">
          <input type="text" placeholder="Search keyword..." />
        </div>
      </div>
    </header>
  );
}

export default Header