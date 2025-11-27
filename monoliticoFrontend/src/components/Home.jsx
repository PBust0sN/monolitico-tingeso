import { Container, Typography, Button, Box, Card, CardContent } from "@mui/material";

const Home = () => {
    return (
      <Box sx = {{position: "relative"}}>
        <Box 
      sx={{
        position: "fixed",                // occupies all the screen
          top: 0,
          left: 0,
          width: "100%",                // total width of the window
          height: "100%",               // total height of the window                    
        backgroundImage: `url("/fondo.jpg")`,
        backgroundSize: "cover",            // cover the entire area
        backgroundPosition: "center",       // centered
        backgroundRepeat: "no-repeat",  
        overflow: "hidden",     // does not repeat
        display: "flex",                    // for centering
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        color: "white",
        textAlign: "center",
        filter: "blur(8px)"
      }}
    >
    </Box>
    <Box
      sx={{
        position: "relative",
        zIndex: 1,
        height: "100%",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        color: "white",
        textAlign: "center",
      }}
      >
      <Typography
        variant="h3"
        gutterBottom
        sx={{
          fontWeight: 700,
          color: "#fff",
          WebkitTextStroke: "1px rgba(0,0,0,0.85)", // webkit outline
          textShadow: [
            "1px 1px 0 rgba(0,0,0,0.85)",
            "-1px 1px 0 rgba(0,0,0,0.85)",
            "1px -1px 0 rgba(0,0,0,0.85)",
            "-1px -1px 0 rgba(0,0,0,0.85)"
          ].join(", "), // fallback for outline in other browsers
        }}
      >
        Bienvenido Al Sistema de Renta de Herramientas Tool Rent
      </Typography>
      <Typography
      variant="h5"
        gutterBottom
        sx={{
          fontWeight: 700,
          color: "#fff",
          WebkitTextStroke: "1px rgba(0,0,0,0.85)", // Wedbkit outline
          textShadow: [
            "1px 1px 0 rgba(0,0,0,0.85)",
            "-1px 1px 0 rgba(0,0,0,0.85)",
            "1px -1px 0 rgba(0,0,0,0.85)",
            "-1px -1px 0 rgba(0,0,0,0.85)"
          ].join(", "), // fallback for outline in other browsers
        }}
        >
        Hace click en el menu lateral para alguna acción
      </Typography>      
      </Box>
    </Box>
    
    );
};

export default Home;