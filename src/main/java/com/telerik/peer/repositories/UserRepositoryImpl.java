package com.telerik.peer.repositories;

import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class UserRepositoryImpl extends AbstractCRUDRepository<User> implements UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class,sessionFactory);
        this.sessionFactory = sessionFactory;
    }
//    public User getByUsername(String username) {
//        try(Session session = sessionFactory.openSession()){
//            Query<User> query = session.createQuery()
//        }
//
//    }
//    @Override
//    public List<User> getAll() {
//        try (Session session = sessionFactory.openSession()) {
//            return session
//                    .createQuery("from User", User.class)
//                    .list();
//        }
//    }
//
//    @Override
//    public User getById(long id) {
//        try (Session session = sessionFactory.openSession()) {
//            User user = session.get(User.class, id);
//            if (user == null) {
//                throw new EntityNotFoundException("User", id);
//            }
//            return user;
//        }
//    }
//
//    @Override
//    public void create(User user) {
//        try (Session session = sessionFactory.openSession()) {
//            session.save(user);
//        }
//    }
//
//    @Override
//    public void update(User user) {
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.update(user);
//            session.getTransaction().commit();
//        }
//    }
//
//    @Override
//    public void delete(long id) {
//        User userToDelete = getById(id);
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.delete(userToDelete);
//            session.getTransaction().commit();
//        }
//    }
}
