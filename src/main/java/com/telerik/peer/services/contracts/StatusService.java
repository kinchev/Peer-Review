package com.telerik.peer.services.contracts;

import com.telerik.peer.models.Status;

public interface StatusService{

    Status getById(long id);

    <V> Status getByField(String fieldName, V value);
}
