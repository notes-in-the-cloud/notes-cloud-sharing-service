package com.notescloud.sharing_service.health;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/healthz")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "sharing-service"
        ));
    }

    @GetMapping("/readyz")
    public ResponseEntity<Map<String, String>> ready() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            return ResponseEntity.ok(Map.of(
                "status", "READY",
                "database", "UP",
                "service", "sharing-service"
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(503).body(Map.of(
                "status", "NOT_READY",
                "database", "DOWN",
                "service", "sharing-service"
            ));
        }
    }
}