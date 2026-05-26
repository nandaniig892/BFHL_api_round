package com.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO – the body that callers send to POST /bfhl.
 *
 * Example:
 * {
 *   "data": ["a", "1", "334", "4", "R", "$"]
 * }
 */
public class BfhlRequest {

    @NotNull(message = "The 'data' field must not be null")
    @JsonProperty("data")
    private List<String> data;

    // ── Constructors ──────────────────────────────────────────────────────────

    public BfhlRequest() {}

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
