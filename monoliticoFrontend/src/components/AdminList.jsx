import React from "react";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
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
import Box from "@mui/material/Box";
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import Typography from "@mui/material/Typography";
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { lime, purple, lightGreen, lightBlue } from '@mui/material/colors';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import VisibilityIcon from '@mui/icons-material/Visibility';
import Collapse from "@mui/material/Collapse";
import IconButton from "@mui/material/IconButton";
import ExpandCircleDownIcon from '@mui/icons-material/ExpandCircleDown';

const AdminList = () => {
  const { keycloak } = useKeycloak(); // <-- agregado
  const [client, setclients] = useState([]);
  const [search, setSearch] = useState("");
  const [expandedRow, setExpandedRow] = useState(null);

  const isAdmin = Boolean(
    keycloak &&
      (
        keycloak.tokenParsed?.realm_access?.roles?.includes("ADMIN") ||
        (typeof keycloak.hasRealmRole === "function" && keycloak.hasRealmRole("ADMIN"))
      )
  );

  // Mostrar solo clientes cuyo rol incluye "CLIENT"
  const filteredClient = client
    .filter(c => {
      // soportar distintos formatos que el backend pueda retornar
      const rolesField = c.roles ?? c.keycloakRoles ?? c.realm_roles ?? c.role;
      if (Array.isArray(rolesField)) {
        return rolesField.map(r => String(r).toUpperCase()).includes("ADMIN");
      }
      if (typeof rolesField === "string") {
        return rolesField.toUpperCase() === "ADMIN";
      }
      return false;
    })
    .filter(c => (c.rut || "").includes(search));
 
  const navigate = useNavigate();

  const theme = createTheme({
    palette: {
      primary: lime,
      secondary: purple,
      success: lightGreen,
      error: lightBlue,
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
    navigate(`/client/edit/${id}`);
  };

  const handleNewLoan = (id) => {
    navigate(`/loan/new/${id}`);
  };


  const handleExpandClick = (id) => {
    setExpandedRow(expandedRow === id ? null : id);
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
                <TableCell colSpan={10} align="center">
                  <Typography variant="h5" sx={{ color: "black", fontWeight: "bold" }}>
                    Listado de Admins
                  </Typography>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell colSpan={8} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar cliente por rut..."
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
                  <Box sx={{ display: "flex", gap: 2, justifyContent: "flex-end" }}>
                    <Link to="/admin/add" style={{ textDecoration: "none" }}>
                      <Button
                        variant="contained"
                        color="primary"
                        startIcon={<PersonAddIcon />}
                        size="large"
                        sx={{ height: 43, minWidth: 170 }}
                      >
                        Añadir Admin
                      </Button>
                    </Link>
                  </Box>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
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
                  Role
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Avaliable
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 150, fontWeight: "bold", color: "black" }}>
                  state
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredClient.map((client) => (
                <React.Fragment key={client.client_id}>
                  <TableRow
                    sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                  >
                    <TableCell align="left" sx={{ maxWidth: 180 }}>{client.client_id}</TableCell>
                    <TableCell align="left" sx={{ maxWidth: 180 }}>{client.rut}</TableCell>
                    <TableCell align="left" sx={{ maxWidth: 180 }}>{client.last_name}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.name}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.mail}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.phone_number}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.role}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.avaliable ? "Si" : "No"}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 150 }}>{client.state}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>
                      <Box sx={{ display: "flex", gap: 1, justifyContent: "center" }}>
                      <Button
                            variant="contained"
                            color="info"
                            size="small"
                            onClick={() => handleEdit(client.client_id)}
                            startIcon={<EditIcon />}
                          >
                            Editar
                      </Button>
                      <Button
                            variant="contained"
                            color="error"
                            size="small"
                            onClick={() => handleDelete(client.client_id)}
                            startIcon={<DeleteIcon />}
                          >
                            Eliminar
                          </Button>
                      </Box>
                    </TableCell>
                  </TableRow>
                </React.Fragment>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Box sx={{display: "flex", flexDirection: "row", justifyContent: "center", gap: 2, mt: 2}}>
            <Button
            variant="contained"
            sx={{ mt: 2 }}
            onClick={() => navigate("/")}
            >
            Volver atras
            </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default AdminList;
