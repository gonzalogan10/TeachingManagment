import { useEffect, useState } from "react";
import "./ComponentsStyle.css";

function TeamSelector(props) {
  // props: onSelect, selected, enabled, courseId
  const [teams, setTeams] = useState([]);

  useEffect(function () {
    if (!props.courseId) {
      setTeams([]);
      return;
    }

    fetch(`http://localhost:8080/api/teams/course/${props.courseId}`, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
      //then parse the response as json. It is necessary to convert the response to json format to be able to work with the data
      //function () means that the function is anonymous and does not have a name
      .then(function (response) {
        if (!response.ok) throw new Error('Failed to fetch');
        return response.json();
      })
      .then(function (data) {
        setTeams(Array.isArray(data) ? data : []);
      })
      .catch(function (error) {
        console.error('Error:', error);
      });
  }, [props.courseId]);

  // Handle team selection
  function handleSelect(team) {
    if (typeof props.onSelect === 'function') {
      props.onSelect(team);
    }
  }

  return (
    <div className="team-selector">
      { // === compares both value and type. If both are the same, it returns No teams.
        teams.length === 0 ? <p>No teams</p> : null}

      {//Map means to iterate over the array of teams and create a button for each team.
        teams.map(t => (
          <button key={t.id}
            disabled={!props.enabled}
            onClick={function () { handleSelect(t); }}
            className={props.selected?.id === t.id ? 'team-button-selected' : ''}
          >
            {t.name}
          </button>
        ))
      }
    </div>
  );
}

export default TeamSelector;
