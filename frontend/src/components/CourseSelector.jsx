import { useEffect, useState } from "react";
import "./ComponentsStyle.css";
//useEffect when using directly, useState for a value that changes over time

function CourseSelector(courseProps) {
  const [courses, setCourses] = useState([]);
  const [selectedValue, setSelectedValue] = useState("");
  const [error, setError] = useState(null);

  useEffect(() => { //lamda function
    fetch("http://localhost:8080/api/courses", {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa("teacher1:test"),
        "Content-Type": "application/json"
      },
      credentials: 'include'
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to fetch courses: ' + response.status);
      }
      return response.json();
    })
    .then(data => {
      console.log('Courses loaded:', data);
      setCourses(Array.isArray(data) ? data : []);
    })
    .catch(error => {
      console.error('Error fetching courses:', error);
      setError(error.message);
    });}, 
    []);

  const handleChange = (event) => {
    const value = event.target.value;
    setSelectedValue(value);
    courseProps.onSelect(value);
  };

  return (
    <div>
      {error && <p className="course-selector-error">Error: {error}</p>}
      <select value={selectedValue} onChange={handleChange}>
        <option value="">Select a course</option>
        {courses.map(course => (
          <option key={course.id} value={course.id}>
            {course.name}
          </option>
        ))}
      </select>
      {!courses.length && !error && <p>Loading courses...</p>}
    </div>
  );
}

export default CourseSelector;
