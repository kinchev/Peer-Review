package com.telerik.peer.services;

import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.services.contracts.CRUDService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements CRUDService {
    TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public List getAll() {
        return teamRepository.getAll();
    }

    @Override
    public Object getById(long id) {
        return teamRepository.getById(id);
    }

    @Override
    public void delete(long id, User owner) {

    }

    @Override
    public void create(Object entity) {

    }

    @Override
    public void update(Object entity, User owner) {

    }

    @Override
    public Object getByField(String fieldName, Object value) {
        return null;
    }
}
