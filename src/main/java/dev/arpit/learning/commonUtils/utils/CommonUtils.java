package dev.arpit.learning.commonUtils.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.util.StringUtils;

public class CommonUtils {
  private static final ILogger logger = LoggerFactory.getLogger(CommonUtils.class);

  public static boolean isEmpty(String string) {
    return !StringUtils.hasText(string);
  }

  public static boolean isEmpty(String[] arr) {
    return Stream.of(arr).map(i -> !StringUtils.hasText(i)).reduce(true, (i, j) -> i && j);
  }

  public static boolean isEmpty(JsonNode textNode) {
    return textNode == null || textNode.isNull() || isEmpty(textNode.textValue());
  }

  public static boolean isEmpty(Object[] objArr) {
    return (objArr == null || objArr.length == 0);
  }

  public static <T> boolean isEmpty(List<T> objList) {
    if (objList == null || objList.isEmpty()) {
      return true;
    }

    for (Object o : objList) {
      if (o != null) {
        return false;
      }
    }

    return true;
  }

  public static boolean isNotEmpty(String string) {
    return !isEmpty(string);
  }

  public static boolean isNotEmpty(JsonNode textNode) {
    return !isEmpty(textNode);
  }

  public static boolean isNotEmpty(Object[] objArr) {
    return !isEmpty(objArr);
  }

  public static <T> boolean isNotEmpty(List<T> objList) {
    return !isEmpty(objList);
  }

  public static @NonNull String getFileExtension(@NonNull String fileName) {
    try {
      URL url = URI.create(fileName).toURL();
      String filename = StringUtils.getFilenameExtension(url.getPath());
      return filename != null ? filename : "";
    } catch (MalformedURLException e) {
      logger.error(CommonUtilLogConstants.GET_FILE_EXTENSION_FAILED, e);
      return "";
    }
  }

  public static boolean hasJsonKeyAndValueIsNotNullAndIsTextual(
      @NonNull JsonNode jsonNode, @NonNull String key) {
    return (jsonNode.has(key) && !jsonNode.path(key).isNull() && jsonNode.path(key).isTextual());
  }

  public static boolean hasJsonKeyAndValueIsNull(
      @NonNull JsonNode jsonNode, @NonNull String jsonKey) {
    return (jsonNode.has(jsonKey) && jsonNode.path(jsonKey).isNull());
  }

  public static LocalDateTime getTodayWithZeroTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDateTime.parse(LocalDateTime.now().format(formatter));
  }

  public static <T> List<T> removeNullEntries(@NonNull List<T> objectList) {
    objectList.removeIf(Objects::isNull);
    return objectList;
  }

  public static int getNonEmptyStringsCount(@NonNull String[] inputArr) {
    int count = 0;
    for (String s : inputArr) {
      if (isNotEmpty(s)) {
        count++;
      }
    }

    return count;
  }

  public static @NonNull List<String> getNonEmptyStrings(@NonNull ArrayNode stringArrayNode) {
    List<String> nonEmptyStrings = new ArrayList<>();
    for (JsonNode jsonNode : stringArrayNode) {
      if (isNotEmpty(jsonNode.textValue())) {
        nonEmptyStrings.add(jsonNode.textValue());
      }
    }
    return nonEmptyStrings;
  }

  public static <T> void putInMap(
      @NonNull Map<String, List<T>> map, @NonNull String key, @NonNull List<T> value) {
    if (map.containsKey(key)) {
      map.get(key).addAll(value);
    } else {
      map.put(key, value);
    }
  }

  public static boolean isValidUrl(String url) {
    try {
      URI.create(url);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
