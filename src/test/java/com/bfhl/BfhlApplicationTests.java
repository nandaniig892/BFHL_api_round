package com.bfhl;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration + unit tests for the BFHL /bfhl endpoint.
 *
 * Covers:
 *  – Happy-path examples A, B, C from the problem statement
 *  – Empty data array edge case
 *  – Missing "data" field → 400 Bad Request
 *  – Malformed JSON body  → 400 Bad Request
 *  – Service-layer unit tests via {@link BfhlService} directly
 */
@SpringBootTest
@AutoConfigureMockMvc
class BfhlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BfhlService bfhlService;

    // ─────────────────────────────────────────────────────────────────────────
    // Example A  –  ["a", "1", "334", "4", "R", "$"]
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example A: mixed alphanumeric and special chars")
    void exampleA_fullIntegration() throws Exception {
        String body = """
                {
                  "data": ["a", "1", "334", "4", "R", "$"]
                }
                """;

        MvcResult result = mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("nandani_gupta_17032005"))
                .andExpect(jsonPath("$.email").value("nandanigupta230995@acropolis.in"))
                .andExpect(jsonPath("$.roll_number").value("0827CS231166"))
                .andReturn();

        BfhlResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), BfhlResponse.class);

        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(response.getSpecialCharacters()).containsExactly("$");
        assertThat(response.getSum()).isEqualTo("339");
        assertThat(response.getConcatString()).isEqualTo("Ra");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Example B  –  ["2", "a", "y", "4", "&", "-", "*", "5", "92", "b"]
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example B: multiple specials and three alphabets")
    void exampleB_fullIntegration() throws Exception {
        String body = """
                {
                  "data": ["2", "a", "y", "4", "&", "-", "*", "5", "92", "b"]
                }
                """;

        MvcResult result = mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        BfhlResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), BfhlResponse.class);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).containsExactly("5");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("2", "4", "92");
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "Y", "B");
        assertThat(response.getSpecialCharacters()).containsExactlyInAnyOrder("&", "-", "*");
        assertThat(response.getSum()).isEqualTo("103");
        assertThat(response.getConcatString()).isEqualTo("ByA");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Example C  –  ["A", "ABCD", "DOE"]
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example C: only multi-char alphabetical tokens")
    void exampleC_fullIntegration() throws Exception {
        String body = """
                {
                  "data": ["A", "ABCD", "DOE"]
                }
                """;

        MvcResult result = mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        BfhlResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), BfhlResponse.class);

        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        // A,A,B,C,D,D,O,E → reversed E,O,D,D,C,B,A,A → EoDdCbAa
        assertThat(response.getConcatString()).isEqualTo("EoDdCbAa");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Edge case: empty data array
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Edge case: empty data array returns zeroed-out response")
    void emptyData_returnsEmptyLists() throws Exception {
        String body = """
                { "data": [] }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value(""))
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.alphabets").isEmpty())
                .andExpect(jsonPath("$.special_characters").isEmpty());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Validation: missing "data" field → 400
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Validation: missing 'data' field returns 400 with is_success=false")
    void missingDataField_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Validation: malformed JSON → 400
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Validation: malformed JSON returns 400 with is_success=false")
    void malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("NOT JSON"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Service unit tests (no HTTP layer)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Service: numbers returned as original strings, not re-parsed")
    void serviceUnit_numbersRetainedAsStrings() {
        BfhlRequest req = new BfhlRequest(List.of("007", "010"));
        BfhlResponse res = bfhlService.process(req);

        // 007 is odd, 010 is even — strings must match the original tokens
        assertThat(res.getOddNumbers()).containsExactly("007");
        assertThat(res.getEvenNumbers()).containsExactly("010");
    }

    @Test
    @DisplayName("Service: single alphabet produces correct concat_string")
    void serviceUnit_singleAlphabetConcatString() {
        BfhlRequest req = new BfhlRequest(List.of("z"));
        BfhlResponse res = bfhlService.process(req);
        // Reversed single char [z] → index 0 → uppercase → "Z"
        assertThat(res.getConcatString()).isEqualTo("Z");
    }

    @Test
    @DisplayName("Service: mixed-content string treated as special character")
    void serviceUnit_mixedContentIsSpecial() {
        BfhlRequest req = new BfhlRequest(List.of("abc123", "hello!", "@"));
        BfhlResponse res = bfhlService.process(req);
        assertThat(res.getSpecialCharacters()).containsExactlyInAnyOrder("abc123", "hello!", "@");
        assertThat(res.getAlphabets()).isEmpty();
    }

    @Test
    @DisplayName("Service: user_id, email and roll_number are always populated")
    void serviceUnit_identityFieldsAlwaysPresent() {
        BfhlRequest req = new BfhlRequest(List.of());
        BfhlResponse res = bfhlService.process(req);
        assertThat(res.getUserId()).isEqualTo("nandani_gupta_17032005");
        assertThat(res.getEmail()).isEqualTo("nandanigupta230995@acropolis.in");
        assertThat(res.getRollNumber()).isEqualTo("0827CS231166");
    }
}
