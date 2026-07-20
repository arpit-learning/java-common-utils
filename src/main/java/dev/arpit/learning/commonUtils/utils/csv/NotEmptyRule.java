package dev.arpit.learning.commonUtils.utils.csv;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.commonUtils.utils.core.ValidationUtils;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.springframework.core.ResolvableType;

public class NotEmptyRule implements ICSVFieldRule {
  @Override
  public void validate(@NonNull String columnName, @Nullable String value)
      throws CSVFieldRuleException {
    if (value == null) {
      throw new CSVFieldRuleException("Value is null for field: " + columnName);
    }

    try {
      if (ValidationUtils.isNullOrEmpty(new Pair<>(value, ResolvableType.forClass(String.class)))) {
        throw new CSVFieldRuleException("Value is empty for field: " + columnName);
      }
    } catch (EmptyValidatorNotFoundException e) {
      throw new CSVFieldRuleException(e.getMessage());
    }
  }
}
