package com.example.monolitico.Controller;

import com.example.monolitico.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "filename", required = false) String filename) {
        Optional<String> savedFilename = imageService.saveImage(file, filename);

        if (savedFilename.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("filename", savedFilename.get());
            response.put("message", "Imagen subida exitosamente");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al guardar la imagen");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        System.out.println("GET /api/images/" + filename + " - solicitado");
        Optional<byte[]> imageData = imageService.getImage(filename);

        if (imageData.isPresent()) {
            System.out.println("GET /api/images/" + filename + " - ENCONTRADO, entregando " + imageData.get().length + " bytes");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                    .header("Content-Type", "image/png")
                    .body(imageData.get());
        } else {
            System.out.println("GET /api/images/" + filename + " - NO ENCONTRADO");
            Map<String, String> error = new HashMap<>();
            error.put("error", "Imagen no encontrada");
            return ResponseEntity.notFound().build();
        }
    }
}
