package com.telerik.peer.services;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.services.contracts.CRUDService;
import com.telerik.peer.services.contracts.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    public static final String ONLY_OWNER_AUTHORIZED = "Only the team owner is authorized to do this action!";
    private final TeamRepository teamRepository;

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
    public <V> Team getByField(String fieldName, V value) {
        return teamRepository.getByField(fieldName, value);
    }

    @Override
    public void delete(long id, User user) {
        Team team = getById(id);
        if (!user.equals(team.getOwner())) {
            throw new UnsupportedOperationException(ONLY_OWNER_AUTHORIZED);
        }
        teamRepository.delete(id);
    }

    @Override
    public void create(Team entity) {
        boolean duplicateExists = true;
        try {
            teamRepository.getByField("teamName", entity.getTeamName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Team", "name", entity.getTeamName());
        }
        User owner = entity.getOwner();
        var members = entity.getMembers();
        members.add(owner);
        entity.setMembers(members);
        teamRepository.create(entity);
    }

    @Override
    public void update(Team entity, User user) {
        if (!user.equals(entity.getOwner())) {
            throw new UnauthorizedOperationException(ONLY_OWNER_AUTHORIZED);
        }
        boolean duplicateMailExists = true;
        try {
            Team existingTeam = teamRepository.getByField("name", entity.getTeamName());
            if (existingTeam.getTeamId() == entity.getTeamId()) {
                duplicateMailExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateMailExists = false;
        }
        if (duplicateMailExists) {
            throw new DuplicateEntityException("Team", "name", entity.getTeamName());
        }


        teamRepository.update(entity);
    }


    @Override
    public void addMemberToTeam(Team team, User user, User userToAdd) {
        var newMemberList = team.getMembers();

        if (newMemberList.contains(userToAdd)) {
            throw new DuplicateEntityException("User", "username", userToAdd.getUsername());
        }
        newMemberList.add(userToAdd);
        team.setMembers(newMemberList);
        teamRepository.update(team);
    }

    @Override
    public void removeMemberFromTeam(Team team, User user, User userToRemove) {
        var newMemberList = team.getMembers();
        if (!newMemberList.contains(userToRemove)) {
            throw new EntityNotFoundException
                    (String.format("The user with id %d is not a member of team %d", userToRemove.getId(), team.getTeamId()));
        }
        if (userToRemove.equals(team.getOwner())) {
            throw new UnauthorizedOperationException("The team owner cannot be removed");
        }
        newMemberList.remove(userToRemove);
        team.setMembers(newMemberList);
        teamRepository.update(team);
    }


    @Override
    public void addWorkitemToTeam(Team team, User user, WorkItem workitem) {
        var newWorkitemList = team.getWorkItems();
        if (newWorkitemList.contains(workitem)) {
            throw new DuplicateEntityException("Workitem", "name", workitem.getTitle());
        }
        newWorkitemList.add(workitem);
        team.setWorkItems(newWorkitemList);
        teamRepository.update(team);
    }



}

