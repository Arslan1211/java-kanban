package service;

import static model.TypeTask.EPIC;
import static model.TypeTask.SUBTASK;
import static model.TypeTask.TASK;

import exception.ManagerReadException;
import exception.ManagerSaveException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import model.TypeTask;

/**
 * Реализация менеджера задач, который сохраняет данные в файл и восстанавливает их из файла.
 * Наследует функциональность {@link InMemoryTaskManager} и добавляет возможность работы с файлами.
 */
public class FileBackedTaskManager extends InMemoryTaskManager {

  protected File file;
  private static final String SEPARATOR = ",";
  private static final String NEW_LINE = "\n";
  protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy/HH:mm");

  /**
   * Конструктор для создания менеджера задач с поддержкой сохранения в файл.
   *
   * @param historyManager менеджер истории для отслеживания просмотренных задач.
   * @param file           файл, в который будут сохраняться данные.
   */
  public FileBackedTaskManager(HistoryManager historyManager, File file) {
    super(historyManager);
    this.file = file;
  }

  /**
   * Сохраняет все задачи, эпики и подзадачи в файл.
   */
  private void save() {
    String taskAsString = "type, id, name, description, status, links, start_time, duration, [end_time,]\n";

    Collection<Task> allTasks = findAllTasks();
    for (Task task : allTasks) {
      taskAsString = taskToString(task);
    }
    Collection<Epic> allEpics = findAllEpics();
    for (Epic epic : allEpics) {
      taskAsString = taskToString(epic);
    }
    Collection<Subtask> allSubtask = findAllSubtasks();
    for (Subtask subtask : allSubtask) {
      taskAsString = taskToString(subtask);
    }
    writeStringToFile(taskAsString);
  }

  /**
   * Записывает строку в файл.
   *
   * @param taskAsString строка, представляющая задачу, которую нужно записать в файл.
   * @throws ManagerSaveException если произошла ошибка при записи в файл.
   */
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

  /**
   * Преобразует задачу в строку для сохранения в файл.
   *
   * @param task задача, которую нужно преобразовать в строку.
   * @return строка, представляющая задачу.
   */
  private String taskToString(Task task) {
    StringBuilder sb = new StringBuilder();
    sb.append(task.getId()).append(SEPARATOR);
    sb.append(task.getType()).append(SEPARATOR);
    sb.append(task.getTitle()).append(SEPARATOR);
    sb.append(task.getDescription()).append(SEPARATOR);
    sb.append(task.getStatus()).append(SEPARATOR);

    if (task.getStartTime() != null) {
      sb.append(ZonedDateTime.ofInstant(task.getStartTime(), ZoneId.systemDefault())
          .format(formatter)).append(SEPARATOR);
    } else {
      sb.append("null").append(SEPARATOR); // Используем "null" как метку
    }

    sb.append(task.getDuration().toMinutes());

    if (task.getType().equals(SUBTASK) && task instanceof Subtask subtask) {
      sb.append(SEPARATOR).append(subtask.getEpicId());
    }

    return sb.toString();
  }

  /**
   * Загружает данные из файла и создает новый экземпляр {@link FileBackedTaskManager}.
   *
   * @param file файл, из которого нужно загрузить данные.
   * @return новый экземпляр {@link FileBackedTaskManager} с восстановленными данными.
   * @throws ManagerReadException если произошла ошибка при чтении файла.
   */
  public static FileBackedTaskManager loadFromFile(File file) {
    try {
      FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(
          new InMemoryHistoryManager(), file);
      Set<String> allLines = new HashSet<>(Files.readAllLines(file.toPath()));

      allLines.stream().filter(line -> line != null && !line.trim().isEmpty())
          .map(FileBackedTaskManager::fromString).filter(Objects::nonNull).forEach(task -> {
            fileBackedTaskManager.id = Math.max(fileBackedTaskManager.id, task.getId());
            TypeTask type = task.getType();
            if (type.equals(TASK)) {
              fileBackedTaskManager.restoreTask(task);

            } else if (type.equals(EPIC) && task instanceof Epic epic) {
              fileBackedTaskManager.epics.put(epic.getId(), epic);

            } else if (type.equals(SUBTASK) && task instanceof Subtask subtask) {
              fileBackedTaskManager.restoreTask(subtask);

              if (fileBackedTaskManager.epics.get(subtask.getEpicId()) != null) {
                fileBackedTaskManager.epics.get(subtask.getEpicId()).getSubtaskIds()
                    .add(subtask.getId());
              }
            }
          });

      return fileBackedTaskManager;
    } catch (IOException e) {
      String errorMessage = "Невозможно прочитать файл, ошибка: " + e.getMessage();
      System.out.println(errorMessage);
      throw new ManagerReadException(errorMessage);
    }
  }

  /**
   * Преобразует строку в объект задачи.
   *
   * @param value строка, представляющая задачу.
   * @return объект задачи ({@link Task}, {@link Epic} или {@link Subtask}).
   * @throws IllegalArgumentException если строка имеет некорректный формат.
   */
  private static Task fromString(String value) {
    try {
      String[] split = value.split(SEPARATOR);
      if (split.length < 7) { // Проверяем, что все поля присутствуют
        System.out.println("Некорректный формат строки: " + value);
        return null;
      }

      int id = Integer.parseInt(split[0]);
      String type = split[1];
      String name = split[2];
      String description = split[3];
      Status status = Status.valueOf(split[4]);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm");
      LocalDateTime dateTime = LocalDateTime.parse(split[5].trim(), formatter);
      Instant startTime = dateTime.atZone(ZoneId.systemDefault()).toInstant();

      long durationMinutes = Long.parseLong(split[6].trim());
      Duration duration = Duration.ofMinutes(durationMinutes);

      return switch (type) {
        case "TASK" -> new Task(id, name, description, status, startTime, duration);
        case "EPIC" -> new Epic(id, name, description, status, startTime, duration);
        case "SUBTASK" -> new Subtask(id, name, description, status,
            Integer.parseInt(split[split.length - 1]), startTime, duration);
        default -> {
          System.out.println(type + " такого типа не существует");
          yield null;
        }
      };
    } catch (Exception e) {
      System.out.println("Ошибка при парсинге строки: " + value + ", ошибка: " + e.getMessage());
      return null;
    }
  }

  private void restoreTask(Task task) {
    tasks.put(task.getId(), task);
    addTaskInPrioritizedList(task);
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
