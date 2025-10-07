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
import toolsLoanReportService from "../services/toolsLoanReport.service";
import toolsReportService from "../services/toolsReport.service";
import TableHead from "@mui/material/TableHead";


function ViewLoanReports() {
	const { reportId } = useParams();
	const [report, setReport] = useState(null);
	const [loansReport, setLoansReport] = useState([]);
	const [toolsByLoan, setToolsByLoan] = useState({});

	useEffect(() => {
		if (reportId) {
			reportsService.get(reportId)
				.then(res => setReport(res.data))
				.catch(() => setReport(null));
			loansReportsService.getAllByReportId(reportId)
				.then(async res => {
					const loans = res.data;
					console.log(loans);
					setLoansReport(loans);
					// Obtener herramientas asociadas a cada préstamo usando toolsLoanReportService y toolsReportService
					const toolsMap = {};
					for (const loan of loans) {
						console.log('Procesando loan:', loan);
						const idsResponse = await toolsLoanReportService.getToolsIdByLoanId(loan.loanReportId);
						const toolIds = Array.isArray(idsResponse.data) ? idsResponse.data : [];
						console.log('loanReportId:', loan.loanReportId, 'toolIds:', toolIds);
						if (toolIds.length === 0) {
							console.log('No hay herramientas asociadas a este préstamo:', loan.loanReportId);
						}
						// Obtener todas las herramientas usando toolsReportService.get(id)
						const toolsList = await Promise.all(
							toolIds.map(id => toolsReportService.get(id).then(res => res.data))
						);
						console.log('toolsList para loanReportId', loan.loanReportId, ':', toolsList);
						toolsMap[loan.loanReportId] = toolsList;
					}
					console.log(toolsMap);
					setToolsByLoan(toolsMap);
				})
				.catch(() => {
					setLoansReport([]);
					setToolsByLoan({});
				});
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
				<Paper sx={{ minWidth: 1200, width: "100%", mb: 3, background: "rgba(255,255,255,0.85)", p: 3 }}>
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
				{/* Frame inferior: lista de préstamos y herramientas asociadas */}
				<Paper sx={{ maxWidth: 1200, width: "100%", background: "rgba(255,255,255,0.85)", p: 3 }}>
					<Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 2 }}>Préstamos y Herramientas Asociadas</Typography>
					{loansReport.length === 0 ? (
						<Typography align="center" color="text.secondary">No hay préstamos asociados a este reporte.</Typography>
					) : (
						loansReport.map((lr, idx) => (
							<Box key={"loan-"+idx} sx={{ mb: 4 }}>
								{/* Info del préstamo en no más de 2 filas */}
								<TableContainer>
									<Table size="small">
										<TableBody>
											<TableRow>
												<TableCell sx={{ fontWeight: "bold" }}>ID Préstamo</TableCell>
												<TableCell>{lr.loanId}</TableCell>
												<TableCell sx={{ fontWeight: "bold" }}>Tipo</TableCell>
												<TableCell>{lr.loanType}</TableCell>
												<TableCell sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
												<TableCell>{lr.amount}</TableCell>
											</TableRow>
											<TableRow>
												<TableCell sx={{ fontWeight: "bold" }}>Fecha Entrega</TableCell>
												<TableCell>{lr.deliveryDate ? new Date(lr.deliveryDate).toLocaleDateString() : "-"}</TableCell>
												<TableCell sx={{ fontWeight: "bold" }}>Fecha Retorno</TableCell>
												<TableCell>{lr.returnDate ? new Date(lr.returnDate).toLocaleDateString() : "-"}</TableCell>
												<TableCell sx={{ fontWeight: "bold" }}>ID Cliente</TableCell>
												<TableCell>{lr.clientId}</TableCell>
											</TableRow>
										</TableBody>
									</Table>
								</TableContainer>
								{/* Herramientas asociadas, identadas */}
								<Box sx={{ pl: 6, pt: 1 }}>
									<Typography variant="subtitle1" sx={{ fontWeight: "bold", mb: 1 }}>Herramientas asociadas</Typography>
									<TableContainer>
										<Table size="small">
											<TableHead>
												<TableRow>
													{/* <TableCell sx={{ fontWeight: "bold" }}>ID Herramienta</TableCell> */}
													<TableCell sx={{ fontWeight: "bold" }}>Nombre</TableCell>
													<TableCell sx={{ fontWeight: "bold" }}>Categoría</TableCell>
													{/* <TableCell sx={{ fontWeight: "bold" }}>Disponibilidad</TableCell> */}
												</TableRow>
											</TableHead>
											<TableBody>
												{(toolsByLoan[lr.loanReportId] && toolsByLoan[lr.loanReportId].length > 0) ? (
													toolsByLoan[lr.loanReportId].map((tool, tIdx) => (
														<TableRow key={tool.toolReportId + "-" + tIdx}>
															{/* <TableCell>{tool.toolId}</TableCell> */}
															<TableCell>{tool.toolName}</TableCell>
															<TableCell>{tool.category}</TableCell>
															{/* <TableCell>{tool.disponibility}</TableCell> */}
														</TableRow>
													))
												) : (
													<TableRow>
														<TableCell colSpan={2} align="center">No hay herramientas asociadas a este préstamo.</TableCell>
													</TableRow>
												)}
											</TableBody>
										</Table>
									</TableContainer>
								</Box>
							</Box>
						))
					)}
				</Paper>
			</Box>
		</Box>
	);
}

export default ViewLoanReports;
