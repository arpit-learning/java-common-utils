---
name: batch-conventions
description: Guides the implementation of Spring Boot Batch jobs, tasklets, chunk-oriented steps, readers, processors, and writers in Java and Groovy. Use when creating or editing batch processing logic or job configurations. Key capabilities include configuring chunk size, transaction boundaries, skip/retry rules, and verifying execution with Gradle. Do NOT use for standard REST controllers, database migrations, or general Spring MVC views.
---
# Batch Conventions

## Critical
- **Strict Idempotency**: All batch jobs must be designed to be idempotent. Re-running a job with the exact same parameters must not duplicate database entries or corrupt downstream systems.
- **Transaction Boundaries**: Always associate a step with the correct `PlatformTransactionManager`. Never perform database writes outside of Spring Batch's managed transactions.
- **No Embedded Secrets**: Never hardcode configuration parameters, URLs, or API keys in the job. Resolve them using `@Value("${property.name}")` or standard environment properties.
- **Service Readiness**: Before verifying or launching batch steps locally, confirm the service health endpoint is up and reporting status: `curl -sf "http://localhost:8080/actuator/health"`.

## Instructions
1. **Define Job and Step Beans**:
   - Create a configuration class annotated with `@Configuration`.
   - Inject `JobBuilderFactory` and `StepBuilderFactory` to define the workflow.
   - *Validation Gate*: Check `build.gradle` to ensure batch dependency is defined: `implementation 'org.springframework.boot:spring-boot-starter-batch'`.

2. **Implement Chunk-Oriented Components**:
   - Implement `ItemReader<I>`, `ItemProcessor<I, O>`, and `ItemWriter<O>` with strong types.
   - Prefer bulk operations in `ItemWriter` over row-by-row repository calls to save processing time.
   - *Validation Gate*: Verify that the processor returns `null` if the item needs to be filtered out/skipped.

3. **Handle Errors and Fault Tolerance**:
   - Set appropriate skip limits and retry limits on steps processing external inputs (e.g., `.faultTolerant().skip(FlatFileParseException.class).skipLimit(10)`).
   - Implement a custom `JobExecutionListener` to log job start, finish, and failure statuses.
   - *Validation Gate*: Run `./gradlew test` to ensure batch configuration classes build and pass context loading tests.

4. **Local Development and Execution**:
   - Launch the application locally using `./gradlew bootRun`.
   - Run batch verification or job launch commands as specified in `run.md`.
   - *Dependency*: Uses configuration from Step 1 and 2.

## Examples
- **User says**: "Add a batch job to process user transactions from a DB table."
- **Actions taken**:
  1. Defined `processTransactionJob` and `processTransactionStep` in `TransactionBatchConfig`.
  2. Used `RepositoryItemReader` to read pending records from the database in chunks of 50.
  3. Created `TransactionProcessor` to evaluate fraud rules and update record statuses.
  4. Used `RepositoryItemWriter` to commit updates.
  5. Ran `./gradlew test` to ensure all tests passed successfully.
- **Result**: Implemented type-safe chunk-oriented processing step with automatic transaction rollbacks on unexpected runtime exceptions.

## Common Issues
- **If you see 'JobInstanceAlreadyCompleteException'**:
  1. Verify the job parameters. Spring Batch prevents running the exact same job instance twice.
  2. Fix: Pass a dynamic parameter (e.g., timestamp `run.id` or uuid) when starting the job so each run has a unique signature.
- **If you see 'TransactionRequiredException' inside an ItemWriter**:
  1. Verify that the Step configuration explicitly sets a transaction manager: `.transactionManager(transactionManager)`.
  2. Ensure target databases support transactions (e.g., InnoDB engine for MySQL/MariaDB).