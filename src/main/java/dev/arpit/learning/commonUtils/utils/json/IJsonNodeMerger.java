package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface IJsonNodeMerger {
  boolean supports(JsonNode valueToBeUpdated, JsonNode updatedValue);

  void merge(
      JsonNode mainNode, JsonNode valueToBeUpdated, JsonNode updatedValue, String updatedFieldName);
}
