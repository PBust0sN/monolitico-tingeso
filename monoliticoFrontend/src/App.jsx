import './App.css'
import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import NavBar from "./components/NavBar"
import Home from "./components/Home";
import { useKeycloak } from "@react-keycloak/web";
import ToolList from './components/ToolList';
import ClientList from './components/ClientList';
import AddTool from './components/AddTool';
import EditTool from './components/EditTool';
import EditClient from './components/EditClient';
import AddClient from './components/AddClient';
import LoanList from './components/LoanList';

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
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />

          <Route
            path="/tool/list"
            element={<PrivateRoute element={<ToolList />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/client/list"
            element={<PrivateRoute element={<ClientList />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/tool/add"
            element={<PrivateRoute element={<AddTool />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/tool/edit/:toolId"
            element={<PrivateRoute element={<EditTool />} rolesAllowed={["ADMIN"]} />}
          />
          <Route
            path="/client/edit/:client_id"
            element={<PrivateRoute element={<EditClient />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/client/add/"
            element={<PrivateRoute element={<AddClient />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/loan/list/"
            element={<PrivateRoute element={<LoanList />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
        </Routes>

      </div>
    </Router>
  );
}

export default App
