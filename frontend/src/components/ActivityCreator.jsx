import { useState } from "react";
import "./ComponentsStyle.css";

function ActivityCreator({ courseId, onActivityCreated }) {
  const [showForm, setShowForm] = useState(false);
  const [name, setName] = useState("");
  const [type, setType] = useState("LECTURE");
  const [loading, setLoading] = useState(false);

  const activityTypes = [
    { value: "LECTURE", label: "📚 Lecture" },
    { value: "PROJECT", label: "📊 Project" },
    { value: "MIDTERM", label: "📝 Midterm Test" },
    { value: "EXAM", label: "✏️ Exam" }
  ];

  const handleCreateActivity = () => {
    if (!name.trim()) {
      alert("Please enter an activity name");
      return;
    }

    setLoading(true);
    fetch(`http://localhost:8080/api/activities/course/${courseId}`, {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include',
      body: JSON.stringify({
        name: name,
        type: type
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
      .then(newActivity => {
        alert("Activity created successfully!");
        setName("");
        setType("LECTURE");
        setShowForm(false);
        if (onActivityCreated) {
          onActivityCreated(newActivity);
        }
      })
      .catch(error => {
        console.error("Error:", error);
        alert("Error creating activity: " + error.message);
      })
      .finally(() => setLoading(false));
  };

  if (!courseId) return null;

  return (
    <div className="activity-container">
      <button
        onClick={() => setShowForm(!showForm)}
        className="activity-creator-toggle-button"
      >
        {showForm ? "Cancel" : "+ Create New Activity"}
      </button>

      {showForm && (
        <div className="activity-creator-form">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Activity name"
            className="activity-creator-input"
          />

          <select
            value={type}
            onChange={(e) => setType(e.target.value)}
            className="activity-creator-select"
          >
            {activityTypes.map(activityType => (
              <option key={activityType.value} value={activityType.value}>
                {activityType.label}
              </option>
            ))}
          </select>
          
          <button 
            onClick={handleCreateActivity} 
            disabled={loading}
            className="activity-creator-submit-button"
          >
            {loading ? "Creating..." : "Create Activity"}
          </button>
        </div>
      )}
    </div>
  ); 
}

export default ActivityCreator;
