import * as React from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import PeopleAltIcon from "@mui/icons-material/PeopleAlt";
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import DiscountIcon from "@mui/icons-material/Discount";
import HailIcon from "@mui/icons-material/Hail";
import MedicationLiquidIcon from "@mui/icons-material/MedicationLiquid";
import HomeIcon from "@mui/icons-material/Home";
import { useNavigate } from "react-router-dom";
import HandymanIcon from '@mui/icons-material/Handyman';
import BorderColorIcon from '@mui/icons-material/BorderColor';
import FolderSpecialIcon from '@mui/icons-material/FolderSpecial';

export default function Sidemenu({ open, toggleDrawer }) {
  const navigate = useNavigate();

  const listOptions = () => (
    <Box
      role="presentation"
      onClick={toggleDrawer(false)}
    >
      <List>
        <ListItemButton onClick={() => navigate("/home")}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <Divider />

        <ListItemButton onClick={() => navigate("/client/list")}>
          <ListItemIcon>
            <PeopleAltIcon />
          </ListItemIcon>
          <ListItemText primary="Clientes" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/tool/list")}>
          <ListItemIcon>
            <HandymanIcon />
          </ListItemIcon>
          <ListItemText primary="Herramientas" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/loan/list")}>
          <ListItemIcon>
            <InsertDriveFileIcon />
          </ListItemIcon>
          <ListItemText primary="Prestamos" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/record/list")}>
          <ListItemIcon>
            <FolderSpecialIcon />
          </ListItemIcon>
          <ListItemText primary="Records" />
        </ListItemButton>
        <ListItemButton onClick={() => navigate("/reports/AnualReport")}>
          <ListItemIcon>
            <BorderColorIcon />
          </ListItemIcon>
          <ListItemText primary="Modificar Planillas" />
        </ListItemButton>
      </List>

      <Divider />

      <List>
        <ListItemButton onClick={() => navigate("/employee/discounts")}>
          <ListItemIcon>
            <DiscountIcon />
          </ListItemIcon>
          <ListItemText primary="Descuentos" />
        </ListItemButton>
        <ListItemButton onClick={() => navigate("/paycheck/vacations")}>
          <ListItemIcon>
            <HailIcon />
          </ListItemIcon>
          <ListItemText primary="Vacaciones" />
        </ListItemButton>
        <ListItemButton onClick={() => navigate("/paycheck/medicalleave")}>
          <ListItemIcon>
            <MedicationLiquidIcon />
          </ListItemIcon>
          <ListItemText primary="Licencias Medicas" />
        </ListItemButton>
      </List>
    </Box>
  );

  return (
    <div>
      <Drawer
  anchor="left"
  open={open}
  onClose={toggleDrawer(false)}
  hideBackdrop={true}
  PaperProps={{
    sx: {
      backgroundColor: "rgba(255, 255, 255, 0.71)", // transparente
      backdropFilter: "blur(8px)",              // opcional: desenfoque
      boxShadow: "none",                        // opcional: sin sombra
    }
  }}
>
  {listOptions()}
      </Drawer>
    </div>
  );
}
