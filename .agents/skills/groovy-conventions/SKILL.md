---
name: groovy-conventions
description: Enforces Groovy coding style, type-safety rules, and idiomatic patterns. Use this when writing or modifying Groovy classes, build scripts (build.gradle), or Groovy test cases (e.g. Spock specifications). Key capabilities include static compilation enforcement, proper type declarations, safe navigation handling, and Spock test patterns. Do NOT use for writing pure Java code or shell scripts.
---
# Groovy Coding Conventions

## Critical
- **Always apply `@CompileStatic`**: Every new Groovy class (except DSL scripts like `build.gradle`) must be annotated with `groovy.transform.CompileStatic` to ensure compile-time type checking and Java-like performance.
- **Explicit Types Over `def`**: Do not use `def` for class fields, method parameters, or public method return types. Explicitly declare the type (e.g., `String`, `List<String>`, `void`) to enable static type checking.
- **Run Compilation Verification**: Always run `./gradlew compileGroovy compileTestGroovy` before declaring a task complete to verify there are no compilation or static analysis failures.

## Instructions
1. **Apply Class Annotations**
   - Place `@CompileStatic` at the class level.
   - Ensure all necessary Spring annotations (like `@Service`, `@Component`, `@RestController`) are also applied if applicable.
   - *Validation Gate*: Verify that `groovy.transform.CompileStatic` is imported and present at the class level.

2. **Define Typed Fields and Methods**
   - Replace generic `def` definitions with explicit types.
   - Use `final` where applicable for immutability.
   - *Validation Gate*: Check all class variables and method signatures in the file and confirm no arguments or return types use `def`.

3. **Utilize Idiomatic Groovy Safely**
   - Use the safe navigation operator `?.` to avoid `NullPointerException` when calling methods or accessing properties on nullable references.
   - Use Groovy closures and collection methods (`collect`, `findAll`, `any`, `every`) instead of traditional `for` loops where readable, ensuring type inference works under `@CompileStatic`.
   - *Validation Gate*: Confirm any nested property access (e.g., `user?.address?.zipCode`) uses the safe navigation operator if null is possible.

4. **Write Idiomatic Spock Tests**
   - When writing tests in Groovy, extend `spock.lang.Specification`.
   - Use descriptive feature method names as strings (e.g., `def "should return user when found"()`).
   - Use Spock blocks: `given:`, `when:`, `then:`, `expect:`, `where:`.
   - *Validation Gate*: Verify that test classes extend `Specification` and utilize correct mock syntax (`Mock(...)` and invocation counts).

5. **Verify Compilation and Run Tests**
   - Execute the gradle compile tasks to ensure everything is correct.
   - Command: `./gradlew compileGroovy compileTestGroovy`
   - *Validation Gate*: The build must complete successfully with `BUILD SUCCESSFUL`.

## Examples
### Example 1: Creating a service class
- **User says**: "Create a Groovy service class named PaymentProcessor that validates a transaction and processes it."
- **Actions taken**:
  1. Created `src/main/groovy/com/example/PaymentProcessor.groovy`.
  2. Applied `@CompileStatic` and `@Service` annotations.
  3. Defined exact method types (`boolean process(Transaction tx)`).
  4. Used safe navigation on `transaction?.amount`.
  5. Verified with `./gradlew compileGroovy`.
- **Result**:
  ```groovy
  package com.example

  import groovy.transform.CompileStatic
  import org.springframework.stereotype.Service

  @Service
  @CompileStatic
  class PaymentProcessor {
      boolean process(Transaction transaction) {
          if (transaction?.amount == null || transaction.amount <= 0) {
              return false
          }
          // processing logic
          return true
      }
  }
  ```

## Common Issues
- **Error**: `Groovyc: [Static type checking] - Cannot find matching method java.lang.Object#...`
  - *Fix*: This occurs under `@CompileStatic` when the compiler doesn't know the exact type of a variable (often from generic collections or dynamic maps). Cast the variable using `as` or declare its type explicitly. E.g., change `def val = map.get("key")` to `String val = map.get("key") as String` or `String val = (String) map.get("key")`.
- **Error**: `No such property: ... for class: java.lang.Object`
  - *Fix*: Ensure properties are accessed on typed objects. If referencing dynamic map entries, use `map.get('prop')` or cast the map/object explicitly.