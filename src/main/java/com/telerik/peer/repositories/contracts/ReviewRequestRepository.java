package com.telerik.peer.repositories.contracts;

import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.User;

import java.util.List;

public interface ReviewRequestRepository extends CRUDRepository<ReviewRequest> {

    List<ReviewRequest> getReviewRequestByCreator(long userId);

    List<ReviewRequest> getReviewRequestByReviewer(long userId);
}
