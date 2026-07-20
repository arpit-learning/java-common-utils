package dev.arpit.learning.commonUtils.utils.crypto;

import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.util.Collections;
import lombok.NonNull;
import org.springframework.util.StringUtils;

public class MaskingUtils {
  private static final ILogger logger = LoggerFactory.getLogger(MaskingUtils.class);

  public static @NonNull String getMaskedName(@NonNull String name) {
    logger.info(LogConstant.MASKING_NAME_STARTED);
    StringBuilder maskedNameBuilder = new StringBuilder();
    boolean firstWord = true;
    if (StringUtils.hasText(name)) {
      String[] words = name.split("\\W+");
      for (String word : words) {
        if (word.length() > 1) {
          String temp =
              word.charAt(0)
                  + String.join("", Collections.nCopies(word.length() - 2, "*"))
                  + word.charAt(word.length() - 1);
          maskedNameBuilder.append(firstWord ? temp : " " + temp);
        } else {
          maskedNameBuilder.append(firstWord ? word : " " + word);
        }
        firstWord = false;
      }
    }
    logger.info(LogConstant.MASKING_NAME_DONE);
    return maskedNameBuilder.toString();
  }

  public static @NonNull String getMaskedEmail(@NonNull String email) {
    logger.info(LogConstant.MASKING_EMAIL_STARTED);
    StringBuilder maskedEmailBuilder = new StringBuilder();
    if (StringUtils.hasText(email)) {
      String emailHead = email.substring(0, email.lastIndexOf("@"));
      String emailTail = email.substring(email.lastIndexOf("@"));
      if (emailHead.length() > 1) {
        emailHead =
            emailHead.charAt(0)
                + String.join("", Collections.nCopies(emailHead.length() - 2, "*"))
                + emailHead.charAt(emailHead.length() - 1);
      }
      maskedEmailBuilder.append(emailHead).append(emailTail);
    }
    logger.info(LogConstant.MASKING_EMAIL_DONE);
    return maskedEmailBuilder.toString();
  }

  public static @NonNull String getMaskedMobileNumber(@NonNull String mobileNumber) {
    logger.info(LogConstant.MASKING_MOBILE_STARTED);
    String maskedMobileNumber = "";
    if (StringUtils.hasText(mobileNumber)) {
      maskedMobileNumber =
          mobileNumber.substring(0, 2)
              + String.join("", Collections.nCopies(mobileNumber.length() - 4, "*"))
              + mobileNumber.substring(mobileNumber.length() - 2);
    }
    logger.info(LogConstant.MASKING_MOBILE_DONE);
    return maskedMobileNumber;
  }
}
