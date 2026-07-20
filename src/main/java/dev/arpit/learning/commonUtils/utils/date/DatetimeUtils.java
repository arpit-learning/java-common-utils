package dev.arpit.learning.commonUtils.utils.date;

import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.commonUtils.constants.LogConstantFields;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class DatetimeUtils {
  private static final ILogger logger = LoggerFactory.getLogger(DatetimeUtils.class);

  public static @NonNull LocalDateTime getCurrentTimestamp() {
    return LocalDateTime.now();
  }

  public static @NonNull String getFormattedTimestamp(
      @NonNull LocalDateTime datetime, @NonNull String datetimeFormat) {
    return DateTimeFormatter.ofPattern(datetimeFormat).format(datetime);
  }

  public static @NonNull String getFormattedCurrentTimestamp() {
    return getFormattedTimestamp(getCurrentTimestamp(), LogConstant.TIMESTAMP_FORMAT.getMessage());
  }

  public static @NonNull String getFormattedCurrentTimestamp(@NonNull String datetimeFormat) {
    return getFormattedTimestamp(getCurrentTimestamp(), datetimeFormat);
  }

  public static long getCurrentTimestampInEpoch() {
    return Instant.now().toEpochMilli();
  }

  public static LocalDateTime convertStringToLocalDateTime(
      @NonNull String datetime, @NonNull String datetimeFormat) {
    try {
      DateTimeFormatter format = DateTimeFormatter.ofPattern(datetimeFormat);
      return format.parse(datetime, LocalDateTime::from);
    } catch (DateTimeParseException e) {
      logger.error(LogConstant.UNABLE_TO_CONVERT_STRING_TO_DATE_FORMAT, e);
      return null;
    }
  }

  public static boolean validateDatetimeWithFormat(
      @NonNull String datetime, @NonNull String datetimeFormat) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimeFormat);
    try {
      LocalDate.parse(datetime, formatter);
      logger.debug(LogConstant.VALID_DATE_FORMAT_FOUND, LogConstantFields.DATE, datetime);
    } catch (DateTimeParseException e) {
      logger.error(LogConstant.INVALID_DATE_ERROR, e);
      return false;
    }

    return true;
  }

  public static long daysBetweenDateTimes(
      @NonNull LocalDateTime firstDatetime, @NonNull LocalDateTime secondDatetime) {
    return ChronoUnit.DAYS.between(
        firstDatetime.toInstant(ZoneOffset.UTC), secondDatetime.toInstant(ZoneOffset.UTC));
  }

  public static @NonNull String addDaysToDatetime(
      @NonNull String datetime, long days, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.parse(datetime).plusDays(days).format(formatter);
  }

  public static int compareDatetimeRange(
      @NonNull String fromDatetime, @NonNull String toDatetime, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    LocalDateTime from = LocalDateTime.parse(fromDatetime, formatter);
    LocalDateTime to = LocalDateTime.parse(toDatetime, formatter);
    return from.compareTo(to);
  }

  public static boolean validateDatetimeRange(
      @NonNull String fromDatetime, @NonNull String toDatetime, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    LocalDateTime from = LocalDateTime.parse(fromDatetime, formatter);
    LocalDateTime to = LocalDateTime.parse(toDatetime, formatter);
    return from.isBefore(to);
  }

  public static int getCurrentWeekNumberOfYear() {
    LocalDate now = LocalDate.now();
    return now.get(WeekFields.ISO.weekOfWeekBasedYear());
  }

  public static int getCurrentWeekNumberOfMonth() {
    LocalDate now = LocalDate.now();
    return now.get(WeekFields.ISO.weekOfMonth());
  }

  public static @NonNull Map<String, Object> getWeekNumberOfYearForGivenDate(
      @NonNull LocalDate date) {
    Map<String, Object> data = new HashMap<>();

    LocalDate currentDate = LocalDate.now();
    int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    String format = "yyyy-MM-dd";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    Pair<String, String> dateRange =
        getDateRangeForGivenWeekNumberOfYear(weekNumber, date.getYear(), format);

    data.put("weekNumber", weekNumber);
    try {
      if (currentDate.isAfter(LocalDate.parse(dateRange.second(), formatter))) {
        data.put("completedWeek", true);
      } else {
        data.put("completedWeek", false);
      }
    } catch (DateTimeParseException e) {
      logger.error(LogConstant.UNABLE_TO_CONVERT_STRING_TO_DATE_FORMAT, e);
    }

    return data;
  }

  public static int getWeekNumOfMonthForGivenDate(@NonNull LocalDate date) {
    return date.get(WeekFields.ISO.weekOfMonth());
  }

  public static @NonNull Pair<String, String> getDateRangeForGivenWeekNumberOfYear(
      @NonNull Integer yearWeekNumber, @NonNull Integer year, @NonNull String format) {
    LocalDate firstDayOfWeek =
        LocalDate.of(year, Month.JANUARY, 1)
            .with(WeekFields.ISO.weekOfWeekBasedYear(), yearWeekNumber)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String from = firstDayOfWeek.format(formatter);
    String to = lastDayOfWeek.format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull Pair<String, String> getDateRangeForGivenWeekNumberOfYear(
      @NonNull Integer yearWeekNumber, @NonNull String format) {
    return getDateRangeForGivenWeekNumberOfYear(yearWeekNumber, LocalDate.now().getYear(), format);
  }

  public static @NonNull Pair<String, String> getDateRangeFromStartOfYearTillGivenDate(
      @NonNull LocalDate date, @NonNull String format) {
    LocalDate firstDayOfYear = LocalDate.of(date.getYear(), Month.JANUARY, 1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String from = firstDayOfYear.format(formatter);
    String to = date.format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull Pair<String, String> getDateRangeFromStartOfMonthTillGivenDate(
      @NonNull LocalDate date, @NonNull String format) {
    LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    String from = firstDayOfMonth.format(formatter);
    String to = date.format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull Pair<String, String> getDateRangeFromStartOfWeekTillGivenDate(
      @NonNull LocalDate date, @NonNull String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    LocalDate firstDayOfWeek = date.with(WeekFields.ISO.getFirstDayOfWeek());
    String from = firstDayOfWeek.format(formatter);
    String to = date.format(formatter);
    return new Pair<>(from, to);
  }

  public static @NonNull LocalDateTime getTodayWithZeroTime() {
    return LocalDate.now().atStartOfDay();
  }

  public static boolean isWeekend(@NonNull LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
  }

  public static @NonNull LocalDateTime getEndOfDay(@NonNull LocalDate date) {
    return date.atTime(LocalTime.MAX);
  }
}
