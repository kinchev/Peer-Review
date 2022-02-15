package com.telerik.peer.repositories;

import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractCRUDRepository<User> implements UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class,sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> search(Optional<String> username, Optional<String> email, Optional<String> number) {

        try (Session session = sessionFactory.openSession()) {
            List<User> users = new ArrayList<>();

            username.ifPresent(value -> {
                Query<User> query = session.createQuery("from User where username like :username",
                        User.class);
                query.setParameter("username", "%" + value + "%");
                users.addAll(query.list());

            });

            email.ifPresent(value -> {
                Query<User> query = session.createQuery("from User where email like :email", User.class);
                query.setParameter("email", "%" + value + "%");
                users.addAll(query.list());

            });

            number.ifPresent(value -> {
                Query<User> query = session.createQuery("from User where number like :number", User.class);
                query.setParameter("number", "%" + value + "%");
                users.addAll(query.list());

            });

            return users;
        }



    }

    @Override
    public User findByEmailId(String emailId) {
        return null;
    }

}
