package dev.arpit.learning.commonUtils.services;

import java.util.List;

public interface ICRUDService<T, ID> {
  List<T> getAll();

  T getById(ID id);

  T create(T t);

  T update();

  T delete(ID id);
}
