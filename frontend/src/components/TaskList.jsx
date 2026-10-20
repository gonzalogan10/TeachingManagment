import { useEffect, useState } from "react";
import "./ComponentsStyle.css";

function TaskList({ activityId, onTaskSelect, selectedTaskId, refreshTrigger }) {
  const [tasks, setTasks] = useState([]);
  const [solvers, setSolvers] = useState([]);
  const [error, setError] = useState(null);
  const [newSolverId, setNewSolverId] = useState({});

  useEffect(() => {
    if (!activityId) return;

    // Load tasks for this activity
    fetch(`http://localhost:8080/api/tasks/activity/${activityId}`, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch tasks');
        return response.json();
      })
      .then(data => {
        console.log('Tasks loaded:', data);
        setTasks(Array.isArray(data) ? data : []);
      })
      .catch(error => {
        console.error("Error fetching tasks:", error);
        setError(error.message);
      });

    // Load all solvers for assignment
    fetch("http://localhost:8080/api/solvers", {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch solvers');
        return response.json();
      })
      .then(data => {
        console.log('Solvers loaded:', data);
        setSolvers(Array.isArray(data) ? data : []);
      })
      .catch(error => {
        console.error("Error fetching solvers:", error);
      });
  }, [activityId, refreshTrigger]);

  const handleAddSolver = (taskId, solverId) => {
    // Check if solver is already in the task
    const task = tasks.find(t => t.id === taskId);
    const solverIdNum = parseInt(solverId);
    
    if (task && task.solvers && task.solvers.some(s => s.id === solverIdNum)) {
      alert('This solver is already assigned to this task');
      return;
    }
    
    if (task && task.mainSolver && task.mainSolver.id === solverIdNum) {
      alert('This solver is already the main solver for this task');
      return;
    }

    fetch(`http://localhost:8080/api/solvers/${solverId}/tasks/${taskId}`, {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to add solver');
        return response.text();
      })
      .then(() => {
        // Reload tasks to get updated data
        return fetch(`http://localhost:8080/api/tasks/activity/${activityId}`, {
          method: "GET",
          headers: {
            "Authorization": "Basic " + btoa("teacher1:test"),
            "Content-Type": "application/json"
          },
          credentials: 'include'
        });
      })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch updated tasks');
        return response.json();
      })
      .then(data => {
        setTasks(Array.isArray(data) ? data : []);
        setNewSolverId({ ...newSolverId, [taskId]: "" });
        alert('Solver added successfully!');
      })
      .catch(error => {
        console.error("Error adding solver:", error);
        alert('Failed to add solver: ' + error.message);
      });
  };

  const handleRemoveSolver = (taskId, solverId) => {
    if (!window.confirm('Are you sure you want to remove this solver?')) {
      return;
    }

    fetch(`http://localhost:8080/api/solvers/${solverId}/tasks/${taskId}`, {
      method: "DELETE",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to remove solver');
        return response.text();
      })
      .then(() => {
        // Reload tasks to get updated data
        return fetch(`http://localhost:8080/api/tasks/activity/${activityId}`, {
          method: "GET",
          headers: {
            "Authorization": "Basic " + btoa("teacher1:test"),
            "Content-Type": "application/json"
          },
          credentials: 'include'
        });
      })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch updated tasks');
        return response.json();
      })
      .then(data => {
        setTasks(Array.isArray(data) ? data : []);
      })
      .catch(error => {
        console.error("Error removing solver:", error);
        alert('Failed to remove solver: ' + error.message);
      });
  };

  const handleBecomeMainSolver = (taskId, solverId) => {
    fetch(`http://localhost:8080/api/solvers/${solverId}/main-solver/${taskId}`, {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to assign solver');
        return response.text();
      })
      .then(() => {
        // Reload tasks to get updated data
        return fetch(`http://localhost:8080/api/tasks/activity/${activityId}`, {
          method: "GET",
          headers: {
            "Authorization": "Basic " + btoa("teacher1:test"),
            "Content-Type": "application/json"
          },
          credentials: 'include'
        });
      })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch updated tasks');
        return response.json();
      })
      .then(data => {
        setTasks(Array.isArray(data) ? data : []);
        setNewSolverId({ ...newSolverId, [taskId]: "" });
      })
      .catch(error => {
        console.error("Error assigning solver:", error);
        alert('Failed to assign solver: ' + error.message);
      });
  };

  const handleDeleteTask = (taskId) => {
    if (!window.confirm('Are you sure you want to delete this task?')) {
      return;
    }

    fetch(`http://localhost:8080/api/tasks/${taskId}`, {
      method: "DELETE",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to delete task');
        return response.text();
      })
      .then(() => {
        // Reload tasks to get updated data
        return fetch(`http://localhost:8080/api/tasks/activity/${activityId}`, {
          method: "GET",
          headers: {
            "Authorization": "Basic " + btoa("teacher1:test"),
            "Content-Type": "application/json"
          },
          credentials: 'include'
        });
      })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch updated tasks');
        return response.json();
      })
      .then(data => {
        setTasks(Array.isArray(data) ? data : []);
        if (selectedTaskId === taskId) {
          if (onTaskSelect) {
            onTaskSelect(null);
          }
        }
      })
      .catch(error => {
        console.error("Error deleting task:", error);
        alert('Failed to delete task: ' + error.message);
      });
  };

  if (!activityId) {
    return null;
  }

const getTaskTypeLabel = (type) => {
    const typeMap = {
      'LECTURE': '📚 Lecture',
      'PROJECT': '📊 Project',
      'MIDTERM': '📝 Midterm Test'
    };
    return typeMap[type] || type;
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Not set';
    return new Date(dateString).toLocaleDateString('en-US');
  };

  const getTotalSolversCount = (task) => {
    if (!task) return 0;
    let count = task.solvers?.length || 0;
    // If mainSolver exists and is not already in the solvers list, count it
    if (task.mainSolver && !task.solvers?.some(s => s.id === task.mainSolver.id)) {
      count += 1;
    }
    return count;
  };

  return (
    <div className="list-container">
      <h3 className="task-list-header">
        Tasks
      </h3>

      {error && <p className="task-list-error">Error loading tasks: {error}</p>}

      {tasks.length === 0 && !error ? (
        <p className="task-list-empty">No tasks for this activity yet</p>
      ) : (
        <ul className="task-list">
          {tasks.map(task => (
            <li
              key={task.id}
              className={`task-item ${selectedTaskId === task.id ? 'selected' : ''}`}
            >
              <div onClick={() => {
                const newId = selectedTaskId === task.id ? null : task.id;
                if (onTaskSelect) {
                  onTaskSelect(newId);
                }
              }}>
                <div className="task-header">
                  <div className="task-title-section">
                    <strong className="task-title">
                      {task.taskOrder}. {task.title}
                    </strong>
                    <div className="task-type">
                      {getTaskTypeLabel(task.type || 'LECTURE')}
                    </div>
                  </div>
                  <div className="task-actions-section">
                    <div className="task-solver-info">
                      {task.mainSolver ? (
                        <span className="task-has-solver">
                          👤 {task.mainSolver.name}
                        </span>
                      ) : (
                        <span className="task-no-solver">
                          ⚠️ No Main Solver
                        </span>
                      )}
                    </div>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleDeleteTask(task.id);
                      }}
                      className="task-delete-button"
                      title="Delete task"
                    >
                      🗑️
                    </button>
                  </div>
                </div>
              </div>

              {selectedTaskId === task.id && (
                <div className="task-details">
                  {task.description && (
                    <p className="task-description">
                      <strong>Description:</strong> {task.description}
                    </p>
                  )}

                  <div className="task-dates-grid">
                    <div>
                      <p className="task-date-info">
                        <strong>Start Date:</strong> {formatDate(task.startDate)}
                      </p>
                    </div>
                    <div>
                      <p className="task-date-info">
                        <strong>End Date:</strong> {formatDate(task.endDate)}
                      </p>
                    </div>
                  </div>

                  <div className="task-solvers-section">
                    <strong className="task-solvers-header">
                      Solvers ({getTotalSolversCount(task)})
                    </strong>

                    {task.mainSolver && (
                      <div className="task-main-solver">
                        <strong>Main Solver:</strong> {task.mainSolver.name}
                        <button
                          onClick={() => handleRemoveSolver(task.id, task.mainSolver.id)}
                          className="task-solver-remove-button"
                          title="Remove solver"
                        >
                          ✕
                        </button>
                      </div>
                    )}

                    {task.solvers && task.solvers.length > 0 && (
                      <div className="task-other-solvers-section">
                        <p className="task-other-solvers-label">
                          <strong>Other Solvers:</strong>
                        </p>
                        {task.solvers.filter(s => s.id !== task.mainSolver?.id).map(solver => (
                          <div key={solver.id} className="task-other-solver-item">
                            <span>• {solver.name}</span>
                            <button
                              onClick={() => handleRemoveSolver(task.id, solver.id)}
                              className="task-solver-remove-button"
                              title="Remove solver"
                            >
                              ✕
                            </button>
                          </div>
                        ))}
                      </div>
                    )}

                    <div className="task-add-solver-section">
                      <p className="task-add-solver-label">
                        <strong>Add Solver:</strong>
                      </p>
                      <div className="task-add-solver-controls">
                        <select
                          value={newSolverId[task.id] || ""}
                          onChange={(e) => setNewSolverId({ ...newSolverId, [task.id]: e.target.value })}
                          className="task-add-solver-select"
                        >
                          <option value="">Select a Solver</option>
                          {solvers.filter(solver => 
                            !task.solvers?.some(s => s.id === solver.id) && 
                            (!task.mainSolver || task.mainSolver.id !== solver.id)
                          ).map(solver => (
                            <option key={solver.id} value={solver.id}>
                              {solver.name}
                            </option>
                          ))}
                        </select>
                        <button
                          onClick={() => {
                            if (newSolverId[task.id]) {
                              handleAddSolver(task.id, newSolverId[task.id]);
                            }
                          }}
                          disabled={!newSolverId[task.id]}
                          className="task-add-solver-button"
                        >
                          + Add Solver
                        </button>
                      </div>
                    </div>

                    <div className="task-assign-section">
                      <p className="task-assign-label">
                        <strong>Become Main Solver:</strong>
                      </p>
                      <div className="task-assign-controls">
                        <select
                          value={newSolverId[task.id] || ""}
                          onChange={(e) => setNewSolverId({ ...newSolverId, [task.id]: e.target.value })}
                          className="task-assign-select"
                        >
                          <option value="">Select a Solver</option>
                          {solvers.filter(solver => 
                            !task.solvers?.some(s => s.id === solver.id) && 
                            (!task.mainSolver || task.mainSolver.id !== solver.id)
                          ).map(solver => (
                            <option key={solver.id} value={solver.id}>
                              {solver.name}
                            </option>
                          ))}
                        </select>
                        <button
                          onClick={() => {
                            if (newSolverId[task.id]) {
                              handleBecomeMainSolver(task.id, newSolverId[task.id]);
                            }
                          }}
                          disabled={!newSolverId[task.id]}
                          className="task-assign-button"
                        >
                          Assign
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default TaskList;
