package dev.arpit.learning.commonUtils.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.NonNull;

public class CSVValidator {
  @Data
  public static class ColumnSchema {
    private boolean isEmptyAllowed;
    private boolean isNullAllowed;
    private @NonNull String regex;
    private @NonNull String columnName;
  }

  public static String REGEX_BOOLEAN = "[0-1]";

  public static String REGEX_DECIMAL = "[0-9]";

  public static String REGEX_STRING_WITH_SPECIAL_CHARACTERS = "^[a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*$";

  public static @NonNull String validateCsvRow(
      @NonNull String[] row, @NonNull List<ColumnSchema> columnSchemas) {
    StringBuilder rowValueBuilder = new StringBuilder();

    for (String entry : row) {
      rowValueBuilder.append(entry).append(",");
    }
    rowValueBuilder.delete(rowValueBuilder.length() - 1, rowValueBuilder.length());
    String rowValue = rowValueBuilder.toString();

    if (!(row.length == columnSchemas.size() - 1 || row.length == columnSchemas.size())) {
      return "Row: "
          + rowValue
          + ", No of columns found: "
          + row.length
          + " and Expected columns: "
          + columnSchemas.size()
          + " or "
          + (columnSchemas.size() - 1);
    }

    StringBuilder resultBuilder = new StringBuilder();
    int i = 0;
    for (ColumnSchema columnSchema : columnSchemas) {
      String entry = row[i++].trim();
      if (!columnSchema.isEmptyAllowed && entry.trim().isEmpty()) {
        resultBuilder
            .append("Empty value is not allowed for field: ")
            .append(columnSchema.columnName)
            .append("; ");
      }
      if (!columnSchema.isNullAllowed && entry.equalsIgnoreCase("NULL")) {
        resultBuilder
            .append("Null is not allowed for field: ")
            .append(columnSchema.columnName)
            .append("; ");
      }

      Pattern pattern = Pattern.compile(columnSchema.regex);
      Matcher matcher = pattern.matcher(entry);

      if (!matcher.find()) {
        resultBuilder
            .append("Value: ")
            .append(entry)
            .append(" entered is not supported for field: ")
            .append(columnSchema.columnName)
            .append("; ");
      }
    }

    return resultBuilder.toString().trim();
  }

  public static @NonNull String validateCsvRows(
      @NonNull List<@NonNull String[]> rows, @NonNull List<ColumnSchema> columnSchemas) {
    StringBuilder resultBuilder = new StringBuilder();

    for (String[] row : rows) {
      resultBuilder.append(validateCsvRow(row, columnSchemas)).append("\n");
    }

    return resultBuilder.toString().trim();
  }
}
