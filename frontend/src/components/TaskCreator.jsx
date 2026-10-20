import { useState } from "react";
import "./ComponentsStyle.css";

function TaskCreator({ activityId, onTaskCreated }) {
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    type: "LECTURE",
    taskOrder: 1,
    startDate: "",
    endDate: ""
  });
  const [loading, setLoading] = useState(false);

  const taskTypes = [
    { value: "LECTURE", label: "📚 Lecture" },
    { value: "PROJECT", label: "📊 Project" },
    { value: "MIDTERM", label: "📝 Midterm Test" }
  ];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === "taskOrder" ? parseInt(value, 10) : value
    }));
  };

  
  const handleCreateTask = () => {
    // Validate input
    if (!formData.title.trim()) {
      alert("Please enter a task title");
      return;
    }

    setLoading(true);
    fetch(`http://localhost:8080/api/tasks/activity/${activityId}`, {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include',
      body: JSON.stringify({
        title: formData.title,
        description: formData.description,
        type: formData.type,
        taskOrder: formData.taskOrder,
        startDate: formData.startDate || null,
        endDate: formData.endDate || null
      })
    })
      .then(response => {
        //response.ok checks if the response status is in the range 200-299, indicating a successful request.
        //This range means that the request was successfull. It is a standard way to check if a request was successful in HTTP.
        if (!response.ok) throw new Error('Failed to create task');
        return response.json();
      })
      .then(newTask => {
        alert("Task created successfully!");
        setFormData({
          title: "",
          description: "",
          type: "LECTURE",
          taskOrder: 1,
          startDate: "",
          endDate: ""
        });
        setShowForm(false);
        if (onTaskCreated) {
          onTaskCreated(newTask);
        }
      })
      .catch(error => {
        console.error("Error:", error);
        alert("Error creating task: " + error.message);
      })
      .finally(() => setLoading(false));
  };

  if (!activityId) {
    return null;
  }

  return (
    <div className="task-container">
      <button
        type="button"
        onClick={() => setShowForm(!showForm)}
        className="task-creator-toggle-button"
      >
        {showForm ? "Cancel" : "+ Create New Task"}
      </button>

      {showForm && (
        <div className="task-creator-form">
          <h4>Create a New Task</h4>

          <div className="task-creator-field">
            <label><strong>Task Order:</strong></label>
            <input
              type="number"
              name="taskOrder"
              value={formData.taskOrder}
              onChange={handleInputChange}
              placeholder="Task order in activity"
              min="1"
              className="task-creator-input"
            />
          </div>

          <div className="task-creator-field">
            <label><strong>Title:</strong></label>
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              placeholder="Enter task title"
              className="task-creator-input"
            />
          </div>

          <div className="task-creator-field">
            <label><strong>Description:</strong></label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Enter task description"
              rows="4"
              className="task-creator-textarea"
            />
          </div>

          <div className="task-creator-field">
            <label><strong>Type:</strong></label>
            <select
              name="type"
              value={formData.type}
              onChange={handleInputChange}
              className="task-creator-select"
            >
              {taskTypes.map(type => (
                <option key={type.value} value={type.value}>{type.label}</option>
              ))}
            </select>
          </div>

          <div className="task-creator-dates-grid">
            <div>
              <label><strong>Start Date:</strong></label>
              <input
                type="date"
                name="startDate"
                value={formData.startDate}
                onChange={handleInputChange}
                className="task-creator-input"
              />
            </div>
            <div>
              <label><strong>End Date:</strong></label>
              <input
                type="date"
                name="endDate"
                value={formData.endDate}
                onChange={handleInputChange}
                className="task-creator-input"
              />
            </div>
          </div>

          <div className="task-creator-actions">
            <button
              type="button"
              onClick={handleCreateTask}
              disabled={loading}
              className="task-creator-submit-button"
            >
              {loading ? "Creating..." : "Create Task"}
            </button>
            <button
              type="button"
              onClick={() => setShowForm(false)}
              className="task-creator-cancel-button"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default TaskCreator;