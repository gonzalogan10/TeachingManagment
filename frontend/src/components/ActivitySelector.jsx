import { useEffect, useState } from "react";
import "./ComponentsStyle.css";

function ActivitySelector(props) {
  // props: onSelect, selected, courseId
  const [activities, setActivities] = useState([]);

  useEffect(function () {
    if (!props.courseId) {
      setActivities([]);
      return;
    }

    fetch(`http://localhost:8080/api/activities/course/${props.courseId}`, {
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
        setActivities(Array.isArray(data) ? data : []);
      })
      .catch(function (error) {
        console.error('Error:', error);
      });
  }, [props.courseId]);


  function handleChange(e) {
    const id = Number(e.target.value);
    const act = activities.find(a => a.id === id) || null;
    if (typeof props.onSelect === 'function') {
      props.onSelect(act);
    }
  }

  return (
    <select value={props.selected?.id || ""} onChange={handleChange} disabled={!props.courseId}>
      <option value="">Select activity</option>
      {activities.map(a => (
        <option key={a.id} value={a.id}>{a.name}</option>
      ))}
    </select>
  );
}

export default ActivitySelector;
