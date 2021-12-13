package com.telerik.peer.repositories.contracts;

import com.telerik.peer.models.WorkItem;

import java.util.List;
import java.util.Optional;

public interface WorkItemRepository extends CRUDRepository<WorkItem> {
List<WorkItem> filter(Optional<String> title, Optional<String> status,
                      Optional<String> creator, Optional<String> reviewer,
                      Optional<String> team, Optional<String> sortBy);
}
