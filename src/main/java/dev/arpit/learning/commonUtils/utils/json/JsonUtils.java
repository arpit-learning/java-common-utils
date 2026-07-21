package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.commonUtils.constants.LogConstantFields;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class JsonUtils {
  private static final ILogger logger = LoggerFactory.getLogger(JsonUtils.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static @NonNull String toJsonString(@NonNull JsonNode obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      logger.error(LogConstant.OBJECT_TO_STRING_CONVERSION_EXC, LogConstantFields.ERR_MSG, e);
      return "";
    }
  }

  public static @NonNull String toJsonString(@NonNull Object obj) {
    JsonNode jsonNode = objectMapper.convertValue(obj, JsonNode.class);
    return toJsonString(jsonNode);
  }

  public static <T> T getFromJson(@NonNull String jsonStr, @NonNull Class<T> objClass) {
    try {
      return objectMapper.readValue(jsonStr, objClass);
    } catch (IOException e) {
      logger.error(LogConstant.OBJECT_TO_STRING_CONVERSION_EXC, LogConstantFields.ERR_MSG, e);
      return null;
    }
  }

  public static <T> @NonNull T getFromJsonWithException(
      @NonNull String jsonStr, @NonNull Class<T> objClass) throws IOException {
    try {
      return objectMapper.readValue(jsonStr, objClass);
    } catch (IOException e) {
      logger.error(LogConstant.OBJECT_TO_STRING_CONVERSION_EXC, LogConstantFields.ERR_MSG, e);
      throw e;
    }
  }

  public static <T> T getFromJsonNode(@NonNull JsonNode jsonNode, @NonNull Class<T> template) {
    try {
      return objectMapper.treeToValue(jsonNode, template);
    } catch (IOException e) {
      logger.error(LogConstant.OBJECT_TO_STRING_CONVERSION_EXC, LogConstantFields.ERR_MSG, e);
      return null;
    }
  }

  public static @NonNull String toJsonString(
      @NonNull String key, @NonNull String value, @NonNull String objMap) {
    if (value.equals("[]")) {
      if (objMap.equals("{}")) {
        return "";
      }
      @SuppressWarnings("unchecked")
      Map<String, String> map = getFromJson(objMap, HashMap.class);
      if (map == null) {
        map = new HashMap<>();
      }
      map.remove(key);
      return toJsonString(map);
    }

    @SuppressWarnings("unchecked")
    Map<String, String> map = getFromJson(objMap, HashMap.class);
    if (map == null) {
      map = new HashMap<>();
    }
    map.put(key, value);
    return toJsonString(map);
  }

  public static <T> @NonNull T convertObjectValueToClass(
      @NonNull Object object, @NonNull Class<T> toClass) {
    return objectMapper.convertValue(object, toClass);
  }

  public static <T> @NonNull T convertObjectValueToType(
      @NonNull Object object, @NonNull TypeReference<T> typeReference) {
    return objectMapper.convertValue(object, typeReference);
  }

  public static @NonNull JsonNode toJsonNode(@NonNull Object object) {
    return objectMapper.convertValue(object, JsonNode.class);
  }

  public static @NonNull JsonNode toJsonObject(@NonNull Object object) {
    return objectMapper.convertValue(object, ObjectNode.class);
  }

  public static @NonNull ArrayNode toArrayNode(@NonNull Object object) {
    return objectMapper.valueToTree(object);
  }

  public static <T> T readValueToType(
      @NonNull String object, @NonNull TypeReference<T> typeReference) throws IOException {
    return objectMapper.readValue(object, typeReference);
  }
}
