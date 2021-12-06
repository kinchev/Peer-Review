package com.telerik.peer.repositories;

import com.telerik.peer.models.Team;
import com.telerik.peer.repositories.contracts.TeamRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TeamRepositoryImpl extends AbstractCRUDRepository<Team> implements TeamRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public TeamRepositoryImpl(SessionFactory sessionFactory) {
        super(Team.class,sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
