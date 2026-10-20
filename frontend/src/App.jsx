import { BrowserRouter, Routes, Route } from 'react-router-dom';
import FirstScreen from "./screen/FirstScreen.jsx";
import ActivityScreen from "./screen/ActivityScreen.jsx";
import EvaluationScreen from "./screen/EvaluationScreen.jsx";
import MenuScreen from "./screen/MenuScreen.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<FirstScreen />} />
        <Route path="/menu" element={<MenuScreen />} />
        <Route path="/activity" element={<ActivityScreen />} />
        <Route path="/evaluation" element={<EvaluationScreen />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
