import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import loansService from "../services/loans.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import DeleteIcon from "@mui/icons-material/Delete";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import VisibilityIcon from '@mui/icons-material/Visibility';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { lime, purple } from '@mui/material/colors';
import RotateRightIcon from '@mui/icons-material/RotateRight';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

const LoanList = () => {
  const [loans, setLoans] = useState([]);
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  // filter loans by date range (loan date or delivery date)
  const filteredLoans = loans.filter((loan) => {
    // IF no date range selected, include all loans
    if (!startDate && !endDate) return true;

    const dateStr = loan.date || loan.deliveryDate || "";
    if (!dateStr) return false;

    // parse date string to Date object
    const loanDate = new Date(dateStr.length === 10 ? dateStr + "T00:00:00Z" : dateStr);
    if (isNaN(loanDate)) return false;

    if (startDate) {
      // compare with start date (inclusive) local
      const s = new Date(startDate);
      s.setHours(0,0,0,0);
      if (loanDate < s) return false;
    }
    if (endDate) {
      // include loans up to the end date (inclusive) local
      const e = new Date(endDate);
      e.setHours(23,59,59,999);
      if (loanDate > e) return false;
    }
    return true;
  });

  const navigate = useNavigate();

  const init = () => {
    loansService
      .getAll()
      .then((response) => {
        setLoans(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de Prestamos.",
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

  const handleDelete = (id) => {
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar este prestamo?"
    );
    if (confirmDelete) {
      loansService
        .remove(id)
        .then(() => {
          init();
        })
        .catch((error) => {
          console.log(
            "Se ha producido un error al intentar eliminar el prestamo",
            error
          );
        });
    }
  };

  const handleViewLoan = (id) => {
    console.log("Printing id", id);
    navigate(`/loan/info/${id}`);
  };

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
      
        <TableContainer component={Paper} sx={{ width: "100%", background: "rgba(198, 198, 198, 0.85)" }}>


          <Table sx={{ width: "100%" }} size="small" aria-label="a dense table">
            <TableHead>
              <TableRow>
                <TableCell colSpan={10} align="center">
                  <Typography variant="h5" sx={{ color: "black", fontWeight: "bold" }}>
                    Listado de prestamos
                  </Typography>
                </TableCell>
              </TableRow> 
              {/* Fila de búsqueda por rango de fechas */}
              <TableRow>
                <TableCell colSpan={10} align="left">
                  <Box sx={{ display: "flex", gap: 1, alignItems: "center" }}>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                      <DatePicker
                        label="Desde"
                        value={startDate}
                        onChange={(newValue) => setStartDate(newValue)}
                        enableAccessibleFieldDOMStructure={false}
                        slots={{ textField: TextField }}
                        slotProps={{
                          textField: {
                            sx: { width: 180, background: "white", borderRadius: 1, '& .MuiInputBase-root': { height: 43 } },
                            InputLabelProps: { shrink: true }
                          }
                        }}
                      />
                      <DatePicker
                        label="Hasta"
                        value={endDate}
                        onChange={(newValue) => setEndDate(newValue)}
                        enableAccessibleFieldDOMStructure={false}
                        slots={{ textField: TextField }}
                        slotProps={{
                          textField: {
                            sx: { width: 180, background: "white", borderRadius: 1, '& .MuiInputBase-root': { height: 43 } },
                            InputLabelProps: { shrink: true }
                          }
                        }}
                      />
                    </LocalizationProvider>

                    <Button
                      variant="contained"
                      onClick={() => { setStartDate(null); setEndDate(null); }}
                      sx={{ height: 43 }}
                    >
                      Limpiar
                    </Button>
                  </Box>
                </TableCell>
                
              </TableRow>
              {/* row of labels */}
              <TableRow>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 100, fontWeight: "bold", color: "black" }}>
                  Estado
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
                <TableCell align="center" sx={{ maxWidth: 50, fontWeight: "bold", color: "black" }}>
                  ID Staff
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 50, fontWeight: "bold", color: "black" }}>
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
                  <TableCell align="left" sx={{ maxWidth: 100 }}>{loan.active? "Finalizado":"Activo"}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{loan.amount}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(loan.deliveryDate)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(loan.returnDate)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{formatDate(loan.date)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 50 }}>{loan.staffId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 50 }}>{loan.clientId}</TableCell>
                  <TableCell align="center" sx={{ width: 180 }}>{loan.extraCharges}</TableCell>
                  <TableCell align= "center" sx={{width:"100%"}}>
                    <ThemeProvider theme={theme} width="100%">
                      <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        width="100%"
                        onClick={() => handleViewLoan(loan.loanId)}
                        style={{ marginLeft: "0.5rem"}}
                        startIcon={<VisibilityIcon />}
                      >
                        Ver mas
                      </Button>
                      <Button
                        variant="contained"
                        color="secondary"
                        size="small"
                        width="100%"
                        onClick={() => navigate(`/loan/return/id/${loan.clientId}/${loan.loanId}`)}
                        style={{ marginLeft: "0.5rem" }}
                        startIcon={<RotateRightIcon />}
                        disabled={!loan.active}
                      >
                        Devolver
                      </Button>
                    </ThemeProvider>

                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      width="100%"
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

export default LoanList;
