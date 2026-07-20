package dev.arpit.learning.commonUtils.utils.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;

public class RegexUtils {
  private static final Map<String, Pattern> mPatternCache = new ConcurrentHashMap<>();

  private static @NonNull Pattern getPattern(@NonNull String regex) {
    return mPatternCache.computeIfAbsent(regex, Pattern::compile);
  }

  public static boolean match(@NonNull String regex, @NonNull String string) {
    return getPattern(regex).matcher(string).matches();
  }

  public static boolean contains(@NonNull String regex, @NonNull String string) {
    return getPattern(regex).matcher(string).find();
  }

  public static boolean startsWith(@NonNull String regex, @NonNull String string) {
    Matcher matcher = getPattern(regex).matcher(string);
    return matcher.find() && (matcher.start() == 0);
  }
}
