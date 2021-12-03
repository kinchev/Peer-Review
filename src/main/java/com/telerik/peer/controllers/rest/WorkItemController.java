package com.telerik.peer.controllers.rest;

import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workitems")
public class WorkItemController {
    AuthenticationHelper authenticationHelper;
    WorkItemService workItemService;
    TeamService teamService;

    public WorkItemController(AuthenticationHelper authenticationHelper, WorkItemService workItemService, TeamService teamService) {
        this.authenticationHelper = authenticationHelper;
        this.workItemService = workItemService;
        this.teamService = teamService;
    }


}
