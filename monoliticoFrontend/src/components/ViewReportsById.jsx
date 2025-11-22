import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
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
import TextField from "@mui/material/TextField";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

const ViewReportsById = () => {
  const { client_id } = useParams();
  const [reports, setReports] = useState([]);
  const [filteredReports, setFilteredReports] = useState([]);
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    console.log("Client ID:",client_id);
    reportsService.getAllByClientId(client_id)
      .then((response) => {
        setReports(response.data);
        setFilteredReports(response.data);
      })
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
              {/* Fila de filtros dentro de la tabla */}
              <TableRow>
                <TableCell colSpan={4}>
                  <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                      <DatePicker
                        label="Fecha inicio"
                        value={startDate}
                        onChange={(newValue) => setStartDate(newValue)}
                        enableAccessibleFieldDOMStructure={false}
                        slots={{ textField: TextField }}
                        slotProps={{
                          textField: {
                            sx: { width: 160, background: "white", borderRadius: 1, '& .MuiInputBase-root': { height: 43 } }
                          }
                        }}
                      />
                      <DatePicker
                        label="Fecha fin"
                        value={endDate}
                        onChange={(newValue) => setEndDate(newValue)}
                        enableAccessibleFieldDOMStructure={false}
                        slots={{ textField: TextField }}
                        slotProps={{
                          textField: {
                            sx: { width: 160, background: "white", borderRadius: 1, '& .MuiInputBase-root': { height: 43 } }
                          }
                        }}
                      />
                    </LocalizationProvider>
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => {
                        let filtered = [...reports];
                        if (startDate) {
                          filtered = filtered.filter(r => new Date(r.reportDate) >= startDate);
                        }
                        if (endDate) {
                          const endOfDay = new Date(endDate);
                          endOfDay.setHours(23,59,59,999);
                          filtered = filtered.filter(r => new Date(r.reportDate) <= endOfDay);
                        }
                        setFilteredReports(filtered);
                      }}
                      sx={{ height: 43 }}
                    >
                      Buscar
                    </Button>
                    <Button
                      variant="outlined"
                      onClick={() => {
                        setStartDate(null); setEndDate(null); setFilteredReports(reports);
                      }}
                      sx={{ height: 43 }}
                    >
                      Limpiar
                    </Button>
                  </Box>
                </TableCell>
              </TableRow>
               <TableRow>
                 <TableCell>ID Reporte</TableCell>
                 <TableCell>Fecha</TableCell>
                 <TableCell>Tipo</TableCell>
                 <TableCell></TableCell>
               </TableRow>
            </TableHead>
            <TableBody>
              {filteredReports.map((report) => {
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
                          onClick={() => navigate(`/viewclientbehind/${report.reportId}`)}
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

export default ViewReportsById;
