package dev.arpit.learning.commonUtils.utils.domain;

import static dev.arpit.learning.commonUtils.utils.core.StringUtils.isNotNullOrEmpty;
import static dev.arpit.learning.commonUtils.utils.core.StringUtils.isNullOrEmpty;

import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.commonUtils.utils.core.ListConversionUtils;
import dev.arpit.learning.commonUtils.utils.core.ValidationUtils;
import dev.arpit.learning.commonUtils.utils.json.JsonUtils;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import org.springframework.core.ResolvableType;

public class DimensionValidationUtils {
  private static final ILogger logger = LoggerFactory.getLogger(DimensionValidationUtils.class);
  private static final ResolvableType stringResolvableType = ResolvableType.forClass(String.class);

  private static String getComplexDimensionAttribute(String complexDimensionAttribute) {
    if (isNullOrEmpty(complexDimensionAttribute)) {
      return "";
    }

    return complexDimensionAttribute.replace(" ", "_").trim();
  }

  private static String validateDuplicateComplexDimensionAttributes(
      List<String> complexDimensionAttributesList) {
    Set<String> complexDimensionAttributesSet = new HashSet<>(complexDimensionAttributesList);

    if (complexDimensionAttributesList.size() != complexDimensionAttributesSet.size()) {
      return "Duplicate attribute is found in complex dimension attributes as given in "
          + JsonUtils.toJsonString(complexDimensionAttributesList);
    }

    return "";
  }

  public static @NonNull Map<String, Object> validateComplexDimensionAttribute(
      String complexDimensionAttribute, Set<String> categoryAttributeSet, String delimiter) {
    Map<String, Object> response = new HashMap<>();
    List<String> errMsgList = new ArrayList<>();

    try {
      if (ValidationUtils.isNullOrEmpty(
          new Pair<>(complexDimensionAttribute, stringResolvableType))) {
        response.put("complex_dim_attr", complexDimensionAttribute);
        return response;
      }
    } catch (EmptyValidatorNotFoundException e) {
      logger.error("EmptyValidatorNotFoundException", e);
    }
    String[] complexDimAttrArray = complexDimensionAttribute.trim().split(delimiter);
    String[] tempComplexDimArray = new String[complexDimAttrArray.length];

    int j = 0;
    for (int i = 0; i < complexDimAttrArray.length; i++) {
      String orgComplexDimAttr =
          complexDimAttrArray[i].trim(); // As few categories have space in attributes
      String curComplexDimAttr = getComplexDimensionAttribute(complexDimAttrArray[i].trim());
      try {
        if (ValidationUtils.isNullOrEmpty(new Pair<>(curComplexDimAttr, stringResolvableType))) {
          continue;
        }
      } catch (EmptyValidatorNotFoundException e) {
        logger.error("EmptyValidatorNotFoundException", e);
      }
      complexDimAttrArray[i] = curComplexDimAttr;
      tempComplexDimArray[j++] = complexDimAttrArray[i];

      if (categoryAttributeSet != null
          && ((!categoryAttributeSet.contains(curComplexDimAttr))
              && (!categoryAttributeSet.contains(orgComplexDimAttr)))) {
        errMsgList.add(
            "Complex Dim attribute = "
                + orgComplexDimAttr
                + ", is not part of category attribute value. ");
      }
      if (categoryAttributeSet != null
          && categoryAttributeSet.contains(
              orgComplexDimAttr)) { // added the code to handle attributes with space as
        // getComplexDimAttr() replaces space with "_"
        int index = j - 1;
        tempComplexDimArray[index] = orgComplexDimAttr;
      }
    }

    List<String> tempComplexDimAttrList = new ArrayList<>(Arrays.asList(tempComplexDimArray));
    ListConversionUtils.removeNullEntries(tempComplexDimAttrList);
    errMsgList =
        new ArrayList<>(new HashSet<>(errMsgList)); // remove the repeated message in errMsgList

    if (!errMsgList.isEmpty()) {
      response.put("error", errMsgList);
      return response;
    }

    String errMsg = validateDuplicateComplexDimensionAttributes(tempComplexDimAttrList);
    if (isNotNullOrEmpty(errMsg)) {
      errMsgList.add(errMsg);
    }

    if (errMsgList.isEmpty()) {
      response.put(
          "complex_dim_attr",
          dev.arpit.learning.commonUtils.utils.core.StringUtils.join(
              tempComplexDimAttrList, delimiter));
      return response;
    }
    response.put("error", errMsgList);
    return response;
  }
}
