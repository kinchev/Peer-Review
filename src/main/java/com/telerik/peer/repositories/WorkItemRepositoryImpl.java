package com.telerik.peer.repositories;

import com.telerik.peer.models.Team;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.WorkItemRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class WorkItemRepositoryImpl extends AbstractCRUDRepository<WorkItem> implements WorkItemRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public WorkItemRepositoryImpl(SessionFactory sessionFactory) {
        super(WorkItem.class, sessionFactory);
        this.sessionFactory = sessionFactory;

    }

    @Override
    public List<WorkItem> filter(Optional<String> title, Optional<String> status,
                                 Optional<String> creator, Optional<String> reviewer,
                                 Optional<String> team, Optional<String> sortBy) {
        try (Session session = sessionFactory.openSession()) {
            var queryString = new StringBuilder(" from WorkItem ");
            var filters = new ArrayList<String>();
            var params = new HashMap<String, Object>();

            title.ifPresent(value -> {

                filters.add("title like: title");
                params.put("title", "%" + value + "%");
            });
            status.ifPresent(value -> {
                filters.add("status.status = :status");
                params.put("status", value);
            });
            creator.ifPresent(value -> {
                filters.add("creator.username like:creator");
                params.put("creator", value);
            });
            reviewer.ifPresent(value -> {
                filters.add("reviewer.username like:reviewer");
                params.put("reviewer", value);
            });
            team.ifPresent(value -> {
                filters.add("team.teamName like:team");
                params.put("team", value);
            });

            if (!filters.isEmpty()) {
                queryString.append("where ").append(String.join(" and ", filters));
            }

            sortBy.ifPresent(value -> {
                queryString.append(generateSortingString(value));
            });
            Query<WorkItem> query = session.createQuery(queryString.toString(), WorkItem.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateSortingString(String value) {
        StringBuilder result = new StringBuilder(" order by ");
        var params = value.toLowerCase().split("_");

        switch (params[0]) {
            case "title":
                result.append("title ");
                break;
            case "status":
                result.append("status.status ");
                break;
            case "creator":
                result.append("creator.name ");
                break;
            case "reviewer":
                result.append("reviewer.name ");
                break;
            case "team":
                result.append("team.name ");
                break;

            default:
                return "";
        }

        if (params.length > 1 && params[1].equals("desc")) {
            result.append("desc");
        }
        return result.toString();
    }

    @Override
    public List<WorkItem> showAllTeamActiveWorkItems(Team team) {
        try (Session session = sessionFactory.openSession()) {
            Query<WorkItem> query = session.createQuery(
                    "from WorkItem where team.id = :teamId and status.statusId < 4", WorkItem.class);
            query.setParameter("teamId", team.getTeamId());

            return query.list();
        }
    }

    @Override
    public List<WorkItem> showAllTeamClosedWorkItems(Team team) {
        try (Session session = sessionFactory.openSession()) {
            Query<WorkItem> query = session.createQuery(
                    "from WorkItem where team.id = :teamId and status.statusId > 3", WorkItem.class);
            query.setParameter("teamId", team.getTeamId());

            return query.list();
        }
    }


}
