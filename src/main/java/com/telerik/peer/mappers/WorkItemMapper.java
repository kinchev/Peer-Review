package com.telerik.peer.mappers;

import com.telerik.peer.models.*;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.models.dto.WorkItemUpdateDto;
import com.telerik.peer.repositories.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkItemMapper {
    private final WorkItemRepository workItemRepository;
    private final TeamRepository teamRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

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
        workItemDto.setComment(workItem.getComment());
        workItemDto.setTeamId(workItem.getTeam().getTeamId());
        workItemDto.setCreatorId(workItem.getCreator().getId());

        workItemDto.setTitle(workItem.getTitle());
        workItemDto.setReviewerId(workItem.getReviewer().getId());
        workItemDto.setDescription(workItem.getDescription());
        return workItemDto;

    }

    public WorkItem fromDto(WorkItemDto workItemDto) {
        WorkItem workItem = new WorkItem();
        dtoToObject(workItemDto, workItem);
        return workItem;
    }

    public WorkItem fromDto(WorkItemUpdateDto workItemDto, long id) {
        WorkItem workItem = workItemRepository.getById(id);
        dtoUpdateToObject(workItemDto, workItem);
        return workItem;
    }

    private void dtoToObject(WorkItemDto workItemDto, WorkItem workItem) {
        User userReviewer = userRepository.getById(workItemDto.getReviewerId());
        User userCreator = userRepository.getById(workItemDto.getCreatorId());
        Team team = teamRepository.getById(workItemDto.getTeamId());
        Status status = statusRepository.getById(1);
        workItem.setTeam(team);
        workItem.setTitle(workItemDto.getTitle());
        workItem.setComment(workItem.getComment());
        workItem.setCreator(userCreator);
        workItem.setReviewer(userReviewer);
        workItem.setDescription(workItemDto.getDescription());
        workItem.setStatus(status);
    }

        private void dtoUpdateToObject(WorkItemUpdateDto workItemDto, WorkItem workItem) {
            workItem.setTitle(workItemDto.getTitle());
            workItem.setDescription(workItemDto.getDescription());
    }

}
