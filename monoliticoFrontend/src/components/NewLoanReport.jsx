import React, { useState } from "react";
import { Box, Typography, Paper, CircularProgress, Button } from "@mui/material";
import loansService from "../services/loans.service";
import loansReportsService from "../services/loansReports.service";
import reportsService from "../services/reports.service";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";

const  NewLoanReport = () =>{
	const { keycloak } = useKeycloak();
	const [loading, setLoading] = useState(false);
	const navigate = useNavigate();

		const handleSaveReport = async () => {
			setLoading(true);
			try {
				const clientId = 2; //keycloak.tokenParsed?.preferred_username;
				if (!clientId) {
					setLoading(false);
					console.log(clientId);
					return;
				}
				// Obtener todos los préstamos y filtrar por clientId
				const allLoansRes = await loansService.getAll();
				const loansList = allLoansRes.data.filter(l => l.clientId === clientId);
				if (loansList.length > 0) {
					const reportRes = await reportsService.create({ loanIdReport: true ,clientIdReport: clientId });
                    const reportId = reportRes.data?.reportId ;
					
                    for (const l of loansList) {
						
                        await loansReportsService.create({
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
					}
				}
				await new Promise(res => setTimeout(res, 1000));
				navigate("/myreports");
			} catch (err) {
				// Silenciar error, no mostrar nada
			}
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
