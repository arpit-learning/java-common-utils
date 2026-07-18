---
name: testing-guide
description: Testing patterns and Gradle command reference for Spring Boot. Use when writing tests, executing Gradle test suites, or debugging test failures. Key capabilities include running unit/integration tests and viewing report files. Do NOT use when running the main application (`./gradlew bootRun`) or performing manual API testing via curl.
---
# Spring Boot Gradle Testing Guide

## Critical
- Always run `./gradlew test` locally before proposing or committing code changes.
- Never write tests that write to shared databases without using `@Transactional` to ensure automatic database rollback.
- All test classes must follow the naming convention of suffixing with `Test` (e.g., `MyServiceTest.java`) and be placed under `src/test/java/` or `src/test/groovy/`.

## Instructions
1. **Identify the Scope of the Test**
   - For pure unit tests (services, utilities), use JUnit 5 and Mockito. Do not spin up Spring Context.
   - For controller or API tests, use `@WebMvcTest` combined with `MockMvc` to test controller endpoints in isolation.
   - For database or full integration tests, use `@SpringBootTest` with `@ActiveProfiles("test")`.
   - *Validation*: Verify that test class annotations match the test scope to prevent slow context loading.

2. **Locate or Create the Test File**
   - Place unit/integration tests under `src/test/java` or `src/test/groovy` in the package matching the class under test.
   - *Validation*: Verify that the package statement in the test file exactly matches its directory structure.

3. **Write the Test Cases**
   - Use standard AAA (Arrange-Act-Assert) structure.
   - Use AssertJ (`org.assertj.core.api.Assertions.assertThat`) for assertions.
   - Mock dependencies using `@Mock` or `@MockBean` (for Spring Context injection).
   - *Validation*: Check that at least one assertion is present and verifies the expected side effect or return value.

4. **Execute the Specific Test**
   - To run a specific test class, use the command:
     `./gradlew test --tests "com.example.package.MyServiceTest"`
   - To run a specific test method, use the command:
     `./gradlew test --tests "com.example.package.MyServiceTest.myTestMethod"`
   - *Validation*: Verify that the task finishes successfully (`BUILD SUCCESSFUL`) and check the console output for any test execution failures.

5. **Review Test Reports**
   - If tests fail, inspect the generated HTML report at `build/reports/tests/test/index.html`.
   - *Validation*: Verify the exact line number of the failure from the stack trace in the report.

## Examples
### Example 1: Creating a Unit Test for a Service
**User says** -> "Write a unit test for the UserService class to test user retrieval."
**Actions taken** ->
1. Located `src/main/java/com/example/UserService.java`.
2. Created a corresponding test file `src/test/java/com/example/UserServiceTest.java`.
3. Used JUnit 5 and Mockito to mock `UserRepository`.
4. Implemented assertions using AssertJ.
5. Ran `./gradlew test --tests "com.example.UserServiceTest"`.
**Result** ->
```java
package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserWhenExists() {
        // Arrange
        User user = new User(1L, "john.doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> found = userService.getUserById(1L);

        // Assert
        assertThat(found).isPresent().contains(user);
    }
}
```

## Common Issues
- **Error: "No tests found for given includes"**
  - **Fix**: Verify that the class name is fully qualified (e.g. `./gradlew test --tests "com.example.MyTest"`) and check for spelling errors. Also, run `./gradlew cleanTest test` to clear previous test caches.
- **Error: "Port 8080 already in use"**
  - **Fix**: If running `@SpringBootTest` with `WebEnvironment.DEFINED_PORT`, switch to `WebEnvironment.RANDOM_PORT` to avoid conflict with the running local development server.
- **Error: "Database access lock/timeout"**
  - **Fix**: Verify that `@Transactional` is used on integration tests or that resources/connections are properly closed in `@AfterEach` methods.