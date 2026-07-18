package dev.arpit.learning.commonUtils.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogFieldConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

public class JsonUtils {
  private static final ILogger logger = LoggerFactory.getLogger(JsonUtils.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static @NonNull String toJsonString(@NonNull JsonNode obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      logger.error(
          CommonUtilLogConstants.OBJECT_TO_STRING_CONVERSION_EXC,
          CommonUtilLogFieldConstants.ERR_MSG,
          e);
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
      logger.error(
          CommonUtilLogConstants.OBJECT_TO_STRING_CONVERSION_EXC,
          CommonUtilLogFieldConstants.ERR_MSG,
          e);
      return null;
    }
  }

  public static <T> @NonNull T getFromJsonWithException(
      @NonNull String jsonStr, @NonNull Class<T> objClass) throws IOException {
    try {
      return objectMapper.readValue(jsonStr, objClass);
    } catch (IOException e) {
      logger.error(
          CommonUtilLogConstants.OBJECT_TO_STRING_CONVERSION_EXC,
          CommonUtilLogFieldConstants.ERR_MSG,
          e);
      throw e;
    }
  }

  public static <T> T getFromJsonNode(@NonNull JsonNode jsonNode, @NonNull Class<T> template) {
    try {
      return objectMapper.treeToValue(jsonNode, template);
    } catch (IOException e) {
      logger.error(
          CommonUtilLogConstants.OBJECT_TO_STRING_CONVERSION_EXC,
          CommonUtilLogFieldConstants.ERR_MSG,
          e);
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

  public static @NonNull JsonNode merge(@NonNull JsonNode mainNode, @NonNull JsonNode updateNode) {

    Iterator<String> fieldNames = updateNode.fieldNames();

    while (fieldNames.hasNext()) {
      String updatedFieldName = fieldNames.next();
      JsonNode valueToBeUpdated = mainNode.get(updatedFieldName);
      JsonNode updatedValue = updateNode.get(updatedFieldName);

      // If the node is an @ArrayNode
      if (valueToBeUpdated != null && valueToBeUpdated.isArray() && updatedValue.isArray()) {
        // running a loop for all elements of the updated ArrayNode
        for (int i = 0; i < updatedValue.size(); i++) {
          JsonNode updatedChildNode = updatedValue.get(i);
          // Create a new Node in the node that should be updated, if there was no
          // corresponding node in it
          // Use-case - where the updateNode will have a new element in its Array
          if (valueToBeUpdated.size() <= i) {
            ((ArrayNode) valueToBeUpdated).add(updatedChildNode);
          }
          // getting reference for the node to be updated
          JsonNode childNodeToBeUpdated = valueToBeUpdated.get(i);
          merge(childNodeToBeUpdated, updatedChildNode);
        }
        // if the Node is an @ObjectNode
      } else if (valueToBeUpdated != null && valueToBeUpdated.isObject()) {
        merge(valueToBeUpdated, updatedValue);
      } else {
        if (mainNode instanceof ObjectNode) {
          ((ObjectNode) mainNode).replace(updatedFieldName, updatedValue);
        }
      }
    }
    return mainNode;
  }

  public static float getFloat(@NonNull JsonNode jsonNode) {
    if (jsonNode.isFloatingPointNumber() || jsonNode.isFloat()) {
      return jsonNode.floatValue();
    } else if (jsonNode.isInt()) {
      return (float) jsonNode.intValue();
    } else if (jsonNode.isTextual()) {
      return Float.parseFloat(jsonNode.textValue());
    }

    return 0;
  }

  public static boolean isValidFloatNode(@NonNull JsonNode jsonNode) {
    return jsonNode.isFloatingPointNumber() || jsonNode.isFloat() || jsonNode.isInt();
  }

  public static @NonNull String getTextValue(@NonNull JsonNode jsonNode) {
    return jsonNode.textValue();
  }

  public static boolean hasJsonKeyAndNotNull(@NonNull JsonNode jsonNode, @Nullable String key) {
    return (jsonNode.has(key) && (!jsonNode.path(key).isNull()));
  }

  public static boolean isTextNode(@NonNull JsonNode jsonNode) {
    return jsonNode.isTextual();
  }

  public static boolean isNotTextNode(@NonNull JsonNode jsonNode) {
    return !isTextNode(jsonNode);
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
