import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import CourseSelector from "../components/CourseSelector";
import ActivitySelector from "../components/ActivitySelector";
import TeamSelector from "../components/TeamSelector";
import CriterionFilter from "../components/CriterionFilter";
import CriterionCreator from "../components/CriterionCreator";
import EvaluationForm from "../components/EvaluationForm";
import EvaluationList from "../components/EvaluationList";
import LoadCalculator from "../components/LoadCalculator";
import "./Screen.css";

function EvaluationScreen() {
  const [courseId, setCourseId] = useState(null);
  const [selectedActivity, setSelectedActivity] = useState(null);
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [criteria, setCriteria] = useState([]);
  const [filteredCriteria, setFilteredCriteria] = useState([]);
  const [totalsByCriterion, setTotalsByCriterion] = useState({});
  const [evaluationRefresh, setEvaluationRefresh] = useState(0);
  const [criteriaRefresh, setCriteriaRefresh] = useState(0);
  const navigate = useNavigate();

  // Reset activity and team when course changes
  const handleCourseChange = (newCourseId) => {
    setCourseId(newCourseId);
    setSelectedActivity(null);
    setSelectedTeam(null);
    setCriteria([]);
    setFilteredCriteria([]);
    setTotalsByCriterion({});
  };

  // Load criteria when activity changes
  const handleActivityChange = function (activity) {
    setSelectedActivity(activity);
    setSelectedTeam(null);
    setTotalsByCriterion({});
    setCriteria([]);
    setFilteredCriteria([]);
    
    if (activity && activity.id) {
      fetch("http://localhost:8080/api/criteria", {
        method: "GET",
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include'
      })
        .then(function (response) {
          if (!response.ok) throw new Error('Failed to fetch');
          return response.json();
        })
        .then(function (data) {
          setCriteria(Array.isArray(data) ? data : []);
          setFilteredCriteria(Array.isArray(data) ? data : []);
        })
        .catch(function (error) {
          console.error('Error:', error);
        });
    }
  };

  // Handle points change from forms. It updates totalsByCriterion state.
  function handlePointsChange(criterionId, type, value) {
    const updated = Object.assign({}, totalsByCriterion);
    if (!updated[criterionId]) { updated[criterionId] = { team: 0, studentsSum: 0 }; }
    if (type === 'TEAM') { updated[criterionId].team = isNaN(value) ? 0 : value; }
    if (type === 'STUDENTS_SUM') { updated[criterionId].studentsSum = isNaN(value) ? 0 : value; }
    setTotalsByCriterion(updated);
  }

  // Handle new criterion creation
  function handleCriterionCreated(newCriterion) {
    // Add the new criterion to the existing list
    setCriteria(prevCriteria => [...prevCriteria, newCriterion]);
    setFilteredCriteria(prevFiltered => [...prevFiltered, newCriterion]);
  }

  // Trigger evaluation list refresh when form saves
  function handleEvaluationSaved() {
    console.log('Evaluation saved, refreshing data for team:', selectedTeam?.id, 'activity:', selectedActivity?.id);
    setEvaluationRefresh(evaluationRefresh + 1);
    // Load assessments to update totals - filtered by activity
    if (selectedTeam?.id && selectedActivity?.id) {
      fetch(`http://localhost:8080/api/evaluations/team/${selectedTeam.id}/activity/${selectedActivity.id}`, {
        method: "GET",
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include'
      })
        .then(response => {
          if (!response.ok) throw new Error('Failed to fetch assessments');
          return response.json();
        })
        .then(data => {
          console.log('Assessments after save:', data);
          // Update totals based on saved assessments
          const newTotals = {};
          if (Array.isArray(data) && data.length > 0) {
            const teamSize = data[0].teamSize || 1;
            // Sum all points per criterion (can be different for each member in PER_STUDENT)
            data.forEach(assessment => {
              const criterionId = assessment.criterionId;
              if (criterionId) {
                if (!newTotals[criterionId]) {
                  newTotals[criterionId] = { team: 0, studentsSum: 0 };
                }
                // Add points (works for both PER_STUDENT and PER_TEAM)
                newTotals[criterionId].studentsSum += (assessment.points || 0);
              }
            });
            // Team = total sum, Students = average per student
            Object.keys(newTotals).forEach(criterionId => {
              const totalSum = newTotals[criterionId].studentsSum;
              newTotals[criterionId].team = totalSum;
              newTotals[criterionId].studentsSum = totalSum / teamSize;
            });
          }
          console.log('Updated totals:', newTotals);
          setTotalsByCriterion(newTotals);
        })
        .catch(error => console.error("Error fetching assessments:", error));
    }
  }

  // When team changes, load existing evaluations for the current activity
  const handleTeamChange = (team) => {
    setSelectedTeam(team);
    // Load existing assessments to show totals - filtered by activity
    if (team?.id && selectedActivity?.id) {
      fetch(`http://localhost:8080/api/evaluations/team/${team.id}/activity/${selectedActivity.id}`, {
        method: "GET",
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include'
      })
        .then(response => response.json())
        // Fetch evaluations for the selected team and then update totals
        .then(data => {
          const newTotals = {};
          if (Array.isArray(data) && data.length > 0) {
            //this if condition checks if the data is an array and has at least one element.

            const teamSize = data[0].teamSize || 1;
            // Sum all points per criterion (can be different for each member in PER_STUDENT)
            data.forEach(assessment => {
              const criterionId = assessment.criterionId;
              if (criterionId) {
                if (!newTotals[criterionId]) {
                  newTotals[criterionId] = { team: 0, studentsSum: 0 };
                }
                newTotals[criterionId].studentsSum += (assessment.points || 0);
              }
            });
            // Team = total sum, Students = average per student
            Object.keys(newTotals).forEach(criterionId => {
              const totalSum = newTotals[criterionId].studentsSum;
              newTotals[criterionId].team = totalSum;
              newTotals[criterionId].studentsSum = totalSum / teamSize;
            });
          }
          setTotalsByCriterion(newTotals);
        })
        .catch(error => console.error("Error fetching assessments:", error));
    } else {
      setTotalsByCriterion({});
    }
  };

  // Render evaluation forms for filtered criteria. Rendering is creating React elements.
    function renderForms() {
        const forms = [];
        for (let i = 0; i < filteredCriteria.length; i++) {
            const crit = filteredCriteria[i];
            forms.push(
                <EvaluationForm
                    onSaved={handleEvaluationSaved}
                    key={crit.id}
                    team={selectedTeam}
                    activity={selectedActivity}
                    criterion={crit}
                    onPointsChange={function (type, value) { handlePointsChange(crit.id, type, value); }}
                />
            );
        }
        return forms;
    }

  // Calculate totals
  let teamTotal = 0;
  let studentsTotal = 0;
  for (const key in totalsByCriterion){
    // Check if the key exists in the object (avoid inherited properties)
    if (Object.prototype.hasOwnProperty.call(totalsByCriterion, key)) {
      // Add team points for this criterion to the total
      teamTotal += totalsByCriterion[key].team || 0;
      // Add student points for this criterion to the total
      studentsTotal += totalsByCriterion[key].studentsSum || 0;
    }
  }

  return ( //final return that contains the whole screen. Its httml structured and contains all components used in the screen
    <div>

      <button className="go-to-page-button" onClick={() => navigate('/menu')}>
        Back to Menu
      </button>

      <h1>Evaluate teams</h1>

      <div className="evaluation-screen-section">
        <h3>Create Evaluation Criteria</h3>
        <CriterionCreator onCriterionCreated={handleCriterionCreated} />
      </div>

      <div className="evaluation-screen-section">
        <h3>Course</h3>
        <CourseSelector onSelect={handleCourseChange} />
      </div>

      {courseId && (
        <div className="evaluation-screen-section">
          <h3>Activity</h3>
          <ActivitySelector onSelect={handleActivityChange} selected={selectedActivity} courseId={courseId} />
        </div>
      )}

      {selectedActivity && (
        <div className="evaluation-screen-section">
          <h3>Team</h3>
          <TeamSelector onSelect={handleTeamChange} selected={selectedTeam} enabled={true} courseId={courseId} />
        </div>
      )}

      {selectedActivity && criteria.length > 0 && (
        <div className="evaluation-screen-section">
          <CriterionFilter criteria={criteria} onFilterChange={setFilteredCriteria} />
        </div>
      )}

       {selectedTeam && selectedActivity && (function () {
        return (
          <div className="details">
            <h3>Session totals</h3>
            <p><strong>Team:</strong> {teamTotal}</p>
            <p><strong>Students:</strong> {studentsTotal.toFixed(2)}</p>
          </div>
        );
      })()}

      {selectedTeam && selectedActivity && (
        <div className="evaluation-screen-section">
          <EvaluationList teamId={selectedTeam?.id} activityId={selectedActivity?.id} criteria={criteria} refreshTrigger={evaluationRefresh} />
          {renderForms()}

          {/* Calculadora de carga de trabajo */}
          <LoadCalculator activityId={selectedActivity?.id} />
        </div>
      )}



    </div>
  );
}

export default EvaluationScreen;
