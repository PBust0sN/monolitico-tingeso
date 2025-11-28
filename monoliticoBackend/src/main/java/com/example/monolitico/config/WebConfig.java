package com.example.monolitico.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.upload.dir:./uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir archivos desde la carpeta de uploads
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().toString();
        registry
            .addResourceHandler("/api/images/**")
            .addResourceLocations("file:" + uploadPath + "/");
    }
}
