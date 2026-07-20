package dev.arpit.learning.commonUtils.utils.csv;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import javax.annotation.Nullable;
import lombok.NonNull;

public class NotNullRule implements ICSVFieldRule {
  @Override
  public void validate(@NonNull String columnName, @Nullable String value)
      throws CSVFieldRuleException {
    if (value == null) {
      throw new CSVFieldRuleException("Value is null for field: " + columnName);
    }

    if (value.equalsIgnoreCase("NULL")) {
      throw new CSVFieldRuleException("Null inside string is not allowed for field: " + columnName);
    }
  }
}
