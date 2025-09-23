import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import Sidemenu from "./SideMenu";
import { useState } from "react";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";
import LogoutIcon from '@mui/icons-material/Logout';

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();

  const toggleDrawer = (open) => (event) => {
    setOpen(open);
  };

  return (
    <Box sx={{ 
      flexGrow: 1 ,
      position: "fixed",
      top: 0,
      left: 0,
      width: "100%",
      zIndex: 1000,
    }}>
      <AppBar position="static"
        sx={{ backgroundColor: "#011cba76"}}
        >
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={toggleDrawer(true)}
          >
            <MenuIcon />
          </IconButton>

          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            ToolRent: Sistema de Renta De Herramientas
          </Typography>
          {initialized && (
            <>
              {keycloak.authenticated ? (
                <>
                  <Typography sx={{ mr: 2 }}>
                    {keycloak.tokenParsed?.preferred_username ||
                      keycloak.tokenParsed?.email}
                  </Typography>
                  <IconButton color="inherit" onClick={() => keycloak.logout()}>
                    <LogoutIcon />
                  </IconButton>
                </>
              ) : (
                <Button variant="outlined" color="inherit" onClick={() => navigate("/login")}>
                  Login
                </Button>
              )}
            </>
          )}
        </Toolbar>
      </AppBar>

      <Sidemenu open={open} toggleDrawer={toggleDrawer}></Sidemenu>
    </Box>
  );
}