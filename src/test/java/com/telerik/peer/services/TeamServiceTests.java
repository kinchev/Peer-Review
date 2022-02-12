package com.telerik.peer.services;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.services.contracts.WorkItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerik.peer.Helper.*;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TeamServiceTests {


    @Mock
    TeamRepository mockRepository;

    @Mock
    WorkItemService workItemService;

    @InjectMocks
    TeamServiceImpl teamService;

    private static User mockUser;
    private static WorkItem mockWorkItem;
    private static List<Team> teams;
    private static Team mockTeam;

    @BeforeEach
    void initializer() {

        mockUser = createMockUser();
        mockWorkItem = createMockWorkItem();
        mockTeam = createMockTeam();
        teams = new ArrayList<>();
        teams.add(mockTeam);
    }

    @Test
    public void getAll_should_callRepository() {
        Mockito.when(mockRepository.getAll()).thenReturn(teams);


        teamService.getAll();


        Assertions.assertEquals(mockTeam, teamService.getAll().get(0));
        Mockito.verify(mockRepository, Mockito.times(2)).getAll();

    }

    @Test
    void create_should_throw_when_NameIsTaken() {


        Team team = createMockTeam();
        team.setTeamName(mockTeam.getTeamName());


        Assertions.assertThrows(DuplicateEntityException.class, () -> teamService.create(team));
    }
}
