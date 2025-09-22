import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import fineService from "../services/fine.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import DeleteIcon from "@mui/icons-material/Delete";
import Box from "@mui/material/Box"; // <-- Agrega esto si no lo tienes
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import Typography from "@mui/material/Typography";
import VisibilityIcon from '@mui/icons-material/Visibility';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { lime, purple } from '@mui/material/colors';

const FineList = () => {
  const [fines, setFines] = useState([]);
  const [search, setSearch] = useState("");

  const filteredFines = fines.filter(fine =>
  String(fine.clientId).toLowerCase().includes(search.toLowerCase())
);

  const navigate = useNavigate();

  const init = () => {
    fineService
      .getAll()
      .then((response) => {
        setFines(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de Multas.",
          error
        );
      });
  };
  const theme = createTheme({
      palette: {
        primary: lime,
        secondary: purple,
      },
    });

  useEffect(() => {
    init();
  }, []);


const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr.length === 10 ? dateStr + "T00:00:00Z" : dateStr);
  if (isNaN(date)) return dateStr;
  const day = String(date.getUTCDate()).padStart(2, "0");
  const month = String(date.getUTCMonth() + 1).padStart(2, "0");
  const year = date.getUTCFullYear();
  return `${day}/${month}/${year}`;
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
                    Listado de Multas
                  </Typography>
                </TableCell>
              </TableRow> 
              {/* Fila de búsqueda y botón */}
              <TableRow>
                <TableCell colSpan={10} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar prestamo Por tipo..."
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
                
              </TableRow>
              {/* Fila de encabezados */}
              <TableRow>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Tipo
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  State
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Client ID
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Loan ID
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Amount
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Date
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Acciones
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredFines.map((fines) => (
                <TableRow
                  key={fines.fineId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{fines.fineId}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{fines.type}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{fines.state}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{fines.clientId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{fines.loanId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{fines.amount}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(fines.date)}</TableCell>
                  <TableCell>

                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={() => handleDelete(loan.loanId)}
                      style={{ marginLeft: "0.5rem" }}
                      startIcon={<DeleteIcon />}
                    >
                      Pagar
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

export default FineList;