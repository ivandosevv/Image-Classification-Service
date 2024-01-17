package com.service.classification.image.repository;

import java.util.List;

public interface Repository<T, ID> {
    T get(ID id);

    T delete(ID id);

    List<T> getAll();

    T save(ID id, T t);
}
