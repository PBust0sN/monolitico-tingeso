import './App.css'
import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import NavBar from "./components/NavBar"
import Home from "./components/Home";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import { useKeycloak } from "@react-keycloak/web";
import ToolList from './components/ToolList';
import ClientList from './components/ClientList';
import AddTool from './components/AddTool';
import EditTool from './components/EditTool';
import EditClient from './components/EditClient';
import AddClient from './components/AddClient';
import LoanList from './components/LoanList';
import NewLoan from './components/NewLoan';
import LoanInfo from './components/LoanInfo';
import RecordList from './components/RecordList';
import AddRecord from './components/AddRecord';
import Login  from './components/Login';
import Typography from "@mui/material/Typography";
import ReturnLoan from './components/ReturnLoan';
import ListLoanId from './components/ListLoanId';
import CalculateCost from './components/CalculateCost';
import FineList from './components/FineList';
import ReturnLoanFinal from './components/ReturnLoanFinal';
import AddReport from './components/AddReport';

import MyReports from './components/MyReports';


function App() {
  const { keycloak } = useKeycloak();

  const PrivateRoute = ({ element, rolesAllowed }) => {
    if(!keycloak.authenticated){
      return (<Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/* Fondo difuminado */}
      <Box
        sx={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          backgroundImage: `url("/fondo.jpg")`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          filter: "blur(8px)",
          zIndex: 0,
        }}
      />
      {/* Frame del login */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            minWidth: 350,
            maxWidth: 450,
            width: "90%",
            background: "rgba(255,255,255,0.85)",
            color: "#222",
            borderRadius: "8px",
            boxShadow: "0 4px 16px rgba(0,0,0,0.15)",
            textAlign: "center",
          }}
        >
            <Typography variant="h6" sx={{ mt: 2 }}>
              Inicia Sesión para ver esta pagina
            </Typography>
        </Paper>
      </Box>
    </Box>
  );
    }
    const roles = keycloak.tokenParsed?.realm_access?.roles || [];
    if (rolesAllowed && !rolesAllowed.some(r => roles.includes(r))) {
      return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/* Fondo difuminado */}
      <Box
        sx={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          backgroundImage: `url("/fondo.jpg")`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          filter: "blur(8px)",
          zIndex: 0,
        }}
      />
      {/* Frame del login */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            minWidth: 350,
            maxWidth: 450,
            width: "90%",
            background: "rgba(255,255,255,0.85)",
            color: "#222",
            borderRadius: "8px",
            boxShadow: "0 4px 16px rgba(0,0,0,0.15)",
            textAlign: "center",
          }}
        >
            <Typography variant="h6" sx={{ mt: 2 }}>
              No tienes permiso para ver esta pagina
            </Typography>
        </Paper>
      </Box>
    </Box>
  );
    }
    return element;
  };
  

  return (
    <Router>
      <div className="container">
        <NavBar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/login" element={<Login />} />

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
          <Route
            path="/loan/new/:client_id"
            element={<PrivateRoute element={<NewLoan />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/loan/info/:loan_id"
            element={<PrivateRoute element={<LoanInfo />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/record/list"
            element={<PrivateRoute element={<RecordList />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/record/add"
            element={<PrivateRoute element={<AddRecord />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/loan/list/:client_id"
            element={<PrivateRoute element={<ListLoanId />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/loan/return/id/:client_id/:loan_id"
            element={<PrivateRoute element={<ReturnLoan />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/loan/return/id/:client_id/:loan_id/calculateCost"
            element={<PrivateRoute element={<CalculateCost />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/fine/list"
            element={<PrivateRoute element={<FineList />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/loan/return/id/:client_id/:loan_id/calculateCost/devolution"
            element={<PrivateRoute element={<ReturnLoanFinal />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/reports/create"
            element={<PrivateRoute element={<AddReport />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/report/loans-by-client"
            element={<PrivateRoute element={<AddReport />} rolesAllowed={["STAFF","ADMIN"]} />}
          />
          <Route
            path="/myreports"
            element={<PrivateRoute element={<MyReports />} rolesAllowed={["STAFF","ADMIN","CLIENT"]} />}
          />
        </Routes>

      </div>
    </Router>
  );
}

export default App
