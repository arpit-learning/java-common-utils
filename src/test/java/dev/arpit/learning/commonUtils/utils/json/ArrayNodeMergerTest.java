package dev.arpit.learning.commonUtils.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

class ArrayNodeMergerTest {
  private final ObjectMapper mapper = new ObjectMapper();
  private final ArrayNodeMerger merger = new ArrayNodeMerger();

  @Test
  void test_supports() {
    ArrayNode arrayNode1 = mapper.createArrayNode();
    ArrayNode arrayNode2 = mapper.createArrayNode();
    ObjectNode objectNode = mapper.createObjectNode();

    // Both are ArrayNode
    assertTrue(merger.supports(arrayNode1, arrayNode2));

    // valueToBeUpdated is null
    assertFalse(merger.supports(null, arrayNode2));

    // valueToBeUpdated is not ArrayNode
    assertFalse(merger.supports(objectNode, arrayNode2));

    // updatedValue is not ArrayNode
    assertFalse(merger.supports(arrayNode1, objectNode));
  }

  @Test
  void test_merge_updatedValueIsLarger() {
    ArrayNode mainArray = mapper.createArrayNode();
    ObjectNode child1 = mapper.createObjectNode();
    child1.put("k1", "v1");
    mainArray.add(child1); // size 1

    ArrayNode updateArray = mapper.createArrayNode();
    ObjectNode updateChild1 = mapper.createObjectNode();
    updateChild1.put("k1", "v2"); // Should merge into child1

    ObjectNode updateChild2 = mapper.createObjectNode();
    updateChild2.put("k2", "v3"); // Should be added directly

    updateArray.add(updateChild1);
    updateArray.add(updateChild2); // size 2

    ObjectNode mainNode = mapper.createObjectNode(); // Just a dummy main node

    merger.merge(mainNode, mainArray, updateArray, "dummyField");

    assertEquals(2, mainArray.size());
    assertEquals("v2", mainArray.get(0).get("k1").asText());
    assertEquals("v3", mainArray.get(1).get("k2").asText());
  }

  @Test
  void test_merge_updatedValueIsSmaller() {
    ArrayNode mainArray = mapper.createArrayNode();
    ObjectNode child1 = mapper.createObjectNode();
    child1.put("k1", "v1");
    ObjectNode child2 = mapper.createObjectNode();
    child2.put("k2", "v2");

    mainArray.add(child1);
    mainArray.add(child2); // size 2

    ArrayNode updateArray = mapper.createArrayNode();
    ObjectNode updateChild1 = mapper.createObjectNode();
    updateChild1.put("k1", "v3");

    updateArray.add(updateChild1); // size 1

    ObjectNode mainNode = mapper.createObjectNode();

    merger.merge(mainNode, mainArray, updateArray, "dummyField");

    assertEquals(2, mainArray.size()); // Size remains 2
    assertEquals("v3", mainArray.get(0).get("k1").asText());
    assertEquals("v2", mainArray.get(1).get("k2").asText()); // Unchanged
  }
}
