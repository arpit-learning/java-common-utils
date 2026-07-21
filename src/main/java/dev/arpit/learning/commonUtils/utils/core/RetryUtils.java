package dev.arpit.learning.commonUtils.utils.core;

import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.commonUtils.constants.LogConstantFields;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.util.function.Supplier;

public class RetryUtils {
  private static final ILogger logger = LoggerFactory.getLogger(RetryUtils.class);

  public static <T> T executeWithRetry(Supplier<T> action, int maxAttempts, long delayMs) {
    int attempt = 1;
    while (true) {
      try {
        return action.get();
      } catch (Exception e) {
        if (attempt >= maxAttempts) {
          logger.error(
              LogConstant.EXCEPTION, LogConstantFields.ERR_MSG, "Max retry attempts reached");
          throw e;
        }
        logger.warn(
            LogConstant.EXCEPTION,
            LogConstantFields.ERR_MSG,
            "Attempt " + attempt + " failed, retrying in " + delayMs + "ms");
        try {
          Thread.sleep(delayMs);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          throw new RuntimeException("Retry interrupted", ie);
        }
        attempt++;
      }
    }
  }
}
