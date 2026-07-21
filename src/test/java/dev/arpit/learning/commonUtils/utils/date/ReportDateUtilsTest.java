package dev.arpit.learning.commonUtils.utils.date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import dev.arpit.learning.commonUtils.models.Pair;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ReportDateUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new ReportDateUtils());
  }

  @Test
  public void test_getDateRangeForLastSixWeeks_withValidDate_shouldReturnMap() {
    // arrange
    LocalDate date = LocalDate.of(2026, 6, 15); // Monday
    String format = "yyyy-MM-dd";

    // act
    Map<String, Pair<String, String>> result =
        ReportDateUtils.getDateRangeForLastSixWeeks(date, format);

    // assert
    assertNotNull(result);
    assertEquals(7, result.size());

    // wtd (Week to date). Week 25 of 2026 starts 2026-06-15.
    assertEquals("2026-06-15", result.get("wtd").first());
    assertEquals(
        "2026-06-15",
        result.get("wtd").second()); // because toDate (2026-06-21) is after date (2026-06-15)

    assertEquals("2026-06-08", result.get("24").first());
    assertEquals("2026-06-14", result.get("24").second());

    assertEquals("2026-06-01", result.get("23").first());
    assertEquals("2026-06-07", result.get("23").second());

    assertEquals("2026-05-25", result.get("22").first());
    assertEquals("2026-05-31", result.get("22").second());

    assertEquals("2026-05-18", result.get("21").first());
    assertEquals("2026-05-24", result.get("21").second());

    assertEquals("2026-05-11", result.get("20").first());
    assertEquals("2026-05-17", result.get("20").second());

    assertEquals("2026-05-04", result.get("19").first());
    assertEquals("2026-05-10", result.get("19").second());
  }

  @Test
  public void test_getDateRangeForLastSixWeeks_withDateAsSunday_shouldReturnMapWithToDateEqual() {
    // arrange
    LocalDate date = LocalDate.of(2026, 6, 21); // Sunday
    String format = "yyyy-MM-dd";

    // act
    Map<String, Pair<String, String>> result =
        ReportDateUtils.getDateRangeForLastSixWeeks(date, format);

    // assert
    assertNotNull(result);
    assertEquals(7, result.size());
    // wtd (Week to date). Week 25 of 2026 starts 2026-06-15.
    assertEquals("2026-06-15", result.get("wtd").first());
    assertEquals("2026-06-21", result.get("wtd").second());
  }

  @Test
  public void test_getDateRangeForLastSixWeeks_withDateTimeParseException_shouldBeCaught() {
    // arrange
    LocalDate date = LocalDate.of(2026, 6, 15);
    String format = "yyyy-MM-dd";

    try (MockedStatic<DatetimeUtils> mockedDatetimeUtils =
        org.mockito.Mockito.mockStatic(
            DatetimeUtils.class, org.mockito.Mockito.CALLS_REAL_METHODS)) {
      mockedDatetimeUtils
          .when(
              () ->
                  DatetimeUtils.getDateRangeForGivenWeekNumberOfYear(
                      anyInt(), anyInt(), anyString()))
          .thenReturn(new Pair<>("2026-06-12", "invalid-date"));

      // act & assert
      assertDoesNotThrow(() -> ReportDateUtils.getDateRangeForLastSixWeeks(date, format));
    }
  }

  @Test
  public void test_getDateRangeForLastSixMonths_withValidDate_shouldReturnMap() {
    // arrange
    LocalDate date = LocalDate.of(2026, 12, 1);
    String format = "yyyy-MM-dd";

    // act
    Map<String, Pair<String, String>> result =
        ReportDateUtils.getDateRangeForLastSixMonths(date, format);

    // assert
    assertNotNull(result);
    assertEquals(7, result.size());
    assertEquals("2026-12-01", result.get("ytd").first());
    assertEquals("2026-12-01", result.get("ytd").second());

    assertEquals("2026-11-01", result.get("202611").first());
    assertEquals("2026-11-30", result.get("202611").second());

    assertEquals("2026-10-01", result.get("202610").first());
    assertEquals("2026-10-31", result.get("202610").second());

    assertEquals("2026-09-01", result.get("202609").first());
    assertEquals("2026-09-30", result.get("202609").second());

    assertEquals("2026-08-01", result.get("202608").first());
    assertEquals("2026-08-31", result.get("202608").second());

    assertEquals("2026-07-01", result.get("202607").first());
    assertEquals("2026-07-31", result.get("202607").second());

    assertEquals("2026-06-01", result.get("202606").first());
    assertEquals("2026-06-30", result.get("202606").second());
  }

  @Test
  public void test_getFromAndToDateForLastSevenMonths_withValidDate_shouldReturnPair() {
    // arrange
    LocalDate date = LocalDate.of(2026, 10, 15);
    String format = "yyyy-MM-dd";

    // act
    Pair<String, String> result = ReportDateUtils.getFromAndToDateForLastSevenMonths(date, format);

    // assert
    assertNotNull(result);
    assertEquals("2026-04-01", result.first());
    assertEquals("2026-10-15", result.second());
  }

  @Test
  public void test_getFromAndToDateForLastSevenDays_withValidDate_shouldReturnPair() {
    // arrange
    LocalDate date = LocalDate.of(2026, 10, 15);
    String format = "yyyy-MM-dd";

    // act
    Pair<String, String> result = ReportDateUtils.getFromAndToDateForLastSevenDays(date, format);

    // assert
    assertNotNull(result);
    assertEquals("2026-10-09", result.first());
    assertEquals("2026-10-15", result.second());
  }

  @Test
  public void test_getFromAndToDateForLastSevenWeeks_withValidDate_shouldReturnPair() {
    // arrange
    LocalDate date = LocalDate.of(2026, 6, 15); // Monday
    String format = "yyyy-MM-dd";

    // act
    Pair<String, String> result = ReportDateUtils.getFromAndToDateForLastSevenWeeks(date, format);

    // assert
    assertNotNull(result);
    // from is week start - 6*7 days (42 days) => 2026-06-15 minus 42 days = 2026-05-04
    assertEquals("2026-05-04", result.first());
    assertEquals("2026-06-15", result.second()); // toDate was 21st, isAfter is true, so uses date
  }

  @Test
  public void
      test_getFromAndToDateForLastSevenWeeks_withDateAsSunday_shouldCoverIsAfterFalseBranch() {
    // arrange
    LocalDate date = LocalDate.of(2026, 6, 21); // Sunday
    String format = "yyyy-MM-dd";

    // act
    Pair<String, String> result = ReportDateUtils.getFromAndToDateForLastSevenWeeks(date, format);

    // assert
    assertNotNull(result);
  }

  @Test
  public void test_getUptoDateMonthRange_withValidDate_shouldReturnPair() {
    // arrange
    LocalDate date = LocalDate.of(2026, 10, 15);
    String format = "yyyy-MM-dd";

    // act
    Pair<String, String> result = ReportDateUtils.getUptoDateMonthRange(date, format);

    // assert
    assertNotNull(result);
    assertEquals("2026-10-01", result.first());
    assertEquals("2026-10-15", result.second());
  }

  @Test
  public void test_getFromAndToDateForLast30Days_withValidDate_shouldReturnPair() {
    // arrange
    LocalDate date = LocalDate.of(2026, 10, 15);
    String format = "yyyy-MM-dd";

    // act
    Pair<String, String> result = ReportDateUtils.getFromAndToDateForLast30Days(date, format);

    // assert
    assertNotNull(result);
    assertEquals("2026-09-16", result.first());
    assertEquals("2026-10-15", result.second());
  }

  @Test
  public void test_getDateRangeForLastSixWeeks_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getDateRangeForLastSixWeeks(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getDateRangeForLastSixWeeks_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getDateRangeForLastSixWeeks(LocalDate.now(), null));
  }

  @Test
  public void test_getDateRangeForLastSixMonths_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getDateRangeForLastSixMonths(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getDateRangeForLastSixMonths_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getDateRangeForLastSixMonths(LocalDate.now(), null));
  }

  @Test
  public void test_getFromAndToDateForLastSevenMonths_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLastSevenMonths(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getFromAndToDateForLastSevenMonths_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLastSevenMonths(LocalDate.now(), null));
  }

  @Test
  public void test_getFromAndToDateForLastSevenDays_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLastSevenDays(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getFromAndToDateForLastSevenDays_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLastSevenDays(LocalDate.now(), null));
  }

  @Test
  public void test_getFromAndToDateForLastSevenWeeks_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLastSevenWeeks(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getFromAndToDateForLastSevenWeeks_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLastSevenWeeks(LocalDate.now(), null));
  }

  @Test
  public void test_getUptoDateMonthRange_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getUptoDateMonthRange(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getUptoDateMonthRange_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getUptoDateMonthRange(LocalDate.now(), null));
  }

  @Test
  public void test_getFromAndToDateForLast30Days_withNullDate_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLast30Days(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_getFromAndToDateForLast30Days_withNullFormat_shouldThrowNPE() {
    assertThrows(
        NullPointerException.class,
        () -> ReportDateUtils.getFromAndToDateForLast30Days(LocalDate.now(), null));
  }

  @Test
  public void test_getYearAndMonth_withNullDate_shouldThrowNPE() throws Exception {
    java.lang.reflect.Method method =
        ReportDateUtils.class.getDeclaredMethod("getYearAndMonth", LocalDate.class);
    method.setAccessible(true);
    try {
      method.invoke(null, (LocalDate) null);
      fail("Expected InvocationTargetException wrapping NullPointerException");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof NullPointerException);
    }
  }

  @Test
  public void test_getMonthFromLocalDate_withNullDate_shouldThrowNPE() throws Exception {
    java.lang.reflect.Method method =
        ReportDateUtils.class.getDeclaredMethod("getMonthFromLocalDate", LocalDate.class);
    method.setAccessible(true);
    try {
      method.invoke(null, (LocalDate) null);
      fail("Expected InvocationTargetException wrapping NullPointerException");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof NullPointerException);
    }
  }
}
