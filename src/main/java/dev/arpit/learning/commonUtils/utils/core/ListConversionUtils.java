package dev.arpit.learning.commonUtils.utils.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import lombok.NonNull;

public class ListConversionUtils {
  private static final ILogger logger = LoggerFactory.getLogger(ListConversionUtils.class);
  private static final ObjectMapper mapper = new ObjectMapper();

  @SuppressWarnings("unchecked")
  public static <T> @NonNull T @NonNull [] listToArray(
      @NonNull List<T> listStr, @NonNull Class<T> clazz) {
    T[] array = (T[]) Array.newInstance(clazz, 0);
    return listStr.toArray(array);
  }

  public static @NonNull List<String> stringToList(@NonNull String str) {
    logger.info(LogConstant.STRING_TO_LIST_CONVERSION);
    List<String> listStr = new ArrayList<>();
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    if (StringUtils.isNullOrEmpty(str)) {
      logger.info(LogConstant.EMPTY_STRING_FOUND);
      return listStr;
    }

    JsonNode jsonNode;
    try {
      jsonNode = mapper.readTree(str);
    } catch (IOException e) {
      logger.error(LogConstant.STRING_TO_LIST_CONVERSION_EXC, e);
      return listStr;
    }

    if (jsonNode == null) {
      logger.error(LogConstant.UNABLE_TO_PROCESS_STRING);
      return listStr;
    }

    listStr = mapper.convertValue(jsonNode, new TypeReference<>() {});
    return listStr;
  }

  public static <T> @NonNull List<T> removeNullEntries(@NonNull List<T> objectList) {
    objectList.removeIf(Objects::isNull);
    return objectList;
  }

  public static <T> void putInMap(
      @NonNull Map<String, List<T>> map, @NonNull String key, @NonNull List<T> value) {
    if (map.containsKey(key)) {
      map.get(key).addAll(value);
    } else {
      map.put(key, value);
    }
  }

  public static int getNonEmptyStringsCount(@NonNull String[] inputArr) {
    int count = 0;
    for (String s : inputArr) {
      if (org.springframework.util.StringUtils.hasText(s)) {
        count++;
      }
    }
    return count;
  }

  public static <T> @NonNull List<T> nullSafeList(List<T> list) {
    return list == null ? List.of() : list;
  }

  public static <T> @NonNull List<List<T>> chunk(@NonNull List<T> list, int chunkSize) {
    if (chunkSize <= 0) {
      throw new IllegalArgumentException("Chunk size must be greater than 0");
    }
    List<List<T>> chunks = new ArrayList<>();
    for (int i = 0; i < list.size(); i += chunkSize) {
      chunks.add(new ArrayList<>(list.subList(i, Math.min(list.size(), i + chunkSize))));
    }
    return chunks;
  }

  public static <T> @NonNull List<T> distinct(@NonNull List<T> list) {
    Set<T> set = new LinkedHashSet<>(list);
    return new ArrayList<>(set);
  }
}
