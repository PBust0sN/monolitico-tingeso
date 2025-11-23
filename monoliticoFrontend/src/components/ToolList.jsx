import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import toolsService from "../services/tools.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Box from "@mui/material/Box"; // <-- Agrega esto si no lo tienes
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import Typography from "@mui/material/Typography";
import BuildIcon from '@mui/icons-material/Build';

const ToolList = () => {
  const [tools, setTools] = useState([]);
  const [search, setSearch] = useState("");

  const filteredTools = tools.filter(tool =>
  (tool.tool_name || "").toLowerCase().includes(search.toLowerCase())
);

  const navigate = useNavigate();

  const init = () => {
    toolsService
      .getAll()
      .then((response) => {
        setTools(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de herramientas.",
          error
        );
      });
  };

  useEffect(() => {
    init();
  }, []);

  const handleDelete = (id) => {
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar esta herramienta?"
    );
    if (confirmDelete) {
      toolsService
        .remove(id)
        .then(() => {
          init();
        })
        .catch((error) => {
          console.log(
            "Se ha producido un error al intentar eliminar la herramienta",
            error
          );
        });
    }
  };

  const handleEdit = (id) => {
    console.log("Printing id", id);
    navigate(`/tool/edit/${id}`);
  };

  return (
    <Box sx={{ position: "relative" }}>
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
          overflow: "hidden",
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
          color: "white",
          textAlign: "center",
          paddingTop: 6,
        }}
      >
      
        <TableContainer component={Paper} sx={{ maxWidth: 1400, background: "rgba(198, 198, 198, 0.85)" }}>


          <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
            <TableHead>
              <TableRow>
                <TableCell colSpan={11} align="center">
                  <Typography variant="h5" sx={{ color: "black", fontWeight: "bold" }}>
                    Listado de Herramientas
                  </Typography>
                </TableCell>
              </TableRow> 
              {/* Fila de búsqueda y botón */}
              <TableRow>
                <TableCell colSpan={9} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar herramienta Por Nombre..."
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                    sx={{ width: 350, background: "white", borderRadius: 1 }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <SearchIcon />
                        </InputAdornment>
                      ),
                      sx: { height: 43 }
                    }}
                    inputProps={{
                      style: { height: 43, boxSizing: "border-box" }
                    }}
                  />
                </TableCell>
                <TableCell colSpan={2} align="right">
                  <Link
                    to="/tool/add"
                    style={{ textDecoration: "none" }}
                  >
                    <Button
                      variant="contained"
                      color="primary"
                      startIcon={<BuildIcon />}
                      size="large"
                      sx={{ height: 43, minWidth: 180 }}
                    >
                      Añadir Herramienta
                    </Button>
                  </Link>
                </TableCell>
              </TableRow>
              {/* Fila de encabezados */}
              <TableRow>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Nombre
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  categoria
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  loan fee
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  reposition fee
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  disponibility
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  diary fine fee
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  stock
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  low dmg fee
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Acciones
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredTools.map((tool) => (
                <TableRow
                  key={tool.toolId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{tool.toolId}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{tool.tool_name}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{tool.category}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{tool.loan_fee}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{tool.reposition_fee}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{tool.disponibility}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{tool.diary_fine_fee}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{tool.stock}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{tool.low_dmg_fee}</TableCell>
                  <TableCell>
                    <Button
                      variant="contained"
                      color="info"
                      size="small"
                      onClick={() => handleEdit(tool.toolId)}
                      style={{ marginLeft: "0.5rem" }}
                      startIcon={<EditIcon />}
                    >
                      Editar
                    </Button>

                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={() => handleDelete(tool.toolId)}
                      style={{ marginLeft: "0.5rem" }}
                      startIcon={<DeleteIcon />}
                    >
                      Eliminar
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default ToolList;
