package com.telerik.peer.mappers;


import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.models.dto.TeamDto;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.repositories.contracts.WorkItemRepository;
import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    private TeamService teamService;
    private UserService userService;


    @Autowired
    public TeamMapper(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    public TeamDto TeamToDto(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamName(team.getTeamName());
        teamDto.setOwnerId(team.getOwner().getId());
        return teamDto;
    }

    public Team fromDto(TeamDto teamDto) {
        Team team = new Team();
        dtoToObject(teamDto, team);
        return team;
    }

    public Team fromDto(TeamDto teamDto, long id) {
        Team team = teamService.getById(id);
        dtoToObject(teamDto, team);
        return team;
    }

    private void dtoToObject(TeamDto teamDto, Team team) {
        User user = userService.getById(teamDto.getOwnerId());
        team.setTeamName(teamDto.getTeamName());
        team.setOwner(user);
    }

}
