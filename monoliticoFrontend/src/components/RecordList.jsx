import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Box from "@mui/material/Box"; // <-- Agrega esto si no lo tienes
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import Typography from "@mui/material/Typography";
import recordsService from "../services/records.service";
import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [search, setSearch] = useState("");

  const filteredRecord = records.filter(record =>
  record.recordType.toLowerCase().includes(search.toLowerCase())
);

  const navigate = useNavigate();

  const init = () => {
    recordsService
      .getAll()
      .then((response) => {
        setRecords(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de records.",
          error
        );
      });
  };

  useEffect(() => {
    init();
  }, []);

  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    if (isNaN(date)) return "";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  };

  const handleDelete = (id) => {
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar este record?"
    );
    if (confirmDelete) {
      recordsService
        .remove(id)
        .then(() => {
          init();
        })
        .catch((error) => {
          console.log(
            "Se ha producido un error al intentar eliminar el record",
            error
          );
        });
    }
  };

  const handleEdit = (id) => {
    console.log("Printing id", id);
    navigate(`/record/edit/${id}`);
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
                <TableCell colSpan={9} align="center">
                  <Typography variant="h5" sx={{ color: "black", fontWeight: "bold" }}>
                    Records
                  </Typography>
                </TableCell>
              </TableRow> 
              {/* Fila de búsqueda y botón */}
              <TableRow>
                <TableCell colSpan={7} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar Record Por Tipo..."
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
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => navigate('/record/add')}
                      startIcon={<CreateNewFolderIcon />}
                      size="large"
                      sx={{ height: 43, minWidth: 180 }}
                    >
                      Nuevo Record
                    </Button>
                </TableCell>
              </TableRow>
              {/* Fila de encabezados */}
              <TableRow>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Record Type
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Record Date
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Tool ID
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Client Id
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Loan ID
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Record Amount
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Acciones
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredRecord.map((record) => (
                <TableRow
                  key={record.recordId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{record.recordId}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{record.recordType}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{formatDate(record.recordDate)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.toolId == null ? "N/A" : record.toolId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.clientId == null ? "N/A" : record.clientId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.loanId == null ? "N/A" : record.loanId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.recordAmount}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 300 }}>
                    
                    <Button
                      variant="contained"
                      color="info"
                      size="small"
                      onClick={() => handleEdit(record.recordId)}
                      style={{ marginLeft: "0.5rem", marginTop: "0.5rem" }}
                      startIcon={<EditIcon />}
                    >
                      Editar
                    </Button>

                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={() => handleDelete(record.recordId)}
                      style={{ marginLeft: "1.9rem", marginTop: "0.5rem" }}
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

export default RecordList;
