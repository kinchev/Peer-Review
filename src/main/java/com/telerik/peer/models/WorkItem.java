package com.telerik.peer.models;

import javax.persistence.*;



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

    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "workitems_users",
//            joinColumns = @JoinColumn(name = "workitem_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private Set<User> reviewers = new HashSet<>();
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "workitems_comments",
//            joinColumns = @JoinColumn(name = "workitem_id"),
//            inverseJoinColumns = @JoinColumn(name = "comment_id")
//    )
//    private Set<Comment> comments = new HashSet<>();


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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WorkItem workitem = (WorkItem) o;
//        return getId() == workitem.getId() &&
//                getTitle().equals(workitem.getTitle()) &&
//                getDescription().equals(workitem.getDescription());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), getTitle(), getDescription());
//    }

}
