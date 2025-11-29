import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import toolsService from "../services/tools.service";
import imagesService from "../services/images.service";
import Paper from "@mui/material/Paper";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import Typography from "@mui/material/Typography";

const EditTool = () => {
  const [category, setCategory] = useState("");
  const [disponibility, setDisponibility] = useState("");
  const [initialState, setInitialState] = useState("");
  const [toolName, setToolName] = useState("");
  const [loanFee, setLoanFee] = useState("");
  const [repositionFee, setRepositionFee] = useState("");
  const [diaryFineFee, setDiaryFineFee] = useState("");
  const [stock, setStock] = useState("");
  const [lowDmgFee, setLowDmgFee] = useState("");
  const [image, setImage] = useState(null);
  const [preview, setPreview] = useState(null);
  const [dragActive, setDragActive] = useState(false);
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
          console.log("Datos de la herramienta cargados:", tool.data);
          setCategory(tool.data.category);
          setDisponibility(tool.data.disponibility);
          setInitialState(tool.data.initial_state);
          setToolName(tool.data.tool_name);
          setLoanFee(String(tool.data.loan_fee));
          setRepositionFee(String(tool.data.reposition_fee));
          setDiaryFineFee(String(tool.data.diary_fine_fee));
          setStock(String(tool.data.stock || ""));
          setLowDmgFee(String(tool.data.low_dmg_fee || ""));
          // Cargar imagen existente
          loadExistingImage(toolId);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    }
  }, [toolId]);

  const loadExistingImage = (toolId) => {
    const filename = `${toolId}.png`;
    imagesService
      .getImage(filename)
      .then((response) => {
        // Convertir blob a URL
        const url = URL.createObjectURL(response.data);
        setPreview(url);
      })
      .catch((error) => {
        console.log(`No hay imagen existente para toolId ${toolId}:`, error);
      });
  };

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

    const st = parseFloat(stock);
    if (isNaN(st) || st < 0) {
      errors.push("Stock debe ser un número mayor o igual a 0.");
      fErrors.stock = true;
    }

    const ldf = parseFloat(lowDmgFee);
    if (isNaN(ldf) || ldf < 0) {
      errors.push("Low Damage Fee debe ser un número mayor o igual a 0.");
      fErrors.lowDmgFee = true;
    }

    return { errors, fErrors };
  };

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === "dragenter" || e.type === "dragover") {
      setDragActive(true);
    } else if (e.type === "dragleave") {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);

    const files = e.dataTransfer.files;
    if (files && files[0]) {
      const file = files[0];
      // validar que sea imagen
      if (file.type.startsWith("image/")) {
        setImage(file);
        const reader = new FileReader();
        reader.onloadend = () => {
          setPreview(reader.result);
        };
        reader.readAsDataURL(file);
        // Subir la imagen usando imagesService
        uploadImage(file);
      } else {
        alert("Por favor, selecciona un archivo de imagen.");
      }
    }
  };

  const handleFileInput = (e) => {
    const files = e.target.files;
    if (files && files[0]) {
      const file = files[0];
      if (file.type.startsWith("image/")) {
        setImage(file);
        const reader = new FileReader();
        reader.onloadend = () => {
          setPreview(reader.result);
        };
        reader.readAsDataURL(file);
        // Subir la imagen usando imagesService
        uploadImage(file);
      } else {
        alert("Por favor, selecciona un archivo de imagen.");
      }
    }
  };

  const uploadImage = (file) => {
    const formData = new FormData();
    formData.append("file", file);
    
    // Nombre personalizado: ${toolId}.png
    const customFilename = `${toolId}.png`;
    
    imagesService
      .uploadImage(formData, customFilename)
      .then((response) => {
        console.log("Imagen subida exitosamente:", response.data);
        console.log("Imagen guardada como:", response.data.filename);
      })
      .catch((error) => {
        console.log("Error al subir la imagen:", error);
        alert("Error al subir la imagen");
      });
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

    // Subir imagen si fue seleccionada
    if (image) {
      const formData = new FormData();
      formData.append("file", image);
      const customFilename = `${toolId}.png`;
      
      imagesService
        .uploadImage(formData, customFilename)
        .then((response) => {
          console.log("Imagen subida exitosamente:", response.data);
          // Después de subir la imagen, guardar la herramienta
          saveToolData();
        })
        .catch((error) => {
          console.log("Error al subir la imagen:", error);
          alert("Error al subir la imagen");
        });
    } else {
      // Si no hay imagen, guardar directamente
      saveToolData();
    }
  };

  const saveToolData = () => {
    const tool = {
      toolId,
      tool_name: toolName.trim(),
      category: category.trim(),
      diary_fine_fee: Number(diaryFineFee),
      loan_fee: Number(loanFee),
      reposition_fee: Number(repositionFee),
      initial_state: initialState.trim(),
      disponibility: disponibility.trim(),
      stock: Number(stock),
      low_dmg_fee: Number(lowDmgFee),
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
            maxWidth: 500,
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
                select
                variant="standard"
                onChange={(e) => setCategory(e.target.value)}
                error={!!fieldErrors.category}
              >
                <MenuItem value="Herramientas manuales">Herramientas manuales</MenuItem>
                <MenuItem value="Electroportátiles">Electroportátiles</MenuItem>
                <MenuItem value="Jardinería">Jardinería</MenuItem>
                <MenuItem value="Medición y nivelación">Medición y nivelación</MenuItem>
                <MenuItem value="Construcción y elevación">Construcción y elevación</MenuItem>
                <MenuItem value="Especializadas">Especializadas</MenuItem>
              </TextField>
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

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="lowDmgFee"
                label="Low Damage Fee"
                type="number"
                value={lowDmgFee}
                variant="standard"
                onChange={(e) => setLowDmgFee(e.target.value)}
                error={!!fieldErrors.lowDmgFee}
              />
            </FormControl>

            <hr />

            {/* Drag and Drop zone */}
            <Typography variant="h6" sx={{ mb: 2, mt: 2, fontWeight: "bold" }}>
              Imagen de la herramienta
            </Typography>

            <Box
              onDragEnter={handleDrag}
              onDragLeave={handleDrag}
              onDragOver={handleDrag}
              onDrop={handleDrop}
              sx={{
                width: "100%",
                border: "2px dashed",
                borderColor: dragActive ? "primary.main" : "gray",
                borderRadius: 2,
                p: 3,
                textAlign: "center",
                backgroundColor: dragActive ? "rgba(25, 118, 210, 0.1)" : "rgba(0,0,0,0.02)",
                cursor: "pointer",
                transition: "all 0.3s ease",
                mb: 2,
              }}
            >
              <input
                type="file"
                id="fileInput"
                accept="image/*"
                onChange={handleFileInput}
                style={{ display: "none" }}
              />
              <label htmlFor="fileInput" style={{ cursor: "pointer", width: "100%" }}>
                <CloudUploadIcon sx={{ fontSize: 40, color: "primary.main", mb: 1 }} />
                <Typography variant="body1" sx={{ mb: 1 }}>
                  Arrastra una imagen aquí o haz clic para seleccionar
                </Typography>
                <Typography variant="caption" sx={{ color: "gray" }}>
                  Formatos soportados: JPG, PNG, GIF, WebP
                </Typography>
              </label>
            </Box>

            {/* Preview de imagen */}
            {preview && (
              <Box sx={{ mb: 2, width: "100%" }}>
                <Typography variant="subtitle2" sx={{ mb: 1 }}>
                  Vista previa:
                </Typography>
                <Box
                  component="img"
                  src={preview}
                  alt="Preview"
                  sx={{
                    width: "100%",
                    maxHeight: 200,
                    objectFit: "contain",
                    borderRadius: 1,
                    border: "1px solid #ccc",
                  }}
                />
              </Box>
            )}

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
