import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import reportsService from "../services/reports.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import VisibilityIcon from '@mui/icons-material/Visibility';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";

const MyReports = () => {
  const [reports, setReports] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    reportsService.getAll()
      .then((response) => setReports(response.data))
      .catch((error) => console.log("Error al cargar reportes", error));
  }, []);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" sx={{ mb: 3 }}>Mis Reportes</Typography>
      <TableContainer component={Paper} sx={{ maxWidth: 1200, background: "rgba(255,255,255,0.95)" }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>ID Reporte</TableCell>
              <TableCell>Fecha</TableCell>
              <TableCell>Préstamo</TableCell>
              <TableCell>Herramienta</TableCell>
              <TableCell>Multa</TableCell>
              <TableCell>Cliente</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {reports.map((report) => (
              <TableRow key={report.reportId}>
                <TableCell>{report.reportId}</TableCell>
                <TableCell>{new Date(report.reportDate).toLocaleString()}</TableCell>
                <TableCell>{report.loanIdReport || "-"}</TableCell>
                <TableCell>{report.toolsIdRanking || "-"}</TableCell>
                <TableCell>{report.fineIdReports || "-"}</TableCell>
                <TableCell>{report.clientIdBehind || "-"}</TableCell>
                <TableCell>
                  {report.loanIdReport && (
                    <Button
                      variant="contained"
                      color="primary"
                      size="small"
                      onClick={() => navigate(`/loan/info/${report.loanIdReport}`)}
                      style={{ marginLeft: "0.5rem" }}
                      startIcon={<VisibilityIcon />}
                    >
                      Ver más
                    </Button>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default MyReports;
