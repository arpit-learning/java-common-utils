---
name: github-copilot-guidelines
description: Manages and updates GitHub Copilot instructions to align suggestions with project-specific Spring Boot and Java conventions. Use when configuring Copilot prompts, updating developer guidelines, or modifying `.github/copilot-instructions.md`. Key capabilities include enforcing Spring Boot patterns, standardizing Java/Groovy code style, and setting up coding rules. Do NOT use for general Git operations or non-Copilot configuration tasks.
---
# GitHub Copilot Guidelines

## Critical
1. Never include sensitive credentials, personal access tokens, or private endpoints in `.github/copilot-instructions.md`.
2. Ensure that any guidelines specified in `.github/copilot-instructions.md` align with the build configurations in `build.gradle` and standard Gradle commands.
3. Validate the markdown formatting and readability of the instructions file before finalizing edits.

## Instructions
1. **Verify Existing Copilot Instructions**
   - Read `.github/copilot-instructions.md` to understand the currently defined coding standards (e.g., Spring Boot structures, testing conventions, and Java code patterns).
   - *Validation Gate*: Confirm the file exists and is readable. If it is missing, create it in the `.github/` directory.

2. **Incorporate Project Tech Stack Details**
   - Explicitly mention in the guidelines that the project uses Java, Groovy, Gradle, and Spring Boot.
   - Document default project configurations, such as the local port `8080` and the actuator health endpoint `/actuator/health` (as described in `run.md`).
   - *Validation Gate*: Verify that these parameters match the actual application settings in `build.gradle` or configuration files.

3. **Define Style and Pattern Rules**
   - Add rules specifying that Controllers must return standard response wrappers, and DB calls must include robust try/catch blocks matching standard Spring Boot exception handling.
   - Direct Copilot to prioritize Gradle wrapper execution using `./gradlew` instead of global `gradle` installations.
   - *Validation Gate*: Run `./gradlew test` to ensure that existing tests verify the standards described.

4. **Commit and Document Changes**
   - Save changes to `.github/copilot-instructions.md`.
   - Document the update in the project commit message.
   - *Validation Gate*: Review git status and diff to confirm no unexpected changes were introduced.

## Examples
- **User says**: "Update Copilot instructions to enforce standard Spring Boot actuator check rules."
- **Actions taken**:
  1. Read `.github/copilot-instructions.md`.
  2. Add instructions to use `/actuator/health` for local health checks and to utilize `./gradlew bootRun` for startup.
  3. Verify layout and format of the updated markdown file.
- **Result**: Updated `.github/copilot-instructions.md` containing explicit guidelines on local startup and health check validation.

## Common Issues
- **Issue**: Copilot continues to generate code that uses standard Gradle commands instead of the wrapper.
  - **Fix**: Add a rule to `.github/copilot-instructions.md`: "Always recommend `./gradlew` instead of `gradle` for building, testing, and running tasks."
- **Issue**: Copilot generates incorrect health check URL references.
  - **Fix**: Update the health check section in `.github/copilot-instructions.md` to reference `http://localhost:8080/actuator/health` exactly.