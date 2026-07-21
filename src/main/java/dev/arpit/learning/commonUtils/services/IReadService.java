package dev.arpit.learning.commonUtils.services;

import java.util.List;

public interface IReadService<T, ID> {
  List<T> getAll();

  T getById(ID id);
}
