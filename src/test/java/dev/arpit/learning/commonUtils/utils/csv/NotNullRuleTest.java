package dev.arpit.learning.commonUtils.utils.csv;

import static org.junit.jupiter.api.Assertions.*;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotNullRuleTest {
  private NotNullRule notNullRule;

  @BeforeEach
  public void setUp() {
    notNullRule = new NotNullRule();
  }

  @Test
  public void test_withValidColumnNameAndValidValue_shouldDoesValidationProperly() {
    // arrange
    String columnName = "name";
    String value = "John";

    // act & assert
    try {
      notNullRule.validate(columnName, value);
    } catch (CSVFieldRuleException e) {
      assertNull(e);
    }
  }

  @Test
  public void test_withValidColumnNameAndEmptyValue_shouldDoesValidationProperly() {
    // arrange
    String columnName = "name";
    String value = "";

    // act & assert
    try {
      notNullRule.validate(columnName, value);
    } catch (CSVFieldRuleException e) {
      assertNull(e);
    }
  }

  @Test
  public void test_withNullColumnNameAndValidValue_shouldThrowNPE() {
    // arrange
    String columnName = null;
    String value = "John";

    // act & assert
    assertThrows(NullPointerException.class, () -> notNullRule.validate(columnName, value));
  }

  @Test
  public void test_withValidColumnNameAndNullValue_shouldThrowValidationError() {
    // arrange
    String columnName = "name";
    String value = null;

    // act & assert
    assertThrows(CSVFieldRuleException.class, () -> notNullRule.validate(columnName, value));
  }

  @Test
  public void test_withValidColumnNameAndNullStringValue_shouldThrowValidationError() {
    // act
    String columnName = "name";
    String value = "null";

    // act & assert
    assertThrows(CSVFieldRuleException.class, () -> notNullRule.validate(columnName, value));
  }
}
