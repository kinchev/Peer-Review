package com.telerik.peer.mappers;

import com.telerik.peer.models.*;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.repositories.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkItemMapper {
    private WorkItemRepository workItemRepository;
    private TeamRepository teamRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private StatusRepository statusRepository;

    @Autowired
    public WorkItemMapper(WorkItemRepository workItemRepository, TeamRepository teamRepository,
                          CommentRepository commentRepository, UserRepository userRepository,
                          StatusRepository statusRepository) {
        this.workItemRepository = workItemRepository;
        this.teamRepository = teamRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }

    public WorkItemDto workitemToDto(WorkItem workItem) {
        WorkItemDto workItemDto = new WorkItemDto();
        workItemDto.setCommentId(workItem.getComment().getId());
        workItemDto.setTeamId(workItem.getTeam().getTeam_id());
        workItemDto.setCreatorId(workItem.getCreator().getId());
        workItemDto.setStatus(workItem.getStatus());
        workItemDto.setReviewerId(workItem.getReviewer().getId());
        workItemDto.setDescription(workItemDto.getDescription());
        return workItemDto;

    }

    public WorkItem fromDto(WorkItemDto workItemDto) {
        WorkItem workItem = new WorkItem();
        dtoToObject(workItemDto, workItem);
        return workItem;
    }
    public WorkItem fromDto(WorkItemDto workItemDto,long id) {
        WorkItem workItem=workItemRepository.getById(id);
        dtoToObject(workItemDto,workItem);
        return workItem;
    }
    private void dtoToObject(WorkItemDto workItemDto, WorkItem workItem) {
        User userReviewer = userRepository.getById(workItemDto.getReviewerId());
        User userCreator = userRepository.getById(workItemDto.getCreatorId());
        Comment comment = commentRepository.getById(workItemDto.getCommentId());
        Team team = teamRepository.getById(workItemDto.getTeamId());
        Status status = statusRepository.getById(1);
        workItem.setTeam(team);
        workItem.setComment(comment);
        workItem.setCreator(userCreator);
        workItem.setReviewer(userReviewer);
        workItem.setDescription(workItemDto.getDescription());
        workItem.setStatus(status);

    }
}
