package service;

import static org.junit.jupiter.api.Assertions.*;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

public abstract class AbstractTaskManagerTest<T extends TaskManager> {

  protected T manager;
  protected static final int DEFAULT_ID = 1;

  @Test
  void createTask() {
    Task task = new Task("Заголовок задачи", "Описание задачи", Status.NEW);
    manager.createTask(task);

    assertNotNull(task);
    assertEquals("Заголовок задачи", task.getTitle());
    assertEquals("Описание задачи", task.getDescription());
    assertEquals(Status.NEW, task.getStatus());
  }

  @Test
  void createEpic() {
    Epic epic = new Epic(DEFAULT_ID, "Заголовок эпика", "Описание эпика", Status.NEW);
    manager.createEpic(epic);

    assertNotNull(epic);
    assertEquals("Заголовок эпика", epic.getTitle());
    assertEquals("Описание эпика", epic.getDescription());
    assertEquals(Status.NEW, epic.getStatus());
  }

  @Test
  void createSubtask() {
    Subtask subtask = new Subtask(DEFAULT_ID, "Заголовок подзадачи", "Описание подзадачи",
        Status.NEW);
    manager.createSubtask(subtask);

    assertNotNull(subtask);
    assertEquals("Заголовок подзадачи", subtask.getTitle());
    assertEquals("Описание подзадачи", subtask.getDescription());
    assertEquals(Status.NEW, subtask.getStatus());
  }

  @Test
  void updateTask() {
    final String name = "Новая задача";
    final String description = "Подробное описание";
    final Status status = Status.DONE;
    Task task = new Task("Заголовок задачи", "Описание задачи", Status.NEW);
    manager.createTask(task);
    task.setTitle(name);
    task.setDescription(description);
    task.setStatus(status);

    manager.updateTask(task);

    assertNotNull(task);
    assertEquals(name, task.getTitle());
    assertEquals(description, task.getDescription());
    assertEquals(Status.DONE, task.getStatus());
  }

  @Test
  void updateEpic() {
    final String name = "Новый эпик";
    final String description = "Подробное описание";
    final Status status = Status.DONE;
    Epic epic = new Epic(DEFAULT_ID, "Заголовок эпика", "Описание эпика", Status.NEW);
    manager.createEpic(epic);
    epic.setTitle(name);
    epic.setDescription(description);
    epic.setStatus(status);

    manager.updateEpic(epic);

    assertNotNull(epic);
    assertEquals(name, epic.getTitle());
    assertEquals(description, epic.getDescription());
    assertEquals(Status.DONE, epic.getStatus());
  }

  @Test
  void updateSubtask() {
    final String name = "Новая подзадача";
    final String description = "Подробное описание";
    final Status status = Status.DONE;
    Subtask subtask = new Subtask(DEFAULT_ID, "Заголовок подзадачи", "Описание подзадачи",
        Status.NEW);
    manager.createSubtask(subtask);
    subtask.setTitle(name);
    subtask.setDescription(description);
    subtask.setStatus(status);

    manager.updateSubtask(subtask);

    assertNotNull(subtask);
    assertEquals(name, subtask.getTitle());
    assertEquals(description, subtask.getDescription());
    assertEquals(Status.DONE, subtask.getStatus());
  }

  @Test
  void deleteTaskById() {
    Task task = new Task("Заголовок задачи", "Описание задачи", Status.NEW);
    manager.createTask(task);

    manager.deleteTaskById(task.getId());

    assertFalse(manager.findAllTasks().contains(task));
  }

  @Test
  void deleteEpicById() {
    Epic epic = new Epic(DEFAULT_ID, "Заголовок эпика", "Описание эпика", Status.NEW);
    manager.createEpic(epic);

    manager.deleteEpicById(epic.getId());

    assertFalse(manager.findAllEpics().contains(epic));
  }

  @Test
  void deleteSubtaskById() {
    Epic epic = new Epic(DEFAULT_ID, "Заголовок эпика", "Описание эпика", Status.NEW);
    manager.createEpic(epic);
    Subtask subtask = new Subtask(DEFAULT_ID, epic.getId(), "Заголовок подзадачи",
        "Описание подзадачи", Status.NEW);
    manager.createSubtask(subtask);

    manager.deleteSubtaskById(subtask.getId());

    assertFalse(manager.findAllSubtasks().contains(subtask));
  }

  @Test
  void deleteAllTasks() {
    Task task1 = new Task("Заголовок задачи 1", "Описание задачи", Status.NEW);
    Task task2 = new Task("Заголовок задачи 2", "Описание задачи", Status.NEW);
    Task task3 = new Task("Заголовок задачи 3", "Описание задачи", Status.NEW);
    manager.createTask(task1);
    manager.createTask(task2);
    manager.createTask(task3);

    manager.deleteAllTasks();

    assertEquals(0, manager.findAllTasks().size());
  }

  @Test
  void deleteAllEpics() {
    Epic epic1 = new Epic(DEFAULT_ID, "Заголовок эпика 1", "Описание эпика", Status.NEW);
    Epic epic2 = new Epic(DEFAULT_ID, "Заголовок эпика 2", "Описание эпика", Status.NEW);
    Epic epic3 = new Epic(DEFAULT_ID, "Заголовок эпика 3", "Описание эпика", Status.NEW);
    manager.createEpic(epic1);
    manager.createEpic(epic2);
    manager.createEpic(epic3);

    manager.deleteAllEpics();

    assertEquals(0, manager.findAllEpics().size());
  }

  @Test
  void deleteAllSubtasks() {
    Epic epic1 = new Epic(DEFAULT_ID, "Заголовок эпика 1", "Описание эпика", Status.NEW);
    Epic epic2 = new Epic(DEFAULT_ID, "Заголовок эпика 2", "Описание эпика", Status.NEW);
    Epic epic3 = new Epic(DEFAULT_ID, "Заголовок эпика 3", "Описание эпика", Status.NEW);
    manager.createEpic(epic1);
    manager.createEpic(epic2);
    manager.createEpic(epic2);
    Subtask subtask1 = new Subtask(DEFAULT_ID, epic1.getId(), "Заголовок подзадачи",
        "Описание подзадачи", Status.NEW);
    Subtask subtask2 = new Subtask(DEFAULT_ID, epic2.getId(), "Заголовок подзадачи",
        "Описание подзадачи", Status.NEW);
    Subtask subtask3 = new Subtask(DEFAULT_ID, epic3.getId(), "Заголовок подзадачи",
        "Описание подзадачи", Status.NEW);
    manager.createSubtask(subtask1);
    manager.createSubtask(subtask2);
    manager.createSubtask(subtask3);

    manager.deleteAllSubtasks();

    assertEquals(0, manager.findAllSubtasks().size());
  }
}