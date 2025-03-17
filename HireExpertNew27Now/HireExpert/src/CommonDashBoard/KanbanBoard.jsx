import React from 'react'

const KanbanBoard = ({candidates}) => {
    return (
        <div className="kanban-board">
          {Object.entries(candidates).map(([status, list]) => (
            <div key={status} className="kanban-column">
              <div className={`column-header ${status}`}>
                <span>{status.toUpperCase()}</span>
                <span className="counter">{list.length}</span>
              </div>
              <div>
                {list.map((candidate, index) => (
                  <div key={index} className="candidate-card">
                    <img src={candidate.image} alt={candidate.name} className="avatar" />
                    <div className="candidate-info">
                      <h3>{candidate.name}</h3>
                      <p>{candidate.email}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      );
}

export default KanbanBoard