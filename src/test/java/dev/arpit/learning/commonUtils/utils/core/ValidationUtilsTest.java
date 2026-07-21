package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;

import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

class ValidationUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new ValidationUtils());
  }

  @Test
  public void test_isNullOrEmpty_withValidString_shouldReturnsFalse()
      throws EmptyValidatorNotFoundException {
    // arrange
    String string = "Hello World";
    ResolvableType resolvableType = ResolvableType.forClass(String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(string, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNullOrEmpty_withEmptyString_shouldReturnsTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    String string = "";
    ResolvableType resolvableType = ResolvableType.forClass(String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(string, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withNullString_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    String string = null;
    ResolvableType resolvableType = ResolvableType.forClass(String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(string, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withValidStringArray_shouldReturnsFalse()
      throws EmptyValidatorNotFoundException {
    // arrange
    String[] strings = new String[] {"Hello World"};
    ResolvableType resolvableType = ResolvableType.forClass(String[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void
      test_isNullOrEmpty_withValidStringArrayContainingAtLeastOneEmptyString_shouldReturnsFalse()
          throws EmptyValidatorNotFoundException {
    // arrange
    String[] strings = new String[] {"", "Hello World"};
    ResolvableType resolvableType = ResolvableType.forClass(String[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNullOrEmpty_withEmptyStringArray_shouldReturnsTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    String[] strings = new String[] {};
    ResolvableType resolvableType = ResolvableType.forClass(String[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withNullStringArray_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    String[] strings = null;
    ResolvableType resolvableType = ResolvableType.forClass(String[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withValidStringList_shouldReturnsFalse()
      throws EmptyValidatorNotFoundException {
    // arrange
    List<String> strings = List.of("Hello World");
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void
      test_isNullOrEmpty_withValidStringListContainingAtLeastOneEmptyString_shouldReturnsFalse()
          throws EmptyValidatorNotFoundException {
    // arrange
    List<String> strings = List.of("", "Hello World");
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNullOrEmpty_withEmptyStringList_shouldReturnsTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    List<String> strings = List.of();
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withNullStringList_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    List<String> strings = null;
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(strings, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withValidMap_shouldReturnsFalse()
      throws EmptyValidatorNotFoundException {
    // arrange
    Map<String, String> map = Map.of("hello", "world");
    ResolvableType resolvableType =
        ResolvableType.forClassWithGenerics(Map.class, String.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNullOrEmpty_withEmptyMap_shouldReturnsTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    Map<String, String> map = Map.of();
    ResolvableType resolvableType =
        ResolvableType.forClassWithGenerics(Map.class, String.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withNullMap_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    Map<String, String> map = null;
    ResolvableType resolvableType =
        ResolvableType.forClassWithGenerics(Map.class, String.class, String.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withValidList_shouldReturnsFalse()
      throws EmptyValidatorNotFoundException {
    // arrange
    List<Integer> map = List.of(1, 2);
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, Integer.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNullOrEmpty_withEmptyList_shouldReturnsTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    List<Integer> map = List.of();
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, Integer.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withNullList_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    List<Integer> map = null;
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, Integer.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withValidSet_shouldReturnsFalse()
      throws EmptyValidatorNotFoundException {
    // arrange
    Set<Integer> map = Set.of(1, 2);
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Set.class, Integer.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertFalse(result);
  }

  @Test
  public void test_isNullOrEmpty_withEmptySet_shouldReturnsTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    Set<Integer> map = Set.of();
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Set.class, Integer.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withNullSet_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    Set<Integer> map = null;
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Set.class, Integer.class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(map, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void
      test_isNullOrEmpty_withValidUnknownClass_shouldThrowEmptyValidatorNotFFoundException() {
    // arrange
    int[] numbers = new int[] {1, 2};
    ResolvableType resolvableType = ResolvableType.forClass(int[].class);

    // act & asset
    assertThrows(
        EmptyValidatorNotFoundException.class,
        () -> ValidationUtils.isNullOrEmpty(new Pair<>(numbers, resolvableType)));
  }

  @Test
  public void test_isNullOrEmpty_withNullArg_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    int[] numbers = new int[] {1, 2};
    ResolvableType resolvableType = ResolvableType.forClass(int[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty((Pair<Object, ResolvableType>[]) null);

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withMultipleNullArgs_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    int[] numbers = new int[] {1, 2};
    ResolvableType resolvableType = ResolvableType.forClass(int[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(null, null);

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withPairHavingFirstAsNull_shouldReturnTrue()
      throws EmptyValidatorNotFoundException {
    // arrange
    int[] numbers = new int[] {1, 2};
    ResolvableType resolvableType = ResolvableType.forClass(int[].class);

    // act
    boolean result = ValidationUtils.isNullOrEmpty(new Pair<>(null, resolvableType));

    // assert
    assertTrue(result);
  }

  @Test
  public void test_isNullOrEmpty_withPairHavingSecondAsNull_shouldThrowIllegalArgumentException()
      throws EmptyValidatorNotFoundException {
    // arrange
    int[] numbers = new int[] {1, 2};
    ResolvableType resolvableType = ResolvableType.forClass(int[].class);

    // act & assert
    assertThrows(
        IllegalArgumentException.class,
        () -> ValidationUtils.isNullOrEmpty(new Pair<>(numbers, null)));
  }
}
