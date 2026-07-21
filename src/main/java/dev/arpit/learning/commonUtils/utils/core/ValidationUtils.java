package dev.arpit.learning.commonUtils.utils.core;

import dev.arpit.learning.commonUtils.constants.LogConstant;
import dev.arpit.learning.commonUtils.exceptions.EmptyValidatorNotFoundException;
import dev.arpit.learning.commonUtils.models.Pair;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.util.*;
import lombok.NonNull;
import org.springframework.core.ResolvableType;
import org.springframework.util.StringUtils;

public class ValidationUtils {
  private static final ILogger logger = LoggerFactory.getLogger(ValidationUtils.class);

  private static final Map<ResolvableType, IEmptyValidator<?>> validators = new HashMap<>();

  static {
    registerValidator(
        new IEmptyValidator<String>() {
          @Override
          public ResolvableType getSupportedResolvedType() {
            return ResolvableType.forClass(String.class);
          }

          @Override
          public boolean isEmpty(@NonNull String obj) {
            return !StringUtils.hasText(obj);
          }
        });

    registerValidator(
        new IEmptyValidator<String[]>() {
          @Override
          public ResolvableType getSupportedResolvedType() {
            return ResolvableType.forClass(String[].class);
          }

          @Override
          public boolean isEmpty(@NonNull String @NonNull [] obj) {
            for (String str : obj) {
              if (StringUtils.hasText(str)) {
                return false;
              }
            }
            return true;
          }
        });

    registerValidator(
        new IEmptyValidator<List<String>>() {
          @Override
          public ResolvableType getSupportedResolvedType() {
            return ResolvableType.forClassWithGenerics(List.class, String.class);
          }

          @Override
          public boolean isEmpty(@NonNull List<@NonNull String> obj) {
            for (String str : obj) {
              if (StringUtils.hasText(str)) {
                return false;
              }
            }
            return true;
          }
        });

    registerValidator(
        new IEmptyValidator<List<?>>() {
          @Override
          public ResolvableType getSupportedResolvedType() {
            return ResolvableType.forClass(List.class);
          }

          @Override
          public boolean isEmpty(@NonNull List<?> obj) {
            return obj.isEmpty();
          }
        });

    registerValidator(
        new IEmptyValidator<Map<?, ?>>() {
          @Override
          public ResolvableType getSupportedResolvedType() {
            return ResolvableType.forClass(Map.class);
          }

          @Override
          public boolean isEmpty(@NonNull Map<?, ?> obj) {
            return obj.isEmpty();
          }
        });

    registerValidator(
        new IEmptyValidator<Set<?>>() {
          @Override
          public ResolvableType getSupportedResolvedType() {
            return ResolvableType.forClass(Set.class);
          }

          @Override
          public boolean isEmpty(@NonNull Set<?> obj) {
            return obj.isEmpty();
          }
        });
  }

  public static void registerValidator(IEmptyValidator<?> validator) {
    validators.put(validator.getSupportedResolvedType(), validator);
  }

  private static boolean isNullOrEmpty(Object obj, ResolvableType resolvableType)
      throws EmptyValidatorNotFoundException {
    if (obj == null) return true;

    if (validators.containsKey(resolvableType)) {
      @SuppressWarnings("unchecked")
      IEmptyValidator<Object> validator = (IEmptyValidator<Object>) validators.get(resolvableType);
      return validator.isEmpty(obj);
    }

    for (Map.Entry<ResolvableType, IEmptyValidator<?>> entry : validators.entrySet()) {
      if (entry.getKey().isAssignableFrom(resolvableType)) {
        @SuppressWarnings("unchecked")
        IEmptyValidator<Object> validator = (IEmptyValidator<Object>) entry.getValue();
        return validator.isEmpty(obj);
      }
    }

    logger.error(
        LogConstant.VALIDATOR_NOT_FOUND,
        " for object: ",
        obj,
        " of resolvableType: ",
        resolvableType);
    throw new EmptyValidatorNotFoundException("Validator not found for object: " + obj);
  }

  @SafeVarargs
  public static boolean isNullOrEmpty(Pair<Object, ResolvableType>... args)
      throws EmptyValidatorNotFoundException {
    if (args == null) return true;

    for (Pair<Object, ResolvableType> arg : args) {
      if (arg == null) return true;
      if (isNullOrEmpty(arg.first(), arg.second())) {
        return true;
      }
    }
    return false;
  }
}
