import { useEffect, useState } from "react";
import { useParams, useNavigate , useLocation} from "react-router-dom";
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

const ReturnLoan = () => {
    const location = useLocation();
    const returnLoanData = location.state?.returnLoanData;


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
      {/* Contenido principal */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          pt: 8,
          gap: 3,
        }}
      >
        {/* Resumen del préstamo */}
        <Paper sx={{ p: 3, mb: 2, minWidth: 1000, maxWidth: 2000, width: "90%" }}>
          <Typography variant="h6" sx={{ mb: 1, fontWeight: "bold" }}>
            Resumen del Préstamo
          </Typography>
          {returnLoanData?.loan ? (
            <Box>
              {/* Primera fila */}
              <Box sx={{ display: "flex", flexDirection: "row", gap: 40, mb: 1 }}>
                <Typography><b>ID:</b> {returnLoanData.loan.loanId}</Typography>
                <Typography><b>Cliente:</b> {returnLoanData.loan.clientId}</Typography>
                <Typography><b>Estado:</b> {returnLoanData.loan.active? "Activo" : "Devuelto"}</Typography>
              </Box>
              {/* Segunda fila */}
              <Box sx={{ display: "flex", flexDirection: "row", gap: 20}}>
                <Typography><b>Fecha entrega:</b> {returnLoanData.loan.deliveryDate}</Typography>
                <Typography><b>Fecha devolución:</b> {returnLoanData.loan.returnDate}</Typography>
              </Box>
            </Box>
          ) : (
            <Typography>No hay información del préstamo.</Typography>
          )}
        </Paper>

        {/* Fines issued */}
        <Paper sx={{ p: 3, mb: 2, minWidth: 1000, maxWidth: 2000, width: "90%" }}>
          <Typography variant="h6" sx={{ mb: 2, fontWeight: "bold" }}>
            Fines issued
          </Typography>
          {/* Behind Fine y Repo Fine en paralelo */}
          <Box sx={{ display: "flex", flexDirection: "row", gap: 4 }}>
            {/* Behind Fine */}
            <Paper sx={{ flex: 1, p: 2, mb: 2, background: "#f9f9f9" }}>
              <Typography variant="subtitle1" sx={{ fontWeight: "bold" }}>
                Behind Fine
              </Typography>
              {returnLoanData?.fine ? (
                <Typography>
                  Monto: <b>{returnLoanData.fine.amount}</b>
                </Typography>
              ) : (
                <Typography>Préstamo devuelto sin atrasos.</Typography>
              )}
            </Paper>
            {/* Repo Fine */}
            <Paper sx={{ flex: 1, p: 2, mb: 2, background: "#f9f9f9" }}>
              <Typography variant="subtitle1" sx={{ fontWeight: "bold" }}>
                Repo Fine
              </Typography>
              {returnLoanData?.RepoFine ? (
                <Typography>
                  Monto: <b>{returnLoanData.RepoFine.amount}</b>
                </Typography>
              ) : (
                <Typography>Préstamo devuelto sin herramientas dañadas.</Typography>
              )}
            </Paper>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default ReturnLoan;