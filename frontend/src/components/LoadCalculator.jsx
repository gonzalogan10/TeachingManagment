import { useEffect, useState } from "react";
import "./ComponentsStyle.css";

function LoadCalculator({ activityId, selectedSolverId, refreshTrigger }) {
  const [load, setLoad] = useState(null);
  const [reports, setReports] = useState([]);
  const [showDetails, setShowDetails] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  

  useEffect(() => {
    if (!activityId) return;

    setLoading(true); //setloading to true when starting the fetch.
    // Needed to show loading indicator
    
    //Promise.all is used to run multiple fetch requests in parallel and wait for all of them to complete.
    //Here, we are fetching both the load data and the reports data for the specified activity.
    Promise.all([
      fetch(`http://localhost:8080/api/activities/${activityId}/load`, {
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include'
      }).then(res => res.json()),
      
      fetch(`http://localhost:8080/api/activities/${activityId}/reports`, {
        headers: {
          "Authorization": "Basic " + btoa("teacher1:test"),
          "Content-Type": "application/json"
        },
        credentials: 'include'
      }).then(res => res.json())
    ])
      .then(([loadData, reportsData]) => {
        setLoad(loadData);
        setReports(Array.isArray(reportsData) ? reportsData : []);
        setError(null);
      })
      .catch(err => {
        console.error("Error fetching load data:", err);
        setError("Error loading calculation data");
      })
      .finally(() => setLoading(false));
  }, [activityId, refreshTrigger]);
  //refreshTrigger to make sure data is re-fetched when needed.

  if (!activityId) return null;

  return (
    <div className="load-calculator-container">
      <button
        onClick={() => setShowDetails(!showDetails)}
        className="load-calculator-toggle-button"
      >
        {showDetails ? "Hide Load Details" : "📊 Show Load Details"}
      </button>

      {showDetails && (
        <div className="load-calculator-panel">
          {loading ? (
            <div className="load-calculator-loading">Loading...</div>
          ) : error ? (
            <div className="load-calculator-error">{error}</div>
          ) : load ? (
            <div className="load-calculator-content">
              <h4>Activity Load Calculation</h4>
              
              <div className="load-calculator-summary">
                <div className="load-item">
                  <span className="load-label">⏱️ Total Hours:</span>
                  <span className="load-value">{load.totalHours}</span>
                </div>
                <div className="load-item">
                  <span className="load-label">👥 Total Students:</span>
                  <span className="load-value">{load.totalStudents}</span>
                </div>
                <div className="load-item">
                  <span className="load-label">📈 Total Proportion:</span>
                  <span className="load-value">{load.totalProportion.toFixed(2)}</span>
                </div>
              </div>

              <div className="load-calculator-details">
                <h5>Detailed Reports</h5>
                {reports.length > 0 ? (
                  <div className="load-reports-list">
                    {reports.map((report, idx) => (
                      <div key={idx} className="load-report-item">
                        <div className="load-report-header">
                          <span className="load-report-solver">
                            {report.solver?.name || "Unknown Solver"}
                          </span>
                        </div>
                        <div className="load-report-body">
                          <p><strong>Content:</strong> {report.content || "N/A"}</p>
                          <p><strong>Time Spent:</strong> {report.timeSpent || "N/A"}</p>
                          <p><strong>Students Evaluated:</strong> {report.numStudents}</p>
                          <p><strong>Proportion:</strong> {report.proportion.toFixed(2)}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="load-no-reports">No reports available for this activity</p>
                )}
              </div>
            </div>
          ) : null}
        </div>
      )}
    </div>
  );
}

export default LoadCalculator;
