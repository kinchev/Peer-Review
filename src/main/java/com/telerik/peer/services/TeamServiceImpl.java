package com.telerik.peer.services;

import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.services.contracts.CRUDService;
import com.telerik.peer.services.contracts.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    TeamRepository teamRepository;
@Autowired
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    @Override
    public List<Team> getAll() {
        return teamRepository.getAll();
    }
    @Override
    public Team getById(long id) {
        return teamRepository.getById(id);
    }

    @Override
    public void delete(long id, User owner) {

    }

    @Override
    public void create(Team entity) {

    }

    @Override
    public void update(Team entity, User owner) {

    }

    @Override
    public <V> Team getByField(String fieldName, V value) {
        return teamRepository.getByField(fieldName, value);
    }
}
