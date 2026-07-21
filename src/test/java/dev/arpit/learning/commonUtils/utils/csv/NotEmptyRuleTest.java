package dev.arpit.learning.commonUtils.utils.csv;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.commonUtils.utils.core.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class NotEmptyRuleTest {
  private NotEmptyRule notEmptyRule;

  @BeforeEach
  public void setUp() {
    notEmptyRule = new NotEmptyRule();
  }

  @Test
  public void test_withValidColumnNameAndValidValue_shouldDoesValidationProperly() {
    // arrange
    String columnName = "name";
    String value = "John";

    // act & assert
    try {
      notEmptyRule.validate(columnName, value);
    } catch (CSVFieldRuleException e) {
      assertNull(e);
    }
  }

  @Test
  public void test_withValidColumnNameAndEmptyValue_shouldThrowValidationError() {
    // arrange
    String columnName = "name";
    String value = "";

    // act & assert
    assertThrows(CSVFieldRuleException.class, () -> notEmptyRule.validate(columnName, value));
  }

  @Test
  public void test_withNullColumnNameAndValidValue_shouldThrowNPE() {
    // arrange
    String columnName = null;
    String value = "John";

    // act & assert
    assertThrows(NullPointerException.class, () -> notEmptyRule.validate(columnName, value));
  }

  @Test
  public void test_withValidColumnNameAndNullValue_shouldThrowValidationError() {
    // arrange
    String columnName = "name";
    String value = null;

    // act & assert
    assertThrows(CSVFieldRuleException.class, () -> notEmptyRule.validate(columnName, value));
  }

  @Test
  public void
      test_withValidColumnNameAndNullValueAndMockedValidationUtilsToThrowEmptyValidatorNotFoundException_shouldThrowValidationError() {
    // act
    String columnName = "name";
    String value = "valid";

    // act & assert
    try (MockedStatic<ValidationUtils> mockedValidationUtils = mockStatic(ValidationUtils.class)) {
      mockedValidationUtils
          .when(() -> ValidationUtils.isNullOrEmpty(any(Pair.class)))
          .thenThrow(EmptyValidatorNotFoundException.class);
      assertThrows(CSVFieldRuleException.class, () -> notEmptyRule.validate(columnName, value));
    }
  }
}
