package dev.arpit.learning.commonUtils.utils;

import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogFieldConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.StringUtils;

public class NullEmptyValidationsUtils {
  private static final ILogger logger = LoggerFactory.getLogger(NullEmptyValidationsUtils.class);

  public static void checkParametersAsNullEmpty(String... args) throws Exception {
    if (args == null) return;

    for (int i = 0; i < args.length; i++) {
      String cs = args[i];
      if (!StringUtils.hasText(cs)) {
        logger.error(
            CommonUtilLogConstants.NULL_OR_EMPTY_ERROR,
            CommonUtilLogFieldConstants.POSITION_OF_NULL,
            i);
        throw new Exception(i + " positioned argument found as null. Please pass valid argument!");
      }
    }
  }

  public static boolean isParametersAsNullEmpty(String... args) {
    if (args == null) return true;

    for (int i = 0; i < args.length; i++) {
      String cs = args[i];
      if (!StringUtils.hasText(cs)) {
        logger.error(
            CommonUtilLogConstants.NULL_OR_EMPTY_ERROR,
            CommonUtilLogFieldConstants.POSITION_OF_NULL,
            i);
        return true;
      }
    }

    return false;
  }

  public static boolean isNullOrEmpty(Object... args) {
    if (args == null) return true;

    for (Object obj : args) {
      switch (obj) {
        case String s -> {
          if (!StringUtils.hasText(s)) {
            return true;
          }
        }
        case Map map -> {
          if (map.isEmpty()) {
            return true;
          }
        }
        case List list -> {
          if (list.isEmpty()) {
            return true;
          }
        }
        case Set set -> {
          if (set.isEmpty()) {
            return true;
          }
        }
        case null, default -> {
          if (obj == null) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static boolean isArrayNullOrEmpty(Object[] array) {
    return array == null || array.length == 0;
  }
}
