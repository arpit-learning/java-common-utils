package dev.arpit.learning.commonUtils.utils.json;

import com.jayway.jsonpath.JsonPath;
import org.springframework.lang.NonNull;

public class JsonPathUtils {
  public static <T> @NonNull T read(@NonNull Object json, @NonNull String jsonPath) {
    return JsonPath.read(json, jsonPath);
  }
}
