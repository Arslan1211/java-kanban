package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

  private FileBackedTaskManager taskManager;
  private File file;

  @BeforeEach
  public void setUp() {
    file = new File("tasks.csv");
    taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
  }

  @AfterEach
  public void tearDown() {
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void testSaveTask() {
    Task task = new Task(1, "Тестовая задача", "Описание", Status.NEW);
    taskManager.createTask(task);

    List<String> lines;
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (IOException e) {
      fail("Не удалось прочитать файл: " + e.getMessage());
      return;
    }

    assertEquals(1, lines.size());
    assertTrue(lines.get(0).contains("Тестовая задача"));
  }

  @Test
  public void testSaveEpic() {
    Epic epic = new Epic(1, "Тестовый эпик", "Описание", Status.NEW);
    taskManager.createEpic(epic);

    List<String> lines;
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (IOException e) {
      fail("Не удалось прочитать файл: " + e.getMessage());
      return;
    }

    assertEquals(1, lines.size());
    assertTrue(lines.get(0).contains("Тестовый эпик"));
  }

  @Test
  public void testSaveSubtask() {
    Epic epic = new Epic(1, "Тестовый эпик", "Описание", Status.NEW);
    taskManager.createEpic(epic);

    Subtask subtask = new Subtask(2, "Тестовая подзадача", "Описание", Status.NEW, 1);
    taskManager.createSubtask(subtask);

    List<String> lines;
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (IOException e) {
      fail("Не удалось прочитать файл: " + e.getMessage());
      return;
    }

    assertEquals(1, epic.getId());
    assertTrue(lines.get(2).contains("1"));
  }

  @Test
  public void testLoadFromFile() {
    Task task = new Task(1, "Тестовая задача", "Описание", Status.NEW);
    taskManager.createTask(task);

    FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);

    assertEquals(1, loadedTaskManager.findAllTasks().size());
    Task loadedTask = loadedTaskManager.findTaskById(1);
    assertNotNull(loadedTask);
    assertEquals("Тестовая задача", loadedTask.getTitle());
  }
}


