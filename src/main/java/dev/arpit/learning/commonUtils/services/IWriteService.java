package dev.arpit.learning.commonUtils.services;

public interface IWriteService<T, ID> {
  T create(T t);

  T update(ID id, T t);

  T delete(ID id);
}
