package model;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

  private Subtask subtask;

  @Test
  void getEpicId() {
    subtask = new Subtask(1, "Вымыть окна", "без разводов", Status.NEW, 1);

    int expected = 1;
    subtask.setEpicId(1);
    int actually = subtask.getEpicId();
    assertEquals(expected, actually);
  }

  @Test
  void testToString() {
    int id = 1;
    String title = "Вымыть окна";
    String description = "без разводов";
    Status status = Status.NEW;
    int epicId = 10;
    Instant startTime = Instant.parse("2025-02-23T14:05:00Z");
    Duration duration = Duration.ofMinutes(30);

    subtask = new Subtask(id, title, description, status, epicId, startTime, duration);
    assertNotNull(subtask);
    assertEquals(id, subtask.getId());
    assertEquals(title, subtask.getTitle());
    assertEquals(description, subtask.getDescription());
    assertEquals(status, subtask.getStatus());
    assertEquals(epicId, subtask.getEpicId());
    assertEquals(startTime, subtask.getStartTime());
    assertEquals(duration, subtask.getDuration());
  }
}