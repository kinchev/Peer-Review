package com.telerik.peer.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "workitems")
public class WorkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workitem_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workItem")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workItem")
    private Set<Attachment> attachments = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getCreator() {
        return creator;
    }

    public User getReviewer() {
        return reviewer;
    }

    public Team getTeam() {
        return team;
    }

    public WorkItem() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkItem workitem = (WorkItem) o;
        return getId() == workitem.getId() &&
                getTitle().equals(workitem.getTitle()) &&
                getDescription().equals(workitem.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription());
    }

}
