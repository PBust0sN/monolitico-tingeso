import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import loansService from "../services/loans.service";
import toolsService from "../services/tools.service";
import toolsLoansService from "../services/toolsLoans.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import MenuItem from "@mui/material/MenuItem";
import TextField from "@mui/material/TextField";

const estadosHerramienta = [
  "Bueno",
  "Regular",
  "Malo",
  "Reparación",
  "Extraviada"
];

const ReturnLoan = () => {
  const { client_id, loan_id } = useParams();
  const [loan, setLoan] = useState(null);
  const [tools, setTools] = useState([]);
  const [toolStates, setToolStates] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    loansService.get(loan_id).then((response) => {
      setLoan(response.data);

      // Obtener los ids de herramientas asociadas a este préstamo
      toolsLoansService.getToolsIdByLoanId(loan_id).then(async (idsResponse) => {
        const toolIds = idsResponse.data; // array de ids

        // Obtener la información de cada herramienta por id
        const toolPromises = toolIds.map((id) =>
          toolsService.get(id).then((res) => res.data)
        );
        const toolDetails = await Promise.all(toolPromises);
        setTools(toolDetails);

        // Inicializa los estados con el valor actual o vacío
        const initialStates = {};
        toolDetails.forEach(tool => {
          initialStates[tool.toolId] = tool.initialState || "";
        });
        setToolStates(initialStates);
      });
    });
  }, [loan_id]);

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr.length === 10 ? dateStr + "T00:00:00Z" : dateStr);
  if (isNaN(date)) return dateStr;
  const day = String(date.getUTCDate()).padStart(2, "0");
  const month = String(date.getUTCMonth() + 1).padStart(2, "0");
  const year = date.getUTCFullYear();
  return `${day}/${month}/${year}`;
};

  const handleStateChange = (toolId, value) => {
    setToolStates({ ...toolStates, [toolId]: value });
  };

  const handleCalculateCosts = () => {
  navigate(`${location.pathname}/calculateCost`, {
    state: {
      loan,
      toolStates,
      tools
    }
  });
};

  if (!loan) {
    return (
      <Box sx={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
        <Typography variant="h6" color="text.secondary">
          Cargando información del préstamo...
        </Typography>
      </Box>
    );
  }

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
      {/* Frame principal */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          paddingTop: 6,
        }}
      >
        {/* Datos del préstamo */}
        <Paper sx={{ minWidth: 700, maxWidth: 700, width: "100%", mb: 2, background: "rgba(255,255,255,0.85)", p: 2 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 1 }}>
            Préstamo #{loan.loanId}
          </Typography>
          <Table size="small">
            <TableBody>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>Tipo</TableCell>
                <TableCell>{loan.loanType}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
                <TableCell>{loan.amount}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Entrega</TableCell>
                <TableCell>{formatDate(loan.deliveryDate)}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Retorno</TableCell>
                <TableCell>{formatDate(loan.returnDate)}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>Staff</TableCell>
                <TableCell>{loan.staffId}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cliente</TableCell>
                <TableCell>{loan.clientId}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cargos Extra</TableCell>
                <TableCell>{loan.extraCharges}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Fecha</TableCell>
                <TableCell>{formatDate(loan.date)}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>

        {/* Herramientas y formulario de estado */}
        <Box sx={{ display: "flex", flexDirection: "row", gap: 2, width: "100%", maxWidth: 1200 }}>
          {/* Tabla de herramientas */}
          <Paper sx={{ flex: 2, background: "rgba(255,255,255,0.85)", p: 2 }}>
            <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 1 }}>
              Herramientas asociadas
            </Typography>
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: "bold" }}>ID</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Nombre</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Categoría</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Disponibilidad</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Estado</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {tools.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={5} align="center">
                        No hay herramientas asociadas a este préstamo.
                      </TableCell>
                    </TableRow>
                  ) : (
                    tools.map((tool) => (
                      <TableRow key={tool.toolId}>
                        <TableCell>{tool.toolId}</TableCell>
                        <TableCell>{tool.tool_name}</TableCell>
                        <TableCell>{tool.category}</TableCell>
                        <TableCell>{tool.disponibility}</TableCell>
                        <TableCell>
                          <TextField
                            select
                            label="Estado"
                            variant="standard"
                            value={toolStates[tool.toolId] || ""}
                            onChange={e => handleStateChange(tool.toolId, e.target.value)}
                            sx={{ minWidth: 120 }}
                          >
                            {estadosHerramienta.map((estado) => (
                              <MenuItem key={estado} value={estado}>
                                {estado}
                              </MenuItem>
                            ))}
                          </TextField>
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </TableContainer>
            <Button
              variant="contained"
              color="info"
              sx={{ mt: 2 }}
              onClick={handleCalculateCosts}
            >
              Calcular Costos
            </Button>
          </Paper>
        </Box>
      </Box>
    </Box>
  );
};

export default ReturnLoan;