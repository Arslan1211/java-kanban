package model;

import adapter.DataTimeFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Task {

  private Integer id;
  private String title;
  private String description;
  private Status status;
  private Duration duration;
  private Instant startTime;

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

  public Task(int id, String taskName, String description, Status taskStatus,
      Instant startTime, Duration duration) {
    this.id = id;
    this.title = taskName;
    this.description = description;
    this.status = taskStatus;
    this.duration = duration;
    this.startTime = startTime;
    this.getEndTime();
  }

  public Task(String taskName, String description, Instant startTime, Duration duration) {
    this.title = taskName;
    this.description = description;
    this.startTime = startTime;
    this.duration = duration;
  }

  public Task(String taskName, String description, Status taskStatus,
      Instant startTime, Duration duration) {
    this.title = taskName;
    this.description = description;
    this.status = taskStatus;
    this.duration = duration;
    this.startTime = startTime;
    this.getEndTime();
  }

  public Integer getId() {
    return id;
  }

  public void setStartTime(Instant startTime) {
    this.startTime = startTime;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public Duration getDuration() {
    return duration;
  }

  public Instant getStartTime() {
    return startTime;
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

  public Instant getEndTime() {
    if (startTime == null || duration == null) {
      return null;
    }
    return startTime.plus(duration);
  }

  @Override
  public String toString() {
    return "Task{" +
        "id=" + getId() +
        ", name=" + getTitle() +
        ", description=" + getDescription() +
        ", status=" + getStatus() +
        ", startTime=" + ZonedDateTime.ofInstant(getEndTime(), ZoneId.systemDefault())
        .format(DataTimeFormat.getDTF()) +
        ", duration=" + duration.toMinutes() +
        ", endTime=" + ZonedDateTime.ofInstant(getEndTime().plus(duration), ZoneId.systemDefault())
        .format(DataTimeFormat.getDTF()) +
        '}';
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