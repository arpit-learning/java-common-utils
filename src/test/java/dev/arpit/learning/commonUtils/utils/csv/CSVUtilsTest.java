package dev.arpit.learning.commonUtils.utils.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CSVUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new CSVUtils());
  }

  @Test
  public void test_isNotEmptyRow_withValidRow_shouldReturnTrue() {
    // arrange
    String[] row = {"a", "b"};

    // act
    boolean result = CSVUtils.isNotEmptyRow(row);

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNotEmptyRow_withValidRowWithEmptyItems_shouldReturnFalse() {
    // arrange
    String[] row = {"", ""};

    // act
    boolean result = CSVUtils.isNotEmptyRow(row);

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNotEmptyRow_withEmptyRow_shouldReturnFalse() {
    // arrange
    String[] row = {};

    // act
    boolean result = CSVUtils.isNotEmptyRow(row);

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNotEmptyRow_withNullRow_shouldThrowNPE() {
    // arrange
    String[] row = null;

    // act
    assertThrows(NullPointerException.class, () -> CSVUtils.isNotEmptyRow(row));
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndValidLinkedHashMapListAndWithoutAppend_shouldGenerateTheCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    List<LinkedHashMap<String, String>> flatJson = new ArrayList<>();
    LinkedHashMap<String, String> row1 = new LinkedHashMap<>();
    row1.put("a", "1");
    row1.put("b", "2");
    flatJson.add(row1);
    LinkedHashMap<String, String> row2 = new LinkedHashMap<>();
    row2.put("b", "3");
    row2.put("c", "4");
    row2.put("a", "5");
    flatJson.add(row2);

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, flatJson, false);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(3, lines.size(), "Should have 1 header row + 2 data rows");
    assertTrue(lines.getFirst().contains("a"), "Header missing 'a'");
    assertTrue(lines.getFirst().contains("b"), "Header missing 'b'");
    assertTrue(lines.getFirst().contains("c"), "Header missing 'c'");
    assertTrue(lines.getFirst().contains("a,b,c"), "Header should be ordered as 'a,b,c'");
    assertTrue(lines.get(1).contains("1,2,"), "Row 1 should have '1,2,'");
    assertTrue(lines.get(2).contains("5,3,4"), "Row 2 should have '5,3,4'");
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndValidLinkedHashMapListAndWithAppend_shouldGenerateTheCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    List<LinkedHashMap<String, String>> flatJson = new ArrayList<>();
    LinkedHashMap<String, String> row1 = new LinkedHashMap<>();
    row1.put("a", "1");
    row1.put("b", "2");
    flatJson.add(row1);
    LinkedHashMap<String, String> row2 = new LinkedHashMap<>();
    row2.put("b", "3");
    row2.put("c", "4");
    row2.put("a", "5");
    flatJson.add(row2);

    List<LinkedHashMap<String, String>> flatJson2 = new ArrayList<>();
    LinkedHashMap<String, String> row3 = new LinkedHashMap<>();
    row3.put("a", "10");
    row3.put("b", "12");
    flatJson2.add(row3);
    LinkedHashMap<String, String> row4 = new LinkedHashMap<>();
    row4.put("b", "13");
    row4.put("c", "14");
    row4.put("a", "15");
    flatJson2.add(row4);

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, flatJson, true);
    CSVUtils.generateCSV(tempFilePath, flatJson2, true);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(5, lines.size(), "Should have 1 header row + 4 data rows");
    assertTrue(lines.getFirst().contains("a"), "Header missing 'a'");
    assertTrue(lines.getFirst().contains("b"), "Header missing 'b'");
    assertTrue(lines.getFirst().contains("c"), "Header missing 'c'");
    assertTrue(lines.getFirst().contains("a,b,c"), "Header should be ordered as 'a,b,c'");
    assertTrue(lines.get(1).contains("1,2,"), "Row 1 should have '1,2,'");
    assertTrue(lines.get(2).contains("5,3,4"), "Row 2 should have '5,3,4'");
    assertTrue(lines.get(3).contains("10,12,"), "Row 3 should have '10,12,'");
    assertTrue(lines.get(4).contains("15,13,14"), "Row 4 should have '15,13,14'");
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndEmptyLinkedHashMapListAndWithoutAppend_shouldGenerateTheCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    List<LinkedHashMap<String, String>> flatJson = new ArrayList<>();
    LinkedHashMap<String, String> row1 = new LinkedHashMap<>();
    flatJson.add(row1);
    LinkedHashMap<String, String> row2 = new LinkedHashMap<>();
    flatJson.add(row2);

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, flatJson, false);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(0, lines.size(), "Should have no row");
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndEmptyLinkedHashMapListAndWithAppend_shouldGenerateTheCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    List<LinkedHashMap<String, String>> flatJson = new ArrayList<>();
    LinkedHashMap<String, String> row1 = new LinkedHashMap<>();
    flatJson.add(row1);
    LinkedHashMap<String, String> row2 = new LinkedHashMap<>();
    flatJson.add(row2);

    List<LinkedHashMap<String, String>> flatJson2 = new ArrayList<>();
    LinkedHashMap<String, String> row3 = new LinkedHashMap<>();
    flatJson2.add(row3);
    LinkedHashMap<String, String> row4 = new LinkedHashMap<>();
    flatJson2.add(row4);

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, flatJson, true);
    CSVUtils.generateCSV(tempFilePath, flatJson2, true);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(0, lines.size(), "Should have no row");
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndNullLinkedHashMapListAndWithoutAppend_shouldThrowNPE(
          @TempDir Path tempDir) throws IOException {
    // arrange
    List<LinkedHashMap<String, String>> flatJson = null;

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVUtils.generateCSV(tempFilePath, flatJson, false));
  }

  @Test
  public void
      test_generateCSVWithNullFilePathAndValidLinkedHashMapListAndWithoutAppend_shouldThrowNPE(
          @TempDir Path tempDir) throws IOException {
    // arrange
    List<LinkedHashMap<String, String>> flatJson = new ArrayList<>();
    LinkedHashMap<String, String> row1 = new LinkedHashMap<>();
    flatJson.add(row1);
    LinkedHashMap<String, String> row2 = new LinkedHashMap<>();
    flatJson.add(row2);

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVUtils.generateCSV(tempFilePath, flatJson, false));
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndValidHeadersArrayAndValidRowArraysListAndWithoutAppend_shouldGenerateCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    String[] headers = new String[] {"a", "b"};
    List<String[]> rows = new ArrayList<>();
    rows.add(new String[] {"1", "2"});
    rows.add(new String[] {"3", "4", "5"});

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, headers, rows, false);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(3, lines.size(), "Should have 1 header row + 2 data rows");
    assertTrue(lines.getFirst().contains("a"), "Header missing 'a'");
    assertTrue(lines.getFirst().contains("b"), "Header missing 'b'");
    assertTrue(lines.getFirst().contains("a,b"), "Header should be ordered as 'a,b'");
    assertTrue(lines.get(1).contains("1,2"), "Row 1 should have '1,2'");
    assertTrue(lines.get(2).contains("3,4"), "Row 2 should have '3,4,5'");
  }

  @Test
  public void
      test_generateCSVWithNullFilePathAndValidHeadersArrayAndValidRowArraysListAndWithoutAppend_shouldThrowNPE(
          @TempDir Path tempDir) throws IOException {
    // arrange
    String[] headers = new String[] {"a", "b"};
    List<String[]> rows = new ArrayList<>();
    rows.add(new String[] {"1", "2"});
    rows.add(new String[] {"3", "4", "5"});

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVUtils.generateCSV(tempFilePath, headers, rows, false));
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndEmptyHeadersArrayAndValidRowArraysListAndWithoutAppend_shouldGenerateCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    String[] headers = new String[] {};
    List<String[]> rows = new ArrayList<>();
    rows.add(new String[] {"1", "2"});
    rows.add(new String[] {"3", "4", "5"});

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, headers, rows, false);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(0, lines.size(), "Should have no rows");
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndValidHeadersArrayAndEmptyRowArraysListAndWithoutAppend_shouldGenerateCSV(
          @TempDir Path tempDir) throws IOException {
    // arrange
    String[] headers = new String[] {"a", "b"};
    List<String[]> rows = new ArrayList<>();

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act
    CSVUtils.generateCSV(tempFilePath, headers, rows, false);

    // assert
    assertTrue(Files.exists(tempFile));
    List<String> lines = Files.readAllLines(tempFile);
    assertEquals(1, lines.size(), "Should have 1 header rows");
    assertTrue(lines.getFirst().contains("a"), "Header missing 'a'");
    assertTrue(lines.getFirst().contains("b"), "Header missing 'b'");
    assertTrue(lines.getFirst().contains("a,b"), "Header should be ordered as 'a,b'");
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndNullHeadersArrayAndValidRowArraysListAndWithoutAppend_shouldThrowNPE(
          @TempDir Path tempDir) throws IOException {
    // arrange
    String[] headers = null;
    List<String[]> rows = new ArrayList<>();
    rows.add(new String[] {"1", "2"});
    rows.add(new String[] {"3", "4", "5"});

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVUtils.generateCSV(tempFilePath, headers, rows, false));
  }

  @Test
  public void
      test_generateCSVWithValidFilePathAndValidHeadersArrayAndNullRowArraysListAndWithoutAppend_shouldThrowNPE(
          @TempDir Path tempDir) throws IOException {
    // arrange
    String[] headers = new String[] {"a", "b"};
    List<String[]> rows = null;

    Path tempFile = tempDir.resolve("temp.csv");
    String tempFilePath = tempFile.toAbsolutePath().toString();

    // act & assert
    assertThrows(
        NullPointerException.class, () -> CSVUtils.generateCSV(tempFilePath, headers, rows, false));
  }

  @Test
  public void test_generateCSV_withInvalidPath_shouldCatchExceptionAndLog(
      @org.junit.jupiter.api.io.TempDir java.nio.file.Path tempDir) {
    // arrange
    String[] headers = {"a"};
    List<String[]> data = new ArrayList<>();
    data.add(new String[] {"1"});

    // Provide a directory path instead of a file path to force a FileOutputStream Exception (Is a
    // directory)
    String invalidFilePath = tempDir.toAbsolutePath().toString();

    // act & assert
    // The exception is caught and logged internally, so it should not be thrown to the caller
    assertDoesNotThrow(() -> CSVUtils.generateCSV(invalidFilePath, headers, data, false));
  }
}
