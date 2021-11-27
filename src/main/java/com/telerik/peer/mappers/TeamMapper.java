package com.telerik.peer.mappers;

import com.telerik.peer.models.Team;
import com.telerik.peer.models.dto.TeamDto;
import com.telerik.peer.repositories.contracts.TeamRepository;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    private TeamRepository teamRepository;


    public TeamMapper(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
    public TeamDto teamToDto(Team team) {

    }
}
