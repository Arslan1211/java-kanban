package model;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Subtask extends Task {

  private int epicId;

  public Subtask(int id, String title, String description, Status status, int epicId) {
    super(id, title, description, status);
    this.epicId = epicId;
  }

  public Subtask(int id, String title, String description, Status status, int epicId,
      Instant startTime, Duration duration) {
    super(id, title, description, status, startTime, duration);
    this.epicId = epicId;
  }

  public int getEpicId() {
    return epicId;
  }

  public void setEpicId(int epicId) {
    this.epicId = epicId;
  }

  public TypeTask getType() {
    return TypeTask.SUBTASK;
  }

  @Override
  public String toString() {
    return "Subtask{" +
        "id=" + getId() +
        ", name=" + getTitle() +
        ", description=" + getDescription() +
        ", status=" + getStatus() +
        ", epicId=" + epicId +
        ", startTime=" + ZonedDateTime.ofInstant(getStartTime(), ZoneId.systemDefault())
        .format(formatter) +
        ", duration=" + getDuration().toMinutes() +
        ", endTime=" + ZonedDateTime.ofInstant(getStartTime().plus(getDuration()),
            ZoneId.systemDefault())
        .format(formatter) +
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
    Subtask subtask = (Subtask) o;
    return epicId == subtask.epicId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), epicId);
  }
}