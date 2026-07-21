package dev.arpit.learning.commonUtils.utils.crypto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import javax.crypto.Mac;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class HmacUtilsTest {
  @Test
  public void test_objectCreation() {
    // act & assert
    assertNotNull(new HmacUtils());
  }

  @Test
  public void test_getHmac_withValidTimestampAndValidMessageAndValidKey() throws Exception {
    // arrange
    String data = "hello world";
    String key = "secret";
    long timestamp = Instant.now().toEpochMilli();

    // act
    String hmac = HmacUtils.getHmac(timestamp, data, key);

    // assert
    assertNotNull(hmac);
  }

  @Test
  public void test_getHmac_withValidTimestampAndNullMessageAndValidKey_shouldThrowNPE()
      throws Exception {
    // arrange
    String data = null;
    String key = "secret";
    long timestamp = Instant.now().toEpochMilli();

    // act & assert
    assertThrows(NullPointerException.class, () -> HmacUtils.getHmac(timestamp, data, key));
  }

  @Test
  public void test_getHmac_withValidTimestampAndValidMessageAndNullKey_shouldThrowNPE()
      throws Exception {
    // arrange
    String data = "hello world";
    String key = null;
    long timestamp = Instant.now().toEpochMilli();

    // act & assert
    assertThrows(NullPointerException.class, () -> HmacUtils.getHmac(timestamp, data, key));
  }

  @Test
  void test_getHmac_withNoSuchAlgorithmException() {
    try (MockedStatic<Mac> mockedMac = mockStatic(Mac.class)) {
      // arrange
      String data = "hello world";
      String key = "secret";
      long timestamp = Instant.now().toEpochMilli();

      mockedMac
          .when(() -> Mac.getInstance(anyString()))
          .thenThrow(new NoSuchAlgorithmException("Mocked exception"));

      // act & assert
      Exception exception =
          assertThrows(Exception.class, () -> HmacUtils.getHmac(timestamp, data, key));
      assertInstanceOf(NoSuchAlgorithmException.class, exception.getCause());
    }
  }

  @Test
  public void test_getChecksum_withValidMessageAndValidKey() throws Exception {
    // arrange
    String data = "hello world";
    String key = "secret";

    // act
    String hmac = HmacUtils.getChecksum(data, key);

    // assert
    assertNotNull(hmac);
  }

  @Test
  public void test_getChecksum_withNullMessageAndValidKey() throws Exception {
    // arrange
    String data = null;
    String key = "secret";

    // act & assert
    assertThrows(NullPointerException.class, () -> HmacUtils.getChecksum(data, key));
  }

  @Test
  void test_getChecksum_withValidMessageAndNullKey() throws Exception {
    // arrange
    String data = "hello world";
    String key = null;

    // act & assert
    assertThrows(NullPointerException.class, () -> HmacUtils.getChecksum(data, key));
  }

  @Test
  void test_getChecksum_withNoSuchAlgorithmException() {
    try (MockedStatic<Mac> mockedMac = mockStatic(Mac.class)) {
      // arrange
      String data = "hello world";
      String key = "secret";

      mockedMac
          .when(() -> Mac.getInstance(anyString()))
          .thenThrow(new NoSuchAlgorithmException("Mocked exception"));

      // act & assert
      Exception exception = assertThrows(Exception.class, () -> HmacUtils.getChecksum(data, key));
      assertInstanceOf(NoSuchAlgorithmException.class, exception.getCause());
    }
  }
}
