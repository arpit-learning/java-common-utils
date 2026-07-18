---
name: spring-boot-patterns
description: Spring Boot architectural patterns, convention rules, and guidelines. Use when creating or modifying controllers, services, repositories, or DTOs. Key capabilities include constructor injection, validation gates, and layered architecture. Do NOT use for shell scripts, build configurations, or non-Java files.
---
# Spring Boot Architectural Patterns

## Critical
- All dependencies must be injected via constructor injection (do not use `@Autowired` on fields).
- Controller methods must return `ResponseEntity<T>` or use explicit HTTP status annotations.
- All database operations must be wrapped in transactional services using `@Transactional`.
- Do not expose database entities directly; always map to DTOs (Data Transfer Objects) before returning from Controllers.

## Instructions
1. **Define the DTO**:
   - Location: `src/main/java/**/dto/` or sibling package of the Controller.
   - Naming: `[Name]Request.java` for inputs, `[Name]Response.java` for outputs.
   - Boilerplate: Use Java records or `@Data` / `@Value` Lombok annotations if Lombok is present.
   - Validation Gate: Verify the DTO fields compile and have validation annotations (e.g. `@NotNull`, `@Size`).
2. **Create the Repository**:
   - Location: `src/main/java/**/repository/`
   - Naming: `[Name]Repository.java`
   - Boilerplate: Extend `JpaRepository<Entity, Long>`.
   - Validation Gate: Run `./gradlew compileJava` to ensure repository mapping is correct.
3. **Implement the Service**:
   - Location: `src/main/java/**/service/`
   - Naming: `[Name]Service.java` (Interface) and `[Name]ServiceImpl.java` (Implementation).
   - Annotations: `@Service` and `@RequiredArgsConstructor`.
   - Validation Gate: Verify dependencies (from Step 2) are declared as `final` and constructor-injected.
4. **Expose the Controller**:
   - Location: `src/main/java/**/controller/`
   - Naming: `[Name]Controller.java`
   - Annotations: `@RestController`, `@RequestMapping("/api/v1/[resource]")`, `@RequiredArgsConstructor`.
   - Validation Gate: Run `./gradlew bootRun` and check actuator endpoint or run `./gradlew test` to verify build succeeds.

## Examples
### User says
"Create a REST endpoint to fetch a User profile by ID"

### Actions taken
1. Create `UserProfileResponse.java` record in `dto` package.
2. Create `UserRepository.java` extending `JpaRepository<UserProfile, Long>`.
3. Create `UserService.java` with method `UserProfileResponse getUserProfile(Long id)`.
4. Create `UserController.java` with `@GetMapping("/{id}")`.
5. Run `./gradlew test` to verify.

### Result
`UserController.java`:
```java
package com.example.controller;

import com.example.dto.UserProfileResponse;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }
}
```

## Common Issues
- **Error: Field injection not recommended**
  - Fix: Replace `@Autowired` on the field with a `final` field and add `@RequiredArgsConstructor` at the class level.
- **Error: LazyInitializationException**
  - Fix: Ensure the service method accessing the lazy relationship is annotated with `@Transactional(readOnly = true)`.
- **Error: Service not ready when checking health**
  - Fix: Check service logs with `./gradlew bootRun` and verify database connection configurations in `src/main/resources/application.yml`.