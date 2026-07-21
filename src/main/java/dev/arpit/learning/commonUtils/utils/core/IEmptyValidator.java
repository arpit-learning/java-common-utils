package dev.arpit.learning.commonUtils.utils.core;

import lombok.NonNull;
import org.springframework.core.ResolvableType;

public interface IEmptyValidator<T> {
  ResolvableType getSupportedResolvedType();

  boolean isEmpty(@NonNull T obj);
}
