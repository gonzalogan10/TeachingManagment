import { useNavigate } from 'react-router-dom';
import './Screen.css';

function MenuScreen() {
  const navigate = useNavigate(); 
  // useNavigate makes it possible to navigate between different screens in the application

  return (
    <div>
      <button className="go-to-page-button" onClick={() => navigate('/')}>
        Back to Home
      </button>
      <div className="menu-container">
        <div className="menu-panel left">
          <h2 className="menu-title light">COURSES</h2>
          <button className="menu-button white" onClick={() => navigate('/activity')}>
            Go to courses
          </button>
        </div>
        <div className="menu-panel right">
          <h2 className="menu-title dark">EVALUATION</h2>
          <button className="menu-button red" onClick={() => navigate('/evaluation')}>
            Go to evaluations
          </button>
        </div>
      </div>
    </div>
  );
}

export default MenuScreen;