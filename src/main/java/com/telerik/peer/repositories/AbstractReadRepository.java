package com.telerik.peer.repositories;

import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.repositories.contracts.CRUDRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static java.lang.String.format;

public abstract class  AbstractReadRepository<T> implements CRUDRepository<T> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    protected AbstractReadRepository(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;

    }

    @Override
    public <V> T getByField(String fieldName, V value) {
        final String query = format("from %s where %s =:value", clazz.getSimpleName(), fieldName);
        final String notFoundErrorMessage = format("%s with %s %s not found", clazz.getSimpleName(), fieldName, value);

        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, clazz)
                    .setParameter("value", value)
                    .list().stream().findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(notFoundErrorMessage));
        }
    }

    @Override
    public T getById(long id) {
        return getByField("id", id);

    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s ", clazz.getName()), clazz).list();
        }
    }
}
