package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new StringUtils());
  }

  @Test
  public void test_isNullOrEmpty_withNullString_returnsTrue() {
    // act & assert
    assertTrue(StringUtils.isNullOrEmpty(null));
  }

  @Test
  public void test_isNullOrEmpty_withEmptyString_returnsTrue() {
    // act & assert
    assertTrue(StringUtils.isNullOrEmpty(""));
  }

  @Test
  public void test_isNullOrEmpty_withStringContainingOnlySpaces_returnsTrue() {
    // act & assert
    assertTrue(StringUtils.isNullOrEmpty("   "));
  }

  @Test
  public void test_isNullOrEmpty_withNonEmptyString_returnsFalse() {
    // act & assert
    assertFalse(StringUtils.isNullOrEmpty("abc"));
  }

  @Test
  public void test_isNotNullOrEmpty_withNullString_returnsFalse() {
    // act & assert
    assertFalse(StringUtils.isNotNullOrEmpty(null));
  }

  @Test
  public void test_isNotNullOrEmpty_withEmptyString_returnsFalse() {
    // act & assert
    assertFalse(StringUtils.isNotNullOrEmpty(""));
  }

  @Test
  public void test_isNotNullOrEmpty_withStringContainingOnlySpaces_returnsFalse() {
    // act & assert
    assertFalse(StringUtils.isNotNullOrEmpty("   "));
  }

  @Test
  public void test_isNotNullOrEmpty_withNonEmptyString_returnsTrue() {
    // act & assert
    assertTrue(StringUtils.isNotNullOrEmpty("abc"));
  }

  @Test
  public void test_defaultIfNullOrEmpty_withNullString_returnsDefaultString() {
    // act & assert
    String result = StringUtils.defaultIfNullOrEmpty(null, "Default");
    assertNotNull(result);
    assertEquals("Default", result);
  }

  @Test
  public void test_defaultIfNullOrEmpty_withEmptyString_returnsDefaultString() {
    // act & assert
    String result = StringUtils.defaultIfNullOrEmpty("", "Default");
    assertNotNull(result);
    assertEquals("Default", result);
  }

  @Test
  public void test_defaultIfNotNullOrEmpty_withStringContainingOnlySpaces_returnsDefaultString() {
    // act & assert
    String result = StringUtils.defaultIfNullOrEmpty("   ", "Default");
    assertNotNull(result);
    assertEquals("Default", result);
  }

  @Test
  public void test_defaultIfNotNullOrEmpty_withNonEmptyString_returnsString() {
    // act & assert
    String result = StringUtils.defaultIfNullOrEmpty("abc", "Default");
    assertNotNull(result);
    assertEquals("abc", result);
  }

  @Test
  public void test_defaultIfNotNullOrEmpty_withNullDefault_returnsThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.defaultIfNullOrEmpty("abc", null));
  }

  @Test
  public void test_countPlaceholders_withValidStringWithoutTemplates_shouldReturnZero() {
    // act
    int count = StringUtils.countPlaceholders("Hello World");

    // assert
    assertEquals(0, count);
  }

  @Test
  public void test_countPlaceholders_withValidStringWithTemplates_shouldReturnExactCount() {
    // act
    int count = StringUtils.countPlaceholders("# Hello World #");

    // assert
    assertEquals(2, count);
  }

  @Test
  public void test_countPlaceholders_withNullString_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.countPlaceholders(null));
  }

  @Test
  public void test_getString_withValidTemplate_shouldReturnFormattedString() {
    // arrange
    String template = "Hello #, welcome to #";

    // act
    String result = StringUtils.getString(template, "John", "Earth");

    // assert
    assertEquals("Hello John, welcome to Earth", result);
  }

  @Test
  public void
      test_getString_withValidTemplateButIncorrectNoOfArgs_shouldThrowIllegalArgumentException() {
    // arrange
    String template = "Hello #, welcome to #";

    // act & assert
    assertThrows(IllegalArgumentException.class, () -> StringUtils.getString(template, "John"));
  }

  @Test
  public void test_getString_withNullTemplate_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.getString(null));
  }

  @Test
  public void test_getString_withNullArgs_shouldThrowNPE() {
    // arrange
    String template = "Hello #, welcome to #";

    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.getString(template, null));
  }

  @Test
  public void test_decode_withValidEncodedStringAndValidFormat_shouldReturnDecodedString()
      throws UnsupportedEncodingException {
    // arrange
    String encodedString = "Hello%20World";
    String format = "UTF-8";
    String decodedString = "Hello World";

    // act
    String result = StringUtils.decode(encodedString, format);

    // assert
    assertNotNull(result);
    assertEquals(decodedString, result);
  }

  @Test
  public void
      test_decode_withInvalidEncodedStringAndValidFormat_shouldThrowIllegalArgumentException() {
    // arrange
    String encodedString = "Hello%"; // Invalid percent encoding
    String format = "UTF-8";

    // act & assert
    assertThrows(IllegalArgumentException.class, () -> StringUtils.decode(encodedString, format));
  }

  @Test
  public void test_decode_withInvalidFormat_shouldThrowUnsupportedEncodingException() {
    // arrange
    String encodedString = "Hello%20World";
    String format = "INVALID_FORMAT";

    // act & assert
    assertThrows(
        UnsupportedEncodingException.class, () -> StringUtils.decode(encodedString, format));
  }

  @Test
  public void test_decode_withNullFormat_shouldThrowNPE() {
    // arrange
    String encodedString = "Hello%20World";

    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.decode(encodedString, null));
  }

  @Test
  public void test_decode_withNullEncodedString_shouldThrowNPE() {
    // arrange
    String format = "INVALID_FORMAT";

    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.decode(null, format));
  }

  @Test
  public void test_join_withValidListAndValidToken_shouldReturnJoinedString() {
    // arrange
    String token = ",";
    List<String> list = List.of("A", "B", "C");
    String expected = "A,B,C";

    // act
    String result = StringUtils.join(list, token);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_join_withEmptyList_shouldReturnEmptyString() {
    // arrange
    String token = ",";
    List<String> list = List.of();
    String expected = "";

    // act
    String result = StringUtils.join(list, token);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_join_withEmptyToken_shouldReturnJoinedString() {
    // act
    String string = StringUtils.join(List.of("A", "B"), "");

    // arrange
    assertEquals("AB", string);
  }

  @Test
  public void test_join_withNullList_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.join(null, ","));
  }

  @Test
  public void test_join_withNullToken_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.join(List.of("A", "B"), null));
  }

  @Test
  public void test_capitalizeEachWord_withSingleWord_shouldReturnCorrectlyCapitalizedWord() {
    // act & assert
    assertEquals("Hello", StringUtils.capitalizeEachWord("hello"));
  }

  @Test
  public void test_capitalizeEachWord_withWords_shouldReturnCorrectlyCapitalizedWords() {
    // act & assert
    assertEquals("Hello World", StringUtils.capitalizeEachWord("hello world"));
  }

  @Test
  public void test_capitalizeEachWord_withNullString_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.capitalizeEachWord(null));
  }

  @Test
  public void test_trimIfNotNull_withValidStringContainingWhitespaces_shouldReturnTrimmedString() {
    // arrange
    String string = "   Hello World   ";
    String expected = "Hello World";

    // act
    String result = StringUtils.trimIfNotNull(string);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_trimIfNotNull_withValidString_shouldReturnString() {
    // arrange
    String string = "Hello World";
    String expected = "Hello World";

    // act
    String result = StringUtils.trimIfNotNull(string);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_trimIfNotNull_withNullString_shouldReturnEmptyString() {
    // arrange
    String string = "Hello World";
    String expected = "";

    // act
    String result = StringUtils.trimIfNotNull(null);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_splitIfNotNull_withValidString_shouldReturnArray() {
    // arrange
    String string = "Hello World";
    String tokenSeparator = " ";
    String[] expected = new String[] {"Hello", "World"};

    // act
    String[] result = StringUtils.splitIfNotNull(string, tokenSeparator);

    // assert
    assertNotNull(result);
    assertArrayEquals(expected, result);
  }

  @Test
  public void test_splitIfNotNull_withEmptyString_shouldReturnEmptyArray() {
    // arrange
    String string = "";
    String tokenSeparator = " ";
    String[] expected = new String[] {""};

    // act
    String[] result = StringUtils.splitIfNotNull(string, tokenSeparator);

    // assert
    assertNotNull(result);
    assertArrayEquals(expected, result);
  }

  @Test
  public void test_splitIfNotNull_withNullString_shouldReturnArrayContainingEmptyString() {
    // arrange
    String string = null;
    String tokenSeparator = " ";
    String[] expected = new String[] {""};

    // act
    String[] result = StringUtils.splitIfNotNull(string, tokenSeparator);

    // assert
    assertNotNull(result);
    assertArrayEquals(expected, result);
  }

  @Test
  public void test_splitIfNotNull_withNullTokenSeparator_shouldReturnThrowNPE() {
    // arrange
    String string = "Hello World";
    String tokenSeparator = null;

    // act & assert
    assertThrows(
        NullPointerException.class, () -> StringUtils.splitIfNotNull(string, tokenSeparator));
  }

  @Test
  public void
      test_substringBefore_withValidStringAndValidDelimiter_shouldReturnSubstringBeforeDelimiter() {
    // arrange
    String str = "hello@example.com";
    String delimiter = "@";
    String expected = "hello";

    // act
    String result = StringUtils.substringBefore(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_substringBefore_withValidStringAndInvalidDelimiter_shouldReturnOriginalString() {
    // arrange
    String str = "hello@example.com";
    String delimiter = "#";
    String expected = "hello@example.com";

    // act
    String result = StringUtils.substringBefore(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_substringBefore_withEmptyString_shouldReturnEmptyString() {
    // arrange
    String str = "";
    String delimiter = "#";
    String expected = "";

    // act
    String result = StringUtils.substringBefore(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_substringBefore_withNullString_shouldThrowNPE() {
    // arrange
    String str = null;
    String delimiter = "#";
    String expected = "";

    // act
    assertThrows(NullPointerException.class, () -> StringUtils.substringBefore(str, delimiter));
  }

  @Test
  public void test_substringBefore_withNullDelimiter_shouldThrowNPE() {
    // arrange
    String str = "hello@example.com";
    String delimiter = null;

    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.substringBefore(str, delimiter));
  }

  @Test
  public void test_substringBefore_withEmptyDelimiter_shouldReturnEmptyString() {
    // arrange
    String str = "hello@example.com";
    String delimiter = "";
    String expected = "";

    // act
    String result = StringUtils.substringBefore(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void
      test_substringAfter_withValidStringAndValidDelimiter_shouldReturnSubstringAfterDelimiter() {
    // arrange
    String str = "hello@example.com";
    String delimiter = "@";
    String expected = "example.com";

    // act
    String result = StringUtils.substringAfter(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_substringAfter_withValidStringAndInvalidDelimiter_shouldReturnOriginalString() {
    // arrange
    String str = "hello@example.com";
    String delimiter = "#";
    String expected = "hello@example.com";

    // act
    String result = StringUtils.substringAfter(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_substringAfter_withEmptyString_shouldReturnEmptyString() {
    // arrange
    String str = "";
    String delimiter = "#";
    String expected = "";

    // act
    String result = StringUtils.substringAfter(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_substringAfter_withNullString_shouldThrowNPE() {
    // arrange
    String str = null;
    String delimiter = "#";
    String expected = "";

    // act
    assertThrows(NullPointerException.class, () -> StringUtils.substringAfter(str, delimiter));
  }

  @Test
  public void test_substringAfter_withNullDelimiter_shouldThrowNPE() {
    // arrange
    String str = "hello@example.com";
    String delimiter = null;
    String expected = "";

    // act
    assertThrows(NullPointerException.class, () -> StringUtils.substringAfter(str, delimiter));
  }

  @Test
  public void test_substringAfter_withEmptyDelimiter_shouldReturnEmptyString() {
    // arrange
    String str = "hello@example.com";
    String delimiter = "";
    String expected = "";

    // act
    String result = StringUtils.substringAfter(str, delimiter);

    // assert
    assertEquals(expected, result);
  }

  @Test
  public void test_containsIgnoreCase_ReturnsTrue() {
    // arrange
    String str = "Hello World";
    String searchStr = "WORLD";

    // act
    boolean result = StringUtils.containsIgnoreCase(str, searchStr);

    // assert
    assertTrue(result);
  }

  @Test
  public void test_containsIgnoreCase_ReturnsFalse() {
    // arrange
    String str = "Hello World";
    String searchStr = "EARCH";

    // act
    boolean result = StringUtils.containsIgnoreCase(str, searchStr);

    // assert
    assertFalse(result);
  }

  @Test
  public void test_containsIgnoreCase_withEmptySearchString_shouldReturnTrue() {
    // arrange
    String str = "Hello World";
    String searchStr = "";

    // act
    boolean result = StringUtils.containsIgnoreCase(str, searchStr);

    // assert
    assertTrue(result);
  }

  @Test
  public void test_containsIgnoreCase_withNullString_shouldThrowNPE() {
    // arrange
    String str = null;
    String searchStr = "WORLD";

    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.containsIgnoreCase(str, searchStr));
  }

  @Test
  public void test_containsIgnoreCase_withNullSearchString_shouldThrowNPE() {
    // arrange
    String str = "Hello World";
    String searchStr = null;

    // act & assert
    assertThrows(NullPointerException.class, () -> StringUtils.containsIgnoreCase(str, searchStr));
  }

  @Test
  public void test_padLeft_withValidStringAndLargerLengthAndValidChar_shouldReturnExpectedString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = 10;
    String expected = "XXXXXHello";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_padLeft_withNullStringAndValidLengthAndValidChar_shouldReturnExpectedString() {
    // arrange
    String str = null;
    char padChar = 'X';
    int length = 10;
    String expected = "XXXXXXXXXX";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padLeft_withValidStringAndSmallerLengthAndValidChar_shouldReturnOriginalString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = 4;
    String expected = "Hello";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padLeft_withValidStringAndLargerLengthAndWhitespaceChar_shouldReturnExpectedString() {
    // arrange
    String str = "Hello";
    char padChar = ' ';
    int length = 10;
    String expected = "     Hello";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_padLeft_withEmptyStringAndLargerLengthAndValidChar_shouldReturnExpectedString() {
    // arrange
    String str = "";
    char padChar = 'X';
    int length = 4;
    String expected = "XXXX";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_padLeft_withValidStringAndZeroLengthAndValidChar_shouldReturnEmptyString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = 0;
    String expected = "Hello";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padLeft_withValidStringAndNegativeLengthAndValidChar_shouldReturnOriginalString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = -4;
    String expected = "Hello";

    // act
    String result = StringUtils.padLeft(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padRight_withValidStringAndLargerLengthAndValidChar_shouldReturnExpectedString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = 10;
    String expected = "HelloXXXXX";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_paRIght_withNullStringAndValidLengthAndValidChar_shouldReturnExpectedString() {
    // arrange
    String str = null;
    char padChar = 'X';
    int length = 10;
    String expected = "XXXXXXXXXX";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padRight_withValidStringAndSmallerLengthAndValidChar_shouldReturnOriginalString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = 4;
    String expected = "Hello";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padRight_withValidStringAndLargerLengthAndWhitespaceChar_shouldReturnExpectedString() {
    // arrange
    String str = "Hello";
    char padChar = ' ';
    int length = 10;
    String expected = "Hello     ";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padRight_withEmptyStringAndLargerLengthAndValidChar_shouldReturnExpectedString() {
    // arrange
    String str = "";
    char padChar = 'X';
    int length = 4;
    String expected = "XXXX";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void test_padRight_withValidStringAndZeroLengthAndValidChar_shouldReturnEmptyString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = 0;
    String expected = "Hello";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void
      test_padRight_withValidStringAndNegativeLengthAndValidChar_shouldReturnOriginalString() {
    // arrange
    String str = "Hello";
    char padChar = 'X';
    int length = -4;
    String expected = "Hello";

    // act
    String result = StringUtils.padRight(str, length, padChar);

    // assert
    assertNotNull(result);
    assertEquals(expected, result);
  }
}
