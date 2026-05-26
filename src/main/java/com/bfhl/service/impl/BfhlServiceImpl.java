package com.bfhl.service.impl;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Core implementation of {@link BfhlService}.
 *
 * Classification rules for each element in the input array:
 * <ul>
 *   <li><b>Purely numeric</b>  – every character is a digit (or a leading '-').
 *       Classified as even or odd based on the parsed long value.</li>
 *   <li><b>Purely alphabetical</b> – every character is a letter.
 *       Stored in uppercase; individual chars contribute to concat_string.</li>
 *   <li><b>Everything else</b> – treated as a special character item.</li>
 * </ul>
 *
 * concat_string algorithm:
 * <ol>
 *   <li>Walk the input in order; for every purely-alphabetical item collect
 *       each character individually into a list.</li>
 *   <li>Reverse that character list.</li>
 *   <li>Apply alternating caps starting with uppercase at index 0.</li>
 * </ol>
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    // ── Candidate identity – edit these constants for each submission ─────────
    private static final String USER_ID     = "nandani_gupta_17032005";
    private static final String EMAIL       = "nandanigupta230995@acropolis.in";
    private static final String ROLL_NUMBER = "0827CS231166";
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public BfhlResponse process(BfhlRequest request) {

        List<String> data = request.getData();

        List<String> evenNumbers       = new ArrayList<>();
        List<String> oddNumbers        = new ArrayList<>();
        List<String> alphabets         = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        List<Character> alphaChars     = new ArrayList<>();  // for concat_string
        long numericSum = 0;

        for (String item : data) {

            if (item == null || item.isEmpty()) {
                // Treat blank / null entries as special characters
                specialCharacters.add(item == null ? "" : item);
                continue;
            }

            if (isPurelyNumeric(item)) {
                long value = Long.parseLong(item);
                numericSum += value;

                if (value % 2 == 0) {
                    evenNumbers.add(item);   // keep original string form
                } else {
                    oddNumbers.add(item);
                }

            } else if (isPurelyAlphabetical(item)) {
                // Uppercase the entire token and remember each character
                alphabets.add(item.toUpperCase());
                for (char c : item.toCharArray()) {
                    alphaChars.add(c);
                }

            } else {
                specialCharacters.add(item);
            }
        }

        String sum          = String.valueOf(numericSum);
        String concatString = buildConcatString(alphaChars);

        return new BfhlResponse(
                true,
                USER_ID,
                EMAIL,
                ROLL_NUMBER,
                evenNumbers,
                oddNumbers,
                alphabets,
                specialCharacters,
                sum,
                concatString
        );
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Returns true if every character in the string is a digit.
     * Handles optional leading '-' for negative numbers.
     */
    private boolean isPurelyNumeric(String s) {
        if (s.isEmpty()) return false;
        int start = s.charAt(0) == '-' ? 1 : 0;
        if (start == s.length()) return false;   // just a '-' sign
        for (int i = start; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Returns true if every character in the string is a letter (a-z / A-Z).
     */
    private boolean isPurelyAlphabetical(String s) {
        if (s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Given the list of individual alphabetical characters (collected in input
     * order), reverses them and applies alternating caps:
     * index 0 → UPPERCASE, index 1 → lowercase, index 2 → UPPERCASE, …
     *
     * Example: [a, R] → reversed [R, a] → "Ra"
     */
    private String buildConcatString(List<Character> chars) {
        if (chars.isEmpty()) return "";

        // Reverse in-place
        int left = 0, right = chars.size() - 1;
        while (left < right) {
            char tmp = chars.get(left);
            chars.set(left, chars.get(right));
            chars.set(right, tmp);
            left++;
            right--;
        }

        StringBuilder sb = new StringBuilder(chars.size());
        for (int i = 0; i < chars.size(); i++) {
            char c = chars.get(i);
            sb.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
