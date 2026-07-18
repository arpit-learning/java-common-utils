---
name: aws-guidelines
description: Provides guidelines and configuration setups for integrating AWS services in a Spring Boot application. Use this when adding or configuring AWS clients (S3, SQS, SNS, Secrets Manager, etc.) or writing code to interact with AWS resources. Key capabilities include dependency setup, property configuration, and boilerplate templates. Do NOT use for general AWS CLI commands, infrastructure-as-code, or non-Spring Boot Java AWS usage.
paths:
  - **/build.gradle
  - **/*.java
  - **/application.yml
  - **/application.properties
---
# AWS Integration Guidelines

## Critical
- **No Hardcoded Credentials**: Never write or commit AWS access keys or secrets to the repository. Always use `AwsCredentialsProvider` (specifically `DefaultCredentialsProvider`) or rely on IAM Roles/Instance Profiles in AWS environments.
- **AWS SDK v2 Only**: Do not use the legacy AWS SDK v1. Use AWS SDK for Java v2 (`software.amazon.awssdk`) or Spring Cloud AWS v3+.
- **Mock/LocalStack for Tests**: Never run integration tests against real AWS resources without specific integration profile settings. Use `@MockBean` or Testcontainers with LocalStack.

## Instructions
1. **Declare Dependencies in build.gradle**:
   Add the Spring Cloud AWS BOM and the necessary starters to `build.gradle`.
   - Boilerplate:
     ```groovy
     dependencyManagement {
         imports {
             mavenBom "io.awspring.cloud:spring-cloud-aws-dependencies:3.1.0"
         }
     }
     dependencies {
         implementation "io.awspring.cloud:spring-cloud-aws-starter-s3"
         implementation "io.awspring.cloud:spring-cloud-aws-starter-sqs"
     }
     ```
   - *Validation*: Run `./gradlew build -x test` to verify dependency resolution.

2. **Configure application.yml**:
   Configure AWS properties under the standard Spring Cloud AWS namespace.
   - Boilerplate:
     ```yaml
     spring:
       cloud:
         aws:
           region:
             static: us-east-1
           credentials:
             use-default-aws-credentials-chain: true
     ```
   - *Validation*: Run `./gradlew bootRun` and check `/actuator/configprops` to confirm configurations are active.

3. **Inject and Use AWS Clients**:
   Use auto-configured templates (e.g., `S3Template`, `SqsTemplate`) or clients (e.g., `S3Client`) instead of manually instantiating them with builder patterns.
   - Boilerplate:
     ```java
     import io.awspring.cloud.s3.S3Template;
     import org.springframework.stereotype.Service;

     @Service
     public class StorageService {
         private final S3Template s3Template;

         public StorageService(S3Template s3Template) {
             this.s3Template = s3Template;
         }
     }
     ```
   - *Validation*: Create a unit test using `@ExtendWith(MockitoExtension.class)` to verify interaction with the client template.

## Examples
### Example 1: Add S3 Upload Capability
- **User says**: "Implement a service to save files to an S3 bucket named 'reports-bucket'"
- **Actions taken**:
  1. Updated `build.gradle` to include S3 dependency.
  2. Created `ReportStorageService.java` utilizing `S3Template`.
  3. Created an integration test using Testcontainers/LocalStack.
- **Result**:
  - `src/main/java/com/example/ReportStorageService.java`:
    ```java
    package com.example;

    import io.awspring.cloud.s3.S3Template;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import java.io.InputStream;

    @Service
    public class ReportStorageService {
        private final S3Template s3Template;
        private final String bucketName;

        public ReportStorageService(S3Template s3Template, @Value("${app.aws.s3.bucket}") String bucketName) {
            this.s3Template = s3Template;
            this.bucketName = bucketName;
        }

        public void uploadReport(String key, InputStream inputStream) {
            s3Template.upload(bucketName, key, inputStream);
        }
    }
    ```

## Common Issues
- **Issue**: `Unable to load AWS credentials from any provider in the chain`
  - **Resolution**:
    1. Verify local environment has credentials set (check `~/.aws/credentials` or environment variables `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY`).
    2. Confirm `spring.cloud.aws.credentials.use-default-aws-credentials-chain` is set to `true` in `application.yml`.
- **Issue**: `S3Exception: Access Denied (Status Code: 403)`
  - **Resolution**:
    1. Verify that the IAM Role or User credentials being used have the `s3:PutObject` and `s3:GetObject` actions allowed on the resource.
    2. Ensure the correct bucket name is referenced.