package com.telerik.peer.models;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="status_id")
  private Long status_id;

  @Column(name="status")
  private String status;

  public Status() {
  }

  public Long getStatus_id() {
    return status_id;
  }

  public void setStatus_id(Long status_id) {
    this.status_id = status_id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
