package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.commonUtils.utils.core.ValidationUtils;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ResolvableType;

public class JsonNodeUtils {
  private static final ILogger logger = LoggerFactory.getLogger(JsonNodeUtils.class);
  private static final ResolvableType stringResolvableType = ResolvableType.forClass(String.class);

  private static final List<IJsonNodeMerger> mergers = new ArrayList<>();
  private static final List<IJsonNodeFloatConverter> floatConverters = new ArrayList<>();

  static {
    mergers.add(new ArrayNodeMerger());
    mergers.add(new ObjectNodeMerger());

    floatConverters.add(
        new IJsonNodeFloatConverter() {
          @Override
          public boolean supports(JsonNode node) {
            return node.isFloatingPointNumber() || node.isFloat();
          }

          @Override
          public float convert(JsonNode node) {
            return node.floatValue();
          }
        });

    floatConverters.add(
        new IJsonNodeFloatConverter() {
          @Override
          public boolean supports(JsonNode node) {
            return node.isInt();
          }

          @Override
          public float convert(JsonNode node) {
            return (float) node.intValue();
          }
        });

    floatConverters.add(
        new IJsonNodeFloatConverter() {
          @Override
          public boolean supports(JsonNode node) {
            return node.isTextual();
          }

          @Override
          public float convert(JsonNode node) {
            return Float.parseFloat(node.textValue());
          }
        });
  }

  public static void registerMerger(IJsonNodeMerger merger) {
    mergers.add(merger);
  }

  public static void registerFloatConverter(IJsonNodeFloatConverter converter) {
    floatConverters.add(converter);
  }

  public static @NonNull JsonNode merge(@NonNull JsonNode mainNode, @NonNull JsonNode updateNode) {
    Iterator<String> fieldNames = updateNode.fieldNames();
    IJsonNodeMerger defaultMerger = new DefaultNodeMerger();

    while (fieldNames.hasNext()) {
      String updatedFieldName = fieldNames.next();
      JsonNode valueToBeUpdated = mainNode.get(updatedFieldName);
      JsonNode updatedValue = updateNode.get(updatedFieldName);

      boolean merged = false;
      for (IJsonNodeMerger merger : mergers) {
        if (merger.supports(valueToBeUpdated, updatedValue)) {
          merger.merge(mainNode, valueToBeUpdated, updatedValue, updatedFieldName);
          merged = true;
          break;
        }
      }

      if (!merged) {
        defaultMerger.merge(mainNode, valueToBeUpdated, updatedValue, updatedFieldName);
      }
    }
    return mainNode;
  }

  public static float getFloat(@NonNull JsonNode jsonNode) {
    for (IJsonNodeFloatConverter converter : floatConverters) {
      if (converter.supports(jsonNode)) {
        return converter.convert(jsonNode);
      }
    }
    return 0;
  }

  public static boolean isValidFloatNode(@NonNull JsonNode jsonNode) {
    for (IJsonNodeFloatConverter converter : floatConverters) {
      if (converter.supports(jsonNode)) {
        return true;
      }
    }
    return false;
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

  public static boolean isEmpty(JsonNode textNode) {
    return textNode == null
        || textNode.isNull()
        || textNode.textValue() == null
        || textNode.textValue().trim().isEmpty();
  }

  public static boolean isNotEmpty(JsonNode textNode) {
    return !isEmpty(textNode);
  }

  public static boolean hasJsonKeyAndValueIsNotNullAndIsTextual(
      @NonNull JsonNode jsonNode, @NonNull String key) {
    return (jsonNode.has(key) && !jsonNode.path(key).isNull() && jsonNode.path(key).isTextual());
  }

  public static boolean hasJsonKeyAndValueIsNull(
      @NonNull JsonNode jsonNode, @NonNull String jsonKey) {
    return (jsonNode.has(jsonKey) && jsonNode.path(jsonKey).isNull());
  }

  public static @NonNull java.util.List<String> getNonEmptyStrings(
      @NonNull ArrayNode stringArrayNode) {
    java.util.List<String> nonEmptyStrings = new java.util.ArrayList<>();
    for (JsonNode jsonNode : stringArrayNode) {
      try {
        if (!ValidationUtils.isNullOrEmpty(
            new Pair<>(jsonNode.textValue(), stringResolvableType))) {
          nonEmptyStrings.add(jsonNode.textValue());
        }
      } catch (EmptyValidatorNotFoundException e) {
        logger.error("EmptyValidatorNotFoundException", e);
      }
    }
    return nonEmptyStrings;
  }
}
