package dev.arpit.learning.commonUtils.utils.crypto;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;

public class HmacUtils {
  public static @NonNull String getHmac(
      long timestamp, @NonNull String message, @NonNull String secretKey) throws Exception {
    String key = timestamp + "|" + secretKey;
    String algorithm = "HMACSHA1";
    SecretKey signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
    Mac mac;
    try {
      mac = Mac.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new Exception(e);
    }
    mac.init(signingKey);
    byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(hmacBytes);
  }

  public static @NonNull String getChecksum(@NonNull String message, @NonNull String secretKey)
      throws Exception {
    String algorithm = "HMACSHA256";
    Mac mac;
    try {
      mac = Mac.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new Exception(e);
    }
    SecretKey signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm);
    mac.init(signingKey);
    byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

    StringBuilder hexStringBuilder = new StringBuilder();
    for (byte b : hmacBytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) hexStringBuilder.append('0');
      hexStringBuilder.append(hex);
    }
    return hexStringBuilder.toString();
  }
}
