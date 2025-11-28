import React, { useState } from "react";
import { Box, Typography, Paper, CircularProgress, Button } from "@mui/material";
import loansService from "../services/loans.service";
import reportsService from "../services/reports.service";
import clientService from "../services/client.service";
import clientBehindService from "../services/clientBehind.service";
import clientBehindLoansService from "../services/clientBehindLoans.service";
import loansReportsService from "../services/loansReports.service";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";

const NewClientBehindReport = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { keycloak } = useKeycloak();
  const [searchParams] = useSearchParams();

  const handleGenerateBehind = async () => {
    setLoading(true);

    // Obtener clientId de la URL si existe, si no usar el de Keycloak
    const urlClientId = searchParams.get("clientId");
    const idFromToken = urlClientId ? parseInt(urlClientId) : parseInt(keycloak?.tokenParsed?.id_real);
    
    if (!idFromToken) {
      setLoading(false);
      return;
    }

    try {
      // 1. get all loans for this client
      const allLoansRes = await loansService.getAll();
      // first filter by client, then exclude loans of type 'return'
      const loansList = (allLoansRes.data || [])
        .filter((l) => String(l.clientId) === String(idFromToken))
        .filter((l) => String(l.loanType).toLowerCase() !== "return")
        .filter((l) => l.status !== false);

      // 2. create a new report entry
      const reportRes = await reportsService.create({ clientIdBehind: true, clientIdReport: idFromToken });
      const reportId = reportRes.data?.reportId;

      // 3. get client data  
      const clientRes = await clientService.get(idFromToken);
      const clientData = clientRes.data;

      // 4. create new entry in clientsBehind
      const clientBehindPayload = {
        reportId: reportId,
        rut: clientData?.rut || "",
        name: clientData?.name || "",
        lastName: clientData?.last_name || clientData?.lastName || "",
        mail: clientData?.mail || "",
        phoneNumber: clientData?.phone_number || clientData?.phoneNumber || "",
        state: clientData?.state || "",
        avaliable: clientData?.avaliable ?? true,
      };

      const clientBehindRes = await clientBehindService.create(clientBehindPayload);
      const clientBehindId = clientBehindRes.data?.clientIdBehind;

      // 5.for each loan, verify with the endpoint checkdates (true if up to date, false otherwise)
      for (const l of loansList) {
        // we call the checkdates endpoint with the loan object
        const checkRes = await loansService.chechDates(l);
        // the response data is boolean: true if on time, false if late
        const onTime = Boolean(checkRes.data);

        // if onTime === false => is behind -> create loanReport and link
        if (!onTime) {
          const loanReportRes = await loansReportsService.create({
            reportId: reportId,
            clientIdBehind: clientBehindId,
            deliveryDate: l.deliveryDate,
            returnDate: l.returnDate,
            loanType: l.loanType,
            date: l.date,
            staffId: l.staffId,
            clientId: l.clientId,
            amount: l.amount,
            extraCharges: l.extraCharges,
            loanId: l.loanId,
          });
          const loanReportId = loanReportRes.data?.loanReportId;
          //create entry in clientBehindLoans linking  loanReportId with clientBehindId
          if (loanReportId && clientBehindId) {
            await clientBehindLoansService.create({
              loanReportId: loanReportId,
              clientIdBehind: clientBehindId,
            });
          }
        }
      }

      // simulate delay for better UX 
      await new Promise((res) => setTimeout(res, 1000));
      setLoading(false);
      navigate("/myreports");
    } catch (e) {
      console.error("Error creating behind report:", e);
      setLoading(false);
    }
  };

  const idFromToken = keycloak?.tokenParsed?.preferred_username || keycloak?.tokenParsed?.username || keycloak?.tokenParsed?.email;

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
            ¿Desea generar el reporte de clientes morosos (Behind)?
          </Typography>
          <Typography variant="body1" sx={{ mb: 2 }}>
            Cliente autenticado: {idFromToken || "(no autenticado)"}
          </Typography>
          <Button variant="contained" color="primary" onClick={handleGenerateBehind} disabled={loading || !idFromToken}>
            Generar Reporte Behind
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

export default NewClientBehindReport;
