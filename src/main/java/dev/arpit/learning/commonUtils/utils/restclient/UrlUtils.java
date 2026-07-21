package dev.arpit.learning.commonUtils.utils.restclient;

import java.net.URI;

public class UrlUtils {
  public static boolean isValidUrl(String url) {
    try {
      URI.create(url);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
