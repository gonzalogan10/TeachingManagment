import { useState, useEffect } from "react";
import "./ComponentsStyle.css";

function TaskReport({ courseId, taskId, onReportCreated }) {
  const [showReportForm, setShowReportForm] = useState(false);
  const [solvers, setSolvers] = useState([]);
  const [selectedSolverId, setSelectedSolverId] = useState("");
  const [reports, setReports] = useState([]);
  const [reportData, setReportData] = useState({
    content: "",
    timeSpent: "",
    proportion: "",
    numStudents: ""
  });

  useEffect(() => {
    if (!taskId) return;

    // Load solvers
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
        setSolvers(Array.isArray(data) ? data : []);
      })
      .catch(error => console.error("Error fetching solvers:", error));

    // Load existing reports for this task
    fetch(`http://localhost:8080/api/reports/task/${taskId}`, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch reports');
        return response.json();
      })
      .then(data => {
        setReports(Array.isArray(data) ? data : []);
      })
      .catch(error => console.error("Error fetching reports:", error));
  }, [taskId]);

  const handleReportInputChange = (e) => {
    const { name, value } = e.target;
    setReportData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmitReport = () => {
    if (!reportData.content.trim()) {
      alert("Please enter a description of the work done");
      return;
    }

    if (!selectedSolverId) {
      alert("Please select a solver");
      return;
    }

    const payload = {
      content: reportData.content,
      timeSpent: reportData.timeSpent || null,
      proportion: reportData.proportion ? parseFloat(reportData.proportion) : 0,
      numStudents: reportData.numStudents ? parseInt(reportData.numStudents) : 0,
      solver: { id: parseInt(selectedSolverId) },
      task: { id: taskId }
    };

    fetch(`http://localhost:8080/api/reports/task/${taskId}/solver/${selectedSolverId}`, {
      method: "POST",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include',
      body: JSON.stringify(payload)
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to submit report');
        return response.json();
      })
      .then(newReport => {
        alert("Report submitted successfully!");
        setReports([...reports, newReport]);
        setReportData({
          content: "",
          timeSpent: "",
          proportion: "",
          numStudents: ""
        });
        setSelectedSolverId("");
        setShowReportForm(false);
        // Notify parent component about the new report
        if (onReportCreated) {
          onReportCreated();
        }
      })
      .catch(error => {
        console.error("Error:", error);
        alert("Error submitting report: " + error.message);
      });
  };

  const handleDeleteReport = (reportId) => {
    if (!window.confirm('Are you sure you want to delete this report?')) {
      return;
    }

    fetch(`http://localhost:8080/api/reports/${reportId}`, {
      method: "DELETE",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error('Failed to delete report');
        alert("Report deleted successfully!");
        setReports(reports.filter(r => r.id !== reportId));
      })
      .catch(error => {
        console.error("Error:", error);
        alert("Error deleting report: " + error.message);
      });
  };

  if (!taskId) {
    return null;
  }

  return (
    <div className="task-container">
      <button
        onClick={() => setShowReportForm(!showReportForm)}
        className="task-report-toggle-button"
      >
        {showReportForm ? "Cancel" : "+ Add Report"}
      </button>

      {showReportForm && (
        <div className="task-report-form">
          <h4>Submit Work Report</h4>

          <div className="task-report-field">
            <label><strong>Select Solver:</strong></label>
            <select
              value={selectedSolverId}
              onChange={(e) => setSelectedSolverId(e.target.value)}
              className="task-report-select"
            >
              <option value="">Choose a solver...</option>
              {solvers.map(solver => (
                <option key={solver.id} value={solver.id}>
                  {solver.name}
                </option>
              ))}
            </select>
          </div>

          <div className="task-report-field">
            <label><strong>What was done? (Description):</strong></label>
            <textarea
              name="content"
              value={reportData.content}
              onChange={handleReportInputChange}
              placeholder="Describe the work completed"
              rows="4"
              className="task-report-textarea"
            />
          </div>

          <div className="task-report-grid">
            <div>
              <label><strong>Hours Spent:</strong></label>
              <input
                type="text"
                name="timeSpent"
                value={reportData.timeSpent}
                onChange={handleReportInputChange}
                placeholder="e.g., 7 hours"
                className="task-report-input"
              />
            </div>
            <div>
              <label><strong>Students Evaluated:</strong></label>
              <input
                type="number"
                name="numStudents"
                value={reportData.numStudents}
                onChange={handleReportInputChange}
                placeholder="e.g., 350"
                className="task-report-input"
              />
            </div>
            <div>
              <label><strong>Proportion of Work:</strong></label>
              <input
                type="text"
                name="proportion"
                value={reportData.proportion}
                onChange={handleReportInputChange}
                placeholder="e.g., 1/6 or 0.166"
                className="task-report-input"
              />
            </div>
          </div>

          <div className="task-report-actions">
            <button
              onClick={handleSubmitReport}
              className="task-report-submit-button"
            >
              Submit Report
            </button>
            <button
              onClick={() => setShowReportForm(false)}
              className="task-report-cancel-button"
            >
              Cancel
            </button>
          </div>
        </div>
      )}

      {reports.length > 0 && (
        <div className="task-reports-list">
          <h4>Reports ({reports.length})</h4>
          {reports.map(report => (
            <div key={report.id} className="task-report-item">
              <div className="task-report-header">
                <div className="task-report-solver">
                  <strong>Solver:</strong> {report.solver?.name}
                </div>
                <button
                  onClick={() => handleDeleteReport(report.id)}
                  className="task-report-delete-button"
                  title="Delete report"
                >
                  ✕
                </button>
              </div>
              <div className="task-report-content">
                <strong>What was done:</strong> {report.content}
              </div>
              {report.timeSpent && (
                <div className="task-report-detail">
                  <strong>Time spent:</strong> {report.timeSpent}
                </div>
              )}
              {report.numStudents > 0 && (
                <div className="task-report-detail">
                  <strong>Students:</strong> {report.numStudents}
                </div>
              )}
              {report.proportion > 0 && (
                <div className="task-report-detail">
                  <strong>Proportion:</strong> {report.proportion}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default TaskReport;
