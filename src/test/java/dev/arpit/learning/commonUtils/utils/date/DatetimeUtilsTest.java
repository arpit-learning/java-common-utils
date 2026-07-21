package dev.arpit.learning.commonUtils.utils.date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import dev.arpit.learning.commonUtils.models.Pair;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class DatetimeUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new DatetimeUtils());
  }

  @Test
  void test_getCurrentTimestamp() {
    // act
    LocalDateTime now = DatetimeUtils.getCurrentTimestamp();

    // assert
    assertNotNull(now);
  }

  @Test
  void
      test_getFormattedTimestamp_withValidLocalDateTimeAndValidFormat_shouldReturnFormattedString() {
    // arrange
    LocalDateTime now = LocalDateTime.now();
    String format = "yyyy-MM-dd";

    // act
    String formatted = DatetimeUtils.getFormattedTimestamp(now, format);

    // assert
    assertNotNull(formatted);
    assertEquals(format.length(), formatted.length());
  }

  @Test
  void test_getFormattedTimestamp_withNullLocalDateTimeAndValidFormat_shouldThrowNPE() {
    // arrange
    LocalDateTime now = null;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        NullPointerException.class, () -> DatetimeUtils.getFormattedTimestamp(now, format));
  }

  @Test
  void
      test_getFormattedTimestamp_withValidLocalDateTimeAndEmptyFormat_shouldReturnFormattedString() {
    // arrange
    LocalDateTime now = LocalDateTime.now();
    String format = "";

    // act
    String formatted = DatetimeUtils.getFormattedTimestamp(now, format);

    // assert
    assertNotNull(formatted);
    assertEquals(format.length(), formatted.length());
  }

  @Test
  void test_getFormattedTimestamp_withValidLocalDateTimeAndNullFormat_shouldThrowNPE() {
    // arrange
    LocalDateTime now = LocalDateTime.now();
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> DatetimeUtils.getFormattedTimestamp(now, format));
  }

  @Test
  public void test_getFormattedCurrentTimestamp_withDefaultFormat_shouldReturnFormattedTimestamp() {
    // act
    String formattedTimestamp = DatetimeUtils.getFormattedCurrentTimestamp();

    // assert
    assertNotNull(formattedTimestamp);
    assertFalse(formattedTimestamp.isEmpty());
  }

  @Test
  public void test_getFormattedCurrentTimestamp_withValidFormat_shouldReturnFormattedTimestamp() {
    // arrange
    String format = "yyyy-MM-dd_HH:mm:ss.SSS";

    // act
    String formattedTimestamp = DatetimeUtils.getFormattedCurrentTimestamp(format);

    // assert
    assertNotNull(formattedTimestamp);
    assertEquals(format.length(), formattedTimestamp.length());
  }

  @Test
  public void test_getFormattedCurrentTimestamp_withNullFormat_shouldThrowNPE() {
    // arrange
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> DatetimeUtils.getFormattedCurrentTimestamp(format));
  }

  @Test
  public void test_getCurrentTimestampInEpoch_shouldGetCurrentTimestampInEpoch() {
    // act
    long epochTimestamp = DatetimeUtils.getCurrentTimestampInEpoch();

    // assert
    assertTrue(epochTimestamp > 0);
  }

  @Test
  public void
      test_convertStringToLocalDateTime_withValidDateTimeStringAndValidFormat_shouldReturnProperLocalDateTime() {
    // arrange
    String dateTimeString = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act
    LocalDateTime localDateTime =
        DatetimeUtils.convertStringToLocalDateTime(dateTimeString, format);

    // assert
    assertNotNull(localDateTime);
  }

  @Test
  public void
      test_convertStringToLocalDateTime_withValidDateTimeStringAndInvalidFormat_shouldReturnNull() {
    // arrange
    String dateTimeString = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd HH:mm:ss";

    // act
    LocalDateTime localDateTime =
        DatetimeUtils.convertStringToLocalDateTime(dateTimeString, format);

    // assert
    assertNull(localDateTime);
  }

  @Test
  public void
      test_convertStringToLocalDateTime_withNullDateTimeStringAndInvalidFormat_shouldThrowNPE() {
    // arrange
    String dateTimeString = null;
    String format = "yyyy-MM-dd HH:mm:ss";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.convertStringToLocalDateTime(dateTimeString, format));
  }

  @Test
  public void
      test_convertStringToLocalDateTime_withValidDateTimeStringAndNullFormat_shouldThrowNPE() {
    // arrange
    String dateTimeString = "2023-01-01T12:00:00";
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.convertStringToLocalDateTime(dateTimeString, format));
  }

  @Test
  void test_validateDatetimeWithFormat_withValidDatetimeAndValidFormat_shouldReturnTrue() {
    // arrange
    String datetime = "2020-01-01";
    String format = "yyyy-MM-dd";

    // act
    boolean isValid = DatetimeUtils.validateDatetimeWithFormat(datetime, format);

    // assert
    assertTrue(isValid);
  }

  @Test
  void test_validateDatetimeWithFormat_withInvalidDatetimeAndValidFormat_shouldReturnFalse() {
    // arrange
    String datetime = "invalid";
    String format = "yyyy-MM-dd";

    // act
    boolean isValid = DatetimeUtils.validateDatetimeWithFormat(datetime, format);

    // assert
    assertFalse(isValid);
  }

  @Test
  void test_validateDatetimeWithFormat_withValidDatetimeAndInvalidFormat_shouldReturnFalse() {
    // arrange
    String datetime = "2020-11-01";
    String format = "invalid";

    // act
    boolean isValid = DatetimeUtils.validateDatetimeWithFormat(datetime, format);

    // assert
    assertFalse(isValid);
  }

  @Test
  void test_validateDatetimeWithFormat_withNullDatetimeAndValidFormat_shouldThrowNPE() {
    // arrange
    String datetime = null;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.validateDatetimeWithFormat(datetime, format));
  }

  @Test
  void test_validateDatetimeWithFormat_withValidDatetimeAndNullFormat_shouldThrowNPE() {
    // arrange
    String datetime = "2020-01-01";
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.validateDatetimeWithFormat(datetime, format));
  }

  @Test
  public void test_daysBetweenDateTimes_withValidBothDateTimes_shouldReturnExactDays() {
    // arrange
    LocalDateTime dateTime1 = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    LocalDateTime dateTime2 = LocalDateTime.of(2023, 1, 2, 12, 0, 0);

    // act
    long daysBetween = DatetimeUtils.daysBetweenDateTimes(dateTime1, dateTime2);

    // assert
    assertEquals(1, daysBetween);
  }

  @Test
  public void
      test_daysBetweenDateTimes_withFirstDateTimeGreaterThanSecondDateTime_shouldThrowIllegalArgumentException() {
    // arrange
    LocalDateTime dateTime1 = LocalDateTime.of(2023, 1, 30, 12, 0, 0);
    LocalDateTime dateTime2 = LocalDateTime.of(2023, 1, 2, 12, 0, 0);

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.daysBetweenDateTimes(dateTime1, dateTime2));
  }

  @Test
  public void
      test_daysBetweenDateTimes_withNullFirstDateTimeAndValidSecondDateTime_shouldThrowNPE() {
    // arrange
    LocalDateTime dateTime1 = null;
    LocalDateTime dateTime2 = LocalDateTime.of(2023, 1, 2, 12, 0, 0);

    // act & assert
    assertThrows(
        NullPointerException.class, () -> DatetimeUtils.daysBetweenDateTimes(dateTime1, dateTime2));
  }

  @Test
  public void
      test_daysBetweenDateTimes_withValidFirstDateTimeAndNullSecondDateTime_shouldThrowNPE() {
    // arrange
    LocalDateTime dateTime1 = LocalDateTime.of(2023, 1, 2, 12, 0, 0);
    LocalDateTime dateTime2 = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> DatetimeUtils.daysBetweenDateTimes(dateTime1, dateTime2));
  }

  @Test
  public void
      test_addDaysToDateTime_withValidLocalDateTimeAndValidDays_shouldAddProperNewLocalDateTime() {
    // arrange
    LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    int daysToAdd = 5;

    // act
    LocalDateTime newDateTime = DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd);

    // assert
    assertEquals(LocalDateTime.of(2023, 1, 6, 12, 0, 0), newDateTime);
  }

  @Test
  public void
      test_addDaysToDateTime_withValidLocalDateTimeAndInvalidDays_shouldThrowIllegalArgumentException() {
    // arrange
    LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    int daysToAdd = -5;

    // act & assert
    assertThrows(
        IllegalArgumentException.class, () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd));
  }

  @Test
  public void test_addDaysToDateTime_withNullLocalDateTimeAndValidDays_shouldThrowNPE() {
    // arrange
    LocalDateTime dateTime = null;
    int daysToAdd = 5;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd));
  }

  @Test
  public void
      test_addDaysToDateTime_withValidLocalDateTimeAndZeroDays_shouldReturnSameLocalDateTime() {
    // arrange
    LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    int daysToAdd = 0;

    // act
    LocalDateTime newDateTime = DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd);

    // assert
    assertEquals(dateTime, newDateTime);
  }

  @Test
  public void
      test_addDaysToDateTime_withValidStringTimeAndValidFormatAndValidDays_shouldReturnProperLocalDateTime() {
    // arrange
    String dateTime = "2023-01-01T12:00:00";
    int daysToAdd = 5;
    String format = "yyyy-MM-dd'T'HH:mm:ss";
    String expectedDateTime = "2023-01-06T12:00:00";

    // act
    String newDateTime = DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format);

    // assert
    assertEquals(expectedDateTime, newDateTime);
  }

  @Test
  public void
      test_addDaysToDateTime_withValidStringTimeAndValidFormatAndNegativeDays_shouldThrowIllegalArgumentException() {
    // arrange
    String dateTime = "2023-01-01T12:00:00";
    int daysToAdd = -5;
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format));
  }

  @Test
  public void
      test_addDaysToDateTime_withValidStringTimeAndValidFormatAndZeroDays_shouldReturnSameLocalDateTime() {
    // arrange
    String dateTime = "2023-01-01T12:00:00";
    int daysToAdd = 0;
    String format = "yyyy-MM-dd'T'HH:mm:ss";
    String expectedDateTime = "2023-01-01T12:00:00";

    // act
    String newDateTime = DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format);

    // assert
    assertEquals(expectedDateTime, newDateTime);
  }

  @Test
  public void
      test_addDaysToDateTime_withValidStringTimeAndInvalidFormatAndZeroDays_shouldThrowIllegalArguementException() {
    // arrange
    String dateTime = "2023-01-01T12:00:00";
    int daysToAdd = 0;
    String format = "invalid";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format));
  }

  @Test
  public void
      test_addDaysToDateTime_withInvalidStringTimeAndValidFormatAndZeroDays_shouldThrowDateTimeParseException() {
    // arrange
    String dateTime = "invalid";
    int daysToAdd = 0;
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        DateTimeParseException.class,
        () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format));
  }

  @Test
  public void test_addDaysToDateTime_withNullStringTimeAndValidFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String dateTime = null;
    int daysToAdd = 0;
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format));
  }

  @Test
  public void test_addDaysToDateTime_withValidStringTimeAndNullFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String dateTime = "2023-01-01T12:00:00";
    int daysToAdd = 0;
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.addDaysToDatetime(dateTime, daysToAdd, format));
  }

  @Test
  public void
      test_compareDateTimeRange_withValidFromTimeLessThanValidToTimeAndValidFormatAndZeroDays_shouldReturnValidResult() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = "2023-01-02T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";
    int expectedResult = -1;

    // act
    int result = DatetimeUtils.compareDatetimeRange(fromDateTime, toDateTime, format);

    // assert
    assertEquals(expectedResult, result);
  }

  @Test
  public void
      test_compareDateTimeRange_withValidFromTimeEqualsToValidToTimeAndValidFormatAndZeroDays_shouldReturnValidResult() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";
    int expectedResult = 0;

    // act
    int result = DatetimeUtils.compareDatetimeRange(fromDateTime, toDateTime, format);

    // assert
    assertEquals(expectedResult, result);
  }

  @Test
  public void
      test_compareDateTimeRange_withValidFromTimeGreaterThanValidToTimeAndValidFormatAndZeroDays_shouldReturnValidResult() {
    // arrange
    String fromDateTime = "2023-01-02T12:00:00";
    String toDateTime = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";
    int expectedResult = 1;

    // act
    int result = DatetimeUtils.compareDatetimeRange(fromDateTime, toDateTime, format);

    // assert
    assertEquals(expectedResult, result);
  }

  @Test
  public void
      test_compareDateTimeRange_withNullFromTimeAndValidToTimeAndValidFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String fromDateTime = null;
    String toDateTime = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.compareDatetimeRange(fromDateTime, toDateTime, format));
  }

  @Test
  public void
      test_compareDateTimeRange_withValidFromTimeAndNullToTimeAndValidFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = null;
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.compareDatetimeRange(fromDateTime, toDateTime, format));
  }

  @Test
  public void
      test_compareDateTimeRange_withValidFromTimeAndValidToTimeAndNullFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = "2023-01-01T12:00:00";
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.compareDatetimeRange(fromDateTime, toDateTime, format));
  }

  @Test
  public void
      test_validateDateTimeRange_withValidFromTimeLessThanValidToTimeAndValidFormatAndZeroDays_shouldReturnValidResult() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = "2023-01-02T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act
    boolean result = DatetimeUtils.validateDatetimeRange(fromDateTime, toDateTime, format);

    // assert
    assertTrue(result);
  }

  @Test
  public void
      test_validateDateTimeRange_withValidFromTimeEqualsToValidToTimeAndValidFormatAndZeroDays_shouldReturnValidResult() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act
    boolean result = DatetimeUtils.validateDatetimeRange(fromDateTime, toDateTime, format);

    // assert
    assertFalse(result);
  }

  @Test
  public void
      test_validateDateTimeRange_withValidFromTimeGreaterThanValidToTimeAndValidFormatAndZeroDays_shouldReturnValidResult() {
    // arrange
    String fromDateTime = "2023-01-02T12:00:00";
    String toDateTime = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act
    boolean result = DatetimeUtils.validateDatetimeRange(fromDateTime, toDateTime, format);

    // assert
    assertFalse(result);
  }

  @Test
  public void
      test_validateDateTimeRange_withNullFromTimeAndValidToTimeAndValidFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String fromDateTime = null;
    String toDateTime = "2023-01-01T12:00:00";
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.validateDatetimeRange(fromDateTime, toDateTime, format));
  }

  @Test
  public void
      test_validateDateTimeRange_withValidFromTimeAndNullToTimeAndValidFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = null;
    String format = "yyyy-MM-dd'T'HH:mm:ss";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.validateDatetimeRange(fromDateTime, toDateTime, format));
  }

  @Test
  public void
      test_validateDateTimeRange_withValidFromTimeAndValidToTimeAndNullFormatAndZeroDays_shouldThrowNPE() {
    // arrange
    String fromDateTime = "2023-01-01T12:00:00";
    String toDateTime = "2023-01-01T12:00:00";
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.validateDatetimeRange(fromDateTime, toDateTime, format));
  }

  @Test
  public void test_getCurrentWeekNumberOfYear_shouldReturnProperWeekNumber() {
    // arrange
    LocalDate date = LocalDate.now();
    int expected = date.get(WeekFields.ISO.weekOfWeekBasedYear());

    // act
    int result = DatetimeUtils.getCurrentWeekNumberOfYear();

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_getCurrentWeekNumberOfMonth_shouldReturnProperWeekNumber() {
    // arrange
    LocalDate date = LocalDate.now();
    int expected = date.get(WeekFields.ISO.weekOfMonth());

    // act
    int result = DatetimeUtils.getCurrentWeekNumberOfMonth();

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_getWeekNumberOfYear_withValidDate_shouldReturnProperWeekNumber() {
    // arrange
    LocalDate date = LocalDate.of(2023, 1, 1); // Sunday
    int expected = date.get(WeekFields.ISO.weekOfWeekBasedYear());

    // act
    Map<String, Object> result = DatetimeUtils.getWeekNumberOfYear(date);

    // assert
    assertEquals(expected, result.get("weekNumber"));
  }

  @Test
  public void
      test_getWeekNumberOfYear_withValidDateAndToDateIsAfterCurrentDate_shouldReturnProperWeekNumber() {
    // arrange
    LocalDate date = LocalDate.now(); // Sunday
    int expected = date.get(WeekFields.ISO.weekOfWeekBasedYear());

    // act
    Map<String, Object> result = DatetimeUtils.getWeekNumberOfYear(date);

    // assert
    assertEquals(expected, result.get("weekNumber"));
  }

  @Test
  public void test_getWeekNumberOfYear_withNullDateAnd_shouldThrowNPE() {
    // arrange
    LocalDate date = null; // Sunday

    // act & assert
    assertThrows(NullPointerException.class, () -> DatetimeUtils.getWeekNumberOfYear(date));
  }

  @Test
  public void test_getWeekNumberOfYear_withDateTimeParseException_shouldBeCaughtAndLogged() {
    // arrange
    LocalDate date = LocalDate.now();
    try (MockedStatic<DatetimeUtils> mockedDatetimeUtils =
        mockStatic(DatetimeUtils.class, Mockito.CALLS_REAL_METHODS)) {

      // Mock getDateRangeForGivenWeekNumberOfYear to return an invalid date string
      mockedDatetimeUtils
          .when(
              () ->
                  DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(
                      anyInt(), anyInt(), anyString()))
          .thenReturn(new Pair<>("2023-01-01", "invalid-date"));

      // act & assert
      assertDoesNotThrow(() -> DatetimeUtils.getWeekNumberOfYear(date));
    }
  }

  @Test
  public void test_getWeekNumberOfMonth_withValidDate_shouldReturnProperWeekNumber() {
    // arrange
    LocalDate date = LocalDate.of(2023, 1, 1); // Sunday
    int expected = date.get(WeekFields.ISO.weekOfMonth());

    // act
    int result = DatetimeUtils.getWeekNumberOfMonth(date);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_getWeekNumberOfMonth_withNullDate_shouldThrowNPE() {
    // arrange
    LocalDate date = null; // Sunday

    // act & assert
    assertThrows(NullPointerException.class, () -> DatetimeUtils.getWeekNumberOfMonth(date));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withValidWeekNumberAndValidYearAndValidFormat_shouldReturnProperDateRange() {
    // arrange
    int weekNumber = 1;
    int year = 2023;
    String format = "yyyy-MM-dd";
    String expectedStartDate = "2023-01-02";
    String expectedEndDate = "2023-01-08";

    // act
    Pair<String, String> result =
        DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year, format);

    // assert
    assertNotNull(result);
    assertEquals(expectedStartDate, result.first());
    assertEquals(expectedEndDate, result.second());
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withInvalidWeekNumberAndValidYearAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    int weekNumber = -1;
    int weekNumber2 = 54;
    int year = 2023;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year, format));
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber2, year, format));
  }

  @Test
  public void test_getDateRangeForGivenWeekNumberOfYear_withNullFormat_shouldThrowNPE() {
    // arrange
    int weekNumber = -1;
    int year = 2023;
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year, format));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withValidWeekNumberAndInvalidYearAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    int weekNumber = 1;
    int year = -2023;
    int year2 = 2123;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year, format));
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year2, format));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withZeroWeekNumberAndValidYearAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    int weekNumber = 0;
    int year = 2023;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year, format));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withValidWeekNumberAndZeroYearAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    int weekNumber = 1;
    int year = 0;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, year, format));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withValidWeekNumberAndValidFormat_shouldReturnValidRange() {
    // arrange
    int weekNumber = 10;
    String format = "yyyy-MM-dd";
    String expectedStartDate = "2026-03-02";
    String expectedEndDate = "2026-03-08";

    // act
    Pair<String, String> result =
        DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, format);

    // assert
    assertNotNull(result);
    assertEquals(expectedStartDate, result.first());
    assertEquals(expectedEndDate, result.second());
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withNegativeWeekNumberAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    int weekNumber = -1;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, format));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withLargeWeekNumberAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    int weekNumber = 54;
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, format));
  }

  @Test
  public void
      test_getDateRangeForGivenWeekNumberOfYear_withValidWeekNumberAndNullFormat_shouldThrowNPE() {
    // arrange
    int weekNumber = 1;
    String format = null;

    // act
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(weekNumber, format));
  }

  @Test
  public void
      test_getDateRangeFromStartOfYearTillGivenDate_withValidDateAndValidFormat_shouldReturnProperRange() {
    // arrange
    LocalDate date = LocalDate.of(2023, 11, 1); // Sunday
    String format = "yyyy-MM-dd";
    String expectedStartDate = "2023-01-01";
    String expectedEndDate = "2023-11-01";

    // act
    Pair<String, String> result =
        DatetimeUtils.getDateRangeFromStartOfYearTillGivenDate(date, format);

    // assert
    assertNotNull(result);
    assertEquals(expectedStartDate, result.first());
    assertEquals(expectedEndDate, result.second());
  }

  @Test
  public void
      test_getDateRangeFromStartOfYearTillGivenDate_withNullDateAndValidFormat_shouldThrowNPE() {
    // arrange
    LocalDate date = null; // Sunday
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeFromStartOfYearTillGivenDate(date, format));
  }

  @Test
  public void
      test_getDateRangeFromStartOfYearTillGivenDate_withValidDateAndNullFormat_shouldThrowNPE() {
    // arrange
    LocalDate date = LocalDate.of(2023, 11, 1); // Sunday
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeFromStartOfYearTillGivenDate(date, format));
  }

  @Test
  public void
      test_getDateRangeFromStartOfMonthTillGivenDate_withValidDateAndValidFormat_shouldReturnProperRange() {
    // arrange
    LocalDate date = LocalDate.of(2023, 11, 5); // Sunday
    String format = "yyyy-MM-dd";
    String expectedStartDate = "2023-11-01";
    String expectedEndDate = "2023-11-05";

    // act
    Pair<String, String> result =
        DatetimeUtils.getDateRangeFromStartOfMonthTillGivenDate(date, format);

    // assert
    assertNotNull(result);
    assertEquals(expectedStartDate, result.first());
    assertEquals(expectedEndDate, result.second());
  }

  @Test
  public void
      test_getDateRangeFromStartOfMonthTillGivenDate_withNullDateAndValidFormat_shouldThrowNPE() {
    // arrange
    LocalDate date = null; // Sunday
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeFromStartOfMonthTillGivenDate(date, format));
  }

  @Test
  public void
      test_getDateRangeFromStartOfMonthTillGivenDate_withValidDateAndNullFormat_shouldThrowNPE() {
    // arrange
    LocalDate date = LocalDate.of(2023, 11, 5); // Sunday
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeFromStartOfMonthTillGivenDate(date, format));
  }

  @Test
  public void
      test_getDateRangeFromStartOfWeekTillGivenDate_withValidDateAndValidFormat_shouldReturnProperRange() {
    // arrange
    LocalDate date = LocalDate.of(2023, 11, 5); // Sunday
    String format = "yyyy-MM-dd";
    String expectedStartDate = "2023-10-30";
    String expectedEndDate = "2023-11-05";

    // act
    Pair<String, String> result =
        DatetimeUtils.getDateRangeFromStartOfWeekTillGivenDate(date, format);

    // assert
    assertNotNull(result);
    assertEquals(expectedStartDate, result.first());
    assertEquals(expectedEndDate, result.second());
  }

  @Test
  public void
      test_getDateRangeFromStartOfWeekTillGivenDate_withNullDateAndValidFormat_shouldThrowNPE() {
    // arrange
    LocalDate date = null; // Sunday
    String format = "yyyy-MM-dd";

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeFromStartOfWeekTillGivenDate(date, format));
  }

  @Test
  public void
      test_getDateRangeFromStartOfWeekTillGivenDate_withValidDateAndNullFormat_shouldThrowNPE() {
    // arrange
    LocalDate date = LocalDate.of(2023, 11, 5); // Sunday
    String format = null;

    // act & assert
    assertThrows(
        NullPointerException.class,
        () -> DatetimeUtils.getDateRangeFromStartOfWeekTillGivenDate(date, format));
  }

  @Test
  public void test_getTodayWithZeroTime_shouldReturnProperLocalDateTime() {
    // arrange
    LocalDateTime expected =
        LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

    // act
    LocalDateTime result = DatetimeUtils.getTodayWithZeroTime();

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_isWeekend_withWeekdayDate_shouldReturnFalse() {
    // arrange
    LocalDate date = LocalDate.of(2023, 1, 3); // Sunday

    // act & assert
    assertFalse(DatetimeUtils.isWeekend(date));
  }

  @Test
  void test_isWeekend_withWeekendDate_shouldReturnTrue() {
    // arrange
    LocalDate date = LocalDate.of(2023, 1, 1); // Sunday
    LocalDate date2 = LocalDate.of(2022, 12, 31); // Saturday

    // act & assert
    assertTrue(DatetimeUtils.isWeekend(date));
    assertTrue(DatetimeUtils.isWeekend(date2));
  }

  @Test
  void test_isWeekend_withNullDate_shouldThrowNPE() {
    // arrange
    LocalDate date = null;

    // act & assert
    assertThrows(NullPointerException.class, () -> DatetimeUtils.isWeekend(date));
  }

  @Test
  void test_getEndOfDay_withValidDate_shouldReturnProperDateTime() {
    // arrange
    LocalDate date = LocalDate.of(2023, 1, 1);
    LocalDateTime expected = LocalDateTime.of(2023, 1, 1, 23, 59, 59, 999999999);

    // act
    LocalDateTime result = DatetimeUtils.getEndOfDay(date);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_getEndOfDay_withNullDate_shouldThrowNPE() {
    // arrange
    LocalDate date = null;

    // act
    assertThrows(NullPointerException.class, () -> DatetimeUtils.getEndOfDay(date));
  }
}
