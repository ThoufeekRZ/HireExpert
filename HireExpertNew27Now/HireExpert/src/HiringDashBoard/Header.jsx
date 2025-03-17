import React from 'react'

const Header = ({setIsModalOpen}) => {
    return (
        <div className="board-header">
            <h1 className="board-title">Hiring Board</h1>
            <button
                className="add-button"
                onClick={() => setIsModalOpen(true)}
            >
                + Add Recruit
            </button>
        </div>
    )
}

export default Header