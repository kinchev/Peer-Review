package com.telerik.peer.models.dto;

public class StatusDto {
    private long id;

    private String comment;

    public StatusDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

