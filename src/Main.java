import java.io.File;
import java.time.Duration;
import java.time.Instant;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

  public static void main(String[] args) {

    File file = new File("fileForSavingTasks.csv");

    TaskManager taskManager = new InMemoryTaskManager();
    checkTaskManager(taskManager);

    FileBackedTaskManager fileBackedTaskManager;
    fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
    checkTaskManager(fileBackedTaskManager);

    FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);
    System.out.println(loadFromFile.getPrioritizedTasks());
  }

  public static void checkTaskManager(TaskManager manager) {
    Task task = new Task(
        1,
        "название",
        "описание",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(15));
    Epic epic = new Epic(
        2,
        "название",
        "описание",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(25));
    Subtask subtask = new Subtask(
        3,
        "название",
        "описание",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(45));
    manager.createTask(task);
    manager.createEpic(epic);
    manager.createSubtask(subtask);
    System.out.println(manager.findAllTasks());
    System.out.println(manager.findAllEpics());
    System.out.println(manager.findAllSubtasks());
  }
}