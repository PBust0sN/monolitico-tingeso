import React, { useState } from "react";
import { Box, Typography, Paper, CircularProgress, Button } from "@mui/material";
import loansService from "../services/loans.service";
import loansReportsService from "../services/loansReports.service";
import reportsService from "../services/reports.service";
import toolsService from "../services/tools.service";
import toolsReportService from "../services/toolsReport.service";
import toolsLoanReportService from "../services/toolsLoanReport.service";
import toolsLoansService from "../services/toolsLoans.service";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";

const  NewLoanReport = () =>{
	const { keycloak } = useKeycloak();
	const [loading, setLoading] = useState(false);
	const navigate = useNavigate();

	const handleSaveReport = async () => {
		setLoading(true);
		const clientId = keycloak?.tokenParsed?.id_real; 
		if (!clientId) {
			setLoading(false);
			console.log(clientId);
			return;
		}
		// Obtener todos los préstamos y filtrar por clientId
		const allLoansRes = await loansService.getAll();
		const loansList = allLoansRes.data.filter(l => l.clientId === clientId);
		console.log(loansList);
		if (loansList.length >= 0) {
			const reportRes = await reportsService.create({ loanIdReport: true ,
				clientIdReport: clientId });
			const reportId = reportRes.data?.reportId ;
			for (const l of loansList) {
				// Crear loanReport
				const reponseLoanReport =  await loansReportsService.create({
					reportId: reportId,
					clientId: l.clientId,
					loanType: l.loanType,
					amount: l.amount,
					deliveryDate: l.deliveryDate,
					returnDate: l.returnDate,
					date: l.date,
					staffId: l.staffId,
					extraCharges: l.extraCharges
				});

				const res = reponseLoanReport.data?.loanReportId;
				// Obtener herramientas asociadas al préstamo
				const toollist = await toolsLoansService.getToolsIdByLoanId(l.loanId);
				const toolIds = toollist.data; 
				console.log(toolIds);
				for (const toolId of toolIds) {
					const toolRes = await toolsService.get(toolId);
					const tool = toolRes.data;
					console.log(tool);
					// Crear toolsReport
					const toolReportRes = await toolsReportService.create({
						toolName: tool.tool_name,
						category: tool.category,
						loanCount: 1 // o el valor que corresponda
					});
					const toolIdReport = toolReportRes.data?.toolIdReport;
					// Vincular con toolsLoanReport
					await toolsLoanReportService.create({
						loanId: res,
						toolId: toolIdReport
					});
				}
			}
		}
		await new Promise(res => setTimeout(res, 1000));
		navigate("/myreports");
		setLoading(false);
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
				<Box sx={{ position: "relative", zIndex: 1, minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
					<Paper sx={{ p: 3, maxWidth: 350, width: "100%", background: "rgba(255,255,255,0.95)", textAlign: "center" }}>
						<Typography variant="h5" sx={{ mb: 2 }}>
							¿Desea generar el reporte de préstamos?
						</Typography>
						<Box sx={{ display: "flex", justifyContent: "center", gap: 2, mt: 3 }}>
						<Button variant="outlined" color="secondary" onClick={() => navigate(-1)} disabled={loading}>
							Volver atrás
						</Button>
						<Button variant="contained" color="primary" onClick={handleSaveReport} disabled={loading}>
							Generar
						</Button>
					</Box>
					{loading && (
						<Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", mt: 3 }}>
							<CircularProgress />
						</Box>
					)}
					</Paper>
				</Box>
			</Box>
		);
	}

	export default NewLoanReport;
