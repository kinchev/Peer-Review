package com.telerik.peer.repositories;

import org.hibernate.SessionFactory;

public abstract class AbstractCRUDRepository<T> extends AbstractReadRepository<T>{

   private final SessionFactory sessionFactory;

    protected AbstractCRUDRepository(Class<T> clazz,SessionFactory sessionFactory) {
        super(clazz,sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
