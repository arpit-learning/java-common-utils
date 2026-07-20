package dev.arpit.learning.commonUtils.utils.file;

import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.commonUtils.constants.LogConstantFields;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.File;
import java.nio.file.Files;
import lombok.NonNull;
import org.springframework.util.StringUtils;

public class FileUtils {
  private static final ILogger logger = LoggerFactory.getLogger(FileUtils.class);

  public static @NonNull String getFileNameExcludingExtension(@NonNull String inputFileName) {
    return StringUtils.stripFilenameExtension(StringUtils.getFilename(inputFileName));
  }

  public static boolean createDirectoryIfNotExist(@NonNull String directoryName) {
    File directory = new File(directoryName);
    if (!directory.exists()) {
      return directory.mkdirs();
    }

    return true;
  }

  public static @NonNull String generateFilenameWithPrefix(
      @NonNull String prefix, @NonNull String delimiter, @NonNull String fileName) {
    return prefix + delimiter + fileName;
  }

  public static boolean isFileValid(@NonNull String filePath) {
    if (filePath.isEmpty()) {
      logger.error(LogConstant.EMPTY_FILE_PATH_FOUND, LogConstantFields.FILE_PATH, filePath);
      return false;
    }

    File file = new File(filePath);
    if (!file.exists() || !Files.isReadable(file.toPath())) {
      logger.error(LogConstant.FILE_NOT_EXIST_OR_NOT_READABLE);
      return false;
    }

    return true;
  }

  public static @NonNull String getFileExtension(@NonNull String fileName) {
    try {
      java.net.URL url = java.net.URI.create(fileName).toURL();
      String filename = StringUtils.getFilenameExtension(url.getPath());
      return filename != null ? filename : "";
    } catch (java.net.MalformedURLException e) {
      logger.error(LogConstant.GET_FILE_EXTENSION_FAILED, e);
      return "";
    }
  }

  public static String readFileToString(@NonNull String filePath) {
    try {
      return Files.readString(new File(filePath).toPath());
    } catch (java.io.IOException e) {
      logger.error(LogConstant.EXCEPTION, LogConstantFields.ERR_MSG, e.getMessage());
      return null;
    }
  }

  public static void writeStringToFile(@NonNull String filePath, @NonNull String content) {
    try {
      Files.writeString(new File(filePath).toPath(), content);
    } catch (java.io.IOException e) {
      logger.error(LogConstant.WRITE_TO_FILE_IO_EXC, e.getMessage());
    }
  }

  public static boolean deleteFileSilently(@NonNull String filePath) {
    try {
      return Files.deleteIfExists(new File(filePath).toPath());
    } catch (java.io.IOException e) {
      logger.error(LogConstant.EXCEPTION, LogConstantFields.ERR_MSG, e.getMessage());
      return false;
    }
  }
}
