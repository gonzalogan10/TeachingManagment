import { useEffect, useState } from "react";
import "./ComponentsStyle.css";

function EvaluationList({ teamId, activityId, criteria, refreshTrigger }) {
  const [assessments, setAssessments] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!teamId || !activityId) return;

    setLoading(true);
    fetch(`http://localhost:8080/api/evaluations/team/${teamId}/activity/${activityId}`, {
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
        console.log('Assessments loaded:', data);
        setAssessments(Array.isArray(data) ? data : []);
      })
      .catch(error => {
        console.error("Error fetching assessments:", error);
        setAssessments([]);
      })
      .finally(() => setLoading(false));
  }, [teamId, activityId, refreshTrigger]);

  // Get criterion name by id
  const getCriterionName = (criterionId) => {
    const criterion = criteria.find(c => c.id === criterionId);
    return criterion ? criterion.name : `Criterion ${criterionId}`;
  };

  // Group assessments by criterion
  const groupedByCriterion = {};
  const teamSize = assessments.length > 0 ? assessments[0].teamSize || 1 : 1;
  
  assessments.forEach(assessment => {
    const criterionId = assessment.criterionId;
    if (!groupedByCriterion[criterionId]) {
      groupedByCriterion[criterionId] = [];
    }
    groupedByCriterion[criterionId].push(assessment);
  });

  if (loading) {
    return <p className="evaluation-list-loading">Loading assessments...</p>;
  }

  if (assessments.length === 0) {
    return <p className="evaluation-list-empty">No evaluations saved yet</p>;
  }

  return (
    <div className="evaluation-list-container">
      <h3>Saved Evaluations</h3>
      {Object.entries(groupedByCriterion).map(([criterionId, assessmentList]) => (
        <div key={criterionId} className="evaluation-criterion-group">
          <h4>{getCriterionName(parseInt(criterionId))}</h4>
          <div className="evaluation-assessments">
            {assessmentList.map((assessment, idx) => (
              <div key={assessment.id || idx} className="evaluation-assessment-item">
                <div className="evaluation-student-name">
                  {assessment.studentName || "Student"}
                </div>
                <div className="evaluation-points-display">
                  {assessment.points} pts
                </div>
                {assessment.feedback && (
                  <div className="evaluation-feedback-display">
                    {assessment.feedback}
                  </div>
                )}
              </div>
            ))}
          </div>
          <div className="evaluation-criterion-total">
            <span>Students: {assessmentList.reduce((sum, a) => sum + (a.points || 0), 0) / teamSize} pts</span>
            <span> | Team: {assessmentList.reduce((sum, a) => sum + (a.points || 0), 0)} pts</span>
          </div>
        </div>
      ))}
    </div>
  );
}

export default EvaluationList;
