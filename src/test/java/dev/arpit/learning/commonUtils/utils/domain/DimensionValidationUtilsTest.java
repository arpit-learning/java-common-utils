package dev.arpit.learning.commonUtils.utils.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.commonUtils.utils.core.ValidationUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class DimensionValidationUtilsTest {
  @Test
  void test_validateComplexDimensionAttribute_withNullComplexDimAttr_shouldReturnMapWithNull() {
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute(null, null, ",");
    assertNotNull(result);
    assertNull(result.get("complex_dim_attr"));
  }

  @Test
  void test_validateComplexDimensionAttribute_withEmptyComplexDimAttr_shouldReturnMapWithEmpty() {
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("", null, ",");
    assertNotNull(result);
    assertEquals("", result.get("complex_dim_attr"));
  }

  @Test
  void test_validateComplexDimensionAttribute_withExceptionAtFirstCheck_shouldCatchAndContinue() {
    try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
      mocked
          .when(() -> ValidationUtils.isNullOrEmpty(any(Pair.class)))
          .thenThrow(new EmptyValidatorNotFoundException("test"));

      Map<String, Object> result =
          DimensionValidationUtils.validateComplexDimensionAttribute("valid", null, ",");
      assertNotNull(result);
      assertEquals("valid", result.get("complex_dim_attr"));
    }
  }

  @Test
  void test_validateComplexDimensionAttribute_withCurComplexDimAttrEmpty_shouldContinue() {
    Set<String> catSet = new HashSet<>();
    catSet.add("valid1");
    catSet.add("valid2");
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("valid1, ,valid2", catSet, ",");
    assertNotNull(result);
    assertEquals("valid1,valid2", result.get("complex_dim_attr"));
  }

  @Test
  void test_validateComplexDimensionAttribute_withExceptionInLoopCheck_shouldCatchAndContinue() {
    try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
      // Return false for first check, throw for second
      mocked
          .when(() -> ValidationUtils.isNullOrEmpty(any(Pair.class)))
          .thenReturn(false)
          .thenThrow(new EmptyValidatorNotFoundException("test"));

      Set<String> catSet = new HashSet<>();
      catSet.add("valid");
      Map<String, Object> result =
          DimensionValidationUtils.validateComplexDimensionAttribute("valid", catSet, ",");
      assertNotNull(result);
      assertEquals("valid", result.get("complex_dim_attr"));
    }
  }

  @Test
  void
      test_validateComplexDimensionAttribute_withCategoryAttrSetNull_shouldNotAddCategoryErrorButMayProceed() {
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("valid", null, ",");
    assertNotNull(result);
    assertEquals("valid", result.get("complex_dim_attr"));
  }

  @Test
  void test_validateComplexDimensionAttribute_withCurComplexDimAttrInCatSet_shouldProceed() {
    Set<String> catSet = new HashSet<>();
    catSet.add("valid_attr");
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("valid attr", catSet, ",");
    assertNotNull(result);
    assertEquals("valid_attr", result.get("complex_dim_attr"));
  }

  @Test
  void
      test_validateComplexDimensionAttribute_withOrgComplexDimAttrInCatSet_shouldProceedAndReplace() {
    Set<String> catSet = new HashSet<>();
    catSet.add("valid attr");
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("valid attr", catSet, ",");
    assertNotNull(result);
    assertEquals("valid attr", result.get("complex_dim_attr"));
  }

  @Test
  @SuppressWarnings("unchecked")
  void test_validateComplexDimensionAttribute_withAttrNotInCatSet_shouldReturnError() {
    Set<String> catSet = new HashSet<>();
    catSet.add("other");
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("invalid", catSet, ",");
    assertNotNull(result);
    assertTrue(result.containsKey("error"));
    List<String> errors = (List<String>) result.get("error");
    assertTrue(errors.get(0).contains("is not part of category attribute value"));
  }

  @Test
  @SuppressWarnings("unchecked")
  void test_validateComplexDimensionAttribute_withDuplicateAttr_shouldReturnError() {
    Set<String> catSet = new HashSet<>();
    catSet.add("dup");
    Map<String, Object> result =
        DimensionValidationUtils.validateComplexDimensionAttribute("dup,dup", catSet, ",");
    assertNotNull(result);
    assertTrue(result.containsKey("error"));
    List<String> errors = (List<String>) result.get("error");
    assertTrue(errors.get(0).contains("Duplicate attribute is found"));
  }

  @Test
  void test_objectCreation() {
    DimensionValidationUtils utils = new DimensionValidationUtils();
    assertNotNull(utils);
  }
}
