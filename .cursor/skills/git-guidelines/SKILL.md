---
name: git-guidelines
description: Git integration guidelines, configuration setups, and best practices. Use when committing code, staging files, creating branches, or verifying repository sync. Key capabilities include branch naming verification, commit formatting validation, pre-push tests, and Gradle bootRun checks. Do NOT use for writing Java domain models or implementing business logic.
---
# Git Guidelines

## Critical
- **Never commit credentials, API keys, or `.env` files.** Always double check `git diff --cached` for secrets before committing.
- **Ensure build and tests pass locally** before pushing code. Run `./gradlew test` and verify success.
- **Do not commit Gradle build outputs or IDE files** (e.g., `build/`, `.gradle/`, `.idea/`, `out/`). Verify they are listed in `.gitignore` or excluded.

## Instructions
1. **Branch Creation**: Create a feature branch from the latest main/master.
   - Command: `git checkout -b feature/<issue-id>-<short-description>` or `bugfix/<issue-id>-<short-description>`
   - Validation Gate: Run `git branch --show-current` to verify you are on the correct new branch and not main/master.
2. **Local Verification**: Verify the codebase builds and tests pass prior to making changes and staging.
   - Command: `./gradlew test`
   - Validation Gate: Ensure build success message is displayed without test failures before modifying files.
3. **Staging and Review**: Stage specific files related to the task.
   - Command: `git add <file-paths>`
   - Validation Gate: Run `git status` to verify only the intended files are staged, and check `git diff --cached` to review the staged changes line-by-line for any unintended edits or commented out debug logs.
4. **Commit Message Formatting**: Commit changes using conventional commit formats.
   - Pattern: `<type>(<scope>): <description>` (e.g., `feat(gradle): update build dependencies` or `fix(auth): correct token validation`).
   - Validation Gate: Review the commit message format. Ensure no work-in-progress (WIP) messages are committed to shared branches.
5. **Pre-push Integration Check**: Before pushing, run the application and execute a health check.
   - Commands:
     - Run the app: `./gradlew bootRun`
     - Health Check: `curl -sf "http://localhost:8080/actuator/health"`
   - Validation Gate: Verify the output is `{"status":"UP"}` (or similar healthy response). Do not push if the service is not ready or health check returns "service not ready".

## Examples
### Example 1: Implementing a new feature and committing changes
- **User says**: "Create a new configuration parameter in `gradle.properties` for JWT timeout and commit it."
- **Actions taken**:
  1. Create a branch: `git checkout -b feature/jwt-timeout-config`
  2. Edit `gradle.properties` to add `jwt.timeout=3600`.
  3. Run `./gradlew test` to ensure no build files are broken.
  4. Stage changes: `git add gradle.properties`
  5. Check diff: `git diff --cached`
  6. Commit: `git commit -m "feat(config): add jwt timeout property"`
- **Result**: Code is committed cleanly following naming conventions and validation gates.

## Common Issues
- **Error: "gradlew: command not found" or permission denied**
  1. Fix: Ensure you use `./gradlew` (with `./`) and check that it has execution permissions. Run `chmod +x gradlew` if needed.
- **Error: Staged files contain sensitive information (like API keys or .env updates)**
  1. Fix: Unstage the sensitive file using `git restore --staged <file>` and add the file path/pattern to `.gitignore` or clean the secrets from the file before staging again.
- **Error: Health check fails with "service not ready" before pushing**
  1. Fix: Run `curl -i http://localhost:8080/actuator/health` to inspect the response headers and body.
  2. Fix: Check Spring Boot application startup logs to identify configuration errors or failing bean creation.