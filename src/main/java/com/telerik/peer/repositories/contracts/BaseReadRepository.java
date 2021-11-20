package com.telerik.peer.repositories.contracts;

import java.util.List;

public interface BaseReadRepository<T> {
    List<T> getAll();

    T getById(long id);

    <V> T getByField(String fieldName, V value);
}
