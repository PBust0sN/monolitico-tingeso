package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ImageController;
import com.example.monolitico.Service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private MultipartFile mockFile;

    @BeforeEach
    void setup() {
        mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "12345".getBytes()
        );
    }

    // ------------------------------
    // Upload Tests
    // ------------------------------

    @Test
    void testUploadImageSuccess() {
        when(imageService.saveImage(eq(mockFile), eq(null)))
                .thenReturn(Optional.of("generated.png"));

        ResponseEntity<?> response = imageController.uploadImage(mockFile, null);

        assertEquals(200, response.getStatusCodeValue());

        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("generated.png", responseBody.get("filename"));
        assertEquals("Imagen subida exitosamente", responseBody.get("message"));

        verify(imageService).saveImage(mockFile, null);
    }

    @Test
    void testUploadImageSuccessWithCustomFilename() {
        when(imageService.saveImage(eq(mockFile), eq("custom.png")))
                .thenReturn(Optional.of("custom.png"));

        ResponseEntity<?> response = imageController.uploadImage(mockFile, "custom.png");

        assertEquals(200, response.getStatusCodeValue());

        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("custom.png", responseBody.get("filename"));

        verify(imageService).saveImage(mockFile, "custom.png");
    }

    @Test
    void testUploadImageFailure() {
        when(imageService.saveImage(eq(mockFile), eq(null)))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = imageController.uploadImage(mockFile, null);

        assertEquals(400, response.getStatusCodeValue());

        verify(imageService).saveImage(mockFile, null);
    }

    // ------------------------------
    // Get Image Tests
    // ------------------------------

    @Test
    void testGetImageSuccess() {
        byte[] mockBytes = "imagebytes".getBytes();

        when(imageService.getImage("test.png"))
                .thenReturn(Optional.of(mockBytes));

        ResponseEntity<?> response = imageController.getImage("test.png");

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(mockBytes, (byte[]) response.getBody());
        assertEquals("inline; filename=\"test.png\"",
                response.getHeaders().getFirst("Content-Disposition"));
        assertEquals("image/png",
                response.getHeaders().getFirst("Content-Type"));

        verify(imageService).getImage("test.png");
    }

    @Test
    void testGetImageNotFound() {
        when(imageService.getImage("missing.png"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = imageController.getImage("missing.png");

        assertEquals(404, response.getStatusCodeValue());
        verify(imageService).getImage("missing.png");
    }
}
