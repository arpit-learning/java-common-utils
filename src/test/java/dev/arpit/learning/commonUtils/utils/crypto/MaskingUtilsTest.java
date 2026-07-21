package dev.arpit.learning.commonUtils.utils.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MaskingUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new MaskingUtils());
  }

  @Test
  void test_getMaskedName_withValidName_shouldReturnMaskedName() {
    // arrange
    String name = "John";
    String expected = "J**n";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_getMaskedName_withEmptyName_shouldReturnMaskedName() {
    // arrange
    String name = "";
    String expected = "";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_getMaskedName_withValidNameWithTwoWords_shouldReturnMaskedName() {
    // arrange
    String name = "John Doe";
    String expected = "J**n D*e";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void
      test_getMaskedName_withValidNameWithTwoWordsWithFirstWordHavingOneCharacter_shouldReturnMaskedName() {
    // arrange
    String name = "J Doe";
    String expected = "J D*e";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void
      test_getMaskedName_withValidNameWithTwoWordsWithFirstWordHavingTwoCharacter_shouldReturnMaskedName() {
    // arrange
    String name = "Jo Doe";
    String expected = "Jo D*e";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void
      test_getMaskedName_withValidNameWithTwoWordsWithSecondWordHavingOneCharacter_shouldReturnMaskedName() {
    // arrange
    String name = "John D";
    String expected = "J**n D";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void
      test_getMaskedName_withValidNameWithTwoWordsWithSecondWordHavingTwoCharacter_shouldReturnMaskedName() {
    // arrange
    String name = "John Do";
    String expected = "J**n Do";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_getMaskedName_withValidNameWithSingleCharacter_shouldReturnSameName() {
    // arrange
    String name = "A";
    String expected = "A";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
    assertEquals("Jo", MaskingUtils.getMaskedName("Jo"));
  }

  @Test
  void test_getMaskedName_withValidNameWithTwoCharacters_shouldReturnSameName() {
    // arrange
    String name = "Jo";
    String expected = "Jo";

    // act
    String result = MaskingUtils.getMaskedName(name);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_getMaskedName_withNullName_shouldThrowNPE() {
    // arrange
    String name = null;
    String expected = "Jo";

    // act & assert
    assertThrows(NullPointerException.class, () -> MaskingUtils.getMaskedName(name));
  }

  @Test
  void test_getMaskedEmail_withValidEmail_shouldReturnMaskedEmail() {
    // arrange
    String email = "john@email.com";
    String expected = "j**n@email.com";

    // act
    String result = MaskingUtils.getMaskedEmail(email);

    // assert
    assertEquals(expected, result);

    assertEquals("j@gmail.com", MaskingUtils.getMaskedEmail("j@gmail.com"));
  }

  @Test
  void test_getMaskedEmail_withEmptyEmail_shouldReturnEmptyEmail() {
    // arrange
    String email = "";
    String expected = "";

    // act
    String result = MaskingUtils.getMaskedEmail(email);

    // assert
    assertEquals(expected, result);

    assertEquals("j@gmail.com", MaskingUtils.getMaskedEmail("j@gmail.com"));
  }

  @Test
  void test_getMaskedEmail_withValidEmailWithSingleCharacter_shouldReturnSameEmail() {
    // arrange
    String email = "j@email.com";
    String expected = "j@email.com";

    // act
    String result = MaskingUtils.getMaskedEmail(email);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void test_getMaskedEmail_withNullEmail_shouldThrowNPE() {
    // arrange
    String email = null;
    String expected = "john@email.com";

    // act & assert
    assertThrows(NullPointerException.class, () -> MaskingUtils.getMaskedEmail(email));
  }

  @Test
  void test_getMaskedMobileNumber_withValidMobileNumber_shouldReturnMaskedMobileNumber() {
    // arrange
    String mobileNumber = "1234567890";
    String expected = "12******90";

    // act
    String result = MaskingUtils.getMaskedMobileNumber(mobileNumber);

    // assert
    assertEquals(expected, result);
  }

  @Test
  void
      test_getMaskedMobileNumber_withValidMobileNumberWithSingleCharacter_shouldThrowIllegalArgumentException() {
    // arrange
    String mobileNumber = "1";
    String expected = "1";

    // act & assert
    assertThrows(
        IllegalArgumentException.class, () -> MaskingUtils.getMaskedMobileNumber(mobileNumber));
  }

  @Test
  void test_getMaskedMobileNumber_withNullMobileNUmber_shouldThrowNPE() {
    // arrange
    String mobileNumber = null;
    String expected = "Jo";

    // act & assert
    assertThrows(
        NullPointerException.class, () -> MaskingUtils.getMaskedMobileNumber(mobileNumber));
  }
}
