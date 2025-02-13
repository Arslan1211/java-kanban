package model;

import java.util.Objects;

public class Task {

  private int id;
  private String title;
  private String description;
  private Status status;

  public Task(String title, String description, Status status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }

  public Task(int id, String taskName, String description, Status taskStatus) {
    this.id = id;
    this.title = taskName;
    this.description = description;
    this.status = taskStatus;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public TypeTask getType() {
    return TypeTask.TASK;
  }

  @Override
  public String toString() {
    String format = "Task ID: %d, название: '%s', описание: '%s', статус: %s";
    return String.format(format, id, title, description, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Task task = (Task) obj;
    return Objects.equals(title, task.title) &&
        Objects.equals(description, task.description) &&
        (id == task.id) &&
        Objects.equals(status, task.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, status);
  }
}