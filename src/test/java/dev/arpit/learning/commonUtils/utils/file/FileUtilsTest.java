package dev.arpit.learning.commonUtils.utils.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

class FileUtilsTest {
  @Test
  void test_getFileNameExcludingExtension() {
    assertEquals("test", FileUtils.getFileNameExcludingExtension("test.txt"));
    assertEquals("test", FileUtils.getFileNameExcludingExtension("/path/to/test.txt"));
    assertEquals("test", FileUtils.getFileNameExcludingExtension("test"));
  }

  @Test
  void test_getFileNameExcludingExtension_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> FileUtils.getFileNameExcludingExtension(null));
  }

  @Test
  void test_createDirectoryIfNotExist(@TempDir Path tempDir) {
    String newDirPath = tempDir.resolve("newDir").toString();

    // Directory does not exist -> creates it
    assertTrue(FileUtils.createDirectoryIfNotExist(newDirPath));
    assertTrue(new File(newDirPath).exists());

    // Directory exists -> returns true
    assertTrue(FileUtils.createDirectoryIfNotExist(newDirPath));
  }

  @Test
  void test_createDirectoryIfNotExist_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> FileUtils.createDirectoryIfNotExist(null));
  }

  @Test
  void test_generateFilenameWithPrefix() {
    assertEquals(
        "prefix_file.txt", FileUtils.generateFilenameWithPrefix("prefix", "_", "file.txt"));
  }

  @Test
  void test_generateFilenameWithPrefix_withNulls() {
    assertThrows(
        NullPointerException.class,
        () -> FileUtils.generateFilenameWithPrefix(null, "_", "file.txt"));
    assertThrows(
        NullPointerException.class,
        () -> FileUtils.generateFilenameWithPrefix("prefix", null, "file.txt"));
    assertThrows(
        NullPointerException.class,
        () -> FileUtils.generateFilenameWithPrefix("prefix", "_", null));
  }

  @Test
  void test_isFileValid(@TempDir Path tempDir) throws IOException {
    // Empty path
    assertFalse(FileUtils.isFileValid(""));

    // Non-existent path
    assertFalse(FileUtils.isFileValid(tempDir.resolve("nonexistent.txt").toString()));

    // Existent and readable
    File validFile = tempDir.resolve("valid.txt").toFile();
    validFile.createNewFile();
    assertTrue(FileUtils.isFileValid(validFile.getAbsolutePath()));
  }

  @Test
  void test_isFileValid_notReadable(@TempDir Path tempDir) throws IOException {
    File unreadableFile = tempDir.resolve("unreadable.txt").toFile();
    unreadableFile.createNewFile();
    unreadableFile.setReadable(false);
    // On some OS, setReadable(false) might not work for owner, so we mock Files.isReadable just in
    // case
    try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
      mockedFiles.when(() -> Files.isReadable(any())).thenReturn(false);
      assertFalse(FileUtils.isFileValid(unreadableFile.getAbsolutePath()));
    }
  }

  @Test
  void test_isFileValid_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> FileUtils.isFileValid(null));
  }

  @Test
  void test_getFileExtension() {
    // Valid URL
    assertEquals("png", FileUtils.getFileExtension("file:///path/to/image.png"));

    // Valid URL without extension
    assertEquals("", FileUtils.getFileExtension("file:///path/to/image"));

    // Unknown protocol -> MalformedURLException -> returns ""
    assertEquals("", FileUtils.getFileExtension("unknown://xyz/image.png"));

    // Relative URI -> throws IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> FileUtils.getFileExtension("invalid_url"));
  }

  @Test
  void test_getFileExtension_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> FileUtils.getFileExtension(null));
  }

  @Test
  void test_readFileToString(@TempDir Path tempDir) throws IOException {
    File testFile = tempDir.resolve("test.txt").toFile();
    Files.writeString(testFile.toPath(), "hello");

    assertEquals("hello", FileUtils.readFileToString(testFile.getAbsolutePath()));
  }

  @Test
  void test_readFileToString_ioException(@TempDir Path tempDir) {
    // Read directory as file or non-existent file
    assertNull(FileUtils.readFileToString(tempDir.resolve("nonexistent.txt").toString()));
  }

  @Test
  void test_readFileToString_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> FileUtils.readFileToString(null));
  }

  @Test
  void test_writeStringToFile(@TempDir Path tempDir) throws IOException {
    File testFile = tempDir.resolve("test.txt").toFile();
    FileUtils.writeStringToFile(testFile.getAbsolutePath(), "content");

    assertEquals("content", Files.readString(testFile.toPath()));
  }

  @Test
  void test_writeStringToFile_ioException(@TempDir Path tempDir) {
    // Write to a directory path instead of a file
    FileUtils.writeStringToFile(tempDir.toString(), "content");
    // Should catch IOException and just log
  }

  @Test
  void test_writeStringToFile_withNulls() {
    assertThrows(NullPointerException.class, () -> FileUtils.writeStringToFile(null, "content"));
    assertThrows(NullPointerException.class, () -> FileUtils.writeStringToFile("path", null));
  }

  @Test
  void test_deleteFileSilently(@TempDir Path tempDir) throws IOException {
    File testFile = tempDir.resolve("test.txt").toFile();
    testFile.createNewFile();

    assertTrue(FileUtils.deleteFileSilently(testFile.getAbsolutePath()));
    assertFalse(testFile.exists());

    // Delete non-existent
    assertFalse(FileUtils.deleteFileSilently(testFile.getAbsolutePath()));
  }

  @Test
  void test_deleteFileSilently_ioException(@TempDir Path tempDir) throws IOException {
    try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
      mockedFiles.when(() -> Files.deleteIfExists(any())).thenThrow(new IOException("test"));
      assertFalse(FileUtils.deleteFileSilently("path"));
    }
  }

  @Test
  void test_deleteFileSilently_withNull_shouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> FileUtils.deleteFileSilently(null));
  }

  @Test
  void test_objectCreation() {
    FileUtils utils = new FileUtils();
    assertNotNull(utils);
  }
}
