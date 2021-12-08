package com.telerik.peer.services.contracts;

import com.telerik.peer.models.Comment;
import com.telerik.peer.models.Status;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;

import java.util.List;
import java.util.Optional;


public interface WorkItemService extends CRUDService<WorkItem> {

    List<WorkItem> filter(Optional<String> title, Optional<String> status, Optional<String> sortBy);

    void setStatus(WorkItem workItem, User updatingUser, Status status, Comment comment);

    void changeReviewer(WorkItem workItem, User updatingUser, User newReviewer);


}
