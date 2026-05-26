package com.bfhl.controller;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Exposes two endpoints required by the challenge:
 *   POST /bfhl  – core data-processing endpoint
 *   GET  /health – liveness check
 */
@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private static final Logger log = LoggerFactory.getLogger(BfhlController.class);

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl
     *
     * Accepts a JSON body with a "data" array, processes it, and returns
     * HTTP 200 with a fully populated {@link BfhlResponse}.
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> handleData(@Valid @RequestBody BfhlRequest request) {
        log.info("POST /bfhl received with {} item(s)", request.getData().size());
        BfhlResponse response = bfhlService.process(request);
        log.info("POST /bfhl returning is_success={}", response.isSuccess());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /health
     *
     * Simple liveness check. Returns HTTP 200 with status "UP" and a
     * server timestamp so the caller can verify the service is running.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("GET /health pinged");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status",    "UP");
        body.put("service",   "bfhl-api");
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(body);
    }
}
