package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class RetryUtilsTest {
  @Test
  void test_objectCreation() {
    // act & assert
    RetryUtils utils = new RetryUtils();
    assertNotNull(utils);
  }

  @Test
  void test_executeWithRetry_SuccessOnFirstTry() {
    // arrange
    AtomicInteger count = new AtomicInteger(0);

    // act
    String result =
        RetryUtils.executeWithRetry(
            () -> {
              count.incrementAndGet();
              return "Success";
            },
            3,
            10);

    // assert
    assertNotNull(result);
    assertEquals("Success", result);
    assertEquals(1, count.get());
  }

  @Test
  void test_executeWithRetry_SuccessOnThirdTry() {
    // arrange
    AtomicInteger count = new AtomicInteger(0);

    // act
    String result =
        RetryUtils.executeWithRetry(
            () -> {
              if (count.incrementAndGet() < 3) {
                throw new RuntimeException("Fail");
              }

              return "Success";
            },
            3,
            10);

    // assert
    assertNotNull(result);
    assertEquals("Success", result);
    assertEquals(3, count.get());
  }

  @Test
  void test_executeWithRetry_FailureExceedsMaxAttempts() {
    // act & assert
    assertThrows(
        RuntimeException.class,
        () ->
            RetryUtils.executeWithRetry(
                () -> {
                  throw new RuntimeException("Always Fail");
                },
                3,
                10));
  }

  @Test
  void test_executeWithRetry_InterruptedException() throws InterruptedException {
    // act & assert
    Thread thread =
        new Thread(
            () ->
                assertThrows(
                    RuntimeException.class,
                    () ->
                        RetryUtils.executeWithRetry(
                            () -> {
                              throw new RuntimeException("Fail to trigger sleep");
                            },
                            5,
                            5000)));

    thread.start();
    // Wait briefly to ensure the thread enters sleep
    Thread.sleep(100);
    // Interrupt the thread while it's sleeping in RetryUtils
    thread.interrupt();
    thread.join();
  }
}
