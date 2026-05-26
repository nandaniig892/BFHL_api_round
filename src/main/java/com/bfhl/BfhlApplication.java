package com.bfhl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the BFHL Qualifier REST API.
 *
 * Exposes a single POST /bfhl endpoint that accepts an array of strings
 * and returns separated even numbers, odd numbers, alphabets, special
 * characters, their sum, and a reverse-alternating-caps concat string.
 */
@SpringBootApplication
public class BfhlApplication {

    public static void main(String[] args) {
        SpringApplication.run(BfhlApplication.class, args);
    }
}
