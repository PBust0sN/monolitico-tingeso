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
import Box from "@mui/material/Box";
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import Typography from "@mui/material/Typography";
import recordsService from "../services/records.service";
import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';
import MenuItem from "@mui/material/MenuItem";
import toolsService from "../services/tools.service";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [tools, setTools] = useState([]);
  const [selectedTool, setSelectedTool] = useState("");
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [filteredRecord, setFilteredRecord] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    recordsService
      .getAll()
      .then((response) => {
        setRecords(response.data);
        setFilteredRecord(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de records.",
          error
        );
      });

    toolsService
      .getAll()
      .then((response) => {
        setTools(response.data);
      })
      .catch((error) => {
        console.log("Error al obtener herramientas", error);
      });
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

  // Handler de búsqueda avanzada
  const handleSearch = () => {
    let filtered = [...records];
    if (selectedTool) {
      filtered = filtered.filter(r => String(r.toolId) === String(selectedTool));
    }
    if (startDate) {
      filtered = filtered.filter(r => {
        const recordDate = new Date(r.recordDate);
        return recordDate >= startDate;
      });
    }
    if (endDate) {
      filtered = filtered.filter(r => {
        const recordDate = new Date(r.recordDate);
        return recordDate <= endDate;
      });
    }
    setFilteredRecord(filtered);
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
                <TableCell colSpan={8} align="center">
                  <Typography variant="h5" sx={{ color: "black", fontWeight: "bold" }}>
                    Records
                  </Typography>
                </TableCell>
              </TableRow> 
              {/* advance search row */}
              <TableRow sx={{ '& .MuiTableCell-root': { paddingTop: 1, paddingBottom: 1 } }}>
                <TableCell colSpan={8} sx={{ py: 0 }}>
                  <Box sx={{ height: 43, display: "flex", gap: 2, alignItems: "center" }}>
                    {/* tools*/}
                    <TextField
                      select
                      label="Herramienta"
                      value={selectedTool}
                      onChange={e => setSelectedTool(e.target.value)}
                      sx={{
                        width: 200,
                        background: "white",
                        borderRadius: 1,
                      }}
                    >
                      <MenuItem value="" sx={{ textAlign: 'center' }}>Todas</MenuItem>
                      {tools.map(tool => (
                        <MenuItem key={tool.toolId} value={tool.toolId} sx={{ textAlign: 'center' }}>
                          {tool.tool_name}
                        </MenuItem>
                      ))}
                    </TextField>
                    {/* calendar */}
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                      <DatePicker
                        label="Fecha inicio"
                        value={startDate}
                        onChange={newValue => setStartDate(newValue)}
                        enableAccessibleFieldDOMStructure={false}
                        slots={{ textField: TextField }}
                        slotProps={{
                          textField: { sx: { width: 160, background: "white", borderRadius: 1} }
                        }}
                      />
                      <DatePicker
                        sx={{ minWidth: 160 }}
                        label="Fecha fin"
                        value={endDate}
                        onChange={newValue => setEndDate(newValue)}
                        enableAccessibleFieldDOMStructure={false}
                        slots={{ textField: TextField }}
                        slotProps={{
                          textField: { sx: { width: 160, background: "white", borderRadius: 1 } }
                        }}
                      />
                    </LocalizationProvider>
                    {/* search button*/}
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={handleSearch}
                      startIcon={<SearchIcon />}
                      sx={{
                        height: 56,          
                        minWidth: 140,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '0.95rem',
                        px: 2
                      }}
                    >
                      Buscar
                    </Button>
                  </Box>
                </TableCell>
                
              </TableRow>
              {/* row of labels */}
              <TableRow>
                <TableCell align="left" sx={{  maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>
                <TableCell align="left" sx={{  maxWidth: 250, fontWeight: "bold", color: "black" }}>
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
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Record Amount
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
                  <TableCell align="left" sx={{ maxWidth: 250 }}>{record.recordType}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{formatDate(record.recordDate)}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.toolId == null ? "N/A" : record.toolId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.clientId == null ? "N/A" : record.clientId}</TableCell>
                  <TableCell align="center" sx={{ maxWidth: 180 }}>{record.loanId == null ? "N/A" : record.loanId}</TableCell>
                  <TableCell align="left" sx={{ maxWidth: 180 }}>{record.recordAmount == null ? "N/A": record.recordAmount}</TableCell>
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
