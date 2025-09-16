import './App.css'
import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import NavBar from "./components/NavBar"
import Home from "./components/Home";
import { useKeycloak } from "@react-keycloak/web";
import ToolList from './components/ToolList';

function App() {
  const { keycloak, initialized } = useKeycloak();
  
  if (!initialized) return <div>Cargando...</div>;

  const isLoggedIn = keycloak.authenticated;
  const roles = keycloak.tokenParsed?.realm_access?.roles || [];

  const PrivateRoute = ({ element, rolesAllowed }) => {
    if (!isLoggedIn) {
      keycloak.login();
      return null;
    }
    if (rolesAllowed && !rolesAllowed.some(r => roles.includes(r))) {
      return <h2>No tienes permiso para ver esta página</h2>;
    }
    return element;
  };

  if (!isLoggedIn) { 
    keycloak.login(); 
    return null; 
  }  

  return (
    <Router>
      <div className="container">
        <NavBar />
        <Routes>
          <Route path="/home" element={<Home />} />

          <Route
            path="/tool/list"
            element={<PrivateRoute element={<ToolList />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
        </Routes>

      </div>
    </Router>
  );
}

export default App
