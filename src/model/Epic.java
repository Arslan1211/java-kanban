package model;

import adapter.DataTimeFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Epic extends Task {

  private Instant endTime;

  private final Collection<Integer> subtaskIds;

  public Epic(int id, String title, String description, Status status) {
    super(id, title, description, status);
    subtaskIds = new ArrayList<>();
  }

  public Epic(int id, String title, String description, Status status, Instant startTime,
      Duration duration) {
    super(id, title, description, status, startTime, duration);
    subtaskIds = new ArrayList<>();
  }

  public Epic(String title, String description, Status status, Instant startTime,
      Duration duration) {
    super(title, description, status, startTime, duration);
    subtaskIds = new ArrayList<>();
  }

  public Epic(String title, String description, Instant startTime,
      Duration duration) {
    super(title, description, startTime, duration);
    subtaskIds = new ArrayList<>();
  }

  public Collection<Integer> getSubtaskIds() {
    return subtaskIds;
  }

  public void deleteSubtask(Integer subtaskId) {
    subtaskIds.remove(subtaskId);
  }

  public void clearSubtaskIds() {
    subtaskIds.clear();
  }

  @Override
  public TypeTask getType() {
    return TypeTask.EPIC;
  }

  @Override
  public String toString() {
    return "Epic{" +
        "id=" + getId() +
        ", name=" + getTitle() +
        ", description=" + getDescription() +
        ", status=" + getStatus() +
        ", startTime=" + ZonedDateTime.ofInstant(getStartTime(), ZoneId.systemDefault())
        .format(DataTimeFormat.getDTF()) +
        ", duration=" + getDuration().toMinutes() +
        ", endTime=" + ZonedDateTime.ofInstant(getStartTime().plus(getDuration()),
            ZoneId.systemDefault())
        .format(DataTimeFormat.getDTF()) +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Epic epic = (Epic) o;
    return Objects.equals(subtaskIds, epic.subtaskIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), subtaskIds);
  }

  @Override
  public Instant getEndTime() {
    return endTime;
  }

  public void setEndTime(Instant endTime) {
    this.endTime = endTime;
  }
}