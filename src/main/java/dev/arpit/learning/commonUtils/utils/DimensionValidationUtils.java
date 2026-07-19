package dev.arpit.learning.commonUtils.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import org.springframework.util.StringUtils;

public class DimensionValidationUtils {

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
      String complexDimAttr, Set<String> categoryAttrSet, String tokenSeperator) {
    Map<String, Object> response = new HashMap<>();
    List<String> errMsgList = new ArrayList<>();

    if (CommonUtils.isEmpty(complexDimAttr)) {
      response.put("complex_dim_attr", complexDimAttr);
      return response;
    }
    String[] complexDimAttrArray = complexDimAttr.trim().split(tokenSeperator);
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
      if (CommonUtils.isEmpty(curComplexDimAttr)) {
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
    CommonUtils.removeNullEntries(tempComplexDimAttrList);
    errMsgList =
        new ArrayList<>(new HashSet<>(errMsgList)); // remove the repeated message in errMsgList

    if (!errMsgList.isEmpty()) {
      response.put("error", errMsgList);
      return response;
    }

    validateDuplicateComplexDimAttr(tempComplexDimAttrList, errMsgList);
    if (errMsgList.isEmpty()) {
      response.put(
          "complex_dim_attr",
          dev.arpit.learning.commonUtils.utils.StringUtils.getTokenSeparatedString(
              tempComplexDimAttrList, tokenSeperator));
      return response;
    }
    response.put("error", errMsgList);
    return response;
  }
}
