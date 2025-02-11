package service;

import exception.ManagerReadException;
import exception.ManagerSaveException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import model.TypeTask;

public class FileBackedTaskManager extends InMemoryTaskManager {

  protected File file;
  private static final String SEPARATOR = ",";
  private static final String NEW_LINE = "\n";

  public FileBackedTaskManager(HistoryManager historyManager, File file) {
    super(historyManager);
    this.file = file;
  }

  private void save() {
    Collection<Task> allTasks = findAllTasks();
    for (Task task : allTasks) {
      String taskAsString = taskToString(task);
      writeStringToFile(taskAsString);
    }

    Collection<Epic> allEpics = findAllEpics();
    for (Epic epic : allEpics) {
      String epicAsString = taskToString(epic);
      writeStringToFile(epicAsString);
    }

    Collection<Subtask> allSubtask = findAllSubtasks();
    for (Subtask subtask : allSubtask) {
      String subtaskAsString = taskToString(subtask);
      writeStringToFile(subtaskAsString);
    }
  }

  private void writeStringToFile(String taskAsString) {
    try (FileWriter fileWriter = new FileWriter(file, true)) {
      fileWriter.write(taskAsString);
      fileWriter.write(NEW_LINE);
    } catch (IOException e) {
      String errorMessage = "Не удалось сохранить файл, ошибка: " + e.getMessage();
      System.out.println(errorMessage);
      throw new ManagerSaveException(errorMessage);
    }
  }

  private String taskToString(Task task) {
    StringBuilder sb = new StringBuilder();
    sb.append(task.getId()).append(SEPARATOR);
    sb.append(task.getType()).append(SEPARATOR);
    sb.append(task.getTitle()).append(SEPARATOR);
    sb.append(task.getStatus()).append(SEPARATOR);
    sb.append(task.getDescription());

    if (task.getType().equals(TypeTask.SUBTASK) && task instanceof Subtask subtask) {
      sb.append(SEPARATOR).append(subtask.getEpicId());
    }

    return sb.toString();
  }

  public static FileBackedTaskManager loadFromFile(File file) {
    try {
      FileBackedTaskManager fileBackedTaskManager;
      fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
      Set<String> allLines = new HashSet<>(Files.readAllLines(file.toPath()));
      for (String line : allLines) {
        Task task = fromString(line);
        assert task != null;
        TypeTask type = task.getType();
        if (type.equals(TypeTask.TASK)) {
          fileBackedTaskManager.tasks.put(task.getId(), task);
        } else if (type.equals(TypeTask.EPIC) && task instanceof Epic epic) {
          fileBackedTaskManager.epics.put(epic.getId(), epic);
        } else if (type.equals(TypeTask.SUBTASK) && task instanceof Subtask subtask) {
          fileBackedTaskManager.subtasks.put(subtask.getId(), subtask);
          fileBackedTaskManager.epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
        }
      }
      return fileBackedTaskManager;
    } catch (IOException e) {
      String errorMessage = "Невозможно прочитать файл, ошибка: " + e.getMessage();
      System.out.println(errorMessage);
      throw new ManagerReadException(errorMessage);
    }
  }

  private static Task fromString(String value) {
    String[] split = value.split(SEPARATOR);
    int id = Integer.parseInt(split[0]);
    String type = split[1];
    String name = split[2];
    Status status = Status.valueOf(split[3]);
    String description = split[4];
    switch (type) {
      case "TASK":
        return new Task(id, name, description, status);
      case "EPIC":
        return new Epic(id, name, description, status);
      case "SUBTASK":
        return new Subtask(id, name, description, status,
            Integer.parseInt(split[split.length - 1]));
      default:
        System.out.println(type + " такого типа не существует");
        return null;
    }
  }

  @Override
  public Task createTask(Task task) {
    Task createdTask = super.createTask(task);
    save();
    return createdTask;
  }

  @Override
  public Epic createEpic(Epic epic) {
    Epic createdEpic = super.createEpic(epic);
    save();
    return createdEpic;
  }

  @Override
  public Subtask createSubtask(Subtask subtask) {
    Subtask createdSubtask = super.createSubtask(subtask);
    save();
    return createdSubtask;
  }

  @Override
  public Task updateTask(Task task) {
    Task updatedTask = super.updateTask(task);
    save();
    return updatedTask;
  }

  @Override
  public Epic updateEpic(Epic epic) {
    Epic updatedEpic = super.updateEpic(epic);
    save();
    return updatedEpic;
  }

  @Override
  public Subtask updateSubtask(Subtask subtask) {
    Subtask updatedSubtask = super.updateSubtask(subtask);
    save();
    return updatedSubtask;
  }

  @Override
  public Task deleteTaskById(int id) {
    Task deletedTaskById = super.deleteTaskById(id);
    save();
    return deletedTaskById;
  }

  @Override
  public Epic deleteEpicById(int id) {
    Epic deletedEpicById = super.deleteEpicById(id);
    save();
    return deletedEpicById;
  }

  @Override
  public Subtask deleteSubtaskById(int id) {
    Subtask deletedSubtaskById = super.deleteSubtaskById(id);
    save();
    return deletedSubtaskById;
  }

  @Override
  public void deleteAllTasks() {
    super.deleteAllTasks();
    save();
  }

  @Override
  public void deleteAllEpics() {
    super.deleteAllEpics();
    save();
  }

  @Override
  public void deleteAllSubtasks() {
    super.deleteAllSubtasks();
    save();
  }
}
