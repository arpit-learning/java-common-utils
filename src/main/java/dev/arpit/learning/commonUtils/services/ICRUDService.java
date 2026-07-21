package dev.arpit.learning.commonUtils.services;

public interface ICRUDService<T, ID> extends IReadService<T, ID>, IWriteService<T, ID> {}
