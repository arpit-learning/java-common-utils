package dev.arpit.learning.commonUtils.utils.csv;

import dev.arpit.learning.commonUtils.exceptions.CSVFieldRuleException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import lombok.NonNull;

public class RegexRule implements ICSVFieldRule {
  private final String regex;
  private final Pattern pattern;

  public RegexRule(@NonNull String regex) {
    this.regex = regex;
    this.pattern = Pattern.compile(regex);
  }

  @Override
  public void validate(@NonNull String columnName, @Nullable String value)
      throws CSVFieldRuleException {
    if (value == null) {
      throw new CSVFieldRuleException("Value is null for field: " + columnName);
    }

    Matcher matcher = pattern.matcher(value);
    if (!matcher.matches()) {
      throw new CSVFieldRuleException(
          "Value: "
              + value
              + " entered is not supported for field: "
              + columnName
              + " as per regex: "
              + regex);
    }
  }
}
