package com.example.monolitico.Services;

import com.example.monolitico.Service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    private ImageService imageService;

    @TempDir
    Path tempDir; // Crea un directorio temporal seguro para pruebas

    @BeforeEach
    void setUp() {
        imageService = new ImageService();
        imageService.setUploadDir(tempDir.toString()); // mucho más limpio
    }

    // -------------------------------------------------------------
    //                TESTS PARA saveImage()
    // -------------------------------------------------------------

    @Test
    void testSaveImage_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.png",
                "image/png",
                "fake image data".getBytes()
        );

        Optional<String> result = imageService.saveImage(file);

        assertTrue(result.isPresent(), "Debe retornar nombre de archivo");
        assertTrue(Files.exists(tempDir.resolve(result.get())), "El archivo debe existir");
    }

    @Test
    void testSaveImage_CustomFilename() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                "contenido".getBytes()
        );

        Optional<String> result = imageService.saveImage(file, "customName.jpg");

        assertTrue(result.isPresent());
        assertEquals("customName.jpg", result.get());
        assertTrue(Files.exists(tempDir.resolve("customName.jpg")));
    }

    @Test
    void testSaveImage_NullFile_ReturnsEmpty() {
        Optional<String> result = imageService.saveImage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveImage_EmptyFile_ReturnsEmpty() {
        MockMultipartFile file = new MockMultipartFile("file", new byte[]{});

        Optional<String> result = imageService.saveImage(file);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveImage_InvalidContentType_Throws() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "archivo.txt",
                "text/plain",
                "no es imagen".getBytes()
        );

        Optional<String> result = imageService.saveImage(file);

        assertTrue(result.isEmpty(), "Debe retornar vacío por imagen inválida");
    }

    // -------------------------------------------------------------
    //                TESTS PARA getImage()
    // -------------------------------------------------------------

    @Test
    void testGetImage_Success() throws IOException {
        // Crear archivo de prueba
        Path filePath = tempDir.resolve("foto.png");
        Files.write(filePath, "datos".getBytes());

        Optional<byte[]> bytes = imageService.getImage("foto.png");

        assertTrue(bytes.isPresent());
        assertArrayEquals("datos".getBytes(), bytes.get());
    }

    @Test
    void testGetImage_FileNotFound() {
        Optional<byte[]> result = imageService.getImage("noExiste.png");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetImage_ErrorReadingFile() throws IOException {
        // Archivo que no puede ser leído (simulado)
        Path filePath = tempDir.resolve("locked.png");
        Files.write(filePath, "aaa".getBytes());

        // Bloquear permisos
        filePath.toFile().setReadable(false);

        Optional<byte[]> result = imageService.getImage("locked.png");

        assertFalse(result.isEmpty());
    }

}
