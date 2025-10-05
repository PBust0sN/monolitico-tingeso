

import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import { useParams } from "react-router-dom";
import reportsService from "../services/reports.service";
import Stack from "@mui/material/Stack";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableRow from "@mui/material/TableRow";
import loansReportsService from "../services/loansReports.service";


function ViewReports() {
	const { reportId } = useParams();
	const [report, setReport] = useState(null);
	const [loansReport, setLoansReport] = useState([]);

	useEffect(() => {
		if (reportId) {
			reportsService.get(reportId)
				.then(res => setReport(res.data))
				.catch(() => setReport(null));
			loansReportsService.getAllByReportId(reportId)
				.then(res => setLoansReport(Array.isArray(res.data) ? res.data : [res.data]))
				.catch(() => setLoansReport([]));
		}
	}, [reportId]);

	let tipo = "-";
	if (report) {
		if (report.loanIdReport) tipo = "Préstamo";
		else if (report.toolsIdRanking) tipo = "Tool Ranking";
		else if (report.fineIdReports) tipo = "Multas";
		else if (report.clientIdBehind) tipo = "Client Behind";
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
			<Box sx={{ position: "relative", zIndex: 1, p: 4, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "flex-start", minHeight: "80vh" }}>
				{/* Frame superior */}
				<Paper sx={{ maxWidth: 1200, width: "100%", mb: 3, background: "rgba(255,255,255,0.85)", p: 3 }}>
					<Typography variant="h5" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Detalle del Reporte</Typography>
					{report ? (
						<Stack direction="row" spacing={6} sx={{ width: "100%", justifyContent: "center", alignItems: "center" }}>
							<Typography variant="body1"><b>ID Reporte:</b> {report.reportId}</Typography>
							<Typography variant="body1"><b>Fecha:</b> {report.reportDate ? new Date(report.reportDate).toLocaleString() : "-"}</Typography>
							<Typography variant="body1"><b>Tipo:</b> {tipo}</Typography>
							<Typography variant="body1"><b>Cliente:</b> {report.clientIdReport}</Typography>
						</Stack>
					) : (
						<Typography variant="body2" color="text.secondary">No se encontró información del reporte.</Typography>
					)}
				</Paper>
				{/* Frame inferior: atributos de loansReport */}
				<Paper sx={{ maxWidth: 700, width: "100%", background: "rgba(255,255,255,0.85)", p: 3 }}>
					<Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Atributos de Préstamo Asociados</Typography>
					<TableContainer>
						<Table>
							<TableBody>
								{loansReport.length === 0 ? (
									<TableRow>
										<TableCell colSpan={2} align="center">
											No hay atributos de préstamo asociados a este reporte.
										</TableCell>
									</TableRow>
								) : (
									loansReport.map((lr, idx) => (
										Object.entries(lr).map(([key, value]) => (
											<TableRow key={key + idx}>
												<TableCell sx={{ fontWeight: "bold" }}>{key}</TableCell>
												<TableCell>{String(value)}</TableCell>
											</TableRow>
										))
									))
								)}
							</TableBody>
						</Table>
					</TableContainer>
				</Paper>
			</Box>
		</Box>
	);
}

export default ViewReports;
