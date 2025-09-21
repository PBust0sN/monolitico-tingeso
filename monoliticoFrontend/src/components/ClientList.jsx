import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import clientService from "../services/client.service";
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
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { lime, purple } from '@mui/material/colors';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import RotateRightIcon from '@mui/icons-material/RotateRight';
import VisibilityIcon from '@mui/icons-material/Visibility';

const ClientList = () => {
  const [client, setclients] = useState([]);
  const [search, setSearch] = useState("");

  const filteredClient = client.filter(client =>
  client.name.toLowerCase().includes(search.toLowerCase())
);

  const navigate = useNavigate();

  const theme = createTheme({
    palette: {
      primary: lime,
      secondary: purple,
    },
  });

  const init = () => {
    clientService
      .getAll()
      .then((response) => {
        setclients(response.data);
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
      clientService
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
    navigate(`/client/edit/${id}`);
  };

  const handleNewLoan = (id) => {
    console.log("Printing id", id);
    navigate(`/loan/new/${id}`);
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
                    Listado de Clientes
                  </Typography>
                </TableCell>
              </TableRow> 
              {/* Fila de búsqueda y botón */}
              <TableRow>
                <TableCell colSpan={7} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar Cliente Por Nombre..."
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
                    to="/client/add"
                    style={{ textDecoration: "none" }}
                  >
                    <Button
                      variant="contained"
                      color="primary"
                      startIcon={<PersonAddIcon />}
                      size="large"
                      sx={{ height: 43, minWidth: 180 }}
                    >
                      Añadir Cliente
                    </Button>
                  </Link>
                </TableCell>
              </TableRow>
              {/* Fila de encabezados */}
              <TableRow>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Rut
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  lastName
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Name
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  email
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  phone Number
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Avaliable
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  state
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Acciones
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredClient.map((client) => (
                <TableRow
                  key={client.client_id}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{client.client_id}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{client.rut}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{client.last_name}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{client.name}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{client.mail}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{client.phone_number}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{client.avaliable ? "Si" : "No"}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{client.state}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 300 }}>
                    <ThemeProvider theme={theme}>
                    <Button
                      variant="contained"
                      color="primary"
                      size="small"
                      onClick={() => handleNewLoan(client.client_id)}
                      style={{ marginLeft: "0.5rem", marginTop: "0.5rem"}}
                      startIcon={<AddCircleIcon />}
                    >
                      New Loan
                    </Button>
                    </ThemeProvider>
                    <ThemeProvider theme={theme}>
                    <Button
                      variant="contained"
                      color="secondary"
                      size="small"
                      onClick={() => navigate(`/loan/list/${client.client_id}`)}
                      style={{ marginLeft: "0.5rem", marginTop: "0.5rem"}}
                      startIcon={<VisibilityIcon />}
                    >
                      See Loans
                    </Button>
                    </ThemeProvider>
                    <Button
                      variant="contained"
                      color="info"
                      size="small"
                      onClick={() => handleEdit(client.client_id)}
                      style={{ marginLeft: "0.5rem", marginTop: "0.5rem" }}
                      startIcon={<EditIcon />}
                    >
                      Editar
                    </Button>

                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={() => handleDelete(client.client_id)}
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

export default ClientList;
