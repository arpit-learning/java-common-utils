package dev.arpit.learning.commonUtils.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;

public class RegexUtils {
  private static final Map<String, Pattern> mPatternCache = new HashMap<>();

  private static @NonNull Pattern getPattern(@NonNull String regex) {
    if (!mPatternCache.containsKey(regex)) {
      mPatternCache.put(regex, Pattern.compile(regex));
    }
    return mPatternCache.get(regex);
  }

  public static boolean match(@NonNull String regex, @NonNull String string) {
    return getPattern(regex).matcher(string).matches();
  }

  public static boolean contains(
      @NonNull String regex, @NonNull String string) {
    return getPattern(regex).matcher(string).find();
  }

  public static boolean startsWith(
      @NonNull String regex, @NonNull String string) {
    Matcher matcher = getPattern(regex).matcher(string);
    return matcher.find() && (matcher.start() == 0);
  }
}
