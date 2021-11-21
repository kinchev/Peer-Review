package com.telerik.peer.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractCRUDRepository<T> extends AbstractReadRepository<T> {

    private final SessionFactory sessionFactory;

    protected AbstractCRUDRepository(Class<T> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public void delete(long id) {
        T toDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(toDelete);
            session.getTransaction().commit();

        }
    }

    public void create(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }

    }
}