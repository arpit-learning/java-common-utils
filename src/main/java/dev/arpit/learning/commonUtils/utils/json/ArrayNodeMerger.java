package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ArrayNodeMerger implements IJsonNodeMerger {
  @Override
  public boolean supports(JsonNode valueToBeUpdated, JsonNode updatedValue) {
    return valueToBeUpdated != null && valueToBeUpdated.isArray() && updatedValue.isArray();
  }

  @Override
  public void merge(
      JsonNode mainNode,
      JsonNode valueToBeUpdated,
      JsonNode updatedValue,
      String updatedFieldName) {
    for (int i = 0; i < updatedValue.size(); i++) {
      JsonNode updatedChildNode = updatedValue.get(i);
      if (valueToBeUpdated.size() <= i) {
        ((ArrayNode) valueToBeUpdated).add(updatedChildNode);
      }
      JsonNode childNodeToBeUpdated = valueToBeUpdated.get(i);
      JsonNodeUtils.merge(childNodeToBeUpdated, updatedChildNode);
    }
  }
}
