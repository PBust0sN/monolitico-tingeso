import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import toolsService from "../services/tools.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

const ToolList = () => {
  const [tools, setTools] = useState([]);

  const navigate = useNavigate();

  const init = () => {
    toolsService
      .getAll()
      .then((response) => {
        console.log("Mostrando listado de las herramientas.", response.data);
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
    console.log("Printing id", id);
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar esta herramienta?"
    );
    if (confirmDelete) {
      toolsService
        .remove(id)
        .then((response) => {
          console.log("herramienta ha sido eliminada.", response.data);
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
    <TableContainer component={Paper}>
      <br />
      <Link
        to="/tool/add"
        style={{ textDecoration: "none", marginBottom: "1rem" }}
      >
        <Button
          variant="contained"
          color="primary"
          startIcon={<PersonAddIcon />}
        >
          Añadir Herramienta
        </Button>
      </Link>
      <br /> <br />
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Id
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Nombre
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              categoria
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              loan fee
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              reposition fee
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              disponibility
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              diary fine fee
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              state
            </TableCell>
            <TableCell align="center" sx={{ fontWeight: "bold" }}>
              Acciones
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {tools.map((tool) => (
            <TableRow
              key={tool.tool_id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell align="left">{tool.toolId}</TableCell>
              <TableCell align="left" sx={{maxWidth: 180}}>{tool.toolName}</TableCell>
              <TableCell align="right">{tool.category}</TableCell>
              <TableCell align="right">{tool.loanFee}</TableCell>
              <TableCell align="right">{tool.repositionFee}</TableCell>
              <TableCell align="right">{tool.disponibility ? "Sí" : "No"}</TableCell>
              <TableCell align="right">{tool.diaryFineFee}</TableCell>
              <TableCell align="right">{tool.initialState}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  color="info"
                  size="small"
                  onClick={() => handleEdit(tool.tool_id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<EditIcon />}
                >
                  Editar
                </Button>

                <Button
                  variant="contained"
                  color="error"
                  size="small"
                  onClick={() => handleDelete(tool.tool_id)}
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
  );
};

export default ToolList;
