package com.telerik.peer.models.dto;


import javax.validation.constraints.Positive;

public class ReviewRequestDto {

    @Positive(message = "Work item Id must be positive.")
    private long workitemId;

    @Positive(message = "Work item Id must be positive.")
    private long creatorId;

    @Positive(message = "Work item Id must be positive.")
    private long reviewerId;

    public ReviewRequestDto() {
    }

    public long getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(long workitemId) {
        this.workitemId = workitemId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(long reviewerId) {
        this.reviewerId = reviewerId;
    }
}
