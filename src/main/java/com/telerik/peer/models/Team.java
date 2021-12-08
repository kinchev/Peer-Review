package com.telerik.peer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "name")
    private String teamName;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teams_users",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<User> members = new HashSet<>();

    @JsonIgnore
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "teams_workitems",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "workitem_id")
    )
    private Set<WorkItem> workItems = new HashSet<>();

    public Team() {
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<WorkItem> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(Set<WorkItem> workItems) {
        this.workItems = workItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return getTeamId() == team.getTeamId() &&
                getTeamName().equals(team.getTeamName()) &&
                getOwner().equals(team.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamId(), getTeamName(), getOwner());
    }

}
