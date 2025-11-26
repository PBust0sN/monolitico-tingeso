import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import Paper from "@mui/material/Paper";
import clientService from "../services/client.service";
import MenuItem from "@mui/material/MenuItem";

const AddClient = () => {
  const [avaliable, setAvaliable] = useState("true");
  const [last_name, setLastName] = useState("");
  const [mail, setMail] = useState("");
  const [name, setName] = useState("");
  const [phone_number, setPhoneNumber] = useState("");
  const [rut, setRut] = useState("");
  const [state, setState] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  // Errors state
  const [errorsList, setErrorsList] = useState([]);
  const [fieldErrors, setFieldErrors] = useState({});

  const validateFields = () => {
    const errors = [];
    const fErrors = {};

    if (!rut || !rut.trim()) {
      errors.push("Rut es obligatorio.");
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
      // simple email regex
      const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRe.test(mail)) {
        errors.push("E-Mail no tiene un formato válido.");
        fErrors.mail = true;
      }
    }

    if (!password || password.length < 6) {
      errors.push("Password obligatorio (mínimo 6 caracteres).");
      fErrors.password = true;
    }

    if (!phone_number || !/^\d+$/.test(phone_number)) {
      errors.push("Phone Number obligatorio y sólo debe contener dígitos.");
      fErrors.phone_number = true;
    }

    return { errors, fErrors };
  };

  const saveClient = (e) => {
    e.preventDefault();

    const { errors, fErrors } = validateFields();
    setErrorsList(errors);
    setFieldErrors(fErrors);

    if (errors.length > 0) {
      window.alert(errors.join("\n"));
      return;
    }

    const client = {
      rut,
      name,
      last_name,
      mail,
      state: "activo",
      phone_number,
      password,
    };

    console.log(client);
    clientService
      .create(client)
      .then((response) => {
        console.log("Cliente añadido.", response.data);
        navigate("/client/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar crear nuevo cliente.", error);
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
            <h3>Nuevo Cliente</h3>
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
                onChange={(e) => setRut(e.target.value)}
                helperText="99999999-9"
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

            {/* Password field */}
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="password"
                label="Password"
                type="password"
                value={password}
                variant="standard"
                onChange={(e) => setPassword(e.target.value)}
                helperText="Mínimo 6 caracteres"
                error={!!fieldErrors.password}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="phone_number"
                label="Phone Number"
                value={phone_number}
                variant="standard"
                onChange={(e) => setPhoneNumber(e.target.value)}
                helperText="Ej. 911112222"
                error={!!fieldErrors.phone_number}
              />
            </FormControl>
            <FormControl sx={{ mb: 2 }}>
              <Button
                variant="contained"
                color="info"
                onClick={saveClient}
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

export default AddClient;