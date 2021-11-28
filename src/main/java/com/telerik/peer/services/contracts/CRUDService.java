package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;

import java.util.List;

public interface CRUDService<T> {
    List<T> getAll();

    T getById(long id);

    void delete(long id, User owner);

    void create(T entity);

    void update(T entity, User owner);


    <V> T getByField(String fieldName, V value);
}
