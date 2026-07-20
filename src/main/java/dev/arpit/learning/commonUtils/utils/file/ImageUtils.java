package dev.arpit.learning.commonUtils.utils.file;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.NonNull;

public class ImageUtils {
  public static @NonNull Image downloadImage(@NonNull String urlString) throws IOException {
    URL url = URI.create(urlString).toURL(); // urlString = "file:/home/sachin/image.jpg";
    return ImageIO.read(url);
  }

  public static byte[] getImageBytes(@NonNull BufferedImage image, @NonNull String type)
      throws IOException {
    ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
    ImageIO.write(image, type, bAOS);
    return bAOS.toByteArray();
  }
}
