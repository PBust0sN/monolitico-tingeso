import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import toolsService from "../services/tools.service";
import Paper from "@mui/material/Paper";
import MenuItem from "@mui/material/MenuItem";

const AddTool = () => {
  const [category, setCategory] = useState("");
  const [disponibility, setDisponibility] = useState("");
  const [initialState, setInitialState] = useState("");
  const [toolName, setToolName] = useState("");
  const [loanFee, setLoanFee] = useState("");
  const [repositionFee, setRepositionFee] = useState("");
  const [diaryFineFee, setDiaryFineFee] = useState("");
  const [stock, setStock] = useState("");
  const [lowDmgFee, setLowDmgFee] = useState("");
  const navigate = useNavigate();

  // Lista y estados de errores
  const [errorsList, setErrorsList] = useState([]);
  const [fieldErrors, setFieldErrors] = useState({});

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

    const loanFeeNum = Number(loanFee);
    if (loanFee === "" || Number.isNaN(loanFeeNum) || loanFeeNum < 0) {
      errors.push("Loan Fee debe ser un número >= 0.");
      fErrors.loanFee = true;
    }

    const repositionFeeNum = Number(repositionFee);
    if (repositionFee === "" || Number.isNaN(repositionFeeNum) || repositionFeeNum < 0) {
      errors.push("Reposition Fee debe ser un número >= 0.");
      fErrors.repositionFee = true;
    }

    const lowDmgFeeNum = Number(lowDmgFee);
    if (
      lowDmgFee === "" ||
      Number.isNaN(lowDmgFeeNum) ||
      lowDmgFeeNum <= 0 ||
      (repositionFee !== "" && !Number.isNaN(repositionFeeNum) && lowDmgFeeNum >= repositionFeeNum)
    ) {
      errors.push("Low Dmg Fee debe ser un número > 0 y menor que Reposition Fee.");
      fErrors.lowDmgFee = true;
    }

    const diaryFineFeeNum = Number(diaryFineFee);
    if (diaryFineFee === "" || Number.isNaN(diaryFineFeeNum) || diaryFineFeeNum < 0) {
      errors.push("Diary Fine Fee debe ser un número >= 0.");
      fErrors.diaryFineFee = true;
    }

    const stockNum = Number(stock);
    if (stock === "" || Number.isNaN(stockNum) || !Number.isInteger(stockNum) || stockNum < 0) {
      errors.push("Stock debe ser un entero >= 0.");
      fErrors.stock = true;
    }

    return { errors, fErrors };
  };

  const saveTool = async (e) => {
    e.preventDefault();

    const { errors, fErrors } = validateFields();
    setErrorsList(errors);
    setFieldErrors(fErrors);

    if (errors.length > 0) {
      window.alert(errors.join("\n"));
      return;
    }

    const tool = {
      tool_name: toolName,
      category: category,
      diary_fine_fee: Number(diaryFineFee),
      loan_fee: Number(loanFee),
      reposition_fee: Number(repositionFee),
      initial_state: initialState,
      disponibility: disponibility,
      stock: Number(stock),
      low_dmg_fee: Number(lowDmgFee),
    };

    try {
      const res = await toolsService.create(tool);
      console.log("Create response:", res);
      // inspecciona res.status / res.data
      if (res && (res.status === 200 || res.status === 201)) {
        navigate("/tool/list");
      } else {
        window.alert("La creación respondió con código: " + (res?.status ?? "unknown"));
      }
    } catch (err) {
      console.error("Error al crear herramienta:", err);
      // muestra mensaje claro al usuario
      window.alert("Error al crear herramienta: " + (err?.response?.data || err.message || err));
    }
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
      {/* Frame del formulario */}
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
            <h3>Nueva Herramienta</h3>
            <hr />

            {/* Lista de errores */}
            {errorsList.length > 0 && (
              <Box
                sx={{
                  width: "100%",
                  mb: 2,
                  p: 1,
                  borderRadius: 1,
                  backgroundColor: "rgba(255,200,200,0.9)",
                }}
              >
                <ul style={{ margin: 0, paddingLeft: "1rem", color: "#700" }}>
                  {errorsList.map((err, idx) => (
                    <li key={idx}>{err}</li>
                  ))}
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
                select
                variant="standard"
                onChange={(e) => setDisponibility(e.target.value)}
                error={!!fieldErrors.disponibility}
              >
                <MenuItem value="disponible">Disponible</MenuItem>
                <MenuItem value="prestada">Prestada</MenuItem>
                <MenuItem value="en reparacion">En reparaci&oacute;n</MenuItem>
                <MenuItem value="dada de baja">Dada de baja</MenuItem>
              </TextField>
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
                id="lowDmgFee"
                label="Low Dmg Fee"
                type="number"
                value={lowDmgFee}
                variant="standard"
                onChange={(e) => setLowDmgFee(e.target.value)}
                error={!!fieldErrors.lowDmgFee}
                helperText="Debe ser > 0 y menor que Reposition Fee"
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

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="stock"
                label="Stock"
                type="number"
                value={stock}
                variant="standard"
                onChange={(e) => setStock(e.target.value)}
                error={!!fieldErrors.stock}
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

export default AddTool;
