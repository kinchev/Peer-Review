package com.telerik.peer.models.dto;



import com.telerik.peer.models.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

public class WorkItemDto {

    @Size(min=10, max=80, message = "Title should be between 10 and 80 characters.")
    private String title;

    @Positive(message = "Team Id should be positive.")
    private long teamId;

    @NotBlank
    @Size(min=5, message = "Description should be minimum 20 symbols.")
    private String description;

    @Positive(message = "Creator Id should be positive.")
    private long creatorId;

    @Positive(message = "Reviewer Id should be positive.")
    private long reviewerId;

    private Set<Comment> comments;

    public WorkItemDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
