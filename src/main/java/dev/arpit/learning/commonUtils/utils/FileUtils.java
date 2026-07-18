package dev.arpit.learning.commonUtils.utils;

import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogFieldConstants;
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
      logger.error(
          CommonUtilLogConstants.EMPTY_FILE_PATH_FOUND,
          CommonUtilLogFieldConstants.FILE_PATH,
          filePath);
      return false;
    }

    File file = new File(filePath);
    if (!file.exists() || !Files.isReadable(file.toPath())) {
      logger.error(CommonUtilLogConstants.FILE_NOT_EXIST_OR_NOT_READABLE);
      return false;
    }

    return true;
  }
}
