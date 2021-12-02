package com.telerik.peer.services.contracts;

import com.telerik.peer.models.WorkItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface WorkItemService extends CRUDService<WorkItem> {

    List<WorkItem> filter(Optional<String> title, Optional<String> status, Optional<String> sortBy);

}
