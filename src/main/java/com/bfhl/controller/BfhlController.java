package com.bfhl.controller;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    @PostMapping
    public ResponseEntity<BfhlResponse> bfhl(
            @Valid @RequestBody BfhlRequest request) {

        BfhlResponse response = bfhlService.process(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public String health() {
        return "API is running";
    }
}
