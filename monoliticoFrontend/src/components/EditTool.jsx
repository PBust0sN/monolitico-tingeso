import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import toolsService from "../services/tools.service";
import Paper from "@mui/material/Paper";

const EditTool = () => {
  const [category, setCategory] = useState("");
  const [disponibility, setDisponibility] = useState("");
  const [initialState, setInitialState] = useState("");
  const [toolName, setToolName] = useState("");
  const [loanFee, setLoanFee] = useState("");
  const [repositionFee, setRepositionFee] = useState("");
  const [diaryFineFee, setDiaryFineFee] = useState("");
  const { toolId } = useParams();
  const navigate = useNavigate();

  // error validation
  const [errorsList, setErrorsList] = useState([]);
  const [fieldErrors, setFieldErrors] = useState({});

  useEffect(() => {
    if (toolId) {
      toolsService
        .get(toolId)
        .then((tool) => {
          setCategory(tool.data.category);
          setDisponibility(tool.data.disponibility);
          setInitialState(tool.data.initialState);
          setToolName(tool.data.toolName);
          setLoanFee(tool.data.loanFee);
          setRepositionFee(tool.data.repositionFee);
          setDiaryFineFee(tool.data.diaryFineFee);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    }
  }, [toolId]);

  const validateFields = () => {
    const errors = [];
    const fErrors = {};

    if (!toolName || !toolName.trim()) {
      errors.push("Tool Name es obligatorio.");
      fErrors.toolName = true;
    }

    if (!category || !category.trim()) {
      errors.push("Category es obligatorio.");
      fErrors.category = true;
    }

    if (!disponibility || !disponibility.trim()) {
      errors.push("Disponibility es obligatorio.");
      fErrors.disponibility = true;
    }

    if (!initialState || !initialState.trim()) {
      errors.push("Initial State es obligatorio.");
      fErrors.initialState = true;
    }

    const lf = parseFloat(loanFee);
    if (isNaN(lf) || lf < 0) {
      errors.push("Loan Fee debe ser un número mayor o igual a 0.");
      fErrors.loanFee = true;
    }

    const rf = parseFloat(repositionFee);
    if (isNaN(rf) || rf < 0) {
      errors.push("Reposition Fee debe ser un número mayor o igual a 0.");
      fErrors.repositionFee = true;
    }

    const df = parseFloat(diaryFineFee);
    if (isNaN(df) || df < 0) {
      errors.push("Diary Fine Fee debe ser un número mayor o igual a 0.");
      fErrors.diaryFineFee = true;
    }

    return { errors, fErrors };
  };

  const saveTool = (e) => {
    e.preventDefault();

    const { errors, fErrors } = validateFields();
    setErrorsList(errors);
    setFieldErrors(fErrors);

    if (errors.length > 0) {
      window.alert(errors.join("\n"));
      return;
    }

    const tool = {
      toolId,
      toolName: toolName.trim(),
      category: category.trim(),
      diaryFineFee: Number(diaryFineFee),
      loanFee: Number(loanFee),
      repositionFee: Number(repositionFee),
      initialState: initialState.trim(),
      disponibility: disponibility.trim(),
    };

    toolsService
      .update(tool)
      .then((response) => {
        console.log("Herramienta actualizada.", response.data);
        navigate("/tool/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar actualizar la herramienta.", error);
      });
  };

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
      {/* Frame of the formulary */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            minWidth: 350,
            maxWidth: 450,
            width: "90%",
            background: "rgba(255,255,255,0.85)",
            color: "#222", 
          }}
        >
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            component="form"
          >
            <h3>Editar Herramienta</h3>
            <hr />

            {errorsList.length > 0 && (
              <Box sx={{ width: "100%", mb: 2, p: 1, borderRadius: 1, backgroundColor: "rgba(255,200,200,0.9)" }}>
                <ul style={{ margin: 0, paddingLeft: "1rem", color: "#700" }}>
                  {errorsList.map((err, idx) => <li key={idx}>{err}</li>)}
                </ul>
              </Box>
            )}

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="toolName"
                label="Tool Name"
                value={toolName}
                variant="standard"
                onChange={(e) => setToolName(e.target.value)}
                helperText="Ej. Martillo"
                error={!!fieldErrors.toolName}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="category"
                label="Category"
                value={category}
                variant="standard"
                onChange={(e) => setCategory(e.target.value)}
                error={!!fieldErrors.category}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="disponibility"
                label="Disponibility"
                value={disponibility}
                variant="standard"
                onChange={(e) => setDisponibility(e.target.value)}
                error={!!fieldErrors.disponibility}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="initialState"
                label="Initial State"
                value={initialState}
                variant="standard"
                onChange={(e) => setInitialState(e.target.value)}
                error={!!fieldErrors.initialState}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="loanFee"
                label="Loan Fee"
                type="number"
                value={loanFee}
                variant="standard"
                onChange={(e) => setLoanFee(e.target.value)}
                error={!!fieldErrors.loanFee}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="repositionFee"
                label="Reposition Fee"
                type="number"
                value={repositionFee}
                variant="standard"
                onChange={(e) => setRepositionFee(e.target.value)}
                error={!!fieldErrors.repositionFee}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="diaryFineFee"
                label="Diary Fine Fee"
                type="number"
                value={diaryFineFee}
                variant="standard"
                onChange={(e) => setDiaryFineFee(e.target.value)}
                error={!!fieldErrors.diaryFineFee}
              />
            </FormControl>

            <FormControl sx={{ mb: 2 }}>
              <Button
                variant="contained"
                color="info"
                onClick={saveTool}
                startIcon={<SaveIcon />}
              >
                Grabar
              </Button>
            </FormControl>
            <hr />
            <Link to="/tool/list">Back to List</Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default EditTool;
