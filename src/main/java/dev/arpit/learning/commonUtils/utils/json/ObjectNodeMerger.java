package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.databind.JsonNode;

public class ObjectNodeMerger implements IJsonNodeMerger {
  @Override
  public boolean supports(JsonNode valueToBeUpdated, JsonNode updatedValue) {
    return valueToBeUpdated != null && valueToBeUpdated.isObject() && !updatedValue.isArray();
  }

  @Override
  public void merge(
      JsonNode mainNode,
      JsonNode valueToBeUpdated,
      JsonNode updatedValue,
      String updatedFieldName) {
    JsonNodeUtils.merge(valueToBeUpdated, updatedValue);
  }
}
