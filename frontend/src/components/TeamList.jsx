import { useEffect, useState, forwardRef, useImperativeHandle } from "react";
import "./ComponentsStyle.css";

//forwardRef is used to pass a ref from a parent component to a child component.
//It is needed here since the parent component (ActivityScreen) needs to call a function in the child component 
//(TeamList) to refresh the list of teams after creating a new team.
const TeamList = forwardRef(function TeamList({ courseId, onTeamSelect, selectedTeamId }, ref) {
  const [teams, setTeams] = useState([]);
  const [students, setStudents] = useState([]);
  const [error, setError] = useState(null);
  const [newMemberId, setNewMemberId] = useState({});

  const fetchTeams = () => {
    if (!courseId) return;

    fetch(`http://localhost:8080/api/teams/course/${courseId}`, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch teams');
        return response.json();
      })
      .then(data => {
        console.log('Teams loaded:', data);
        setTeams(Array.isArray(data) ? data : []);
      })
      .catch(error => {
        console.error("Error fetching teams:", error);
        setError(error.message);
      });
  };

  const fetchStudents = () => {
    fetch("http://localhost:8080/api/students", {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch students');
        return response.json();
      })
      .then(data => {
        console.log('Students loaded:', data);
        setStudents(Array.isArray(data) ? data : []);
      })
      .catch(error => {
        console.error("Error fetching students:", error);
      });
  };

  useEffect(() => {
    fetchTeams();
    fetchStudents();
  }, [courseId]);

  // Expose refreshTeams method to parent via ref, meaning that the parent component can call this function to refresh the list of teams.
  // Referesh is needed when a new team is created in the parent component.
  useImperativeHandle(ref, () => ({
    refreshTeams: fetchTeams
  }));

  const handleAddMember = (teamId, studentId) => {
    fetch(`http://localhost:8080/api/team-memberships/team/${teamId}/student/${studentId}`, {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include',
      body: JSON.stringify({
        courseId: courseId
      })
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to add member');
        return response.json();
      })
      .then(updatedTeam => {
        setTeams(teams.map(t => t.id === teamId ? updatedTeam : t));
        setNewMemberId({ ...newMemberId, [teamId]: "" });
        // Refresh teams to ensure we have the latest data
        fetchTeams();
      })
      .catch(error => {
        console.error("Error adding member:", error);
        alert('Failed to add member: ' + error.message);
      });
  };

  // Handle team deletion and creation
  const handleDeleteTeam = (teamId) => {
    if (window.confirm("Are you sure you want to delete this team?")) {
      fetch(`http://localhost:8080/api/teams/${teamId}`, {
        method: "DELETE",
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include'
      })
        .then(response => {
          if (!response.ok) throw new Error('Failed to delete team');
          setTeams(teams.filter(t => t.id !== teamId));
          if (selectedTeamId === teamId) {
            onTeamSelect(null);
          }
        })
        .catch(error => {
          console.error("Error deleting team:", error);
          alert('Failed to delete team: ' + error.message);
        });
    }
  };

  if (!courseId) {
    return null;
  }

  return (
    <div className="list-container">
      <h2 className="team-list-header">
        Teams in Course
      </h2>

      {error && <p className="team-list-error">Error loading teams: {error}</p>}

      {teams.length === 0 && !error ? (
        <p className="team-list-empty">No teams for this course yet</p>
      ) : (
        <ul className="team-list">
          {teams.map(team => (
            <li
              key={team.id}
              className={`team-item ${selectedTeamId === team.id ? 'selected' : ''}`}
            >
              <div onClick={() => {
                const newId = selectedTeamId === team.id ? null : team.id;
                if (onTeamSelect) {
                  onTeamSelect(newId);
                }
              }}>
                <div className="team-header">
                  <div className="team-title-section">
                    <strong className="team-title">
                      {team.name}
                    </strong>
                    <div className="team-member-count">
                      👥 {team.memberships ? team.memberships.length : 0} members
                    </div>
                  </div>
                </div>
              </div>

              {selectedTeamId === team.id && (
                <div className="team-details">
                  <div className="team-members-section">
                    <strong className="team-members-header">
                      Members ({team.memberships ? team.memberships.length : 0})
                    </strong>

                    {team.memberships && team.memberships.length > 0 ? (
                      <div className="team-members-list">
                        {team.memberships.map(membership => (
                          <div key={membership.id} className="team-member-item">
                            • {membership.student.firstName} {membership.student.lastName}
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p className="team-no-members">No members yet</p>
                    )}

                    <div className="team-add-member-section">
                      <p className="team-add-label">
                        <strong>Add Member:</strong>
                      </p>
                      <div className="team-add-controls">
                        <select
                          value={newMemberId[team.id] || ""}
                          onChange={(e) => setNewMemberId({ ...newMemberId, [team.id]: e.target.value })}
                          className="team-add-select"
                        >
                          <option value="">Select a Student</option>
                          {students.map(student => (
                            <option key={student.id} value={student.id}>
                              {student.firstName} {student.lastName}
                            </option>
                          ))}
                        </select>
                        <button
                          onClick={() => {
                            if (newMemberId[team.id]) {
                              handleAddMember(team.id, newMemberId[team.id]);
                            }
                          }}
                          disabled={!newMemberId[team.id]}
                          className="team-add-button"
                        >
                          Add
                        </button>
                      </div>
                    </div>
                  </div>

                  <div className="team-actions">
                    <button
                      onClick={() => handleDeleteTeam(team.id)}
                      className="team-delete-button"
                    >
                      🗑️ Delete Team
                    </button>
                  </div>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
});

TeamList.displayName = 'TeamList';

export default TeamList;
