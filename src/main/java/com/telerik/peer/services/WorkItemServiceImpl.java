package com.telerik.peer.services;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.models.*;
import com.telerik.peer.repositories.contracts.CommentRepository;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.repositories.contracts.WorkItemRepository;
import com.telerik.peer.services.contracts.ReviewRequestService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkItemServiceImpl implements WorkItemService {
    public static final String MODIFY_NOT_AUTHORIZED = "Only the item owner or the reviewers are allowed to do this action";
    public static final String ONLY_OWNER_AUTHORIZED = "Only the owner is authorised to change the reviewers.";
    private static final String REVIEWER_AND_CREATOR_ARE_THE_SAME_ERROR = "Creator and reviewer are the same!";
    private static final String REVIEWER_DIFFERENT_FROM_ITEM_TEAM = "Reviewer different from item team!";
    private static final String CREATOR_DIFFERENT_FROM_ITEM_TEAM = "Creator different from item team!";
    public static final String UPDATING_USER_IS_NOT_REVIEWER = "The status can be updated only by a work item reviewer.";
    public static final String COMMENT_REQUIRED = "This status requires a comment to be added";

    private final WorkItemRepository workItemRepository;
    private final TeamRepository teamRepository;
    private final ReviewRequestService reviewRequestService;
    private final CommentRepository commentRepository;

    @Autowired
    public WorkItemServiceImpl(WorkItemRepository workItemRepository, TeamRepository teamRepository,
                               ReviewRequestService reviewRequestService, CommentRepository commentRepository) {
        this.workItemRepository = workItemRepository;
        this.teamRepository = teamRepository;
        this.reviewRequestService = reviewRequestService;
        this.commentRepository = commentRepository;
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

        boolean duplicateExists = true;
        try {
            workItemRepository.getByField("title", workitem.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Workitem", "title", workitem.getTitle());
        }

        validateCreatorAndReviewer(workitem);

        workItemRepository.create(workitem);
        var workItems = workitem.getTeam().getWorkItems();
        Team team = workitem.getTeam();
        workItems.add(workitem);
        team.setWorkItems(workItems);
        teamRepository.update(team);
        ReviewRequest reviewRequest = createReviewRequest(workitem);
        reviewRequestService.create(reviewRequest);
    }

    @Override
    public void update(WorkItem workitem, User owner) {
        if (workitem.getCreator().getId() != owner.getId() && workitem.getReviewer().getId() != owner.getId()) {
            throw new UnauthorizedOperationException(MODIFY_NOT_AUTHORIZED);
        }
        boolean duplicateExists = true;
        try {
            WorkItem existingWorkitem = workItemRepository.getByField("title", workitem.getTitle());
            if (existingWorkitem.getTitle().equals(workitem.getTitle())) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Workitem", "title", workitem.getTitle());
        }

        workItemRepository.update(workitem);
    }

    @Override
    public void setStatus(WorkItem workItem, User updatingUser, Status status, String commentToAdd) {
        if (!updatingUser.equals(workItem.getReviewer())) {
            throw new InvalidUserInputException(UPDATING_USER_IS_NOT_REVIEWER);
        }
        if (statusRequiresComment(status) && commentToAdd == null) {
            throw new InvalidUserInputException(COMMENT_REQUIRED);
        }
        workItem.setStatus(status);
        if (commentToAdd != null) {
            Comment comment = addComment(commentToAdd, updatingUser, workItem);
            var comments = workItem.getComments();
            comments.add(comment);
            workItem.setComments(comments);
        }
        workItemRepository.update(workItem);
    }

    @Override
    public void setWorkItemComment(WorkItem workItem, User updatingUser, String commentToAdd) {
        if (workItem.getCreator().getId() !=updatingUser.getId() && workItem.getReviewer().getId() != updatingUser.getId()) {
            throw new UnauthorizedOperationException(MODIFY_NOT_AUTHORIZED);
        }
        Comment comment = addComment(commentToAdd, updatingUser, workItem);
        var comments = workItem.getComments();
        comments.add(comment);
        workItem.setComments(comments);
        workItemRepository.update(workItem);
    }

    @Override
    public void changeReviewer(WorkItem workitem, User updatingUser, User newReviewer) {
        if (workitem.getCreator().getId() != updatingUser.getId()) {
            throw new UnauthorizedOperationException(ONLY_OWNER_AUTHORIZED);
        }
        workitem.setReviewer(newReviewer);
        validateCreatorAndReviewer(workitem);
        workItemRepository.update(workitem);
        ReviewRequest reviewRequest = createReviewRequest(workitem);
        reviewRequestService.create(reviewRequest);
    }

    @Override
    public <V> WorkItem getByField(String fieldName, V value) {
        return workItemRepository.getByField(fieldName, value);
    }

    @Override
    public List<WorkItem> filter(Optional<String> title, Optional<String> status, Optional<String> sortBy) {
        return workItemRepository.filter(title, status, sortBy);
    }

    private void validateCreatorAndReviewer(WorkItem workitem) {
        if (!workitem.getTeam().getMembers().contains(workitem.getReviewer())) {
            throw new InvalidUserInputException(REVIEWER_DIFFERENT_FROM_ITEM_TEAM);
        }
        if (!workitem.getTeam().getMembers().contains(workitem.getCreator())) {
            throw new InvalidUserInputException(CREATOR_DIFFERENT_FROM_ITEM_TEAM);
        }
        if (workitem.getCreator().getId() == workitem.getReviewer().getId()) {
            throw new InvalidUserInputException(REVIEWER_AND_CREATOR_ARE_THE_SAME_ERROR);
        }
    }

    private boolean statusRequiresComment(Status status) {
        return status.getStatus_id() == 3 || status.getStatus_id() == 5;
    }

    private ReviewRequest createReviewRequest(WorkItem workItem) {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setWorkItem(workItem);
        reviewRequest.setCreator(workItem.getCreator());
        reviewRequest.setReviewer(workItem.getReviewer());
        return reviewRequest;
    }

    public Comment addComment(String commentToAdd, User user, WorkItem workItem) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setComment(commentToAdd);
        comment.setWorkItem(workItem);
        commentRepository.create(comment);
        return comment;
    }

}

//    private boolean isReviewerInItemTeam(WorkItem workitem) {
//        return workitem.getTeam().getMembers().stream().anyMatch(member -> member.equals(workitem.getReviewer()));
//    }
//
//    private boolean titleExists(WorkItem workitem, long workitemToUpdate) {
//        Optional<WorkItem> duplicateWorkitem = getAll().stream().filter(item -> item.getTitle().equalsIgnoreCase(workitem.getTitle())).findFirst();
//        return duplicateWorkitem.isPresent() && duplicateWorkitem.get().getId() != workitemToUpdate;
//    }

