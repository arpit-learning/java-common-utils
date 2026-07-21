package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DefaultNodeMerger implements IJsonNodeMerger {
  @Override
  public boolean supports(JsonNode valueToBeUpdated, JsonNode updatedValue) {
    return true; // Fallback
  }

  @Override
  public void merge(
      JsonNode mainNode,
      JsonNode valueToBeUpdated,
      JsonNode updatedValue,
      String updatedFieldName) {
    if (mainNode instanceof ObjectNode) {
      ((ObjectNode) mainNode).replace(updatedFieldName, updatedValue);
    }
  }
}
