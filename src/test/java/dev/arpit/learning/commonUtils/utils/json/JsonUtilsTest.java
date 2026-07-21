package dev.arpit.learning.commonUtils.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {
  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void test_toJsonString_jsonNode() {
    ObjectNode node = mapper.createObjectNode();
    node.put("key", "val");
    String result = JsonUtils.toJsonString(node);
    assertTrue(result.contains("val"));
  }

  @Test
  void test_toJsonString_jsonNode_withNull() {
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonString((JsonNode) null));
  }

  @Test
  void test_toJsonString_object() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "val");
    String result = JsonUtils.toJsonString((Object) map);
    assertTrue(result.contains("val"));
  }

  @Test
  void test_toJsonString_object_withNull() {
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonString((Object) null));
  }

  @Test
  void test_toJsonString_jsonNode_JsonProcessingException() {
    class BadPojo {
      public String getBad() {
        throw new RuntimeException("bad");
      }
    }
    JsonNode pojoNode = mapper.getNodeFactory().pojoNode(new BadPojo());
    String result = JsonUtils.toJsonString(pojoNode);
    assertEquals("", result);
  }

  @Test
  void test_getFromJson() {
    String json = "{\"key\":\"val\"}";
    Map map = JsonUtils.getFromJson(json, Map.class);
    assertNotNull(map);
    assertEquals("val", map.get("key"));
  }

  @Test
  void test_getFromJson_invalidJson() {
    Map map = JsonUtils.getFromJson("invalid", Map.class);
    assertNull(map);
  }

  @Test
  void test_getFromJson_withNulls() {
    assertThrows(NullPointerException.class, () -> JsonUtils.getFromJson(null, Map.class));
    assertThrows(NullPointerException.class, () -> JsonUtils.getFromJson("{}", null));
  }

  @Test
  void test_getFromJsonWithException() throws IOException {
    String json = "{\"key\":\"val\"}";
    Map map = JsonUtils.getFromJsonWithException(json, Map.class);
    assertNotNull(map);
    assertEquals("val", map.get("key"));
  }

  @Test
  void test_getFromJsonWithException_invalidJson() {
    assertThrows(IOException.class, () -> JsonUtils.getFromJsonWithException("invalid", Map.class));
  }

  @Test
  void test_getFromJsonWithException_withNulls() {
    assertThrows(
        NullPointerException.class, () -> JsonUtils.getFromJsonWithException(null, Map.class));
    assertThrows(NullPointerException.class, () -> JsonUtils.getFromJsonWithException("{}", null));
  }

  @Test
  void test_getFromJsonNode() {
    ObjectNode node = mapper.createObjectNode();
    node.put("key", "val");
    Map map = JsonUtils.getFromJsonNode(node, Map.class);
    assertNotNull(map);
    assertEquals("val", map.get("key"));
  }

  @Test
  void test_getFromJsonNode_invalid() {
    // Map requires object node, pass array node to map class might fail or not, let's pass
    // something that fails conversion
    JsonNode node = mapper.createArrayNode();
    // treeToValue might just return empty map? Let's use a class with strict structure
    class Dummy {
      public String key;
    }
    Dummy d = JsonUtils.getFromJsonNode(node, Dummy.class);
    assertNull(d);
  }

  @Test
  void test_getFromJsonNode_withNulls() {
    assertThrows(NullPointerException.class, () -> JsonUtils.getFromJsonNode(null, Map.class));
    ObjectNode node = mapper.createObjectNode();
    assertThrows(NullPointerException.class, () -> JsonUtils.getFromJsonNode(node, null));
  }

  @Test
  void test_toJsonString_key_value_objMap() {
    // value is "[]" and objMap is "{}"
    assertEquals("", JsonUtils.toJsonString("k", "[]", "{}"));

    // value is "[]" and objMap has the key
    assertEquals("{}", JsonUtils.toJsonString("k", "[]", "{\"k\":\"v\"}"));

    // value is "[]" and objMap is invalid -> map becomes null -> creates new map
    assertEquals("{}", JsonUtils.toJsonString("k", "[]", "invalid"));

    // value is not "[]"
    assertEquals("{\"k\":\"v\"}", JsonUtils.toJsonString("k", "v", "{}"));

    // value is not "[]" and objMap is invalid
    assertEquals("{\"k\":\"v\"}", JsonUtils.toJsonString("k", "v", "invalid"));
  }

  @Test
  void test_toJsonString_key_value_objMap_withNulls() {
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonString(null, "v", "{}"));
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonString("k", null, "{}"));
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonString("k", "v", null));
  }

  @Test
  void test_convertObjectValueToClass() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "val");
    JsonNode node = JsonUtils.convertObjectValueToClass(map, JsonNode.class);
    assertTrue(node.has("key"));
  }

  @Test
  void test_convertObjectValueToClass_withNulls() {
    assertThrows(
        NullPointerException.class,
        () -> JsonUtils.convertObjectValueToClass(null, JsonNode.class));
    assertThrows(
        NullPointerException.class,
        () -> JsonUtils.convertObjectValueToClass(new HashMap<>(), null));
  }

  @Test
  void test_convertObjectValueToType() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "val");
    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
    Map<String, String> result = JsonUtils.convertObjectValueToType(map, typeRef);
    assertEquals("val", result.get("key"));
  }

  @Test
  void test_convertObjectValueToType_withNulls() {
    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
    assertThrows(
        NullPointerException.class, () -> JsonUtils.convertObjectValueToType(null, typeRef));
    assertThrows(
        NullPointerException.class,
        () -> JsonUtils.convertObjectValueToType(new HashMap<>(), null));
  }

  @Test
  void test_toJsonNode() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "val");
    JsonNode node = JsonUtils.toJsonNode(map);
    assertTrue(node.has("key"));
  }

  @Test
  void test_toJsonNode_withNull() {
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonNode(null));
  }

  @Test
  void test_toJsonObject() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "val");
    JsonNode node = JsonUtils.toJsonObject(map);
    assertTrue(node instanceof ObjectNode);
    assertTrue(node.has("key"));
  }

  @Test
  void test_toJsonObject_withNull() {
    assertThrows(NullPointerException.class, () -> JsonUtils.toJsonObject(null));
  }

  @Test
  void test_toArrayNode() {
    List<String> list = Arrays.asList("a", "b");
    ArrayNode node = JsonUtils.toArrayNode(list);
    assertEquals(2, node.size());
  }

  @Test
  void test_toArrayNode_withNull() {
    assertThrows(NullPointerException.class, () -> JsonUtils.toArrayNode(null));
  }

  @Test
  void test_readValueToType() throws IOException {
    String json = "{\"key\":\"val\"}";
    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
    Map<String, String> map = JsonUtils.readValueToType(json, typeRef);
    assertEquals("val", map.get("key"));
  }

  @Test
  void test_readValueToType_withNulls() {
    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
    assertThrows(NullPointerException.class, () -> JsonUtils.readValueToType(null, typeRef));
    assertThrows(NullPointerException.class, () -> JsonUtils.readValueToType("{}", null));
  }

  @Test
  void test_objectCreation() {
    JsonUtils utils = new JsonUtils();
    assertNotNull(utils);
  }
}
