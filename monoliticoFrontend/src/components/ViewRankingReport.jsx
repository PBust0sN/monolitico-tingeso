import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import { useParams } from "react-router-dom";
import reportsService from "../services/reports.service";
import toolsRankingService from "../services/toolsRanking.service";
import toolsReportService from "../services/toolsReport.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";

function ViewRankingReport() {
  const { reportId } = useParams();
  const [report, setReport] = useState(null);
  const [tools, setTools] = useState([]);

  useEffect(() => {
    if (reportId) {
      reportsService.get(reportId)
        .then(res => setReport(res.data))
        .catch(() => setReport(null));
      toolsRankingService.getAllByReportId(reportId)
        .then(async res => {
          const rankingList = res.data;
          
          const toolsList = await Promise.all(
            rankingList.map(r => toolsReportService.get(r.toolId).then(res => res.data))
          );
          setTools(toolsList);
        })
        .catch(() => setTools([]));
    }
  }, [reportId]);

  return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/* background */}
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
        {/* report info */}
        <Paper sx={{ minWidth: 800, width: "100%", mb: 3, background: "rgba(255,255,255,0.85)", p: 3 }}>
          <Typography variant="h5" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Detalle del Reporte de Ranking</Typography>
          {report ? (
            <Box sx={{ display: "flex", gap: 6, justifyContent: "center", alignItems: "center" }}>
              <Typography variant="body1"><b>ID Reporte:</b> {report.reportId}</Typography>
              <Typography variant="body1"><b>Fecha:</b> {report.reportDate ? new Date(report.reportDate).toLocaleString() : "-"}</Typography>
              <Typography variant="body1"><b>Tipo:</b> Tool Ranking</Typography>
            </Box>
          ) : (
            <Typography variant="body2" color="text.secondary">No se encontró información del reporte.</Typography>
          )}
        </Paper>
        {/* ranking of tools */}
        <Paper sx={{ minWidth: 800, width: "100%", background: "rgba(255,255,255,0.85)", p: 3 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Herramientas del Ranking</Typography>
          {tools.length === 0 ? (
            <Typography align="center" color="text.secondary">No hay herramientas asociadas a este ranking.</Typography>
          ) : (
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: "bold" }}>Nombre</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Categoría</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>Cantidad Préstamos</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {tools.map((tool, idx) => (
                    <TableRow key={tool.toolReportId + "-" + idx}>
                      <TableCell>{tool.toolName}</TableCell>
                      <TableCell>{tool.category}</TableCell>
                      <TableCell>{tool.loanCount}</TableCell>
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

export default ViewRankingReport;
