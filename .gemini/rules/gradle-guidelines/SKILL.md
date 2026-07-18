---
name: gradle-guidelines
description: Gradle integration guidelines, configuration setups, and best practices. Use when modifying build.gradle, settings.gradle, or running build/test commands. Key capabilities include dependency management, task execution, and build troubleshooting. Do NOT use for general Java coding style guidelines.
---
# Gradle Integration Guidelines

## Critical
- **Always use the Gradle Wrapper**: Execute Gradle commands using `./gradlew` (macOS/Linux) or `gradlew.bat` (Windows). Never use a globally installed `gradle` command.
- **Verify before and after changes**: Run `./gradlew help` or `./gradlew tasks` to verify configuration sanity before editing `build.gradle` or `settings.gradle`. Run them again after modifications to ensure build scripts remain parseable.
- **Do not commit Gradle lockfiles or IDE files**: Ensure files inside `.gradle/`, `build/`, `.idea/`, or build artifacts are ignored via `.gitignore`.

## Instructions

1. **Locate Build Configurations**
   - Locate the root `build.gradle`, `settings.gradle`, and `gradle.properties` files.
   - Validation: Check that `gradlew` exists in the root directory. If not executable on macOS/Linux, run `chmod +x gradlew`.

2. **Inspect and Update Dependencies**
   - Open `build.gradle`. Identify the `dependencies` block.
   - Ensure you use the correct configuration scope:
     - Use `implementation` for internal dependencies.
     - Use `compileOnly` for compile-time only dependencies (e.g., Lombok).
     - Use `runtimeOnly` for runtime-only dependencies (e.g., database drivers).
     - Use `testImplementation` for testing dependencies (e.g., JUnit, Mockito).
   - Validation: Run `./gradlew dependencies` or `./gradlew compileJava` to verify dependencies resolve successfully.

3. **Running the Application Locally**
   - Start the Spring Boot application using the bootRun task.
   - Command: `./gradlew bootRun`
   - Validation: Run `curl -sf http://localhost:8080/actuator/health` to confirm the application started and is healthy.

4. **Running Tests**
   - Execute test suites to ensure changes do not break existing functionality.
   - Command: `./gradlew test`
   - Validation: Check build reports at `build/reports/tests/test/index.html` if tests fail.

## Examples

### User says: "Add the Lombok library to compile configurations and run tests"
- **Actions taken**:
  1. Open `build.gradle` and inspect dependencies.
  2. Add Lombok dependencies:
     ```groovy
     compileOnly 'org.projectlombok:lombok:1.18.28'
     annotationProcessor 'org.projectlombok:lombok:1.18.28'
     testCompileOnly 'org.projectlombok:lombok:1.18.28'
     testAnnotationProcessor 'org.projectlombok:lombok:1.18.28'
     ```
  3. Verify with `./gradlew compileJava`.
  4. Run `./gradlew test`.
- **Result**: Lombok dependencies are added and confirmed compiling and passing all tests.

## Common Issues

- **Error**: Permission denied on `./gradlew`
  - Fix: Run `chmod +x gradlew` to make the wrapper executable.
- **Error**: `Could not resolve all dependencies for configuration ':compileClasspath'`
  - Fix: Check that repository definitions (e.g., `mavenCentral()`) exist in `build.gradle` and verify network connectivity or proxy settings in `gradle.properties`.
- **Error**: `Class path contains multiple SLF4J bindings`
  - Fix: Exclude conflicting logging libraries inside `build.gradle`:
    ```groovy
    implementation('some:library:1.0') {
        exclude group: 'ch.qos.logback', module: 'logback-classic'
    }
    ```