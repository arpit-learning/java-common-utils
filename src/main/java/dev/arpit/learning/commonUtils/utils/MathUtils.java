package dev.arpit.learning.commonUtils.utils;

import com.github.f4b6a3.uuid.UuidCreator;
import java.security.SecureRandom;
import java.util.UUID;
import lombok.NonNull;

public class MathUtils {
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public static int roundOff(float value) {
    return Math.round(value);
  }

  public static boolean checkFloatContainsDecimal(Float value) {
    if (value == null) {
      return false;
    }

    return value - value.intValue() != 0;
  }

  public static boolean returnRandom(int totalRange, int rangeBoundary) {
    int r = SECURE_RANDOM.nextInt(10_000_000);
    int m = r % totalRange;
    return m < rangeBoundary;
  }

  public static @NonNull UUID generateUUIDV4() {
    return UuidCreator.getRandomBased();
  }

  public static @NonNull UUID generateUUIDV7() {
    return UuidCreator.getTimeOrderedEpoch();
  }
}
