---
name: cursor-guidelines
description: Cursor integration guidelines, configuration setups, and best practices. Use when configuring Cursor rules under `.cursor/rules/`, hooks in `.cursor/hooks.json`, or `.cursorignore`. Do NOT use for general Java/Spring Boot development.
---
# Cursor Integration Guidelines

## Critical
- All Cursor rule files MUST be located in `.cursor/rules/` and use the `.mdc` extension.
- Every Cursor rule in `.cursor/rules/` must have a corresponding Claude rule in `.claude/rules/` to ensure consistency between developer environments.
- Do NOT commit sensitive credentials or API keys; verify they are only loaded via environment variables or `.env` files which must be added to `.cursorignore` and `.gitignore`.

## Instructions
1. **Analyze Existing Cursor Configuration**
   - Check the active Cursor rules in `.cursor/rules/` (e.g. `agentic-sync.mdc`, `agentic-learnings.mdc`, `agentic-pre-commit.mdc`, `agentic-setup.mdc`).
   - Read `.cursor/hooks.json` to understand automated hooks triggerable by Cursor.
   - *Validation Gate:* Confirm that the target rule does not duplicate an existing rule file.

2. **Align with Claude Rules**
   - When creating or modifying a rule in `.cursor/rules/foo.mdc`, make sure to create/modify the matching Claude rule at `.claude/rules/foo.md` (or equivalent).
   - Ensure the hook configurations in `.cursor/hooks.json` invoke the appropriate shell scripts located in `.claude/hooks/` (e.g., `agentic-check-sync.sh`).
   - *Validation Gate:* Run the sync validation script (e.g., `./.claude/hooks/agentic-check-sync.sh`) to verify symmetry.

3. **Format Rules according to MDC Specifications**
   - Write MDC rules using proper metadata frontmatter (e.g., description, globs) and a clear structure.
   - Specify precise triggers in the rule markdown so that the rules apply only to target paths.
   - *Validation Gate:* Verify that the rule applies correctly to target paths and doesn't run globally unless intended.

## Examples
### Example 1: Add a new custom rule for database migrations
- **User says:** "Create a rule to make sure we always run database migration checks before running bootRun."
- **Actions taken:**
  1. Create `.cursor/rules/db-migration-check.mdc` with migration check instructions and trigger globs `src/main/resources/db/migration/*.sql`.
  2. Create matching `.claude/rules/db-migration-check.md`.
  3. Update `.cursor/hooks.json` if a pre-command script is needed.
- **Result:** A new `.mdc` rule is created that automatically prompts the developer to run `./gradlew test` or verify migrations when touching sql files.

## Common Issues
- **Issue:** Rule is not picked up or triggered by Cursor.
  - *Fix:* Ensure the rule file ends with `.mdc` and resides exactly in the `.cursor/rules/` directory. Check that the `globs` filter in the rule's frontmatter matches the files you are editing.
- **Issue:** Cursor sync validation fails.
  - *Fix:* Run `bash ./.claude/hooks/agentic-check-sync.sh` to see which rules are out of alignment. Manually copy over missing changes between `.cursor/rules/` and `.claude/rules/`.