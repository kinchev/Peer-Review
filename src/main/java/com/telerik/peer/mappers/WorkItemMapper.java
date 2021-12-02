package com.telerik.peer.mappers;

import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.repositories.contracts.TeamRepository;
import com.telerik.peer.repositories.contracts.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkItemMapper {
    private WorkItemRepository workItemRepository;
    private TeamRepository teamRepository;

@Autowired

    public WorkItemMapper(WorkItemRepository workItemRepository, TeamRepository teamRepository) {
        this.workItemRepository = workItemRepository;
        this.teamRepository = teamRepository;
    }
public WorkItemDto workitemDto(WorkItem workItem){
    WorkItemDto workItemDto=new WorkItemDto();
    workItemDto.setTeamId(workItem.getTeam().getId());
}
}
