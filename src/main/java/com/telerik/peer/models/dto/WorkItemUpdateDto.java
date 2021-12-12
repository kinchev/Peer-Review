package com.telerik.peer.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class WorkItemUpdateDto {

    @Size(min=10, max=80, message = "Title should be between 10 and 80 characters.")
    private String title;

    @NotBlank
    @Size(min=5, message = "Description should be minimum 20 symbols.")
    private String description;

    public WorkItemUpdateDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
