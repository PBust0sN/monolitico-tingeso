import { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import toolsService from "../services/tools.service";
import loansService from "../services/loans.service";
import { useKeycloak } from "@react-keycloak/web";
import Typography from "@mui/material/Typography";
import SaveIcon from "@mui/icons-material/Save";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import TextField from '@mui/material/TextField';

const CARD_WIDTH = 200;
const CARD_HEIGHT = 270;
const TITLE_FONT_SIZE = "1.15rem";


const NewLoan = () => {
  const { client_id } = useParams();
  const [days, setDays] = useState("");
  const [toolOptions, setToolOptions] = useState([]);
  const [selectedTools, setSelectedTools] = useState([]);
  const navigate = useNavigate();
  const { keycloak } = useKeycloak();
  const staff_id = Number(keycloak.tokenParsed?.id_real);
  const clientNumber = Number(client_id);

  useEffect(() => {
    toolsService
      .getAll()
      .then((response) => {
        setToolOptions(response.data);
      })
      .catch((error) => {
        console.log("Error al obtener herramientas:", error);
      });
  }, []);

  // Selecciona/des-selecciona una herramienta
  const handleToolClick = (toolId) => {
    setSelectedTools((prev) =>
      prev.includes(toolId)
        ? prev.filter((id) => id !== toolId)
        : [...prev, toolId]
    );
  };

  const saveLoan = (e) => {
    e.preventDefault();
    const loan = {
      staff_id: staff_id,
      client_id: clientNumber,
      days: Number(days),
      tools_id: selectedTools,
    };
    loansService
      .newLoan(loan)
      .then((response) => {
        // Suponiendo que response.data es la lista de errores
        if (Array.isArray(response.data) && response.data.length === 0) {
          window.alert("Préstamo añadido exitosamente");
          navigate("/loan/list");
        } else if (Array.isArray(response.data)) {
          // Muestra los errores en una ventana
          window.alert("Errores:\n" + response.data.join("\n"));
          navigate(-1);
        } else {
          // Si la respuesta no es una lista, muestra un mensaje genérico
          window.alert("Respuesta inesperada del servidor.");
        }
      })
      .catch((error) => {
        window.alert("Ha ocurrido un error al intentar crear nuevo préstamo.");
        console.log("Ha ocurrido un error al intentar crear nuevo préstamo.", error);
      });
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
      {/* Frame principal de herramientas */}
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
        <Paper
          elevation={6}
          sx={{
            p: 4,
            width: "90vw",
            maxWidth: "1400px",
            minHeight: "70vh",
            background: "rgba(255,255,255,0.95)",
            color: "#222",
            mb: 3,
          }}
        >
          <Typography variant="h4" align="center" sx={{ mb: 3, fontWeight: "bold" }}>
            Selecciona Herramientas para el Préstamo
          </Typography>
          <Box sx={{ mb: 3, display: "flex", justifyContent: "center" }}>
            <TextField
              id="filled-search"
              label="Días de préstamo"
              type="number"
              variant="filled"
              value={days}
              onChange={e => setDays(e.target.value)}
              sx={{ width: 260 }} // más ancho
              inputProps={{ min: 1 }}
              required
            />
          </Box>
          <Box
            sx={{
              display: "flex",
              flexWrap: "wrap",
              gap: 3,
              justifyContent: "center",
              alignItems: "flex-start",
              minHeight: "50vh",
            }}
          >
            {toolOptions.map((tool) => (
              <Paper
                key={tool.toolId}
                elevation={selectedTools.includes(tool.toolId) ? 8 : 2}
                sx={{
                  width: `${CARD_WIDTH}px`,
                  minWidth: `${CARD_WIDTH}px`,
                  maxWidth: `${CARD_WIDTH}px`,
                  height: `${CARD_HEIGHT}px`,
                  minHeight: `${CARD_HEIGHT}px`,
                  maxHeight: `${CARD_HEIGHT}px`,
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "flex-start",
                  alignItems: "stretch",
                  p: 2,
                  border: selectedTools.includes(tool.toolId)
                    ? "2px solid #1976d2"
                    : "2px solid transparent",
                  background: selectedTools.includes(tool.toolId)
                    ? "rgba(25, 118, 210, 0.08)"
                    : "white",
                  transition: "border 0.2s, background 0.2s",
                }}
              >
                <Box
                  sx={{
                    width: "100%",
                    height: 120,
                    mb: 2,
                    background: "#eee",
                    borderRadius: "12px",
                    overflow: "hidden",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <img
                    src={`/${tool.toolId}.png`}
                    alt={tool.tool_name}
                    style={{
                      width: "100%",
                      height: "100%",
                      objectFit: "cover",
                    }}
                  />
                </Box>
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: "bold",
                    mb: 1,
                    textAlign: "left",
                    fontSize: TITLE_FONT_SIZE,
                    width: "100%",
                  }}
                >
                  {tool.tool_name}
                </Typography>
                <Box sx={{ display: "flex", alignItems: "center", width: "100%", justifyContent: "space-between", mt: 1 }}>
                  <Box sx={{ textAlign: "left" }}>
                    <Typography variant="body2" color="text.secondary">
                      {tool.category}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {tool.disponibility}
                    </Typography>
                  </Box>
                  <Button
                    color={selectedTools.includes(tool.toolId) ? "primary" : "inherit"}
                    onClick={() => handleToolClick(tool.toolId)}
                    sx={{ minWidth: 0, ml: 2 }}
                  >
                    <AddCircleIcon />
                  </Button>
                </Box>
              </Paper>
            ))}
          </Box>
          <Box sx={{ mt: 4, display: "flex", justifyContent: "center", gap: 2 }}>
            <Button
              variant="contained"
              color="info"
              onClick={saveLoan}
              startIcon={<SaveIcon />}
              disabled={!days || selectedTools.length === 0}
            >
              Grabar Préstamo
            </Button>
            <Link to="/client/list" style={{ textDecoration: "none" }}>
              <Button variant="outlined" color="primary">
                Volver al Listado
              </Button>
            </Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default NewLoan;