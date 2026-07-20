package dev.arpit.learning.commonUtils.utils.csv;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import javax.annotation.Nullable;
import lombok.NonNull;

public interface ICSVFieldRule {
  void validate(@NonNull String columnName, @Nullable String value) throws CSVFieldRuleException;
}
