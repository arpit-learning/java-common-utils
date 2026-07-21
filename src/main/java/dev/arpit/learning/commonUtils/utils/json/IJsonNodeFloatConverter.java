package dev.arpit.learning.commonUtils.utils.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface IJsonNodeFloatConverter {
  boolean supports(JsonNode node);

  float convert(JsonNode node);
}
