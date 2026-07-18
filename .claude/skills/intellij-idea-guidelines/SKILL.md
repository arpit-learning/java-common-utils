---
name: intellij-idea-guidelines
description: Configures and validates IntelliJ IDEA project settings including SDKs, Gradle integration, and VCS mappings. Use when the user requests IDE setup, IntelliJ configuration repair, or when modifying files in `.idea/`. Key capabilities include checking `.idea/misc.xml`, `.idea/gradle.xml`, and `.idea/vcs.xml` for structural correctness. Do NOT use for general Java programming, database setup, or writing source code unless it specifically relates to IDE configuration.
paths:
  - .idea/**/*
  - **/*.iml
  - build.gradle
  - settings.gradle
---
# IntelliJ IDEA Project Integration Guidelines

## Critical
- Never commit developer-specific settings (e.g., `workspace.xml`, `shelf/`, `tasks.xml`) to version control. Verify `.idea/.gitignore` contains exclusions for these before making modifications.
- Ensure all `.idea` configuration updates are standard across standard IntelliJ environments to prevent breaking other team members' setups.
- Verify the JDK version used in `.idea/misc.xml` matches the Java version configured in `build.gradle` or `gradle.properties`.

## Instructions
1. **Inspect Existing Settings**
   - Read `.idea/misc.xml` to verify the configured SDK and language level.
   - Read `.idea/gradle.xml` to check the Gradle settings.
   - Validation Gate: Ensure `.idea/misc.xml` contains a valid `<component name="ProjectRootManager" ...>` entry with languageLevel corresponding to the project's target JDK.
2. **Configure Project SDK (misc.xml)**
   - If the SDK is missing or incorrect, update `.idea/misc.xml` to define the project-jdk-name and languageLevel.
   - Example template:
     ```xml
     <component name="ProjectRootManager" version="2" languageLevel="JDK_17" default="true" project-jdk-name="17" project-jdk-type="JavaSDK">
     ```
   - Validation Gate: Verify that `project-jdk-name` matches the expected JDK.
3. **Set Up Gradle Integration (gradle.xml)**
   - Ensure `.idea/gradle.xml` delegates builds to the local Gradle Wrapper.
   - Ensure `<option name="distributionType" value="DEFAULT_WRAPPED" />` is present.
   - Validation Gate: Confirm the `externalProjectPath` matches the project directory.
4. **Define VCS Mappings (vcs.xml)**
   - Confirm `.idea/vcs.xml` maps the root directory to Git.
   - Validation Gate: The file must have `<mapping directory="" vcs="Git" />`.

## Examples
### Example 1: Updating Project JDK in IntelliJ Settings
* **User says**: "IntelliJ is showing compilation errors because it is pointing to Java 11 but we use Java 17."
* **Actions taken**:
  1. Read `.idea/misc.xml` and locate `ProjectRootManager`.
  2. Notice `languageLevel="JDK_11"` and `project-jdk-name="11"`.
  3. Use `replace_file_content` to update these values to `JDK_17` and `17` respectively.
  4. Confirm the modification by checking the file again.
* **Result**: IntelliJ project configuration updated to use JDK 17.

## Common Issues
- **Error: SDK '17' is not defined in IntelliJ**
  - Fix: Ensure that you have Java 17 installed locally, and that `project-jdk-name` in `.idea/misc.xml` matches the exact name of the SDK configured in IntelliJ (usually "17" or "openjdk-17").
- **Error: Gradle sync fails in IntelliJ**
  - Fix: Check `.idea/gradle.xml`. Ensure that `<option name="distributionType" value="DEFAULT_WRAPPED" />` is selected so that IntelliJ uses the wrapper settings from `gradle/wrapper/gradle-wrapper.properties`.