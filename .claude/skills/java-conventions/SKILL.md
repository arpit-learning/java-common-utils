---
name: java-conventions
description: Standardizes Java coding patterns, styling guidelines, and Spring Boot type-safety rules. Use when creating or refactoring Java classes, Spring Boot controllers, services, repositories, or unit tests (triggered by *.java file modifications). Key capabilities include constructor injection enforcement, DTO separation, JSR validation, and testing setups. Do NOT use for editing build files (build.gradle), settings (settings.gradle), or shell scripts (e.g., gradlew).
paths:
  - **/*.java
---
# Java Conventions

## Critical
- **Never use Field Injection**: Always use constructor injection (preferably using `@RequiredArgsConstructor` from Lombok if configured, or explicit constructors) for dependency injection. Do not use `@Autowired` on fields.
- **Strict DTO Separation**: Never expose JPA entities directly in controller endpoints or accept them as request bodies. Use dedicated DTOs (Data Transfer Objects) for all external API contracts.
- **Fail-Fast Validation**: Always validate incoming request bodies using `@Valid` and JSR-380 validation annotations (e.g., `@NotNull`, `@NotBlank`, `@Size`) at the controller entry point.
- **Consistent Testing**: Every new service or controller must have a corresponding test class. Use JUnit 5 and Mockito.

## Instructions
1. **Define the Data Transfer Objects (DTOs)**
   - Create request and response DTOs in the `src/main/java/.../dto` package.
   - Use standard record types (Java 16+) or classes with standard getters/setters (or Lombok `@Data`/`@Value`).
   - Add JSR-380 validation annotations on request DTO fields.
   - *Validation Gate*: Verify that fields are annotated with appropriate constraints and that the package matches `dto`.
   - *Dependencies*: Output of this step is used in Step 2 (Controllers) and Step 3 (Services).

2. **Implement the Controller / API Endpoints**
   - Create controller classes in the `src/main/java/.../controller` package.
   - Annotate controllers with `@RestController` and `@RequestMapping("/api/v1/...")`.
   - Ensure the endpoints accept `@Valid @RequestBody` DTOs and return `ResponseEntity<DTO>`.
   - *Validation Gate*: Verify that the controller compile tests pass and there is no direct reference to database entities.
   - *Dependencies*: Uses the DTOs from Step 1.

3. **Implement the Service Layer**
   - Create services in the `src/main/java/.../service` package.
   - Annotate service implementations with `@Service` and `@Transactional` (if doing database operations).
   - Inject repositories or other dependencies via constructor injection.
   - *Validation Gate*: Verify that exceptions are mapped to specific business/HTTP response statuses using `@RestControllerAdvice` or explicit checks.
   - *Dependencies*: Invoked by Step 2, and uses repositories from Step 4.

4. **Add Unit / Integration Tests**
   - Create tests in the `src/test/java/...` directory.
   - Use `@WebMvcTest` for controller slice testing and `@SpringBootTest` for integration testing.
   - Use Mockito (`@MockBean`, `@Mock`) to stub dependencies.
   - *Validation Gate*: Run `./gradlew test` to ensure all tests pass successfully.
   - *Dependencies*: Uses the classes implemented in Steps 1-3.

## Examples
### Example 1: Creating a User Endpoint
- **User says**: "Add a new API endpoint to create a user with validation."
- **Actions taken**:
  1. Created `CreateUserRequest.java` DTO with `@NotBlank` and `@Email` validation.
  2. Created `UserResponse.java` DTO.
  3. Created `UserController.java` with `@PostMapping` injecting the service.
  4. Created `UserService.java` to handle user creation.
  5. Created `UserControllerTest.java` using `@WebMvcTest(UserController.class)` and `MockMvc` to test validation and success paths.
  6. Ran `./gradlew test` to verify.
- **Result**: A secure, type-safe API endpoint with complete test coverage.

## Common Issues
- **Error**: `Field injection is not recommended` warning or failure.
  - *Fix*: Replace `@Autowired private UserService userService;` with a constructor injection pattern or `@RequiredArgsConstructor` with `private final UserService userService;`.
- **Error**: `MethodArgumentNotValidException` not returning standard error body.
  - *Fix*: Add a method in `@RestControllerAdvice` to catch `MethodArgumentNotValidException` and extract binding errors.
- **Error**: `./gradlew bootRun` failing due to database connectivity or port conflict.
  - *Fix*:
    1. Check if another process is using port 8080: `lsof -i :8080`.
    2. Check environment variables in `.env` or application configuration files for correct database URLs.