import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Button from "@mui/material/Button";
import loansService from "../services/loans.service";
import toolsService from "../services/tools.service";

const estadosHerramienta = [
  "Bueno",
  "Regular",
  "Malo",
  "Reparación",
  "Extraviada"
];

const CalculateCost = () => {
  const { state } = useLocation();
  const navigate = useNavigate();
  const [costDto, setCostDto] = useState(null);

  useEffect(() => {
    if (state && state.loan) {
      loansService.calculateCost(state.loan.loanId)
        .then(response => {
          setCostDto(response.data);
        })
        .catch(() => {
          setCostDto(null);
        });
    }
  }, [state]);

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr.length === 10 ? dateStr + "T00:00:00Z" : dateStr);
  if (isNaN(date)) return dateStr;
  const day = String(date.getUTCDate()).padStart(2, "0");
  const month = String(date.getUTCMonth() + 1).padStart(2, "0");
  const year = date.getUTCFullYear();
  return `${day}/${month}/${year}`;
};

const handleDevolution = async () => {
  // update all the tools to "Bueno"
  const updates = state.tools.map(tool =>
    toolsService.updateState(tool.toolId, "Bueno")
  );
  await Promise.all(updates);

  // add the tools to the stock
  const stocks = state.tools.map(tool =>
    toolsService.updateStock(tool.toolId)
  );

  await Promise.all(stocks);

  // navigates to the devolution page with state
  navigate(`${location.pathname}/devolution`, {
    state: {
      loan: state.loan,
      toolStates: state.toolStates,
      tools: state.tools
    }
  });
};
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
            Préstamo #{state.loan.loanId}
          </Typography>
          <Table size="small">
            <TableBody>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>Tipo</TableCell>
                <TableCell>{state.loan.loanType}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
                <TableCell>{state.loan.amount}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Entrega</TableCell>
                <TableCell>{formatDate(state.loan.deliveryDate)}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Retorno</TableCell>
                <TableCell>{formatDate(state.loan.returnDate)}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>Staff</TableCell>
                <TableCell>{state.loan.staffId}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cliente</TableCell>
                <TableCell>{state.loan.clientId}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cargos Extra</TableCell>
                <TableCell>{state.loan.extraCharges}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Fecha</TableCell>
                <TableCell>{formatDate(state.loan.date)}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>

        {/* Herramientas y estados */}
        <Paper sx={{ width: "100%", maxWidth: 700, background: "rgba(255,255,255,0.85)", p: 2, mb: 2 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 1 }}>
            Herramientas y estados
          </Typography>
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Nombre</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Estado Seleccionado</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {state.tools.map((tool) => (
                  <TableRow key={tool.toolId}>
                    <TableCell>{tool.toolId}</TableCell>
                    <TableCell>{tool.tool_name}</TableCell>
                    <TableCell>{state.toolStates[tool.toolId]}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>
        <Box sx={{ display: "flex", gap: 3, width: "100%", maxWidth: 1000, mb: 2 }}>
          <Paper sx={{ maxWidth: 450, width: "90%", background: "rgba(255,255,255,0.85)", p: 4, textAlign: "center" }}>
            <Typography variant="h5" sx={{ fontWeight: "bold", mb: 2 }}>
              Costos del Préstamo
            </Typography>
            {costDto ? (
              <>
                <Typography variant="h6" sx={{ mb: 2 }}>
                  Repo Fine: ${costDto.repoAmount}
                </Typography>
                <Typography variant="h6" sx={{ mb: 2 }}>
                  Fine: ${costDto.fineAmount}
                </Typography>
              </>
            ) : (
              <Typography variant="body1" color="text.secondary">
                No se pudo calcular el costo.
              </Typography>
            )}
          </Paper>
          <Paper sx={{ maxWidth: 450, width: "90%", background: "rgba(255,255,255,0.85)", p: 4, textAlign: "center" }}>
            <Typography variant="h5" sx={{ fontWeight: "bold", mb: 2 }}>
              Devoluciones
            </Typography>
            {costDto ? (
              <>
                <Typography variant="h6" sx={{ mb: 2 }}>
                  Devolution Amount: ${costDto.returnPayment}
                </Typography>
              </>
            ) : (
              <Typography variant="body1" color="text.secondary">
                No se pudo calcular la devolución.
              </Typography>
            )}
          </Paper>
        </Box>
        <Box sx={{ display: "flex", gap: 2, justifyContent: "center", mt: 3 }}>
          <Button variant="contained" onClick={() => navigate(-1)}>
            Volver
          </Button>
          <Button
            variant="contained"
            color="secondary"
            onClick={handleDevolution}
          >
            Hacer Devolución
          </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default CalculateCost;