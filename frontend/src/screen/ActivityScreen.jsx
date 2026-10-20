import { useState, useEffect, useRef } from "react";
import { useNavigate } from 'react-router-dom';
import CourseSelector from  "../components/CourseSelector.jsx";
import ActivityCreator from "../components/ActivityCreator.jsx";
import ActivityList from "../components/ActivityList.jsx";
import TaskCreator from "../components/TaskCreator.jsx";
import TaskList from "../components/TaskList.jsx";
import TaskReport from "../components/TaskReport.jsx";
import LoadCalculator from "../components/LoadCalculator.jsx";
import TeamCreator from "../components/TeamCreator.jsx";
import TeamList from "../components/TeamList.jsx";
import './Screen.css';

function ActivityScreen() {
  const [courseId, setCourseId] = useState(null);
  const [selectedActivityId, setSelectedActivityId] = useState(null);
  const [selectedTaskId, setSelectedTaskId] = useState(null);
  const [selectedTeamId, setSelectedTeamId] = useState(null);
  const [taskListRefresh, setTaskListRefresh] = useState(0);
  const [activityListRefresh, setActivityListRefresh] = useState(0);
  const [loadCalculatorRefresh, setLoadCalculatorRefresh] = useState(0);
  const teamListRef = useRef(null);
  const navigate = useNavigate();

  // Reset activity and task when course changes
  useEffect(() => {
    setSelectedActivityId(null);
    setSelectedTaskId(null);
    setSelectedTeamId(null);
  }, [courseId]);

  // Reset task when activity changes
  const handleTeamCreated = () => {
    if (teamListRef.current) {
      teamListRef.current.refreshTeams();
    }
  };

  const handleTaskCreated = () => {
    // Trigger TaskList to refresh
    setTaskListRefresh(prev => prev + 1);
  };

  const handleActivityCreated = () => {
    // Trigger ActivityList to refresh
    setActivityListRefresh(prev => prev + 1);
  };

  const handleReportCreated = () => {
    // Trigger LoadCalculator to refresh
    setLoadCalculatorRefresh(prev => prev + 1);
  };

  return (
    <div>

      <button className="go-to-page-button" onClick={() => navigate('/menu')}>
        Back to Menu
      </button>

      <h1>Teaching Management</h1>
      
      <div className="activity-screen-course-section">
        <h2>Select a Course</h2>
        <CourseSelector onSelect={setCourseId} />
      </div>

      {courseId && (
        <div className="details">
          <h3>Selected Course</h3>
          <p><strong>ID:</strong> {courseId}</p>
        </div>
      )}

      {courseId && (
        <div className="activity-screen-teams-section">
          <h2>Team Management</h2>
          <TeamCreator courseId={courseId} onTeamCreated={handleTeamCreated} />
          <TeamList ref={teamListRef} courseId={courseId} onTeamSelect={setSelectedTeamId} selectedTeamId={selectedTeamId} />
        </div>
      )}

      {courseId && (
        <div className="activity-screen-activities-section">
          <ActivityCreator courseId={courseId} onActivityCreated={handleActivityCreated} />
          <ActivityList courseId={courseId} onActivitySelect={setSelectedActivityId} selectedActivityId={selectedActivityId} refreshTrigger={activityListRefresh} />
        </div>
      )}

      {selectedActivityId && (
        <div className="activity-screen-tasks-section">
          <TaskCreator activityId={selectedActivityId} onTaskCreated={handleTaskCreated} />
          <TaskList activityId={selectedActivityId} onTaskSelect={setSelectedTaskId} selectedTaskId={selectedTaskId} refreshTrigger={taskListRefresh} />
          {selectedTaskId && <TaskReport courseId={courseId} taskId={selectedTaskId} onReportCreated={handleReportCreated} />}
          <LoadCalculator activityId={selectedActivityId} refreshTrigger={loadCalculatorRefresh} />
        </div>
      )}
    </div>
  );
}

export default ActivityScreen;
