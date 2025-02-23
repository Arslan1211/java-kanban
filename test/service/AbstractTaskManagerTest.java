package service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

public abstract class AbstractTaskManagerTest<T extends TaskManager> {

  protected Task task;
  protected Epic epic;
  protected Subtask subtask;
  protected T manager;
  protected static final Integer DEFAULT_ID = 1;
  Instant startTime = Instant.parse("2025-02-23T14:05:00Z");

  @Test
  void createTask() {
    task = new Task(
        DEFAULT_ID,
        "Заголовок задачи",
        "Описание задачи",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15));
    manager.createTask(task);

    Task checkEntity = manager.findTaskById(task.getId());

    assertNotNull(checkEntity);
    assertEquals("Заголовок задачи", checkEntity.getTitle());
    assertEquals("Описание задачи", checkEntity.getDescription());
    assertEquals(Status.NEW, checkEntity.getStatus());
  }

  @Test
  void createEpic() {
    epic = new Epic(
        DEFAULT_ID,
        "Заголовок эпика",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15));
    manager.createEpic(epic);

    Epic checkEntity = manager.findEpicById(epic.getId());

    assertNotNull(checkEntity);
    assertEquals("Заголовок эпика", checkEntity.getTitle());
    assertEquals("Описание эпика", checkEntity.getDescription());
    assertEquals(Status.NEW, checkEntity.getStatus());
  }

  @Test
  void createSubtask() {
    epic = new Epic(
        DEFAULT_ID,
        "Заголовок эпика",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    subtask = new Subtask(DEFAULT_ID,
        "Заголовок подзадачи",
        "Описание подзадачи",
        Status.NEW,
        epic.getId(),
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic);
    manager.createSubtask(subtask);

    Subtask checkEntity = manager.findSubtaskById(subtask.getId());

    assertNotNull(checkEntity);
    assertEquals("Заголовок подзадачи", checkEntity.getTitle());
    assertEquals("Описание подзадачи", checkEntity.getDescription());
    assertEquals(Status.NEW, checkEntity.getStatus());
  }

  @Test
  void updateTask() {
    final String name = "Новая задача";
    final String description = "Подробное описание";
    final Status status = Status.DONE;
    task = new Task(DEFAULT_ID,
        "Заголовок задачи",
        "Описание задачи",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createTask(task);
    task.setTitle(name);
    task.setDescription(description);
    task.setStatus(status);

    Task checkEntity = manager.updateTask(task);
    manager.updateTask(task);

    assertNotNull(checkEntity);
    assertEquals(name, checkEntity.getTitle());
    assertEquals(description, checkEntity.getDescription());
    assertEquals(Status.DONE, checkEntity.getStatus());
  }

  @Test
  void updateEpic() {
    final String name = "Новый эпик";
    final String description = "Подробное описание";
    final Status status = Status.DONE;
    epic = new Epic(DEFAULT_ID,
        "Заголовок эпика",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic);
    epic.setTitle(name);
    epic.setDescription(description);
    epic.setStatus(status);

    Epic checkEntity = manager.updateEpic(epic);
    manager.updateEpic(epic);

    assertNotNull(checkEntity);
    assertEquals(name, checkEntity.getTitle());
    assertEquals(description, checkEntity.getDescription());
    assertEquals(Status.DONE, checkEntity.getStatus());
  }

  @Test
  void updateSubtask() {
    final String name = "Новая подзадача";
    final String description = "Подробное описание";
    final Status status = Status.DONE;
    epic = new Epic(
        DEFAULT_ID,
        "Заголовок эпика",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    subtask = new Subtask(DEFAULT_ID,
        "Заголовок подзадачи",
        "Описание подзадачи",
        Status.NEW,
        epic.getId(),
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic);
    manager.createSubtask(subtask);
    subtask.setTitle(name);
    subtask.setDescription(description);
    subtask.setStatus(status);

    Subtask checkEntity = manager.updateSubtask(subtask);
    manager.updateSubtask(subtask);

    assertNotNull(checkEntity);
    assertEquals(name, checkEntity.getTitle());
    assertEquals(description, checkEntity.getDescription());
    assertEquals(Status.DONE, checkEntity.getStatus());
    assertEquals(epic.getId(), checkEntity.getEpicId());
  }

  @Test
  void deleteTaskById() {
    task = new Task(
        DEFAULT_ID,
        "Заголовок задачи",
        "Описание задачи",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createTask(task);

    manager.deleteTaskById(task.getId());

    assertFalse(manager.findAllTasks().contains(task));
    assertEquals(0, manager.findAllTasks().size());
    assertTrue(manager.findAllTasks().isEmpty());
  }

  @Test
  void deleteEpicById() {
    epic = new Epic(
        DEFAULT_ID,
        "Заголовок эпика",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic);

    manager.deleteEpicById(epic.getId());

    assertFalse(manager.findAllEpics().contains(epic));
    assertEquals(0, manager.findAllEpics().size());
    assertTrue(manager.findAllEpics().isEmpty());
  }

  @Test
  void deleteSubtaskById() {
    epic = new Epic(
        DEFAULT_ID,
        "Заголовок эпика",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic);
    subtask = new Subtask(
        DEFAULT_ID,
        "Заголовок подзадачи",
        "Описание подзадачи",
        Status.NEW,
        epic.getId(),
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createSubtask(subtask);

    manager.deleteSubtaskById(subtask.getId());

    assertFalse(manager.findAllSubtasks().contains(subtask));
    assertEquals(0, manager.findAllSubtasks().size());
    assertTrue(manager.findAllSubtasks().isEmpty());
  }

  @Test
  void deleteAllTasks() {
    Task task1 = new Task(
        DEFAULT_ID,
        "Заголовок задачи 1",
        "Описание задачи",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    Task task2 = new Task(
        DEFAULT_ID,
        "Заголовок задачи 1",
        "Описание задачи",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    Task task3 = new Task(
        DEFAULT_ID,
        "Заголовок задачи 1",
        "Описание задачи",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createTask(task1);
    manager.createTask(task2);
    manager.createTask(task3);

    manager.deleteAllTasks();

    assertEquals(0, manager.findAllTasks().size());
  }

  @Test
  void deleteAllEpics() {
    Epic epic1 = new Epic(
        DEFAULT_ID,
        "Заголовок эпика 1",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    Epic epic2 = new Epic(
        DEFAULT_ID,
        "Заголовок эпика 1",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    Epic epic3 = new Epic(
        DEFAULT_ID,
        "Заголовок эпика 1",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic1);
    manager.createEpic(epic2);
    manager.createEpic(epic3);

    manager.deleteAllEpics();

    assertEquals(0, manager.findAllEpics().size());
  }

  @Test
  void deleteAllSubtasks() {
    Epic epic1 = new Epic(
        DEFAULT_ID,
        "Заголовок эпика 1",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    Epic epic2 = new Epic(
        DEFAULT_ID,
        "Заголовок эпика 1",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    Epic epic3 = new Epic(
        DEFAULT_ID,
        "Заголовок эпика 1",
        "Описание эпика",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createEpic(epic1);
    manager.createEpic(epic2);
    manager.createEpic(epic3);
    Subtask subtask1 = new Subtask(
        DEFAULT_ID,
        "Заголовок подзадачи",
        "Описание подзадачи",
        Status.NEW,
        epic1.getId(),
        startTime,
        Duration.ofMinutes(15)
    );
    Subtask subtask2 = new Subtask(
        DEFAULT_ID,
        "Заголовок подзадачи",
        "Описание подзадачи",
        Status.NEW,
        epic1.getId(),
        startTime,
        Duration.ofMinutes(15)
    );
    Subtask subtask3 = new Subtask(
        DEFAULT_ID,
        "Заголовок подзадачи",
        "Описание подзадачи",
        Status.NEW,
        epic1.getId(),
        startTime,
        Duration.ofMinutes(15)
    );
    manager.createSubtask(subtask1);
    manager.createSubtask(subtask2);
    manager.createSubtask(subtask3);

    manager.deleteAllSubtasks();

    assertEquals(0, manager.findAllSubtasks().size());
  }
}
