package com.telerik.peer.services.contracts;

import com.telerik.peer.models.ReviewRequest;

import java.util.List;

public interface ReviewRequestService extends CRUDService<ReviewRequest> {
    List<ReviewRequest> getReviewRequestByCreator(long userId);

    List<ReviewRequest> getReviewRequestByReviewer(long userId);
}
