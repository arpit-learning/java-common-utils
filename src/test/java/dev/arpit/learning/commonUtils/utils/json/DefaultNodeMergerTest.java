package dev.arpit.learning.commonUtils.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Test;

class DefaultNodeMergerTest {
  private final ObjectMapper mapper = new ObjectMapper();
  private final DefaultNodeMerger merger = new DefaultNodeMerger();

  @Test
  void test_supports() {
    assertTrue(merger.supports(null, null));
    assertTrue(merger.supports(mapper.createObjectNode(), mapper.createArrayNode()));
  }

  @Test
  void test_merge_withObjectNode() {
    ObjectNode mainNode = mapper.createObjectNode();
    mainNode.put("key", "oldVal");

    TextNode updatedValue = new TextNode("newVal");

    // valueToBeUpdated is oldVal, updatedValue is newVal
    merger.merge(mainNode, mainNode.get("key"), updatedValue, "key");

    assertEquals("newVal", mainNode.get("key").asText());
  }

  @Test
  void test_merge_withNonObjectNode() {
    ArrayNode mainNode = mapper.createArrayNode();
    mainNode.add("oldVal");

    TextNode updatedValue = new TextNode("newVal");

    // Should gracefully do nothing and not throw ClassCastException
    assertDoesNotThrow(() -> merger.merge(mainNode, mainNode.get(0), updatedValue, "0"));

    // The array remains unchanged because DefaultNodeMerger only replaces on ObjectNode
    assertEquals("oldVal", mainNode.get(0).asText());
  }
}
