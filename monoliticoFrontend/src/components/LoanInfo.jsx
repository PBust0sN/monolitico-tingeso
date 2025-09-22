import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import loansService from "../services/loans.service";
import toolsService from "../services/tools.service";
import toolsLoansService from "../services/toolsLoans.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

const LoanInfo = () => {
  const { loan_id } = useParams();
  const [loan, setLoan] = useState(null);
  const [tools, setTools] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    // Obtener la información del préstamo
    loansService
      .get(loan_id)
      .then((response) => {
        setLoan(response.data);

        // Obtener los ids de herramientas asociadas a este préstamo
        toolsLoansService
          .getToolsIdByLoanId(loan_id)
          .then(async (idsResponse) => {
            const toolIds = idsResponse.data; // debe ser un array de ids

            // Obtener la información de cada herramienta por id
            const toolPromises = toolIds.map((id) =>
              toolsService.get(id).then((res) => res.data)
            );
            const toolDetails = await Promise.all(toolPromises);
            setTools(toolDetails);
          })
          .catch((error) => {
            console.log("Error al obtener ids de herramientas:", error);
          });
      })
      .catch((error) => {
        console.log("Error al obtener préstamo:", error);
      });
  }, [loan_id]);


  const handleDelete = (id) => {
      const confirmDelete = window.confirm(
        "¿Esta seguro que desea borrar esta herramienta?"
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


  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    if (isNaN(date)) return "";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  };

  if (!loan) {
    return (
      <Box
        sx={{
          position: "relative",
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Typography variant="h6" color="text.secondary">
          Cargando información del préstamo...
        </Typography>
      </Box>
    );
  }

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
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          color: "black",
          textAlign: "left",
          paddingTop: 6,
        }}
      >
        <Paper
          sx={{
            maxWidth: 700,
            width: "100%",
            mb: 3,
            background: "rgba(255,255,255,0.85)",
            p: 3,
          }}
        >
          <Typography
            variant="h5"
            align="center"
            sx={{ fontWeight: "bold", mb: 2 }}
          >
            Información del Préstamo
          </Typography>
          <TableContainer>
            <Table>
              <TableBody>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID Préstamo</TableCell>
                  <TableCell>{loan.loanId}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Tipo</TableCell>
                  <TableCell>{loan.loanType}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
                  <TableCell>{loan.amount}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Fecha de Entrega</TableCell>
                  <TableCell>{formatDate(loan.deliveryDate)}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Fecha de Retorno</TableCell>
                  <TableCell>{formatDate(loan.returnDate)}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Fecha</TableCell>
                  <TableCell>{formatDate(loan.date)}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID Staff</TableCell>
                  <TableCell>{loan.staffId}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID Cliente</TableCell>
                  <TableCell>{loan.clientId}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Cargos Extra</TableCell>
                  <TableCell>{loan.extraCharges}</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>

        {/* Lista de herramientas asociadas */}
        <Paper
          sx={{
            maxWidth: 700,
            width: "100%",
            background: "rgba(255,255,255,0.85)",
            p: 3,
          }}
        >
          <Typography
            variant="h6"
            align="center"
            sx={{ fontWeight: "bold", mb: 2 }}
          >
            Herramientas asociadas
          </Typography>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID Herramienta</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Nombre</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Categoría</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Disponibilidad</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {tools.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={4} align="center">
                      No hay herramientas asociadas a este préstamo.
                    </TableCell>
                  </TableRow>
                ) : (
                  tools.map((tool) => (
                    <TableRow key={tool.toolId}>
                      <TableCell>{tool.toolId}</TableCell>
                      <TableCell>{tool.tool_name}</TableCell>
                      <TableCell>{tool.category}</TableCell>
                      <TableCell>{tool.disponibility}</TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>
        <Box sx={{display: "flex", flexDirection: "row", justifyContent: "center", gap: 2, mt: 2}}>
            <Button
            variant="contained"
            sx={{ mt: 2 }}
            onClick={() => navigate("/")}
            >
            Volver atras
            </Button>
            

            <Button
                variant="contained"
                color="error"
                sx = {{ mt: 2}}
                onClick={() => handleDelete(loan.loan_id)}
                startIcon={<DeleteIcon />}
            >
                Eliminar
            </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default LoanInfo;
