import React, { useState } from "react";
import { Box, Typography, Paper, CircularProgress, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import loansService from "../services/loans.service";
import loansReportsService from "../services/loansReports.service";
import reportsService from "../services/reports.service";
import { useNavigate } from "react-router-dom";

const NewBehindReport = () => {
  const [loading, setLoading] = useState(false);
  const [loans, setLoans] = useState([]);
  const [clientId, setClientId] = useState("");
  const [reportId, setReportId] = useState(null);
  const navigate = useNavigate();

  const handleFetchLoans = async () => {
    setLoading(true);
    const allLoansRes = await loansService.getAll();
    const loansList = allLoansRes.data.filter(l => String(l.clientId) === String(clientId));
    setLoans(loansList);
    setLoading(false);
  };

  const handleCreateReport = async () => {
    setLoading(true);
    // Crear el reporte principal
    const reportRes = await reportsService.create({ clientIdBehind: clientId });
    const newReportId = reportRes.data?.reportId;
    setReportId(newReportId);
    // Guardar cada préstamo en loanreport
    for (const l of loans) {
      await loansReportsService.create({
        reportId: newReportId,
        clientId: l.clientId,
        loanType: l.loanType,
        amount: l.amount,
        deliveryDate: l.deliveryDate,
        returnDate: l.returnDate,
        date: l.date,
        staffId: l.staffId,
        extraCharges: l.extraCharges,
        loanId: l.loanId
      });
    }
    setLoading(false);
    navigate("/myreports");
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
      <Box sx={{ position: "relative", zIndex: 1, minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
        <Paper sx={{ p: 3, maxWidth: 700, width: "100%", background: "rgba(255,255,255,0.95)", textAlign: "center" }}>
          <Typography variant="h5" sx={{ mb: 2 }}>
            Generar Reporte de Préstamos por Cliente (Behind)
          </Typography>
          <Box sx={{ mb: 2 }}>
            <input
              type="text"
              placeholder="ID Cliente"
              value={clientId}
              onChange={e => setClientId(e.target.value)}
              style={{ padding: 8, fontSize: 16, width: 200 }}
            />
            <Button variant="contained" color="secondary" sx={{ ml: 2 }} onClick={handleFetchLoans} disabled={loading || !clientId}>
              Buscar Préstamos
            </Button>
          </Box>
          {loading && <CircularProgress sx={{ my: 2 }} />}
          {loans.length > 0 && (
            <>
              <TableContainer sx={{ mb: 2 }}>
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell>ID Préstamo</TableCell>
                      <TableCell>Tipo</TableCell>
                      <TableCell>Cantidad</TableCell>
                      <TableCell>Fecha Entrega</TableCell>
                      <TableCell>Fecha Retorno</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {loans.map((l) => (
                      <TableRow key={l.loanId}>
                        <TableCell>{l.loanId}</TableCell>
                        <TableCell>{l.loanType}</TableCell>
                        <TableCell>{l.amount}</TableCell>
                        <TableCell>{l.deliveryDate ? new Date(l.deliveryDate).toLocaleDateString() : "-"}</TableCell>
                        <TableCell>{l.returnDate ? new Date(l.returnDate).toLocaleDateString() : "-"}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
              <Button variant="contained" color="primary" onClick={handleCreateReport} disabled={loading}>
                Generar Reporte
              </Button>
            </>
          )}
        </Paper>
      </Box>
    </Box>
  );
};

export default NewBehindReport;
