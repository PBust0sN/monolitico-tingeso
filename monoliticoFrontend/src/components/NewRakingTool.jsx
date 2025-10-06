import React, { useState } from "react";
import { Box, Typography, Paper, CircularProgress, Button } from "@mui/material";
import toolsService from "../services/tools.service";
import toolsReportService from "../services/toolsReport.service";
import reportsService from "../services/reports.service";
import toolsRankingService from "../services/toolsRanking.service";
import { useNavigate } from "react-router-dom";

const NewRakingTool = () => {
	const [loading, setLoading] = useState(false);
	const navigate = useNavigate();

	const handleGenerateRanking = async () => {
		setLoading(true);
		// fisrt we obtain the first 10 tools hith the most loan count in the tools table
		const allToolsRes = await toolsService.getAll();
		const toolsList = allToolsRes.data;

        // then we create a list to store the ids of the tools created in toolsReport
		const toolReportIds = [];
		for (const tool of toolsList) {
			const toolReportRes = await toolsReportService.create({
				toolName: tool.toolName,
				category: tool.category,
				loanCount: tool.loanCount || 0
			});
            //the we push gthe values into the list
			toolReportIds.push({
				toolId: toolReportRes.data?.toolIdReport,
				originalToolId: tool.toolId
			});
		}

		//then we create a new report, with the toolsIdRanking set to true
		const reportRes = await reportsService.create({ toolsIdRanking: true });
		const reportId = reportRes.data?.reportId;

		// then we create the ranking entries in toolsRanking
		for (const tool of toolReportIds) {
			await toolsRankingService.create({
				reportId: reportId,
				toolId: tool.toolId
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
				<Paper sx={{ p: 3, maxWidth: 400, width: "100%", background: "rgba(255,255,255,0.95)", textAlign: "center" }}>
					<Typography variant="h5" sx={{ mb: 2 }}>
						¿Desea generar el ranking de herramientas?
					</Typography>
					<Button variant="contained" color="primary" onClick={handleGenerateRanking} disabled={loading}>
						Generar Ranking
					</Button>
					{loading && (
						<Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", mt: 3 }}>
							<CircularProgress />
						</Box>
					)}
				</Paper>
			</Box>
		</Box>
	);
};

export default NewRakingTool;
