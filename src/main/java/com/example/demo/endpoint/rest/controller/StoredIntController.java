package com.example.demo.endpoint.rest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;

@RestController
public class StoredIntController {

    private static final Logger logger = LoggerFactory.getLogger(StoredIntController.class);
    private final SecureRandom random = new SecureRandom();

    @Value("${app.storage.file-path:/tmp/stored-int.txt}")
    private String filePath;

    @GetMapping("/stored-int")
    public ResponseEntity<Integer> getStoredInt() {
        try {
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                int value = Integer.parseInt(content);
                logger.debug("Valeur lue: {}", value);
                return ResponseEntity.ok(value);
            }

            // Générer nouvelle valeur
            int newValue = random.nextInt(100_000);
            Files.writeString(path, String.valueOf(newValue), StandardOpenOption.CREATE);

            logger.info("Nouvelle valeur générée: {}", newValue);
            return ResponseEntity.ok(newValue);

        } catch (IOException | NumberFormatException e) {
            logger.error("Erreur fichier: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}