package com.telerik.peer.services.contracts;

import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;

public interface TeamService extends CRUDService<Team>{


    void addMemberToTeam(Team team, User user, User userToAdd);

    void removeMemberFromTeam(Team team, User user, User userToRemove);

    void addWorkitemToTeam(Team team, User user, WorkItem workitem);
}
