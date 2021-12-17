package com.telerik.peer.services.contracts;

import com.telerik.peer.models.Status;

import java.util.List;

public interface StatusService{

    List<Status> getAll();

    Status getById(long id);

    <V> Status getByField(String fieldName, V value);
}
