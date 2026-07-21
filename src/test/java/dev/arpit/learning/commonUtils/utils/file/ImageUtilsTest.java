package dev.arpit.learning.commonUtils.utils.file;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ImageUtilsTest {
  @Test
  void test_downloadImage(@TempDir Path tempDir) throws IOException {
    // Create a dummy image
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    File tempImg = tempDir.resolve("test.jpg").toFile();
    ImageIO.write(img, "jpg", tempImg);

    String url = "file://" + tempImg.getAbsolutePath();
    Image downloaded = ImageUtils.downloadImage(url);
    assertNotNull(downloaded);
  }

  @Test
  void test_downloadImage_invalidUrl() {
    assertThrows(Exception.class, () -> ImageUtils.downloadImage("invalid_url"));
  }

  @Test
  void test_downloadImage_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> ImageUtils.downloadImage(null));
  }

  @Test
  void test_getImageBytes() throws IOException {
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    byte[] bytes = ImageUtils.getImageBytes(img, "jpg");
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
  }

  @Test
  void test_getImageBytes_withNulls() {
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    assertThrows(NullPointerException.class, () -> ImageUtils.getImageBytes(null, "jpg"));
    assertThrows(NullPointerException.class, () -> ImageUtils.getImageBytes(img, null));
  }

  @Test
  void test_objectCreation() {
    ImageUtils utils = new ImageUtils();
    assertNotNull(utils);
  }
}
