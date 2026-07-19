package dev.arpit.learning.commonUtils.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import lombok.NonNull;

public class StringUtils {
  public static boolean isEmptyOrNull(String str) {
    if (str == null) {
      return true;
    }

    return !org.springframework.util.StringUtils.hasText(str);
  }

  /**
   * Counts the number of placeholders (#) in a template string.
   *
   * @param template - Template string containing placeholders.
   * @return - The number of placeholders in the template.
   */
  private static int countPlaceholders(@NonNull String template) {
    int count = 0;
    int index = 0;
    while ((index = template.indexOf("#", index)) != -1) {
      count++;
      index++;
    }
    return count;
  }

  /**
   * Replaces placeholders which should be # in a template string with the corresponding values.
   *
   * @param template - Template string containing placeholders.
   * @param values - Values to replace placeholders with.
   * @return - The template string with placeholders replaced by the corresponding values.
   */
  public static String getString(@NonNull String template, Object @NonNull ... values) {
    int placeholderCount = countPlaceholders(template);

    if (placeholderCount != values.length) {
      throw new IllegalArgumentException(
          String.format(
              "Template has %d placeholders but %d arguments were provided. The counts must match.",
              placeholderCount, values.length));
    }

    String result = template;
    for (Object value : values) {
      result = result.replaceFirst("#", String.valueOf(value));
    }

    return result;
  }

  public static String decode(String encodedStr, String format)
      throws UnsupportedEncodingException {
    return URLDecoder.decode(encodedStr, format);
  }

  public static <T> @NonNull String getTokenSeparatedString(
      @NonNull List<T> objects, @NonNull String token) {
    if (objects.isEmpty() || !isEmptyOrNull(token)) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    for (Object curr : objects) {
      sb.append(curr);
      sb.append(token);
    }
    sb.delete(sb.length() - token.length(), sb.length());
    return sb.toString();
  }

  public static @NonNull String capitalizeEachWord(@NonNull String text) {
    String[] words = text.split(" ");
    for (int i = 0; i < words.length; i++) {
      String word = words[i];
      word = org.springframework.util.StringUtils.capitalize(word);
      words[i] = word;
    }

    return String.join(" ", words);
  }

  public static @NonNull String trimIfNotNull(String checkStr) {
    if (checkStr == null) return "";
    return checkStr.trim();
  }

  public static String[] splitIfNotNull(String str, @NonNull String tokenSeparator) {
    String trimmed = trimIfNotNull(str);
    return trimmed.split(tokenSeparator);
  }
}
