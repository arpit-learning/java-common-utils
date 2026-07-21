package dev.arpit.learning.commonUtils.utils.csv;

import static org.junit.jupiter.api.Assertions.*;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import org.junit.jupiter.api.Test;

class RegexRuleTest {
  private RegexRule regexRule;

  @Test
  public void
      test_validate_withValidRegexAndValidColumnNameAndValidValue_shouldDoValidationProperly() {
    // arrange
    String columnName = "name";
    String regex = "[a-zA-Z]+";
    String value = "John";

    // act & assert
    regexRule = new RegexRule(regex);
    try {
      regexRule.validate(columnName, value);
    } catch (CSVFieldRuleException e) {
      assertNull(e);
    }
  }

  @Test
  public void
      test_validate_withValidRegexAndValidColumnNameAndNullValue_shouldThrowValidationError() {
    // arrange
    String columnName = "name";
    String regex = "[a-zA-Z]+";
    String value = null;

    // act & assert
    regexRule = new RegexRule(regex);
    try {
      regexRule.validate(columnName, value);
    } catch (CSVFieldRuleException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void
      test_validate_withValidRegexAndValidColumnNameAndInvalidValue_shouldThrowValidationError() {
    // arrange
    String columnName = "name";
    String regex = "[a-zA-Z]+";
    String value = "John1";

    // act & assert
    regexRule = new RegexRule(regex);
    assertThrows(CSVFieldRuleException.class, () -> regexRule.validate(columnName, value));
  }

  @Test
  public void
      test_validate_withValidRegexAndValidColumnNameAndEmptyValue_shouldThrowValidationError() {
    // arrange
    String columnName = "name";
    String regex = "[a-zA-Z]+";
    String value = "";

    // act & assert
    regexRule = new RegexRule(regex);
    assertThrows(CSVFieldRuleException.class, () -> regexRule.validate(columnName, value));
  }

  @Test
  public void test_validate_withValidRegexAndNullColumnNameAndEmptyValue_shouldThrowNPE() {
    // arrange
    String columnName = null;
    String regex = "[a-zA-Z]+";
    String value = "John";

    // act & assert
    regexRule = new RegexRule(regex);
    assertThrows(NullPointerException.class, () -> regexRule.validate(columnName, value));
  }

  @Test
  public void test_objectCreation_withNullRegex_shouldThrowNPE() {
    // arrange
    String regex = null;

    // act & assert
    assertThrows(NullPointerException.class, () -> new RegexRule(regex));
  }
}
