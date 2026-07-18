---
name: shell-conventions
description: Guides the creation and modification of shell/bash scripts in the project. Use this when writing git hooks, automated tasks, setup scripts, or modifying gradlew. Key capabilities include enforcing robust error-handling (set -euo pipefail), portable path resolution, and exit code validation. Do NOT use for Java files, Groovy build files, or general documentation.
---
# Shell Conventions

## Critical

* **Error Prevention**: Every bash script must begin with `set -euo pipefail` to ensure immediate termination on errors, unbound variables, or pipeline failures.
* **Portability**: Do not assume standard utilities have GNU flags. Avoid using flags not supported by default macOS/BSD tools. Use `command -v` instead of `which` to check for executable presence.
* **Path Resolution**: Never use hardcoded absolute paths. Always resolve directories relative to the script location using `SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"`.
* **No Silent Failures**: All error messages must be printed to standard error (`>&2`) and exit with a non-zero code.

## Instructions

1. **Script Boilerplate and Setup**:
   - Start the file with the shebang `#!/usr/bin/env bash` followed immediately by the safety flags:
     ```bash
     #!/usr/bin/env bash
     set -euo pipefail
     IFS=$'\n\t'
     ```
   - Determine the absolute workspace root relative to the script location:
     ```bash
     SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
     # If script is in .claude/hooks/,
     WORKSPACE_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
     ```
   - **Validation Gate**: Run `bash -n <script-path>` to verify syntax checks pass without warnings before adding logic.

2. **Input and Dependency Validation**:
   - Check for required commands using `command -v`:
     ```bash
     for cmd in curl git; do
       if ! command -v "$cmd" >/dev/null 2>&1; then
         echo "Error: Required command '$cmd' is not installed." >&2
         exit 1
       fi
     done
     ```
   - If the script depends on environment variables (like API keys or local configuration), check their presence with default fallback checks:
     ```bash
     if [ -z "${MY_VAR:-}" ]; then
       echo "Error: MY_VAR environment variable must be set." >&2
       exit 1
     fi
     ```
   - **Validation Gate**: Run the script structure with dummy environment variables and verify it exits with `1` when they are unset. This step uses the folder structure resolved in Step 1.

3. **Formatting and Output Rules**:
   - Ensure all output statements to user/log are formatted cleanly. Use standard logging formats:
     - Info messages: `echo "[INFO] Message"`
     - Error messages: `echo "[ERROR] Message" >&2`
   - Ensure any temporary files are safely created in a scratch directory (e.g. `"$WORKSPACE_DIR/scratch"` or using `mktemp`) and cleaned up using a `trap` hook:
     ```bash
     TMPFILE=$(mktemp)
     trap 'rm -f "$TMPFILE"' EXIT
     ```
   - **Validation Gate**: Verify that the trap triggers cleanup by interrupting a test execution of the script (e.g. with Ctrl+C or forcing an early exit) and checking if the temporary file is deleted.

4. **Local Verification and Integration**:
   - Test the script by running it directly from the workspace root (e.g., `./.claude/hooks/agentic-session-freshness.sh`).
   - If updating a hook script under `.claude/hooks/`, verify it is referenced correctly in `.claude/settings.json` or `.cursor/hooks.json`.
   - **Validation Gate**: Run `./gradlew test` or the appropriate hook validation command (e.g. `./.claude/hooks/agentic-check-sync.sh`) to ensure no project build processes are broken.

## Examples

### Example 1: Creating a sync validation hook

* **User says**: "Write a script in `.claude/hooks/check-gradle-properties.sh` to ensure `gradle.properties` has `org.gradle.jvmargs` configured."
* **Actions taken**:
  1. Created `.claude/hooks/check-gradle-properties.sh` with standard shebang and flags.
  2. Resolved `WORKSPACE_DIR` using script-relative logic.
  3. Read `gradle.properties` safely using `grep` and printed error if missing.
  4. Tested the hook locally.
* **Result** (file `.claude/hooks/check-gradle-properties.sh`):
  ```bash
  #!/usr/bin/env bash
  set -euo pipefail
  IFS=$'\n\t'

  SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
  WORKSPACE_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
  PROPERTIES_FILE="$WORKSPACE_DIR/gradle.properties"

  if [ ! -f "$PROPERTIES_FILE" ]; then
    echo "[ERROR] gradle.properties not found at $PROPERTIES_FILE" >&2
    exit 1
  fi

  if ! grep -q "org.gradle.jvmargs" "$PROPERTIES_FILE"; then
    echo "[ERROR] org.gradle.jvmargs is missing in gradle.properties" >&2
    exit 1
  fi

  echo "[INFO] JVM arguments configured correctly."
  exit 0
  ```

## Common Issues

* **Issue**: Script fails with "unbound variable" when checking an optional environment variable.
  * **Fix**: Use parameter expansion with a default value fallback (e.g., `${VAR:-}` or `${1:-}`) instead of direct expansion (`$VAR` or `$1`).
* **Issue**: File paths not found when executing the script from a different directory.
  * **Fix**: Ensure `SCRIPT_DIR` is correctly evaluated relative to `${BASH_SOURCE[0]}` and prefix all paths with `"$SCRIPT_DIR/"` or `"$WORKSPACE_DIR/"`.
* **Issue**: Script fails on Windows/git-bash due to carriage return characters.
  * **Fix**: Ensure the line endings are LF (`\n`) instead of CRLF (`\r\n`). Run `dos2unix <filename>` or convert line endings in the editor.