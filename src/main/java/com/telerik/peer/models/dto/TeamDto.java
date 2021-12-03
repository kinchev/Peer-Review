package com.telerik.peer.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class TeamDto {

    @NotBlank
    @Size(min = 2, max = 20, message = "Team name must be between 3 and 30 characters.")
    private String teamName;

    @Positive(message = "Owner Id must be positive.")
    private long ownerId;

    public TeamDto() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
