package com.telerik.peer.services;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.WorkItemRepository;
import com.telerik.peer.services.contracts.WorkItemService;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class WorkItemServiceImpl implements WorkItemService {
    private static final String REVIEWER_AND_CREATOR_ARE_THE_SAME_ERROR = "Creator and reviewer are the same!";
    private static final String REVIEWER_DIFFERENT_FROM_ITEM_TEAM = "Reviewer different from item team!";
    WorkItemRepository workItemRepository;

    @Autowired
    public WorkItemServiceImpl(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }

    @Override
    public List<WorkItem> getAll() {
        return workItemRepository.getAll();
    }

    @Override
    public WorkItem getById(long id) {
        return workItemRepository.getById(id);
    }

    @Override
    public void delete(long id, User owner) {

    }

    @Override
    public void create(WorkItem workitem) {
        if (titleExists(workitem, workitem.getId())) {
            throw new DuplicateEntityException("Entity", "title", workitem.getTitle());
        }
        if (userAndCreatorAreTheSame(workitem)) {
            throw new InvalidUserInputException(REVIEWER_AND_CREATOR_ARE_THE_SAME_ERROR);
        }
        if (isReviewerDifferentFromItemTeam(workitem)) {
            throw new InvalidUserInputException(String.format(REVIEWER_DIFFERENT_FROM_ITEM_TEAM, workitem.getTeam().getTeamName()));
        }
        workItemRepository.create(workitem);
    }


    @Override
    public void update(WorkItem workitem, User owner) {
        WorkItem workitemToUpdate = workItemRepository.getById(workitem.getId());
        if (titleExists(workitem, workitemToUpdate.getId())) {
            throw new DuplicateEntityException("Item", "name", workitem.getTitle());
        }
        if (userAndCreatorAreTheSame(workitem)) {
            throw new InvalidUserInputException(REVIEWER_AND_CREATOR_ARE_THE_SAME_ERROR);
        }

        if (isReviewerDifferentFromItemTeam(workitem)) {
            throw new InvalidUserInputException(REVIEWER_AND_CREATOR_ARE_THE_SAME_ERROR);

        }
//    if(isStatusChangeToReject(workitem,itemToUpdate)) {
//        if(workitem.getComment()==null){
//            throw  new InvalidUserInputException("Empty comment");
//        }
//        Comment comment=new Comment();
        workItemRepository.update(workitem);
    }

    @Override
    public <V> WorkItem getByField(String fieldName, V value) {
        return null;
    }

    private boolean isReviewerDifferentFromItemTeam(WorkItem workitem) {
//    return workitem.getTeam().getMembers().stream().anyMatch(member->member.equals(workitem.getReviewers()));
        return true;
    }

    //
    private boolean userAndCreatorAreTheSame(WorkItem workitem) {
//        return workitem.getCreator().getId() == workitem.getReviewers().getId();
        return true;
    }

    //
    private boolean titleExists(WorkItem workitem, Long id) {
//        Stream<WorkItem> duplicateWorkitem=getAll().stream().filter(item->item.getTitle().equalsIgnoreCase(workitem.getTitle()));
//        return duplicateWorkitem.isPresent() && duplicateWorkitem.get().getId() !=workitemToUpdate;
        return true;
    }
}
