package com.telerik.peer.services.contracts;

import com.telerik.peer.models.*;

import java.util.List;
import java.util.Optional;


public interface WorkItemService extends CRUDService<WorkItem> {

    List<WorkItem> filter(Optional<String> title, Optional<String> status,
                          Optional<String> creator, Optional<String> reviewer,
                          Optional<String> team, Optional<String> sortBy);

    void setStatus(WorkItem workItem, User updatingUser, Status status, String commentToAdd);

    void setWorkItemComment(WorkItem workItem, User updatingUser, String commentToAdd);

    void changeReviewer(WorkItem workItem, User updatingUser, User newReviewer);

    List<WorkItem> showAllTeamActiveWorkItems(Team team);

    List<WorkItem> showAllTeamClosedWorkItems(Team team);

}
