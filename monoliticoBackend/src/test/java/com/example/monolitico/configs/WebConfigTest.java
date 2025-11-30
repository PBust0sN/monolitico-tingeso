package com.example.monolitico.configs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
// 1. Activa el perfil "test" para leer application-test.properties (H2)
@ActiveProfiles("test")
// 2. addFilters = false DESACTIVA la seguridad (Login/Keycloak) para este test.
// Así no necesitas pelear con la configuración de JWT en el properties.
@AutoConfigureMockMvc(addFilters = false)
class WebConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void errorAlEncontrarImagen(@TempDir Path tempDir) throws Exception {
        // --- PREPARACIÓN ---
        // Usamos la ruta que definimos en application-test.properties:
        // image.upload.dir=${java.io.tmpdir}/test-uploads
        Path fixedUploadDir = Path.of(System.getProperty("java.io.tmpdir"), "test-uploads");

        // Nos aseguramos de crear la carpeta y limpiarla si existía
        if (!Files.exists(fixedUploadDir)) {
            Files.createDirectories(fixedUploadDir);
        }

        String filename = "1.png";
        Path filePath = fixedUploadDir.resolve(filename);
        Files.write(filePath, "contenido-falso-de-imagen".getBytes());

        // --- EJECUCIÓN Y VERIFICACIÓN ---
        try {
            mockMvc.perform(get("/api/images/" + filename))
                    .andExpect(status().is(404));
        } finally {
            // --- LIMPIEZA ---
            // Es buena práctica borrar el archivo creado para no llenar la carpeta temporal del sistema
            Files.deleteIfExists(filePath);
        }
    }
}