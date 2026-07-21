package dev.arpit.learning.commonUtils.utils.json;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.commonUtils.utils.core.ValidationUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class JsonNodeUtilsTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void test_registerMerger() {
    IJsonNodeMerger mockMerger =
        new IJsonNodeMerger() {
          @Override
          public boolean supports(JsonNode mainNode, JsonNode updateNode) {
            return false;
          }

          @Override
          public void merge(
              JsonNode mainNode,
              JsonNode valueToBeUpdated,
              JsonNode updatedValue,
              String updatedFieldName) {}
        };
    assertDoesNotThrow(() -> JsonNodeUtils.registerMerger(mockMerger));
  }

  @Test
  void test_registerFloatConverter() {
    IJsonNodeFloatConverter mockConverter =
        new IJsonNodeFloatConverter() {
          @Override
          public boolean supports(JsonNode node) {
            return false;
          }

          @Override
          public float convert(JsonNode node) {
            return 0;
          }
        };
    assertDoesNotThrow(() -> JsonNodeUtils.registerFloatConverter(mockConverter));
  }

  @Test
  void test_merge_withObjectNodes_hitsObjectMerger() {
    ObjectNode mainNode = mapper.createObjectNode();
    ObjectNode updateNode = mapper.createObjectNode();

    ObjectNode childMain = mapper.createObjectNode();
    ObjectNode childUpdate = mapper.createObjectNode();
    childUpdate.put("key", "val");

    mainNode.set("child", childMain);
    updateNode.set("child", childUpdate);

    JsonNode result = JsonNodeUtils.merge(mainNode, updateNode);
    assertTrue(result.get("child").has("key"));
  }

  @Test
  void test_merge_withTextNodes_hitsDefaultMerger() {
    ObjectNode mainNode = mapper.createObjectNode();
    ObjectNode updateNode = mapper.createObjectNode();

    mainNode.put("key", "mainVal");
    updateNode.put("key", "updateVal");

    JsonNode result = JsonNodeUtils.merge(mainNode, updateNode);
    assertEquals("updateVal", result.get("key").asText());
  }

  @Test
  void test_merge_withNull_throwsNPE() {
    assertThrows(
        NullPointerException.class, () -> JsonNodeUtils.merge(null, mapper.createObjectNode()));
    assertThrows(
        NullPointerException.class, () -> JsonNodeUtils.merge(mapper.createObjectNode(), null));
  }

  @Test
  void test_getFloat() {
    assertEquals(1.5f, JsonNodeUtils.getFloat(new FloatNode(1.5f)));
    assertEquals(2.0f, JsonNodeUtils.getFloat(new IntNode(2)));
    assertEquals(3.5f, JsonNodeUtils.getFloat(new TextNode("3.5")));
    assertEquals(0.0f, JsonNodeUtils.getFloat(BooleanNode.TRUE)); // fallback
  }

  @Test
  void test_getFloat_withNull_throwsNPE() {
    assertThrows(NullPointerException.class, () -> JsonNodeUtils.getFloat(null));
  }

  @Test
  void test_isValidFloatNode() {
    assertTrue(JsonNodeUtils.isValidFloatNode(new FloatNode(1.5f)));
    assertTrue(JsonNodeUtils.isValidFloatNode(new IntNode(2)));
    assertTrue(JsonNodeUtils.isValidFloatNode(new TextNode("3.5")));
    assertFalse(JsonNodeUtils.isValidFloatNode(BooleanNode.TRUE));
  }

  @Test
  void test_isValidFloatNode_withNull_throwsNPE() {
    assertThrows(NullPointerException.class, () -> JsonNodeUtils.isValidFloatNode(null));
  }

  @Test
  void test_getTextValue() {
    assertEquals("val", JsonNodeUtils.getTextValue(new TextNode("val")));
    assertNull(JsonNodeUtils.getTextValue(new IntNode(1))); // textValue() returns null for non-text
  }

  @Test
  void test_getTextValue_withNull_throwsNPE() {
    assertThrows(NullPointerException.class, () -> JsonNodeUtils.getTextValue(null));
  }

  @Test
  void test_hasJsonKeyAndNotNull() {
    ObjectNode node = mapper.createObjectNode();
    node.put("key1", "val");
    node.set("key2", NullNode.getInstance());

    assertTrue(JsonNodeUtils.hasJsonKeyAndNotNull(node, "key1"));
    assertFalse(JsonNodeUtils.hasJsonKeyAndNotNull(node, "key2"));
    assertFalse(JsonNodeUtils.hasJsonKeyAndNotNull(node, "key3"));
  }

  @Test
  void test_hasJsonKeyAndNotNull_withNull_throwsNPE() {
    assertThrows(
        NullPointerException.class, () -> JsonNodeUtils.hasJsonKeyAndNotNull(null, "key1"));
  }

  @Test
  void test_isTextNode() {
    assertTrue(JsonNodeUtils.isTextNode(new TextNode("val")));
    assertFalse(JsonNodeUtils.isTextNode(new IntNode(1)));
  }

  @Test
  void test_isTextNode_withNull_throwsNPE() {
    assertThrows(NullPointerException.class, () -> JsonNodeUtils.isTextNode(null));
  }

  @Test
  void test_isNotTextNode() {
    assertFalse(JsonNodeUtils.isNotTextNode(new TextNode("val")));
    assertTrue(JsonNodeUtils.isNotTextNode(new IntNode(1)));
  }

  @Test
  void test_isNotTextNode_withNull_throwsNPE() {
    assertThrows(NullPointerException.class, () -> JsonNodeUtils.isNotTextNode(null));
  }

  @Test
  void test_isEmpty() {
    assertTrue(JsonNodeUtils.isEmpty(null));
    assertTrue(JsonNodeUtils.isEmpty(NullNode.getInstance()));
    assertTrue(JsonNodeUtils.isEmpty(new IntNode(1))); // textValue is null
    assertTrue(JsonNodeUtils.isEmpty(new TextNode("")));
    assertTrue(JsonNodeUtils.isEmpty(new TextNode("   ")));
    assertFalse(JsonNodeUtils.isEmpty(new TextNode("val")));
  }

  @Test
  void test_isNotEmpty() {
    assertFalse(JsonNodeUtils.isNotEmpty(null));
    assertTrue(JsonNodeUtils.isNotEmpty(new TextNode("val")));
  }

  @Test
  void test_hasJsonKeyAndValueIsNotNullAndIsTextual() {
    ObjectNode node = mapper.createObjectNode();
    node.put("key1", "val");
    node.put("key2", 1);
    node.set("key3", NullNode.getInstance());

    assertTrue(JsonNodeUtils.hasJsonKeyAndValueIsNotNullAndIsTextual(node, "key1"));
    assertFalse(JsonNodeUtils.hasJsonKeyAndValueIsNotNullAndIsTextual(node, "key2"));
    assertFalse(JsonNodeUtils.hasJsonKeyAndValueIsNotNullAndIsTextual(node, "key3"));
    assertFalse(JsonNodeUtils.hasJsonKeyAndValueIsNotNullAndIsTextual(node, "missing"));
  }

  @Test
  void test_hasJsonKeyAndValueIsNotNullAndIsTextual_withNulls() {
    assertThrows(
        NullPointerException.class,
        () -> JsonNodeUtils.hasJsonKeyAndValueIsNotNullAndIsTextual(null, "key1"));
    ObjectNode node = mapper.createObjectNode();
    assertThrows(
        NullPointerException.class,
        () -> JsonNodeUtils.hasJsonKeyAndValueIsNotNullAndIsTextual(node, null));
  }

  @Test
  void test_hasJsonKeyAndValueIsNull() {
    ObjectNode node = mapper.createObjectNode();
    node.put("key1", "val");
    node.set("key2", NullNode.getInstance());

    assertFalse(JsonNodeUtils.hasJsonKeyAndValueIsNull(node, "key1"));
    assertTrue(JsonNodeUtils.hasJsonKeyAndValueIsNull(node, "key2"));
    assertFalse(JsonNodeUtils.hasJsonKeyAndValueIsNull(node, "missing"));
  }

  @Test
  void test_hasJsonKeyAndValueIsNull_withNulls() {
    assertThrows(
        NullPointerException.class, () -> JsonNodeUtils.hasJsonKeyAndValueIsNull(null, "key1"));
    ObjectNode node = mapper.createObjectNode();
    assertThrows(
        NullPointerException.class, () -> JsonNodeUtils.hasJsonKeyAndValueIsNull(node, null));
  }

  @Test
  void test_getNonEmptyStrings() {
    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add("val1");
    arrayNode.add(""); // Empty
    arrayNode.add("val2");
    arrayNode.add(1); // non-text, textValue is null

    List<String> result = JsonNodeUtils.getNonEmptyStrings(arrayNode);
    assertEquals(2, result.size());
    assertTrue(result.contains("val1"));
    assertTrue(result.contains("val2"));
  }

  @Test
  void test_getNonEmptyStrings_withNull_throwsNPE() {
    assertThrows(NullPointerException.class, () -> JsonNodeUtils.getNonEmptyStrings(null));
  }

  @Test
  void test_getNonEmptyStrings_exceptionInValidation() {
    try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
      mocked
          .when(() -> ValidationUtils.isNullOrEmpty(any(Pair.class)))
          .thenThrow(new EmptyValidatorNotFoundException("test exception"));

      ArrayNode arrayNode = mapper.createArrayNode();
      arrayNode.add("val1");

      List<String> result = JsonNodeUtils.getNonEmptyStrings(arrayNode);
      assertTrue(result.isEmpty()); // The loop continues but we catch and log
    }
  }

  @Test
  void test_objectCreation() {
    JsonNodeUtils utils = new JsonNodeUtils();
    assertNotNull(utils);
  }
}
