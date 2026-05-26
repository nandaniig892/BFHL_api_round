package com.bfhl.service.impl;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BfhlServiceImpl implements BfhlService {

    private static final String USER_ID = "nandani_gupta_17032005";
    private static final String EMAIL = "nandanigupta230995@acropolis.in";
    private static final String ROLL_NUMBER = "0827CS231166";

    @Override
    public BfhlResponse process(BfhlRequest request) {

        List<String> evenNumbers = new ArrayList<>();
        List<String> oddNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();

        List<Character> chars = new ArrayList<>();

        long sum = 0;

        for (String item : request.getData()) {

            if (isNumber(item)) {

                long num = Long.parseLong(item);
                sum += num;

                if (num % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }

            } else if (isAlphabet(item)) {

                alphabets.add(item.toUpperCase());

                for (char c : item.toCharArray()) {
                    chars.add(c);
                }

            } else {
                specialCharacters.add(item);
            }
        }

        String concatString = makeConcatString(chars);

        return new BfhlResponse(
                true,
                USER_ID,
                EMAIL,
                ROLL_NUMBER,
                evenNumbers,
                oddNumbers,
                alphabets,
                specialCharacters,
                String.valueOf(sum),
                concatString
        );
    }

    private boolean isNumber(String str) {

        if (str == null || str.isEmpty()) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isAlphabet(String str) {

        if (str == null || str.isEmpty()) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    private String makeConcatString(List<Character> chars) {

        StringBuilder sb = new StringBuilder();

        int index = 0;

        for (int i = chars.size() - 1; i >= 0; i--) {

            char ch = chars.get(i);

            if (index % 2 == 0) {
                sb.append(Character.toUpperCase(ch));
            } else {
                sb.append(Character.toLowerCase(ch));
            }

            index++;
        }

        return sb.toString();
    }
}
