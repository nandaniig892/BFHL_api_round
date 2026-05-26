# BFHL API – Bajaj Finserv Health Qualifier

A Spring Boot REST API that satisfies the BFHL hiring challenge requirements.

## Endpoint

| Method | Route  | Status |
|--------|--------|--------|
| POST   | /bfhl  | 200 OK |

## Request

```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

## Response

```json
{
  "is_success": true,
  "user_id": "nandani_gupta_17032005",
  "email": "nandanigupta230995@acropolis.in",
  "roll_number": "0827CS231166",
  "even_numbers": ["334", "4"],
  "odd_numbers": ["1"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

## Classification Rules

| Category | Condition |
|----------|-----------|
| Even / Odd number | Every character is a digit; classified by `value % 2` |
| Alphabet | Every character is a letter; stored **uppercase** |
| Special character | Everything else (mixed, symbols, etc.) |

## `concat_string` Algorithm

1. Walk the input array in order.
2. For each purely-alphabetical item, collect its individual characters.
3. Reverse the collected character list.
4. Apply alternating caps: index 0 → UPPER, index 1 → lower, …

## Running Locally

```bash
mvn spring-boot:run
```

## Running Tests

```bash
mvn test
```

## Building the JAR

```bash
mvn clean package -DskipTests
```

## Project Structure

```
src/
├── main/java/com/bfhl/
│   ├── BfhlApplication.java          # Spring Boot entry point
│   ├── controller/
│   │   └── BfhlController.java       # POST /bfhl handler
│   ├── dto/
│   │   ├── BfhlRequest.java          # Request DTO
│   │   └── BfhlResponse.java         # Response DTO
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   └── service/
│       ├── BfhlService.java          # Interface
│       └── impl/
│           └── BfhlServiceImpl.java  # Implementation
└── test/java/com/bfhl/
    └── BfhlApplicationTests.java     # 9 test cases
```
