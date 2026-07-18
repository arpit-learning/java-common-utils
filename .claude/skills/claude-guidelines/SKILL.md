---
name: claude-guidelines
description: Configures and manages Claude rules, hook scripts, settings, and best practices in a Spring Boot/Gradle workspace. Use when modifying .claude/settings.json, writing rules in .claude/rules/, configuring session/sync hook scripts in .claude/hooks/, or updating CLAUDE.md. Key capabilities include validating settings structure, managing pre-command hook scripts, and ensuring command execution guidelines. Do NOT use for general Java feature development, Spring Boot backend testing, or Gradle build configurations unrelated to Claude integration.
paths:
  - .claude/**/*
  - CLAUDE.md
---
# Claude Integration Guidelines

Configure, validate, and maintain Claude rules, settings, and hook scripts to ensure consistent agent environments and commands execution.

## Critical

- **Do Not Commit Secrets**: Never place API keys, passwords, or personal credentials inside `.claude/` files or `CLAUDE.md`.
- **Command Compliance**: Always use the Gradle wrapper (`./gradlew`) rather than a global `gradle` install.
- **Hook Executability**: Any shell script under `.claude/hooks/` must be executable. Run `chmod +x` on them before committing.
- **Check Sync Status**: Always verify git synchronization hooks before performing major workspace modifications.

## Instructions

1. **Verify or Configure settings.json**
   - Ensure `.claude/settings.json` contains valid JSON matching Claude configuration rules.
   - *Validation Gate*: Run `jq . .claude/settings.json` (or use a JSON parser) to verify it is syntax-error free.

2. **Define or Update Rules in .claude/rules/**
   - Create or edit rule markdown files under `.claude/rules/` (e.g., `gradle-commands.md` or `agentic-hooks.md`).
   - Use standard frontmatter or headers as established in the directory.
   - *Validation Gate*: Ensure rules clearly state target commands, triggers, and expected behaviors.

3. **Install and Set Up Hook Scripts**
   - Place shell scripts in `.claude/hooks/` (e.g., `agentic-session-freshness.sh`, `agentic-check-sync.sh`).
   - Write hook scripts in standard POSIX-compliant shell (`/bin/sh` or `/bin/bash`).
   - *Validation Gate*: Run `./.claude/hooks/<script-name>.sh` manually and verify it exits with `0`.

4. **Verify Application Readiness**
   - Run the validation command specified in `run.md` to ensure the project runs properly under Claude.
   - Run `./gradlew test` to verify the rule or hook changes did not break the build.
   - *Validation Gate*: Query `curl -sf "http://localhost:8080/actuator/health"` to ensure health endpoint is up if bootRun is active.

## Examples

### Example 1: Add a pre-command hook check
- **User says**: "Add a check to make sure our git branch is not main before running tests."
- **Actions taken**:
  1. Created a script `.claude/hooks/check-branch.sh`:
     ```bash
     #!/bin/bash
     CURRENT_BRANCH=$(git branch --show-current)
     if [ "$CURRENT_BRANCH" = "main" ]; then
       echo "Error: Cannot run tests directly on main branch!" >&2
       exit 1
     fi
     ```
  2. Made it executable: `chmod +x .claude/hooks/check-branch.sh`.
  3. Added configuration or documented the hook in `.claude/rules/agentic-hooks.md`.
- **Result**: Running tests on `main` branch now correctly aborts and prints a descriptive error.

## Common Issues

- **Permission Denied for Hook Scripts**:
  - *Error message*: `sh: .claude/hooks/agentic-session-freshness.sh: Permission denied`
  - *Fix*: Run `chmod +x .claude/hooks/agentic-session-freshness.sh` in the terminal and commit the permission change.

- **Actuator Health Endpoint Offline**:
  - *Error message*: `service not ready` or connection refused on `http://localhost:8080/actuator/health`
  - *Fix*:
    1. Ensure the app is running by executing `./gradlew bootRun`.
    2. Check that no other process is bound to port `8080` (e.g., run `lsof -i :8080`).