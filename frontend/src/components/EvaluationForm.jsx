import { useState, useEffect } from "react";
import "./ComponentsStyle.css";

function EvaluationForm(props) {
  // For PER_STUDENT evaluations
  const [studentPoints, setStudentPoints] = useState({}); // { studentId: points }
  
  // For PER_TEAM evaluations
  const [points, setPoints] = useState(0);
  
  const [feedback, setFeedback] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [isEvaluated, setIsEvaluated] = useState(false);
  const [checkRefresh, setCheckRefresh] = useState(0);
  const [teamMembers, setTeamMembers] = useState([]);
  const [loadingMembers, setLoadingMembers] = useState(false);
  const [existingFeedback, setExistingFeedback] = useState("");

  // Determine if this is per-student or per-team evaluation
  const isPerStudent = props.criterion?.evaluationMethod === "PER_STUDENT";

  // Load team members ONLY for PER_STUDENT evaluations
  useEffect(() => {
    if (!props.team?.id || !isPerStudent) {
      setTeamMembers([]);
      setStudentPoints({});
      return;
    }

    setLoadingMembers(true);
    fetch(`http://localhost:8080/api/teams/${props.team.id}`, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to load team members');
        return response.json();
      })
      .then(data => {
        console.log('Team data loaded:', data);
        const members = data.memberships || [];
        setTeamMembers(members);
        // Initialize student points with 0 for all members
        const initialPoints = {};
        members.forEach(membership => {
          const studentId = membership.student?.id || membership.id;
          if (studentId) {
            initialPoints[studentId] = 0;
          }
        });
        setStudentPoints(initialPoints);
        setLoadingMembers(false);
      })
      .catch(error => {
        console.error("Error loading team members:", error);
        setTeamMembers([]);
        setStudentPoints({});
        setLoadingMembers(false);
      });
  }, [props.team?.id, isPerStudent]);

  // Check if this criterion is already evaluated and load existing values
  useEffect(() => {
    if (!props.team?.id || !props.criterion?.id || !props.activity?.id) return;

    fetch(`http://localhost:8080/api/evaluations/criterion/${props.criterion.id}/team/${props.team.id}/activity/${props.activity.id}`, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to check evaluation');
        return response.json();
      })
      .then(data => {
        // Check if response is an array with [false] or an object/array with assessment data
        if (Array.isArray(data) && data[0] === false) {
          // No evaluation exists
          setIsEvaluated(false);
          if (isPerStudent) {
            setStudentPoints({});
          } else {
            setPoints(0);
          }
          setFeedback("");
        } else if (Array.isArray(data) && data.length > 0) {
          // Array of assessments returned (per-student)
          setIsEvaluated(true);
          const pointsByStudent = {};
          data.forEach(assessment => {
            const studentId = assessment.studentId;
            pointsByStudent[studentId] = assessment.points || 0;
          });
          if (isPerStudent) {
            setStudentPoints(pointsByStudent);
          } else {
            // For team, take first assessment's points
            setPoints(data[0]?.points || 0);
          }
          setExistingFeedback(data[0]?.feedback || "");
          setFeedback(data[0]?.feedback || "");
        } else if (data && data.id) {
          // Single assessment returned
          setIsEvaluated(true);
          if (isPerStudent) {
            setStudentPoints({ [data.studentId]: data.points || 0 });
          } else {
            setPoints(data.points || 0);
          }
          setExistingFeedback(data.feedback || "");
          setFeedback(data.feedback || "");
        } else {
          // No evaluation
          setIsEvaluated(false);
          if (isPerStudent) {
            setStudentPoints({});
          } else {
            setPoints(0);
          }
          setFeedback("");
        }
      })
      .catch(error => {
        console.error("Error checking evaluation:", error);
        setIsEvaluated(false);
      });
  }, [props.team?.id, props.criterion?.id, props.activity?.id, checkRefresh, isPerStudent]);

  // Check if redistribution is allowed
  const canRedistribute = props.criterion?.pointsRedistribution === true;
  const isLockedForEdit = isEvaluated && !canRedistribute;

  function submitEvaluation(event) {
    event.preventDefault();
    
    if (!props.team?.id || !props.criterion?.id) return;
    
    // Check if trying to edit without redistribution permission
    if (isLockedForEdit) {
      setMessage("⛔ Cannot edit: Points redistribution is not allowed for this criterion");
      return;
    }
    
    setMessage("");
    setIsSaving(true);

    // If already evaluated and redistribution allowed, use PUT to update
    if (isEvaluated && canRedistribute) {
      const evaluation = isPerStudent
        ? { points: studentPoints, feedback: feedback }
        : { points: parseFloat(points) || 0, feedback: feedback };

      fetch(`http://localhost:8080/api/evaluations/criterion/${props.criterion.id}/team/${props.team.id}`, {
        method: "PUT",
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include',
        body: JSON.stringify(evaluation)
      })
        .then(function (r) {
          setIsSaving(false);
          if (!r.ok) {
            return r.text().then(text => {
              setMessage("Error updating: " + (text || "Failed to update"));
            });
          }
          setMessage("✓ Updated successfully");
          setExistingFeedback(feedback);
          // Call callback to refresh evaluation list
          if (typeof props.onSaved === 'function') {
            props.onSaved();
          }
        })
        .catch(function (error) {
          setIsSaving(false);
          setMessage("Error: " + error.message);
        });
    } else {
      // Create new evaluation
      const evaluation = isPerStudent
        ? {
            teamId: props.team.id,
            activityId: props.activity?.id,
            criterionId: props.criterion.id,
            points: studentPoints,
            feedback: feedback
          }
        : {
            teamId: props.team.id,
            activityId: props.activity?.id,
            criterionId: props.criterion.id,
            points: parseFloat(points) || 0,
            feedback: feedback
          };

      fetch("http://localhost:8080/api/evaluations", {
        method: "POST",
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include',
        body: JSON.stringify(evaluation)
      })
        .then(function (r) {
          setIsSaving(false);
          if (!r.ok) {
            return r.text().then(text => {
              setMessage("Error: " + text || "Failed to save");
            });
          }
          return r.json().then(data => {
            setMessage("✓ Saved");
            // Mark as evaluated
            setIsEvaluated(true);
            setExistingFeedback(feedback);
            setCheckRefresh(checkRefresh + 1);
            // Call callback to refresh evaluation list
            if (typeof props.onSaved === 'function') {
              props.onSaved();
            }
          });
        })
        .catch(function (error) {
          setIsSaving(false);
          setMessage("Error: " + error.message);
        });
    }
  }

  function handleStudentPointsChange(studentId, value) {
    const max = props.criterion.maxPoints || 0;
    let num = parseFloat(value) || 0;
    if (num > max) { num = max; }
    
    setStudentPoints(prev => ({
      ...prev,
      [studentId]: num
    }));
    
    if (typeof props.onPointsChange === 'function') {
      props.onPointsChange(studentId, num);
    }
  }

  function handlePointsChange(e) {
    const max = props.criterion.maxPoints || 0;
    let num = parseFloat(e.target.value) || 0;
    if (num > max) { num = max; }
    setPoints(num);
    if (typeof props.onPointsChange === 'function') {
      props.onPointsChange('TEAM', num);
    }
  }

  // Determine button text and state
  let buttonText = "Save";
  let buttonDisabled = isSaving;

  if (isLockedForEdit) {
    buttonText = "Locked (No Redistribution)";
    buttonDisabled = true;
  } else if (isEvaluated && canRedistribute) {
    buttonText = isSaving ? "Updating..." : "Update";
    buttonDisabled = isSaving;
  } else if (isEvaluated) {
    buttonText = "Already Evaluated";
    buttonDisabled = true;
  } else if (isSaving) {
    buttonText = "Saving...";
  }

  return (
    <form className="evaluation-form" onSubmit={submitEvaluation}>
      <h4>{props.criterion.name}</h4>
      <p>Max points: {props.criterion.maxPoints}</p>
      
      {isEvaluated && (
        <div className={canRedistribute ? "evaluation-already-evaluated editable" : "evaluation-already-evaluated locked"}>
          {canRedistribute ? 
            "✓ Evaluated (editable)" : 
            "✓ Evaluated (locked - no redistribution)"}
        </div>
      )}

      {/* PER_STUDENT evaluation */}
      {isPerStudent ? (
        <>
          {loadingMembers ? (
            <p>Loading team members...</p>
          ) : teamMembers.length === 0 ? (
            <p className="evaluation-message">No team members found</p>
          ) : (
            <div className="evaluation-students-container">
              {teamMembers.map(membership => {
                const studentId = membership.student?.id;
                const firstName = membership.student?.firstName || '';
                const lastName = membership.student?.lastName || '';
                return (
                  <div key={studentId} className="evaluation-student-row">
                    <label>{firstName} {lastName}</label>
                    <input
                      className="evaluation-points-given"
                      type="number"
                      value={studentPoints[studentId] || 0}
                      onChange={(e) => handleStudentPointsChange(studentId, e.target.value)}
                      min="0"
                      max={props.criterion.maxPoints}
                      step="0.5"
                      disabled={isLockedForEdit}
                    />
                  </div>
                );
              })}
            </div>
          )}
        </>
      ) : (
        /* PER_TEAM evaluation */
        <input
          className="evaluation-points-given"
          type="number"
          value={points}
          onChange={handlePointsChange}
          min="0"
          max={props.criterion.maxPoints}
          step="0.5"
          disabled={isLockedForEdit}
        />
      )}

      <textarea
        className="evaluation-feedback"
        placeholder="Feedback"
        value={feedback}
        onChange={function (e) { setFeedback(e.target.value); }}
        disabled={isLockedForEdit}
      />

      {message && <p className="evaluation-message">{message}</p>}

      <button className="evaluation-submit" disabled={buttonDisabled}>
        {buttonText}
      </button>
    </form>
  );
}

export default EvaluationForm;