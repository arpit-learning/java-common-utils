---
name: development-workflow
description: Sets up, runs, and verifies the local Spring Boot development environment. Use when starting development, running the project, executing tests, or setting up environment configurations. Key capabilities include managing environment variables, starting the application with Gradle, validating health status, and running local tests. Do NOT use for writing Java application code, creating API endpoints, or designing database schemas.
---
# Development Workflow

## Critical
- **Never commit secrets**: All API keys and credentials must be set in a local `.env` file and loaded as environment variables. Never commit this file to VCS.
- **Port Availability**: Verify that port `8080` is free before running the application.
- **Gradle Wrapper**: Always execute Gradle commands using the local `./gradlew` (or `gradlew.bat` on Windows) wrapper rather than a global Gradle installation.

## Instructions
1. **Environment Configuration**:
   - Create a `.env` file in the project root directory if it does not exist. Define required environment variables here.
   - *Validation*: Verify that `.env` is listed in `.gitignore` by running `git check-ignore .env` before writing credentials to it.
2. **Start the Application**:
   - Run the Spring Boot application using the Gradle wrapper:
     ```bash
     ./gradlew bootRun
     ```
   - *Dependency*: This step requires environment variables configured in Step 1 to be loaded correctly.
   - *Validation*: Verify that the console logs show `Started [ApplicationName]Application` and no exceptions are thrown.
3. **Health Check**:
   - Check the service health endpoint to ensure the application started successfully:
     ```bash
     curl -sf "http://localhost:8080/actuator/health" || echo "service not ready"
     ```
   - *Dependency*: Uses the running server instance from Step 2.
   - *Validation*: Verify that the health check returns `{"status":"UP"}`.
4. **Running Tests**:
   - Execute the test suite using Gradle:
     ```bash
     ./gradlew test
     ```
   - *Validation*: Verify that the test execution completes successfully and all tests pass.

## Examples
### Example 1: Local Setup and Verification
- **User says**: "How do I start the development environment and verify everything works?"
- **Actions taken**:
  1. Checks for `.env` file and verifies it is git-ignored.
  2. Starts the server with `./gradlew bootRun`.
  3. Checks endpoint status with `curl -sf "http://localhost:8080/actuator/health"`.
  4. Runs tests using `./gradlew test`.
- **Result**: Server runs successfully, health endpoint returns UP, and all unit tests pass.

## Common Issues
- **Error**: `service not ready` or connection refused on port `8080`
  - *Fix*:
    1. Verify `./gradlew bootRun` is still running in the background.
    2. Inspect console logs for any boot failures or missing configuration exceptions.
    3. Check if another process is occupying port 8080 using `lsof -i :8080`.
- **Error**: Gradle wrapper not found or permission denied
  - *Fix*:
    1. Verify `gradlew` exists in the root directory.
    2. Run `chmod +x gradlew` to ensure the wrapper is executable.