package dev.arpit.learning.commonUtils.utils.csv;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import dev.arpit.learning.commonUtils.utils.core.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

public class CSVValidator {
  @Data
  @AllArgsConstructor
  public static class ColumnSchema {
    private @NonNull String columnName;
    private @NonNull List<ICSVFieldRule> rules;
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

    if (row.length != columnSchemas.size()) {
      return "Row: "
          + rowValue
          + ", No of columns found: "
          + row.length
          + " and Expected no of columns: "
          + columnSchemas.size();
    }

    StringBuilder resultBuilder = new StringBuilder();
    int i = 0;
    for (ColumnSchema columnSchema : columnSchemas) {
      String entry = row[i++].trim();
      for (ICSVFieldRule rule : columnSchema.getRules()) {
        String error = null;
        try {
          rule.validate(columnSchema.getColumnName(), entry);
        } catch (CSVFieldRuleException e) {
          error = e.getMessage();
        }
        if (StringUtils.isNotNullOrEmpty(error)) {
          resultBuilder.append(error).append("; ");
        }
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
