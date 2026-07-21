package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class MathUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new MathUtils());
  }

  @Test
  public void test_roundOff_withValidFloat_shouldRoundedToFloor() {
    // act & assert
    assertEquals(5, MathUtils.roundOff(5.4f));
  }

  @Test
  public void test_roundOff_withValidFloat_shouldRoundedToCeil() {
    // act & assert
    assertEquals(6, MathUtils.roundOff(5.6f));
  }

  @Test
  public void test_roundOff_withNullFloat_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> MathUtils.roundOff((Float) null));
  }

  @Test
  public void test_checkFloatContainsDecimal_withValidFloat_shouldReturnTrue() {
    // act & assert
    assertTrue(MathUtils.checkFloatContainsDecimal(5.4f));
  }

  @Test
  public void test_checkFloatContainsDecimal_withValidFloat_shouldReturnFalse() {
    // act & assert
    assertFalse(MathUtils.checkFloatContainsDecimal(5.0f));
  }

  @Test
  public void test_checkFloatContainsDecimal_withNullFloat_shouldThrowNPE() {
    // act & assert
    assertThrows(
        NullPointerException.class, () -> MathUtils.checkFloatContainsDecimal((Float) null));
  }

  @Test
  public void test_generateVVIDV4() {
    // act
    UUID uuid = MathUtils.generateUUIDV4();

    // assert
    assertNotNull(uuid);
    assertEquals(36, uuid.toString().length());
  }

  @Test
  public void test_generateVVIDV7() {
    // act
    UUID uuid = MathUtils.generateUUIDV7();

    // assert
    assertNotNull(uuid);
    assertEquals(36, uuid.toString().length());
  }

  @Test
  public void test_clampInt_returningMin() {
    assertEquals(5, MathUtils.clamp(1, 5, 10));
  }

  @Test
  public void test_clampInt_returningValue() {
    assertEquals(7, MathUtils.clamp(7, 5, 10));
  }

  @Test
  public void test_clampInt_returningMax() {
    assertEquals(10, MathUtils.clamp(15, 5, 10));
  }

  @Test
  public void test_clampInt_minGreaterThanMax_throwsError() {
    assertThrows(IllegalArgumentException.class, () -> MathUtils.clamp(10, 10, 5));
  }

  @Test
  public void test_clampFloat_returningMin() {
    assertEquals(5.5, MathUtils.clamp(1.1, 5.5, 10.1));
  }

  @Test
  public void test_clampFloat_returningValue() {
    assertEquals(7.5, MathUtils.clamp(7.5, 5.5, 10.1));
  }

  @Test
  public void test_clampFloat_returningMax() {
    assertEquals(10.1, MathUtils.clamp(15.5, 5.5, 10.1));
  }

  @Test
  public void test_clampFloat_minGreaterThanMax_throwsError() {
    assertThrows(IllegalArgumentException.class, () -> MathUtils.clamp(10.1, 10.1, 5.5));
  }

  @Test
  public void test_shouldReturnRandom_withAlwaysTrueBoundary() {
    // If rangeBoundary is equal to totalRange, m (which is r % totalRange) will always be strictly
    // less than rangeBoundary.
    assertTrue(MathUtils.shouldReturnRandom(10, 10));
  }

  @Test
  public void test_shouldReturnRandom_withAlwaysFalseBoundary() {
    // If rangeBoundary is 0, m (which is >= 0) can never be strictly less than 0.
    assertFalse(MathUtils.shouldReturnRandom(10, 0));
  }
}
