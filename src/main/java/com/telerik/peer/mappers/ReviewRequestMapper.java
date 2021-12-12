package com.telerik.peer.mappers;

import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.ReviewRequestDto;
import com.telerik.peer.models.dto.TeamDto;
import com.telerik.peer.services.contracts.ReviewRequestService;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewRequestMapper {
    private ReviewRequestService reviewRequestService;
    private UserService userService;
    private WorkItemService workItemService;

    @Autowired
    public ReviewRequestMapper(UserService userService, WorkItemService workItemService) {
        this.userService = userService;
        this.workItemService = workItemService;
    }

    public ReviewRequestDto ReviewRequestToDto(ReviewRequest reviewRequest) {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setWorkitemId(reviewRequest.getWorkItem().getId());
        reviewRequestDto.setCreatorId(reviewRequest.getCreator().getId());
        reviewRequestDto.setReviewerId(reviewRequest.getReviewer().getId());
        return reviewRequestDto;
    }

    public ReviewRequest fromDto(ReviewRequestDto reviewRequestDto) {
        ReviewRequest reviewRequest = new ReviewRequest();
        dtoToObject(reviewRequestDto, reviewRequest);
        return reviewRequest;
    }

    public ReviewRequest fromDto(ReviewRequestDto reviewRequestDto, long id) {
        ReviewRequest reviewRequest = reviewRequestService.getById(id);
        dtoToObject(reviewRequestDto, reviewRequest);
        return reviewRequest;
    }

    private void dtoToObject(ReviewRequestDto reviewRequestDto, ReviewRequest reviewRequest) {
        WorkItem workItem = workItemService.getById(reviewRequestDto.getWorkitemId());
        User creator = userService.getById(reviewRequestDto.getCreatorId());
        User reviewer = userService.getById(reviewRequestDto.getReviewerId());
        reviewRequest.setWorkItem(workItem);
        reviewRequest.setCreator(creator);
        reviewRequest.setReviewer(reviewer);
    }


}
