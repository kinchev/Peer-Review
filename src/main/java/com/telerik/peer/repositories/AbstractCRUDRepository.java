package com.telerik.peer.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    protected List<T> executeQuery(String queryStr, Map<String, Object> parameters) {
        try (Session session = sessionFactory.openSession()) {

            Query<T> query = session.createQuery(queryStr, clazz);
            query.setProperties(parameters);
            return query.getResultList();
        }
    }

    protected Optional<T> executeQueryFindOne(String queryStr, Map<String, Object> parameters) {
        List<T> result = executeQuery(queryStr, parameters);
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(result.get(0));
    }
}
