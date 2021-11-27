package com.telerik.peer.models.dto;

import javax.validation.constraints.Positive;

public class TeamDto {
    @Positive(message = "Team ID must be a positive number")
    private long teamId;

    @Positive(message = "Creator ID must be a positive number.")
    private long creatorId;


}
