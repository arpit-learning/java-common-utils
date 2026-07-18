package dev.arpit.learning.commonUtils.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.f4b6a3.uuid.UuidCreator;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.util.StringUtils;

public class CommonUtils {
  private static final ILogger logger = LoggerFactory.getLogger(CommonUtils.class);
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public static @NonNull UUID generateUUIDV4() {
    return UuidCreator.getRandomBased();
  }

  public static @NonNull UUID generateUUIDV7() {
    return UuidCreator.getTimeOrderedEpoch();
  }

  public static boolean isEmpty(String string) {
    return !StringUtils.hasText(string);
  }

  public static boolean isEmpty(String[] arr) {
    return Stream.of(arr).map(i -> !StringUtils.hasText(i)).reduce(true, (i, j) -> i && j);
  }

  public static boolean isEmpty(JsonNode textNode) {
    return textNode == null || textNode.isNull() || isEmpty(textNode.textValue());
  }

  public static boolean isEmpty(Object[] objArr) {
    return (objArr == null || objArr.length == 0);
  }

  public static <T> boolean isEmpty(List<T> objList) {
    if (objList == null || objList.isEmpty()) {
      return true;
    }

    for (Object o : objList) {
      if (o != null) {
        return false;
      }
    }

    return true;
  }

  public static boolean isNotEmpty(String string) {
    return !isEmpty(string);
  }

  public static boolean isNotEmpty(JsonNode textNode) {
    return !isEmpty(textNode);
  }

  public static boolean isNotEmpty(Object[] objArr) {
    return !isEmpty(objArr);
  }

  public static <T> boolean isNotEmpty(List<T> objList) {
    return !isEmpty(objList);
  }

  public static @NonNull String getFileExtension(@NonNull String fileName) {
    try {
      URL url = URI.create(fileName).toURL();
      String filename = StringUtils.getFilenameExtension(url.getPath());
      return filename != null ? filename : "";
    } catch (MalformedURLException e) {
      logger.error(CommonUtilLogConstants.GET_FILE_EXTENSION_FAILED, e);
      return "";
    }
  }

  public static boolean checkStringStartsWithRegexPattern(
      @NonNull String regex, @NonNull String string) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(string);
    boolean matchExists = matcher.find();
    return matchExists && (matcher.start() == 0);
  }

  public static boolean checkStringMatchesWithRegexPattern(
      @NonNull String regex, @NonNull String string) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(string);
    return matcher.find();
  }

  public static boolean hasJsonKeyAndValueIsNotNullAndIsTextual(
      @NonNull JsonNode jsonNode, @NonNull String key) {
    return (jsonNode.has(key) && !jsonNode.path(key).isNull() && jsonNode.path(key).isTextual());
  }

  public static boolean hasJsonKeyAndValueIsNull(
      @NonNull JsonNode jsonNode, @NonNull String jsonKey) {
    return (jsonNode.has(jsonKey) && jsonNode.path(jsonKey).isNull());
  }

  public static LocalDateTime getTodayWithZeroTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDateTime.parse(LocalDateTime.now().format(formatter));
  }

  public static <T> List<T> removeNullEntries(@NonNull List<T> objectList) {
    objectList.removeIf(Objects::isNull);
    return objectList;
  }

  public static int getNonEmptyStringsCount(@NonNull String[] inputArr) {
    int count = 0;
    for (String s : inputArr) {
      if (isNotEmpty(s)) {
        count++;
      }
    }

    return count;
  }

  public static @NonNull List<String> getNonEmptyStrings(@NonNull ArrayNode stringArrayNode) {
    List<String> nonEmptyStrings = new ArrayList<>();
    for (JsonNode jsonNode : stringArrayNode) {
      if (isNotEmpty(jsonNode.textValue())) {
        nonEmptyStrings.add(jsonNode.textValue());
      }
    }
    return nonEmptyStrings;
  }

  public static <T> @NonNull String getTokenSeparatedString(
      @NonNull List<T> objects, @NonNull String token) {
    if (isEmpty(objects) || isEmpty(token)) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    for (Object curr : objects) {
      sb.append(curr);
      sb.append(token);
    }
    sb.delete(sb.length() - token.length(), sb.length());
    return sb.toString();
  }

  public static int roundOff(float value) {
    return Math.round(value);
  }

  public static @NonNull String capitalizeEachWord(@NonNull String text) {
    String[] words = text.split(" ");
    for (int i = 0; i < words.length; i++) {
      String word = words[i];
      word = StringUtils.capitalize(word);
      words[i] = word;
    }

    return String.join(" ", words);
  }

  private static String getComplexDimAttr(String complexDimAttr) {
    if (!StringUtils.hasText(complexDimAttr)) {
      return "";
    }

    return complexDimAttr.replace(" ", "_").trim();
  }

  private static void validateDuplicateComplexDimAttr(
      List<String> tempComplexDimAttrList, List<String> errMsgList) {
    Set<String> complexDimAttrSet = new HashSet<String>(tempComplexDimAttrList);
    if (tempComplexDimAttrList.size() != complexDimAttrSet.size()) {
      errMsgList.add(
          "Duplicate attribute is found in complex dim attribute as given "
              + JsonUtils.toJsonString(tempComplexDimAttrList));
    }
  }

  public static @NonNull Map<String, Object> validateComplexDimAttr(
      String complexDimAttr, Set<String> categoryAttrSet, String TOKENSEPERATOR) {
    Map<String, Object> response = new HashMap<>();
    List<String> errMsgList = new ArrayList<>();

    if (isEmpty(complexDimAttr)) {
      response.put("complex_dim_attr", complexDimAttr);
      return response;
    }
    String[] complexDimAttrArray = complexDimAttr.trim().split(TOKENSEPERATOR);
    String[] tempComplexDimArray = new String[complexDimAttrArray.length];

    if (complexDimAttrArray.length < 1) {
      /*
       * errMsgList.add("Complex Dim attribute is invalid as given "+jsonUtils.
       * toJsonString(complexDimAttr));
       */
      response.put("complex_dim_attr", "");
      return response;
    }
    int j = 0;
    for (int i = 0; i < complexDimAttrArray.length; i++) {
      String orgComplexDimAttr =
          complexDimAttrArray[i].trim(); // As few categories have space in attributes
      String curComplexDimAttr = getComplexDimAttr(complexDimAttrArray[i].trim());
      if (isEmpty(curComplexDimAttr)) {
        continue;
      }
      complexDimAttrArray[i] = curComplexDimAttr;
      tempComplexDimArray[j++] = complexDimAttrArray[i];

      if (categoryAttrSet != null
          && ((!categoryAttrSet.contains(curComplexDimAttr))
              && (!categoryAttrSet.contains(orgComplexDimAttr)))) {
        errMsgList.add(
            "Complex Dim attribute = "
                + orgComplexDimAttr
                + ", is not part of category attribute value. ");
      }
      if (categoryAttrSet != null
          && categoryAttrSet.contains(
              orgComplexDimAttr)) { // added the code to handle attributes with space as
        // getComplexDimAttr() replaces space with "_"
        int index = j - 1;
        tempComplexDimArray[index] = orgComplexDimAttr;
      }
    }

    List<String> tempComplexDimAttrList = new ArrayList<>(Arrays.asList(tempComplexDimArray));
    removeNullEntries(tempComplexDimAttrList);
    errMsgList =
        new ArrayList<>(new HashSet<>(errMsgList)); // remove the repeated message in errMsgList

    if (!errMsgList.isEmpty()) {
      response.put("error", errMsgList);
      return response;
    }

    validateDuplicateComplexDimAttr(tempComplexDimAttrList, errMsgList);
    if (errMsgList.isEmpty()) {
      response.put(
          "complex_dim_attr", getTokenSeparatedString(tempComplexDimAttrList, TOKENSEPERATOR));
      return response;
    }
    response.put("error", errMsgList);
    return response;
  }

  public static <T> void putInMap(
      @NonNull Map<String, List<T>> map, @NonNull String key, @NonNull List<T> value) {
    if (map.containsKey(key)) {
      map.get(key).addAll(value);
    } else {
      map.put(key, value);
    }
  }

  public static @NonNull String trimIfNotNull(String checkStr) {
    if (checkStr == null) return "";
    return checkStr.trim();
  }

  public static boolean checkFloatContainsDecimal(Float value) {
    if (value == null) {
      return false;
    }

    return value - value.intValue() != 0;
  }

  public static String[] splitIfNotNull(String str, @NonNull String tokenSeparator) {
    String trimmed = trimIfNotNull(str);
    return trimmed.split(tokenSeparator);
  }

  public static boolean isValidUrl(String url) {
    try {
      URI.create(url);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean returnRandom(int totalRange, int rangeBoundary) {
    int r = SECURE_RANDOM.nextInt(10_000_000);
    int m = r % totalRange;
    return m < rangeBoundary;
  }
}
