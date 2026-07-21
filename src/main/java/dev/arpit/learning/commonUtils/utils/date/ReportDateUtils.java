package dev.arpit.learning.commonUtils.utils.date;

import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NonNull;

public class ReportDateUtils {
  private static final ILogger logger = LoggerFactory.getLogger(ReportDateUtils.class);

  public static @NonNull Map<String, Pair<String, String>> getDateRangeForLastSixWeeks(
      @NonNull LocalDate date, @NonNull String format) {
    int currentWeekNumber = (Integer) DatetimeUtils.getWeekNumberOfYear(date).get("weekNumber");
    Pair<String, String> dateRange =
        DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(
            currentWeekNumber, LocalDate.now().getYear(), format);
    Map<String, Pair<String, String>> results = new LinkedHashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

    try {
      String fromDate = dateRange.first();
      String toDate = dateRange.second();
      results.put(
          "wtd",
          new Pair<>(
              fromDate,
              LocalDate.parse(toDate, formatter).isAfter(date) ? date.format(formatter) : toDate));
    } catch (DateTimeParseException e) {
      logger.error(LogConstant.UNABLE_TO_CONVERT_STRING_TO_DATE_FORMAT, e);
    }

    LocalDate localDate = date;
    // get date range for last 6 weeks
    for (int i = 0; i < 6; i++) {
      localDate = localDate.minusDays(1);
      String to = localDate.format(formatter);
      localDate = localDate.minusDays(6);
      String from = localDate.format(formatter);
      int WeekNumber = (Integer) DatetimeUtils.getWeekNumberOfYear(localDate).get("weekNumber");
      results.put(String.valueOf(WeekNumber), new Pair<>(from, to));
    }

    return results;
  }

  public static @NonNull Map<String, Pair<String, String>> getDateRangeForLastSixMonths(
      @NonNull LocalDate date, @NonNull String format) {
    Map<String, Pair<String, String>> result = new LinkedHashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    LocalDate firstDayOfMonth = date.withDayOfMonth(1);
    result.put("ytd", new Pair<>(firstDayOfMonth.format(formatter), date.format(formatter)));

    for (int i = 0; i < 6; i++) {
      date = date.minusMonths(1);
      firstDayOfMonth = date.withDayOfMonth(1);
      LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
      result.put(
          getYearAndMonth(date),
          new Pair<>(firstDayOfMonth.format(formatter), lastDayOfMonth.format(formatter)));
    }

    return result;
  }

  public static @NonNull Pair<String, String> getFromAndToDateForLastSevenMonths(
      @NonNull LocalDate date, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String to = date.format(formatter);
    LocalDate fromLocalDate = date.minusMonths(6).withDayOfMonth(1);
    String from = fromLocalDate.format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull Pair<String, String> getFromAndToDateForLastSevenDays(
      @NonNull LocalDate date, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String to = date.format(formatter);
    String from = date.minusDays(6).format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull Pair<String, String> getFromAndToDateForLastSevenWeeks(
      @NonNull LocalDate date, @NonNull String format) {
    int currentWeekNumber = (Integer) DatetimeUtils.getWeekNumberOfYear(date).get("weekNumber");
    Pair<String, String> dateRange =
        DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(
            currentWeekNumber, LocalDate.now().getYear(), format);
    String toDate = dateRange.second();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String to = LocalDate.parse(toDate, formatter).isAfter(date) ? date.format(formatter) : toDate;
    String from = LocalDate.parse(dateRange.first(), formatter).minusDays(6 * 7).format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull Pair<String, String> getUptoDateMonthRange(
      @NonNull LocalDate date, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    LocalDate startMonth = date.withDayOfMonth(1);
    return new Pair<>(startMonth.format(formatter), date.format(formatter));
  }

  public static @NonNull Pair<String, String> getFromAndToDateForLast30Days(
      @NonNull LocalDate date, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String to = date.format(formatter);
    String from = date.minusDays(29).format(formatter);
    return new Pair<>(from, to);
  }

  private static @NonNull String getYearAndMonth(@NonNull LocalDate date) {
    int year = date.getYear();
    String month = getMonthFromLocalDate(date);
    return year + month;
  }

  private static @NonNull String getMonthFromLocalDate(@NonNull LocalDate date) {
    int month = date.getMonthValue();
    if (month < 10) {
      return "0" + month;
    }
    return String.valueOf(month);
  }
}
