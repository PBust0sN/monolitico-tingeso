import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import loansService from "../services/loans.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import Typography from "@mui/material/Typography";
import VisibilityIcon from '@mui/icons-material/Visibility';
import DeleteIcon from "@mui/icons-material/Delete";
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { lime, purple } from '@mui/material/colors';

const ListLoanId = () => {
  const { client_id } = useParams();
  const [loans, setLoans] = useState([]);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  const theme = createTheme({
    palette: {
      primary: lime,
      secondary: purple,
    },
  });

  useEffect(() => {
    loansService
      .getAll()
      .then((response) => {
        setLoans(response.data);
      })
      .catch((error) => {
        console.log("Error al obtener préstamos.", error);
      });
  }, []);

  // Filtrar por client_id y por búsqueda
  const filteredLoans = loans
    .filter(loan => String(loan.clientId) === String(client_id))
    .filter(loan =>
      (loan.loanType || "").toLowerCase().includes(search.toLowerCase())
    );

  const handleDelete = (id) => {
    const confirmDelete = window.confirm(
      "¿Está seguro que desea borrar este préstamo?"
    );
    if (confirmDelete) {
      loansService
        .remove(id)
        .then(() => {
          setLoans(loans.filter(loan => loan.loanId !== id));
        })
        .catch((error) => {
          console.log("Error al eliminar el préstamo", error);
        });
    }
  };

  const handleViewLoan = (id) => {
    navigate(`/loan/info/${id}`);
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    if (isNaN(date)) return "";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
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
                    Préstamos del Cliente {client_id}
                  </Typography>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell colSpan={10} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar préstamo por tipo..."
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
              <TableRow>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Tipo
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Cantidad
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Fecha de Entrega
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Fecha de Retorno
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Fecha
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  ID Staff
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  ID Cliente
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Cargos Extra
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Acciones
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredLoans.map((loan) => (
                <TableRow
                  key={loan.loanId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{loan.loanId}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{loan.loanType}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{loan.amount}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(loan.deliveryDate)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(loan.returnDate)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(loan.date)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{loan.staffId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{loan.clientId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{loan.extraCharges}</TableCell>
                  <TableCell>
                    <ThemeProvider theme={theme}>
                      <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        onClick={() => handleViewLoan(loan.loanId)}
                        style={{ marginLeft: "0.5rem" }}
                        startIcon={<VisibilityIcon />}
                      >
                        Ver más
                      </Button>
                    </ThemeProvider>
                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={() => handleDelete(loan.loanId)}
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
        <Box sx={{ display: "flex", justifyContent: "center", mt: 3 }}>
          <Button
            variant="contained"
            color="info"
            onClick={() => navigate(-1)}
          >
            Volver atrás
          </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default ListLoanId;