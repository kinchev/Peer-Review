package com.telerik.peer.services;

import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.ReviewRequestRepository;
import com.telerik.peer.services.contracts.ReviewRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewRequestServiceImpl implements ReviewRequestService {

    public static final String NOT_SAME_CREATOR = "The creator of the review request must be same as the creator of the workitem";
    public static final String NOT_REVIEWER_FROM_THE_LIST = "The reviewer of the review request must be form the list of the work item's reviewers";
    public static final String ONLY_THE_CREATOR_CAN_DELETE = "Only the creator can delete the review request";
    private final ReviewRequestRepository reviewRequestRepository;

    @Autowired
    public ReviewRequestServiceImpl(ReviewRequestRepository reviewRequestRepository) {
        this.reviewRequestRepository = reviewRequestRepository;
    }

    @Override
    public List<ReviewRequest> getAll() {
        return reviewRequestRepository.getAll();
    }

    @Override
    public ReviewRequest getById(long id) {
        return reviewRequestRepository.getById(id);
    }

    @Override
    public void delete(long id, User owner) {
        ReviewRequest reviewRequest = reviewRequestRepository.getById(id);
        if (reviewRequest.getCreator().getId() != owner.getId()) {
            throw new InvalidUserInputException(ONLY_THE_CREATOR_CAN_DELETE);
        }
        reviewRequestRepository.delete(id);
    }

    @Override
    public void create(ReviewRequest entity) {
        if (!creatorIsSame(entity)) {
            throw new InvalidUserInputException(NOT_SAME_CREATOR);
        }
        if (!reviewerIsSame(entity)) {
            throw new InvalidUserInputException(NOT_REVIEWER_FROM_THE_LIST);
        }
        reviewRequestRepository.create(entity);
    }

    @Override
    public void update(ReviewRequest entity, User owner) {
        reviewRequestRepository.update(entity);
    }

    @Override
    public <V> ReviewRequest getByField(String fieldName, V value) {
        return reviewRequestRepository.getByField(fieldName, value);
    }

    private boolean creatorIsSame(ReviewRequest reviewRequest) {
        return reviewRequest.getWorkItem().getCreator().getId() == reviewRequest.getCreator().getId();
    }
    private boolean reviewerIsSame(ReviewRequest reviewRequest) {
        return reviewRequest.getWorkItem().getReviewer().getId() == reviewRequest.getReviewer().getId();
    }

    @Override
    public List<ReviewRequest> getReviewRequestByCreator(long userId) {
        return reviewRequestRepository.getReviewRequestByCreator(userId);
    }

    @Override
    public List<ReviewRequest> getReviewRequestByReviewer(long userId) {
        return reviewRequestRepository.getReviewRequestByReviewer(userId);
    }
}
