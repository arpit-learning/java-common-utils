package dev.arpit.learning.commonUtils.utils.restclient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UrlUtilsTest {
  @Test
  void testIsValidUrl() {
    assertTrue(UrlUtils.isValidUrl("http://google.com"));
  }

  @Test
  void testIsValidUrl_withInvalidUrl_returnsFalse() {
    // Null triggers NullPointerException in URI.create
    assertFalse(UrlUtils.isValidUrl(null));
    // Invalid URI syntax triggers IllegalArgumentException
    assertFalse(UrlUtils.isValidUrl("http://[::1"));
    assertFalse(UrlUtils.isValidUrl(" "));
  }

  @Test
  void testObjectCreation() {
    UrlUtils utils = new UrlUtils();
    assertNotNull(utils);
  }
}
