import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import Paper from "@mui/material/Paper";
import clientService from "../services/client.service";
import MenuItem from "@mui/material/MenuItem";

const EditClient = () => {
  const [last_name, setLastName] = useState("");
  const [mail, setMail] = useState("");
  const [name, setName] = useState("");
  const [phone_number, setPhoneNumber] = useState("");
  const [rut, setRut] = useState("");
  const [state, setState] = useState("");
  const [role, setRole] = useState("");
  const [password, setPassword] = useState("");
  const { client_id } = useParams();
  const navigate = useNavigate();

  // control de errores (igual que en AddClient)
  const [errorsList, setErrorsList] = useState([]);
  const [fieldErrors, setFieldErrors] = useState({});

  useEffect(() => {
    if (client_id) {
      clientService
        .get(client_id)
        .then((client) => {
          // quitar puntos si vienen desde backend
          setRut((client.data.rut || "").replace(/\./g, ""));
          setName(client.data.name);
          setLastName(client.data.last_name);
          setMail(client.data.mail);
          setState(client.data.state);
          setPhoneNumber(client.data.phone_number);
          setPassword(client.data.password);
          setRole(client.data.role);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    }
  }, [client_id]);

  const validateFields = () => {
    const errors = [];
    const fErrors = {};

    if (!rut || !rut.trim()) {
      errors.push("Rut es obligatorio.");
      fErrors.rut = true;
    }
    // prohibir puntos en el RUT
    if ((rut || "").includes(".")) {
      errors.push("Rut no debe contener puntos.");
      fErrors.rut = true;
    }

    if (!name || !name.trim()) {
      errors.push("Name es obligatorio.");
      fErrors.name = true;
    }

    if (!last_name || !last_name.trim()) {
      errors.push("Last Name es obligatorio.");
      fErrors.last_name = true;
    }

    if (!mail || !mail.trim()) {
      errors.push("E-Mail es obligatorio.");
      fErrors.mail = true;
    } else {
      const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRe.test(mail)) {
        errors.push("E-Mail no tiene un formato válido.");
        fErrors.mail = true;
      }
    }

    if (!phone_number || !/^\+?\d+$/.test(phone_number)) {
      errors.push("Phone Number obligatorio y sólo debe contener dígitos (puede incluir +).");
      fErrors.phone_number = true;
    }

    return { errors, fErrors };
  };

  const saveTool = (e) => {
    e.preventDefault();

    const { errors, fErrors } = validateFields();
    setErrorsList(errors);
    setFieldErrors(fErrors);

    if (errors.length > 0) {
      window.alert(errors.join("\n"));
      return;
    }

    const client = { client_id, rut, name, last_name, mail, state, phone_number, password, role };
    clientService
      .update(client)
      .then((response) => {
        console.log("Cliente actualizado.", response.data);
        navigate("/client/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar actualizar el cliente.", error);
      });
  };

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
      {/* Frame del formulario */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            minWidth: 350,
            maxWidth: 450,
            width: "90%",
            background: "rgba(255,255,255,0.85)",
            color: "#222",
          }}
        >
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            component="form"
          >
            <h3>Editar Cliente</h3>
            <hr />

            {/* Lista de errores */}
            {errorsList.length > 0 && (
              <Box
                sx={{
                  width: "100%",
                  mb: 2,
                  p: 1,
                  borderRadius: 1,
                  backgroundColor: "rgba(255,200,200,0.9)",
                }}
              >
                <ul style={{ margin: 0, paddingLeft: "1rem", color: "#700" }}>
                  {errorsList.map((err, idx) => (
                    <li key={idx}>{err}</li>
                  ))}
                </ul>
              </Box>
            )}

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="rut"
                label="Rut"
                value={rut}
                variant="standard"
                onChange={(e) => setRut(e.target.value.replace(/\./g, ""))}
                helperText="99999999-9 (sin puntos)"
                error={!!fieldErrors.rut}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="name"
                label="Name"
                value={name}
                variant="standard"
                onChange={(e) => setName(e.target.value)}
                error={!!fieldErrors.name}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="last_name"
                label="Last Name"
                value={last_name}
                variant="standard"
                onChange={(e) => setLastName(e.target.value)}
                error={!!fieldErrors.last_name}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="mail"
                label="E-Mail"
                value={mail}
                variant="standard"
                onChange={(e) => setMail(e.target.value)}
                error={!!fieldErrors.mail}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="state"
                label="State"
                value={state}
                variant="standard"
                onChange={(e) => setState(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="phone_number"
                label="Phone Number"
                value={phone_number}
                variant="standard"
                onChange={(e) => setPhoneNumber(e.target.value)}
                helperText="Ej. +56911112222"
                error={!!fieldErrors.phone_number}
              />
            </FormControl>

            <FormControl sx={{ mb: 2 }}>
              <Button
                variant="contained"
                color="info"
                onClick={saveTool}
                startIcon={<SaveIcon />}
              >
                Grabar
              </Button>
            </FormControl>
            <hr />
            <Link to="/client/list">Back to List</Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default EditClient;
