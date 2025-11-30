package com.example.monolitico.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public Optional<String> saveImage(MultipartFile file) {
        return saveImage(file, null);
    }

    public Optional<String> saveImage(MultipartFile file, String customFilename) {
        if (file == null || file.isEmpty()) {
            return Optional.empty();
        }

        try {
            // Validar que sea una imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IOException("El archivo no es una imagen válida");
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Determinar el nombre del archivo
            String uniqueFilename;
            if (customFilename != null && !customFilename.isEmpty()) {
                // Usar el nombre personalizado proporcionado
                uniqueFilename = customFilename;
            } else {
                // Generar nombre único con UUID
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            }

            // Guardar el archivo
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.write(filePath, file.getBytes());

            System.out.println("Imagen guardada en: " + filePath.toAbsolutePath());

            return Optional.of(uniqueFilename);
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<byte[]> getImage(String filename) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Path filePath = uploadPath.resolve(filename);
            
            System.out.println("Buscando imagen en: " + filePath);
            System.out.println("¿Existe el archivo?: " + Files.exists(filePath));
            
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                byte[] fileBytes = Files.readAllBytes(filePath);
                System.out.println("Imagen entregada: " + filename + " (" + fileBytes.length + " bytes)");
                return Optional.of(fileBytes);
            } else {
                System.err.println("Archivo no encontrado: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al leer la imagen: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
