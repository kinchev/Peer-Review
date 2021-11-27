package com.telerik.peer.repositories;

import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.WorkItemRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class WorkItemRepositoryImpl extends AbstractCRUDRepository<WorkItem> implements WorkItemRepository {
    private final SessionFactory sessionFactory;

    public WorkItemRepositoryImpl(SessionFactory sessionFactory) {
        super(WorkItem.class, sessionFactory);
        this.sessionFactory = sessionFactory;

    }
}
