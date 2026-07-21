package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RegexUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new RegexUtils());
  }

  @Test
  public void test_match_withMatchingString_shouldReturnTrue() {
    assertTrue(RegexUtils.match("[a-z]+", "abc"));
  }

  @Test
  public void test_match_withNotMatchingString_shouldReturnFalse() {
    assertFalse(RegexUtils.match("[a-z]+", "123"));
  }

  @Test
  public void test_match_withNullString_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> RegexUtils.match("[a-z]+", null));
  }

  @Test
  public void test_match_withNullRegex_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> RegexUtils.match(null, "123"));
  }

  @Test
  public void test_contains_withMatchingString_shouldReturnTrue() {
    assertTrue(RegexUtils.contains("[a-z]+", "123abc123"));
  }

  @Test
  public void test_contains_withNotMatchingString_shouldReturnFalse() {
    assertFalse(RegexUtils.contains("^[a-z]+$", "123"));
  }

  @Test
  public void test_contains_withNullString_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> RegexUtils.contains("^[a-z]+$", null));
  }

  @Test
  public void test_contains_withNullRegex_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> RegexUtils.contains(null, "123"));
  }

  @Test
  public void test_startsWith_withMatchingString_shouldReturnTrue() {
    assertTrue(RegexUtils.startsWith("hel", "hello"));
  }

  @Test
  public void test_startsWith_withNotMatchingString_shouldReturnFalse() {
    assertFalse(RegexUtils.startsWith("llo", "hello"));
  }

  @Test
  public void test_startsWith_withNotMatchingString_shouldReturnFalse_case2() {
    assertFalse(RegexUtils.startsWith("llo", "abc"));
  }

  @Test
  public void test_startsWith_withNullString_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> RegexUtils.startsWith("llo", null));
  }

  @Test
  public void test_startsWith_withNullRegex_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> RegexUtils.startsWith(null, "abc"));
  }
}
