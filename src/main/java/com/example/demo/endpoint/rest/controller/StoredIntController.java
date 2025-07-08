package com.example.demo.endpoint.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class StoredIntController {

    private static final Logger logger = LoggerFactory.getLogger(StoredIntController.class);
    private static final SecureRandom random = new SecureRandom();
    private static final AtomicInteger storedValue = new AtomicInteger(-1);

    @GetMapping("/stored-int")
    public ResponseEntity<Integer> getStoredInt() {
        try {
            int current = storedValue.get();

            if (current == -1) {
                // Première fois, générer une valeur
                int newValue = random.nextInt(100_000);
                storedValue.set(newValue);
                logger.info("Nouvelle valeur générée: {}", newValue);
                return ResponseEntity.ok(newValue);
            }

            logger.debug("Valeur existante: {}", current);
            return ResponseEntity.ok(current);

        } catch (Exception e) {
            logger.error("Erreur: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}