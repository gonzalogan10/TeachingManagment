import { useState } from "react";
import "./ComponentsStyle.css";

function CriterionCreator({ onCriterionCreated }) {
  const [showForm, setShowForm] = useState(false);
  const [category, setCategory] = useState("");
  const [name, setName] = useState("");
  const [checkingProcedure, setCheckingProcedure] = useState("");
  const [evaluationMethod, setEvaluationMethod] = useState("PER_STUDENT");
  const [maxPoints, setMaxPoints] = useState("");
  const [pointsRedistribution, setPointsRedistribution] = useState(false);
  const [defaultFeedback, setDefaultFeedback] = useState("");
  const [loading, setLoading] = useState(false);

  const evaluationMethods = [
    { value: "PER_STUDENT", label: "Per Student (individual points)" },
    { value: "PER_TEAM", label: "Per Team (divide among members)" }
  ];

  const handleCreateCriterion = () => {
    if (!category.trim()) {
      alert("Please enter a category");
      return;
    }
    if (!name.trim()) {
      alert("Please enter a criterion name");
      return;
    }
    if (!maxPoints || maxPoints <= 0) {
      alert("Please enter a valid max points value");
      return;
    }

    setLoading(true);
    fetch("http://localhost:8080/api/criteria", {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include',
      body: JSON.stringify({
        category: category,
        name: name,
        checkingProcedure: checkingProcedure,
        evaluationMethod: evaluationMethod,
        maxPoints: parseFloat(maxPoints),
        pointsRedistribution: pointsRedistribution,
        defaultFeedback: defaultFeedback
      })
    })
      .then(response => {
        if (!response.ok) {
          return response.text().then(text => {
            throw new Error(`Server error (${response.status}): ${text}`);
          });
        }
        return response.json();
      })
      .then(newCriterion => {
        // Reset form
        setCategory("");
        setName("");
        setCheckingProcedure("");
        setEvaluationMethod("PER_STUDENT");
        setMaxPoints("");
        setPointsRedistribution(false);
        setDefaultFeedback("");
        setShowForm(false);
        // Pass the new criterion to parent component
        if (onCriterionCreated) {
          onCriterionCreated(newCriterion);
        }
      })
      .catch(error => {
        console.error("Error:", error);
        alert("Error creating criterion: " + error.message);
      })
      .finally(() => setLoading(false));
  };

  return (
    <div className="criterion-container">
      <button
        onClick={() => setShowForm(!showForm)}
        className="criterion-creator-toggle-button"
      >
        {showForm ? "Cancel" : "+ Create New Criterion"}
      </button>

      {showForm && (
        <div className="criterion-creator-form">
          <h4>Create Evaluation Criterion</h4>

          <div className="form-group">
            <label>Category *</label>
            <input
              type="text"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              placeholder="e.g., Technical Skills, Teamwork, Communication"
              className="criterion-creator-input"
            />
          </div>

          <div className="form-group">
            <label>Criterion Name *</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g., Code Quality, Presentation Skills"
              className="criterion-creator-input"
            />
          </div>

          <div className="form-group">
            <label>Checking Procedure (Description)</label>
            <textarea
              value={checkingProcedure}
              onChange={(e) => setCheckingProcedure(e.target.value)}
              placeholder="Describe how to check/verify this criterion..."
              className="criterion-creator-textarea"
              rows="4"
            />
          </div>

          <div className="form-group">
            <label>Evaluation Method *</label>
            <select
              value={evaluationMethod}
              onChange={(e) => setEvaluationMethod(e.target.value)}
              className="criterion-creator-select"
            >
              {evaluationMethods.map(method => (
                <option key={method.value} value={method.value}>
                  {method.label}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Max Points *</label>
            <input
              type="number"
              value={maxPoints}
              onChange={(e) => setMaxPoints(e.target.value)}
              placeholder="e.g., 10"
              className="criterion-creator-input"
              min="0"
              step="0.5"
            />
          </div>

          <div className="form-group">
            <label className="checkbox-label">
              <input
                type="checkbox"
                checked={pointsRedistribution}
                onChange={(e) => setPointsRedistribution(e.target.checked)}
              />
              Allow Points Redistribution (can adjust after evaluation)
            </label>
          </div>

          <div className="form-group">
            <label>Default Feedback (optional)</label>
            <textarea
              value={defaultFeedback}
              onChange={(e) => setDefaultFeedback(e.target.value)}
              placeholder="Predefined feedback text for evaluators..."
              className="criterion-creator-textarea"
              rows="3"
            />
          </div>

          <button
            onClick={handleCreateCriterion}
            disabled={loading}
            className="criterion-creator-submit-button"
          >
            {loading ? "Creating..." : "Create Criterion"}
          </button>
        </div>
      )}
    </div>
  );
}

export default CriterionCreator;
