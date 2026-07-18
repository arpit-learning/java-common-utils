package dev.arpit.learning.commonUtils.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.util.StringUtils;

public class ListConversionUtils {
  private static final ILogger logger = LoggerFactory.getLogger(ListConversionUtils.class);
  private static final ObjectMapper mapper = new ObjectMapper();

  public static @NonNull String[] listToArray(@NonNull List<String> listStr) {
    return listStr.toArray(new String[0]);
  }

  public static @NonNull List<String> stringToList(@NonNull String str) {
    logger.info(CommonUtilLogConstants.STRING_TO_LIST_CONVERSION);
    List<String> listStr = new ArrayList<>();
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    if (!StringUtils.hasText(str)) {
      logger.info(CommonUtilLogConstants.EMPTY_STRING_FOUND);
      return listStr;
    }

    JsonNode jsonNode = null;
    try {
      jsonNode = mapper.readTree(str);
    } catch (IOException e) {
      logger.error(CommonUtilLogConstants.STRING_TO_LIST_CONVERSION_EXC, e);
      return listStr;
    }

    if (jsonNode == null) {
      logger.error(CommonUtilLogConstants.UNABLE_TO_PROCESS_STRING);
      return listStr;
    }

    listStr = mapper.convertValue(jsonNode, new TypeReference<>() {});
    return listStr;
  }
}
