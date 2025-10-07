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

  const formatDate = (dateStr) => {
        if (!dateStr) return "";
        const date = new Date(dateStr.length === 10 ? dateStr + "T00:00:00Z" : dateStr);
        if (isNaN(date)) return dateStr;
        const day = String(date.getUTCDate()).padStart(2, "0");
        const month = String(date.getUTCMonth() + 1).padStart(2, "0");
        const year = date.getUTCFullYear();
        return `${day}/${month}/${year}`;
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
      <Box sx={{ position: "relative", zIndex: 1, p: 4 }}>
        <Typography variant="h4" sx={{ mb: 3 }}>Mis Reportes</Typography>
        <TableContainer component={Paper} sx={{ maxWidth: 1200, background: "rgba(255,255,255,0.95)" }}>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>ID Reporte</TableCell>
                <TableCell>Fecha</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {reports.map((report) => {
                let tipo = "";
                if (report.loanIdReport) tipo = "Préstamo";
                else if (report.toolsIdRanking) tipo = "Tool Ranking";
                else if (report.fineIdReports) tipo = "Multas";
                else if (report.clientIdBehind) tipo = "Client Behind";
                return (
                  <TableRow key={report.reportId}>
                    <TableCell>{report.reportId}</TableCell>
                    <TableCell>{formatDate(report.reportDate)}</TableCell>
                    <TableCell>{tipo}</TableCell>
                    <TableCell>
                      {report.loanIdReport ? (
                        <Button
                          variant="contained"
                          color="primary"
                          size="small"
                          onClick={() => navigate(`/viewLoanreports/${report.reportId}`)}
                          style={{ marginLeft: "0.5rem" }}
                          startIcon={<VisibilityIcon />}
                        >
                          Ver más
                        </Button>
                      ) : report.toolsIdRanking ? (
                        <Button
                          variant="contained"
                          color="primary"
                          size="small"
                          onClick={() => navigate(`/viewrankingreport/${report.reportId}`)}
                          style={{ marginLeft: "0.5rem" }}
                          startIcon={<VisibilityIcon />}
                        >
                          Ver más
                        </Button>
                      ) : report.fineIdReports ? (
                        <Button
                          variant="contained"
                          color="primary"
                          size="small"
                          onClick={() => navigate(`/viewreports/${report.reportId}`)}
                          style={{ marginLeft: "0.5rem" }}
                          startIcon={<VisibilityIcon />}
                        >
                          Ver más
                        </Button>
                      ) : report.clientIdBehind ? (
                        <Button
                          variant="contained"
                          color="primary"
                          size="small"
                          onClick={() => navigate(`/viewreports/${report.reportId}`)}
                          style={{ marginLeft: "0.5rem" }}
                          startIcon={<VisibilityIcon />}
                        >
                          Ver más
                        </Button>
                      ) : null}
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>

    </Box>
  );
}

export default MyReports;
