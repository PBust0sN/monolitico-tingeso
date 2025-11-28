import React, { useState } from "react";
import { Box, Typography, Paper, CircularProgress, Button } from "@mui/material";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate, useSearchParams } from "react-router-dom";
import loansService from "../services/loans.service";
import loansReportsService from "../services/loansReports.service";
import reportsService from "../services/reports.service";
import toolsService from "../services/tools.service";
import toolsReportService from "../services/toolsReport.service";
import toolsLoanReportService from "../services/toolsLoanReport.service";
import toolsLoansService from "../services/toolsLoans.service";

const  NewLoanReport = () =>{
	const { keycloak } = useKeycloak();
	const [searchParams] = useSearchParams();
	const [loading, setLoading] = useState(false);
	const navigate = useNavigate();

	const handleSaveReport = async () => {
		setLoading(true);
		// Obtener clientId de la URL si existe, si no usar el de Keycloak
		const urlClientId = searchParams.get("clientId");
		console.log("URL clientId (raw):", urlClientId, "Type:", typeof urlClientId);
		console.log("Keycloak clientId (raw):", keycloak?.tokenParsed?.id_real, "Type:", typeof keycloak?.tokenParsed?.id_real);
		const clientId = urlClientId ? parseInt(urlClientId) : parseInt(keycloak?.tokenParsed?.id_real);
		console.log("Final clientId after parseInt:", clientId, "Type:", typeof clientId);
		if (!clientId) {
			setLoading(false);
			console.log("No clientId found:", clientId);
			return;
		}
		console.log("Using clientId:", clientId);
		// get all the loans and the filtered by client id
		const allLoansRes = await loansService.getAll();
		console.log("All loans:", allLoansRes.data);
		console.log("First loan full object:", JSON.stringify(allLoansRes.data[0], null, 2)); // Ver todos los valores
		const loansList = allLoansRes.data.filter(l => {
			console.log("Comparing l.clientId:", l.clientId, "type:", typeof l.clientId, "with clientId:", clientId, "type:", typeof clientId, "Match:", l.clientId === clientId);
			return l.clientId === clientId;
		});
		console.log("Filtered loans:", loansList);
		if (loansList.length > 0) {
			console.log("Creating report with clientIdReport:", clientId);
			const reportRes = await reportsService.create({ loanIdReport: true ,
				clientIdReport: clientId });
			console.log("Report created with response:", reportRes.data);
			const reportId = reportRes.data?.reportId ;
			for (const l of loansList) {
				// create loanReport
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
				// get tools by loan id 
				const toollist = await toolsLoansService.getToolsIdByLoanId(l.loanId);
				const toolIds = toollist.data; 
				console.log(toolIds);
				for (const toolId of toolIds) {
					const toolRes = await toolsService.get(toolId);
					const tool = toolRes.data;
					console.log(tool);
					// create toolsReport
					const toolReportRes = await toolsReportService.create({
						toolName: tool.tool_name,
						category: tool.category,
						loanNount: tool.loan_count
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
