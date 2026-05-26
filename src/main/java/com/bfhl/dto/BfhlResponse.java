package com.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO – the body returned by POST /bfhl.
 *
 * All number values are kept as strings per the problem specification.
 * The builder pattern makes it easy to construct the response in the
 * service layer without long constructor chains.
 */
public class BfhlResponse {

    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    /** Numbers from the input that are even, returned as strings. */
    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    /** Numbers from the input that are odd, returned as strings. */
    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    /** Purely alphabetical items converted to uppercase. */
    @JsonProperty("alphabets")
    private List<String> alphabets;

    /** Items that are neither purely numeric nor purely alphabetical. */
    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    /** String representation of the total sum of all numeric items. */
    @JsonProperty("sum")
    private String sum;

    /**
     * All individual alphabetical characters across every item, collected
     * in input order, then reversed, then formatted with alternating caps
     * (index 0 → uppercase, index 1 → lowercase, …).
     */
    @JsonProperty("concat_string")
    private String concatString;

    // ── No-arg constructor (needed by Jackson) ────────────────────────────────

    public BfhlResponse() {}

    // ── All-args constructor ──────────────────────────────────────────────────

    public BfhlResponse(boolean isSuccess,
                        String userId,
                        String email,
                        String rollNumber,
                        List<String> evenNumbers,
                        List<String> oddNumbers,
                        List<String> alphabets,
                        List<String> specialCharacters,
                        String sum,
                        String concatString) {
        this.isSuccess         = isSuccess;
        this.userId            = userId;
        this.email             = email;
        this.rollNumber        = rollNumber;
        this.evenNumbers       = evenNumbers;
        this.oddNumbers        = oddNumbers;
        this.alphabets         = alphabets;
        this.specialCharacters = specialCharacters;
        this.sum               = sum;
        this.concatString      = concatString;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public List<String> getEvenNumbers() { return evenNumbers; }
    public void setEvenNumbers(List<String> evenNumbers) { this.evenNumbers = evenNumbers; }

    public List<String> getOddNumbers() { return oddNumbers; }
    public void setOddNumbers(List<String> oddNumbers) { this.oddNumbers = oddNumbers; }

    public List<String> getAlphabets() { return alphabets; }
    public void setAlphabets(List<String> alphabets) { this.alphabets = alphabets; }

    public List<String> getSpecialCharacters() { return specialCharacters; }
    public void setSpecialCharacters(List<String> specialCharacters) { this.specialCharacters = specialCharacters; }

    public String getSum() { return sum; }
    public void setSum(String sum) { this.sum = sum; }

    public String getConcatString() { return concatString; }
    public void setConcatString(String concatString) { this.concatString = concatString; }
}
