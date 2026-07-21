package dev.arpit.learning.commonUtils.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Test;

class ObjectNodeMergerTest {
  private final ObjectMapper mapper = new ObjectMapper();
  private final ObjectNodeMerger merger = new ObjectNodeMerger();

  @Test
  void test_supports() {
    ObjectNode objectNode1 = mapper.createObjectNode();
    ObjectNode objectNode2 = mapper.createObjectNode();
    ArrayNode arrayNode = mapper.createArrayNode();
    TextNode textNode = new TextNode("test");

    // Valid case: valueToBeUpdated is ObjectNode, updatedValue is not ArrayNode (it's ObjectNode)
    assertTrue(merger.supports(objectNode1, objectNode2));

    // Valid case: valueToBeUpdated is ObjectNode, updatedValue is not ArrayNode (it's TextNode)
    assertTrue(merger.supports(objectNode1, textNode));

    // valueToBeUpdated is null
    assertFalse(merger.supports(null, objectNode2));

    // valueToBeUpdated is not ObjectNode
    assertFalse(merger.supports(arrayNode, objectNode2));

    // updatedValue is ArrayNode
    assertFalse(merger.supports(objectNode1, arrayNode));
  }

  @Test
  void test_merge() {
    ObjectNode valueToBeUpdated = mapper.createObjectNode();
    valueToBeUpdated.put("key1", "val1");

    ObjectNode updatedValue = mapper.createObjectNode();
    updatedValue.put("key2", "val2");
    updatedValue.put("key1", "newVal1"); // Should overwrite

    ObjectNode dummyMain = mapper.createObjectNode();

    merger.merge(dummyMain, valueToBeUpdated, updatedValue, "dummyField");

    assertEquals("newVal1", valueToBeUpdated.get("key1").asText());
    assertEquals("val2", valueToBeUpdated.get("key2").asText());
  }
}
