package dev.arpit.learning.commonUtils.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonPathUtilsTest {

  @Test
  void test_read() {
    java.util.Map<String, Object> jsonMap = new java.util.HashMap<>();
    jsonMap.put("name", "Arpit");
    jsonMap.put("age", 30);

    String name = JsonPathUtils.read(jsonMap, "$.name");
    Integer age = JsonPathUtils.read(jsonMap, "$.age");

    assertEquals("Arpit", name);
    assertEquals(30, age);
  }

  @Test
  void test_objectCreation() {
    JsonPathUtils utils = new JsonPathUtils();
    assertNotNull(utils);
  }
}
