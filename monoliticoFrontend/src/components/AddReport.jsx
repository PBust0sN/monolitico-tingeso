import { Link } from "react-router-dom";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";

const reportOptions = [
  {
    label: "Préstamos por Cliente",
    description: "Visualiza todos los préstamos realizados por cada cliente.",
    path: "/report/loans-by-client",
  },
  {
    label: "Herramientas Más Usadas",
    description: "Consulta cuáles son las herramientas más solicitadas.",
    path: "/report/top-tools",
  },
  {
    label: "Préstamos atrasados",
    description: "Revisa los préstamos que aún no han sido devueltos.",
    path: "/report/behind-loans",
  },
];

const AddReport = () => {
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
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          pt: 6,
        }}
      >
        <Typography variant="h4" align="center" sx={{ mb: 4, fontWeight: "bold" }}>
          Selecciona un Reporte
        </Typography>
        <Box
          sx={{
            display: "flex",
            flexWrap: "wrap",
            gap: 4,
            justifyContent: "center",
            alignItems: "flex-start",
            width: "100%",
            maxWidth: "1200px",
          }}
        >
          {reportOptions.map((report) => (
            <Paper
              key={report.path}
              elevation={6}
              sx={{
                p: 3,
                width: 300,
                minHeight: 180,
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between",
                alignItems: "flex-start",
                background: "rgba(255,255,255,0.95)",
                color: "#222",
              }}
            >
              <Typography variant="h6" sx={{ fontWeight: "bold", mb: 1 }}>
                {report.label}
              </Typography>
              <Typography variant="body2" sx={{ mb: 2 }}>
                {report.description}
              </Typography>
              <Link to={report.path} style={{ textDecoration: "none", width: "100%" }}>
                <Button variant="contained" color="primary" fullWidth>
                  Generar Reporte
                </Button>
              </Link>
            </Paper>
          ))}
        </Box>
      </Box>
    </Box>
  );
};

export default AddReport;