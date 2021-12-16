package com.telerik.peer.models;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="status_id")
  private long statusId;

  @Column(name="status")
  private String status;

  public Status() {
  }

  public long getStatusId() {
    return statusId;
  }

  public void setStatusId(long statusId) {
    this.statusId = statusId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
