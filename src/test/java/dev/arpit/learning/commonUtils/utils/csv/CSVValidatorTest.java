package dev.arpit.learning.commonUtils.utils.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CSVValidatorTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new CSVValidator());
  }

  @Test
  public void test_validateCsvRow_withValidRowAndValidColumnSchemasList_shouldReturnEmptyString() {
    // arrange
    String[] row = {"1"};
    List<CSVValidator.ColumnSchema> columnSchemas = new ArrayList<>();
    List<ICSVFieldRule> fieldRules = new ArrayList<>();
    fieldRules.add(new NotNullRule());
    fieldRules.add(new NotEmptyRule());
    columnSchemas.add(new CSVValidator.ColumnSchema("a", fieldRules));

    // act
    String result = CSVValidator.validateCsvRow(row, columnSchemas);

    // assert
    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  public void
      test_validateCsvRow_withUnequalNoOfRowEntriesAndColumnSchemasList_shouldReturnEmptyString() {
    // arrange
    String[] row = {"1", "2"};
    List<CSVValidator.ColumnSchema> columnSchemas = new ArrayList<>();
    List<ICSVFieldRule> fieldRules = new ArrayList<>();
    fieldRules.add(new NotNullRule());
    fieldRules.add(new NotEmptyRule());
    columnSchemas.add(new CSVValidator.ColumnSchema("a", fieldRules));

    // act
    String result = CSVValidator.validateCsvRow(row, columnSchemas);

    // assert
    assertNotNull(result);
    assertNotEquals("", result);
  }

  @Test
  public void
      test_validateCsvRow_withRowEntyViolatingCSVValidatorRuleAndColumnSchemasList_shouldReturnEmptyString() {
    // arrange
    String[] row = {""};
    List<CSVValidator.ColumnSchema> columnSchemas = new ArrayList<>();
    List<ICSVFieldRule> fieldRules = new ArrayList<>();
    fieldRules.add(new NotNullRule());
    fieldRules.add(new NotEmptyRule());
    columnSchemas.add(new CSVValidator.ColumnSchema("a", fieldRules));

    // act
    String result = CSVValidator.validateCsvRow(row, columnSchemas);

    // assert
    assertNotNull(result);
    assertNotEquals("", result);
  }

  @Test
  public void test_validateCsvRow_withNullRowAndValidColumnSchemasList_shouldThrowsNPE() {
    // arrange
    String[] row = null;
    List<CSVValidator.ColumnSchema> columnSchemas = new ArrayList<>();
    List<ICSVFieldRule> fieldRules = new ArrayList<>();
    fieldRules.add(new NotNullRule());
    fieldRules.add(new NotEmptyRule());
    columnSchemas.add(new CSVValidator.ColumnSchema("a", fieldRules));

    // act & assert
    assertThrows(NullPointerException.class, () -> CSVValidator.validateCsvRow(row, columnSchemas));
  }

  @Test
  public void test_validateCsvRow_withValidRowAndNullColumnSchemasList_shouldThrowsNPE() {
    // arrange
    String[] row = {"1"};
    List<CSVValidator.ColumnSchema> columnSchemas = null;

    // act & assert
    assertThrows(NullPointerException.class, () -> CSVValidator.validateCsvRow(row, columnSchemas));
  }

  @Test
  public void
      test_validateCsvRows_withValidRowsAndValidColumnSchemasList_shouldDoValidationProperly() {
    // arrange
    List<String[]> rows = new ArrayList<>();
    String[] row1 = {"1", "2"};
    String[] row2 = {"3", "4"};
    rows.add(row1);
    rows.add(row2);
    List<CSVValidator.ColumnSchema> columnSchemas = new ArrayList<>();
    List<ICSVFieldRule> fieldRules = new ArrayList<>();
    fieldRules.add(new NotNullRule());
    fieldRules.add(new NotEmptyRule());
    columnSchemas.add(new CSVValidator.ColumnSchema("a", List.copyOf(fieldRules)));
    columnSchemas.add(new CSVValidator.ColumnSchema("b", List.copyOf(fieldRules)));

    // act
    String result = CSVValidator.validateCsvRows(rows, columnSchemas);

    // assert
    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  public void test_validateCsvRows_withNullRowsAndValidColumnSchemasList_shouldThrowNPE() {
    // arrange
    List<String[]> rows = null;
    List<CSVValidator.ColumnSchema> columnSchemas = new ArrayList<>();
    List<ICSVFieldRule> fieldRules = new ArrayList<>();
    fieldRules.add(new NotNullRule());
    fieldRules.add(new NotEmptyRule());
    columnSchemas.add(new CSVValidator.ColumnSchema("a", List.copyOf(fieldRules)));
    columnSchemas.add(new CSVValidator.ColumnSchema("b", List.copyOf(fieldRules)));

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVValidator.validateCsvRows(rows, columnSchemas));
  }

  @Test
  public void test_validateCsvRows_withValidRowsAndNullColumnSchemasList_shouldThrowNPE() {
    // arrange
    List<String[]> rows = new ArrayList<>();
    String[] row1 = {"1", "2"};
    String[] row2 = {"3", "4"};
    rows.add(row1);
    rows.add(row2);
    List<CSVValidator.ColumnSchema> columnSchemas = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVValidator.validateCsvRows(rows, columnSchemas));
  }
}
