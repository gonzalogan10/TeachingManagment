import { useNavigate } from 'react-router-dom'
import vut_logo from '../assets/vut_brno.jpg'
import './Screen.css'

function FirstScreen() {
  const navigate = useNavigate()

  return (
      <div id="first-screen-container">
        <h1>BUT: Brno University of Technology</h1>
        
        <a href="https://www.vut.cz/en/" target="_blank" rel="noopener noreferrer">
          Visit VUT Website
        </a>

        <div className="info">
            <h3>Click on the VUT logo to start using the teaching managment system!</h3>
        </div>
        
        <div className="card">
          <button onClick={() => navigate('/menu')} id="logo-button">
            <img src={vut_logo} className="logo" alt="VUT logo" />
          </button>
          <p className="subtitle">
            This is a project for the GJAE subject
          </p>
        </div>
      </div>
    )
  }

export default FirstScreen
