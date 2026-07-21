package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class ListConversionUtilsTest {
  @Test
  public void test_objectCreation() {
    // act
    ListConversionUtils listConversionUtils = new ListConversionUtils();

    // assert
    assertNotNull(listConversionUtils);
  }

  @Test
  public void test_listToArray_with_validList_shouldReturnValidArray() {
    // arrange
    List<String> list = Arrays.asList("A", "B");

    // act
    String[] arr = ListConversionUtils.listToArray(list, String.class);

    // assert
    assertNotNull(arr);
    assertEquals(2, arr.length);
    assertEquals("A", arr[0]);
    assertEquals("B", arr[1]);
  }

  @Test
  public void test_listToArray_with_nullList_shouldThrowNPE() {
    // act & assert
    assertThrows(
        NullPointerException.class, () -> ListConversionUtils.listToArray(null, String.class));
  }

  @Test
  public void test_listToArray_with_nullClassType_shouldThrowNPE() {
    // act & assert
    assertThrows(
        NullPointerException.class, () -> ListConversionUtils.listToArray(List.of(), null));
  }

  @Test
  public void test_chunk_withValidListAndValidChunkSize_shouldReturnValidChunks() {
    // arrange
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    // act
    List<List<Integer>> chunks = ListConversionUtils.chunk(list, 2);

    // assert
    assertNotNull(chunks);
    assertEquals(3, chunks.size());
    assertEquals(List.of(1, 2), chunks.get(0));
    assertEquals(List.of(3, 4), chunks.get(1));
    assertEquals(List.of(5), chunks.get(2));
  }

  @Test
  public void test_chunk_withValidListAndZeroChunkSize_shouldThrowIllegalArgumentException() {
    // arrange
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    // act & assert
    assertThrows(IllegalArgumentException.class, () -> ListConversionUtils.chunk(list, 0));
  }

  @Test
  public void test_chunk_withValidListAndNegativeChunkSize_shouldThrowIllegalArgumentException() {
    // arrange
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    // act & assert
    assertThrows(IllegalArgumentException.class, () -> ListConversionUtils.chunk(list, -1));
  }

  @Test
  public void test_chunk_withNullList_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> ListConversionUtils.chunk(null, 2));
  }

  @Test
  public void test_distinct_withValidList_shouldReturnDistinctList() {
    // arrange
    List<Integer> list = Arrays.asList(1, 1, 2, 3, 3);

    // act
    List<Integer> dist = ListConversionUtils.distinct(list);

    // assert
    assertNotNull(dist);
    assertEquals(3, dist.size());
    assertEquals(Arrays.asList(1, 2, 3), dist);
  }

  @Test
  public void test_distinct_withNullList_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> ListConversionUtils.distinct(null));
  }

  @Test
  public void test_stringToList_with_validJsonString_shouldReturnList() {
    // act
    List<String> list = ListConversionUtils.stringToList("[\"a\", \"b\"]");

    // assert
    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
  }

  @Test
  public void test_stringToList_with_nullString_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> ListConversionUtils.stringToList(null));
  }

  @Test
  public void test_stringToList_with_emptyString_shouldReturnEmptyList() {
    // act
    List<String> list = ListConversionUtils.stringToList("");

    // assert
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test
  public void test_stringToList_with_invalidJsonString_shouldReturnEmptyList() {
    // act
    List<String> list = ListConversionUtils.stringToList("invalid json");

    // assert
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test
  public void test_removeNullEntries_with_validList_shouldRemoveNulls() {
    // arrange
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add(null);
    list.add("b");

    // act
    List<String> result = ListConversionUtils.removeNullEntries(list);

    // assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("a", result.get(0));
    assertEquals("b", result.get(1));
  }

  @Test
  public void test_removeNullEntries_with_nullList_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> ListConversionUtils.removeNullEntries(null));
  }

  @Test
  public void test_putInMap_with_existingKey_shouldAddToList() {
    // arrange
    Map<String, List<String>> map = new HashMap<>();
    List<String> list1 = new ArrayList<>();
    list1.add("a");
    map.put("key1", list1);

    // act
    ListConversionUtils.putInMap(map, "key1", Arrays.asList("b", "c"));

    // assert
    assertNotNull(map.get("key1"));
    assertEquals(3, map.get("key1").size());
    assertTrue(map.get("key1").containsAll(Arrays.asList("a", "b", "c")));
  }

  @Test
  public void test_putInMap_with_newKey_shouldPutList() {
    // arrange
    Map<String, List<String>> map = new HashMap<>();

    // act
    ListConversionUtils.putInMap(map, "key1", Arrays.asList("a", "b"));

    // assert
    assertNotNull(map.get("key1"));
    assertEquals(2, map.get("key1").size());
    assertTrue(map.get("key1").containsAll(Arrays.asList("a", "b")));
  }

  @Test
  public void test_putInMap_with_nullArgs_shouldThrowNPE() {
    // act & assert
    assertThrows(
        NullPointerException.class, () -> ListConversionUtils.putInMap(null, "key", List.of()));
    assertThrows(
        NullPointerException.class,
        () -> ListConversionUtils.putInMap(new HashMap<>(), null, List.of()));
    assertThrows(
        NullPointerException.class,
        () -> ListConversionUtils.putInMap(new HashMap<>(), "key", null));
  }

  @Test
  public void test_getNonEmptyStringsCount_with_validArray_shouldReturnCount() {
    // act & assert
    assertEquals(
        2, ListConversionUtils.getNonEmptyStringsCount(new String[] {"a", "", " ", "b", null}));
  }

  @Test
  public void test_getNonEmptyStringsCount_with_nullArray_shouldThrowNPE() {
    // act & assert
    assertThrows(
        NullPointerException.class, () -> ListConversionUtils.getNonEmptyStringsCount(null));
  }

  @Test
  public void test_nullSafeList_with_validList_shouldReturnSameList() {
    // arrange
    List<String> list = Arrays.asList("a", "b");

    // act & assert
    assertEquals(list, ListConversionUtils.nullSafeList(list));
  }

  @Test
  public void test_nullSafeList_with_nullList_shouldReturnEmptyList() {
    // act
    List<String> result = ListConversionUtils.nullSafeList(null);

    // assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
