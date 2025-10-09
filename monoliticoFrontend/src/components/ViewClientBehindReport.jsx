import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import { useParams } from "react-router-dom";
import clientBehindService from "../services/clientBehind.service";
import loansReportsService from "../services/loansReports.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";

function ViewClientBehindReport() {
  const { reportId } = useParams();
  const [clientBehind, setClientBehind] = useState(null);
  const [loansReport, setLoansReport] = useState([]);

  useEffect(() => {
    if (!reportId) return;
    let mounted = true;

    const fetchByReport = async () => {
      try {
        const clientRes = await clientBehindService.getAllByReportId(reportId);
        const clientData = clientRes.data;
        if (mounted) {
          setClientBehind(clientData || null);
          console.log("clientData:", clientData);
        }

        const loansRes = await loansReportsService.getAllByReportId(reportId);
        const loansData =  loansRes.data ;
        if (mounted) {
          setLoansReport(loansData);
          console.log("loansReport:", loansData);
        }
      } catch (err) {
        console.error("Error fetching clientBehind or loansReport:", err);
      }
    };

    fetchByReport();

    return () => {
      mounted = false;
    };
  }, [reportId]);

  const formatDate = (dateStr) => {
    if (!dateStr) return "-";
    try {
      const d = new Date(dateStr.length === 10 ? dateStr + "T00:00:00Z" : dateStr);
      if (isNaN(d)) return dateStr;
      return d.toLocaleDateString();
    } catch (e) {
      return dateStr;
    }
  };

  const diffDays = (deliveryDate, returnDate) => {
    // Measure difference relative to the current time.
    // If returnDate exists: difference = now - returnDate
    // Otherwise: difference = now - deliveryDate
    const now = new Date();
    const refStr = returnDate;
    if (!refStr) return "-";
    const ref = new Date(refStr.length === 10 ? refStr + "T00:00:00Z" : refStr);
    if (isNaN(ref)) return "-";
    const ms = ref.getTime() - now.getTime();
    // convert to days (positive means the ref date is in the past relative to now)
    const days = Math.round(ms / (1000 * 60 * 60 * 24));
    return days;
  };

  return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
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
      <Box sx={{ position: "relative", zIndex: 1, p: 4, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "flex-start", minHeight: "80vh" }}>
        <Paper sx={{ minWidth: 800, width: "100%", mb: 3, background: "rgba(255,255,255,0.85)", p: 3 }}>
          <Typography variant="h5" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Detalle Cliente Moroso</Typography>
          {clientBehind ? (
            <Box sx={{ display: "flex", gap: 6, justifyContent: "center", alignItems: "center" }}>
              <Typography variant="body1"><b>ID clientBehind:</b> {clientBehind.clientIdBehind}</Typography>
              <Typography variant="body1"><b>Nombre:</b> {clientBehind.name} {clientBehind.lastName}</Typography>
              <Typography variant="body1"><b>RUT:</b> {clientBehind.rut}</Typography>
              <Typography variant="body1"><b>Email:</b> {clientBehind.mail}</Typography>
            </Box>
          ) : (
            <Typography variant="body2" color="text.secondary">No se encontró información del cliente para este reporte.</Typography>
          )}
        </Paper>

        <Paper sx={{ maxWidth: 1200, width: "100%", background: "rgba(255,255,255,0.85)", p: 3 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Préstamos Asociados</Typography>
          {loansReport.length === 0 ? (
            <Typography align="center" color="text.secondary">No hay préstamos asociados a este reporte.</Typography>
          ) : (
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: "bold" }}>ID Préstamo</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Tipo</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Fecha Entrega</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Fecha Retorno</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Diferencia (días)</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {loansReport.map((lr) => (
                    <TableRow key={lr.loanReportId}>
                      <TableCell>{lr.loanId}</TableCell>
                      <TableCell>{lr.loanType}</TableCell>
                      <TableCell>{lr.amount}</TableCell>
                      <TableCell>{formatDate(lr.deliveryDate)}</TableCell>
                      <TableCell>{formatDate(lr.returnDate)}</TableCell>
                      <TableCell>{diffDays(lr.deliveryDate, lr.returnDate)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </Paper>
      </Box>
    </Box>
  );
}

export default ViewClientBehindReport;
