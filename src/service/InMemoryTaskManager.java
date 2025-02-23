package service;

import java.time.Duration;
import java.time.Instant;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;

/**
 * Реализация менеджера задач, который хранит данные в оперативной памяти. Поддерживает создание,
 * обновление, удаление и поиск задач, эпиков и подзадач. Также предоставляет функциональность для
 * работы с историей просмотров и приоритетными задачами.
 */
public class InMemoryTaskManager implements TaskManager {

  protected int id = 0;
  protected final Map<Integer, Task> tasks;
  protected final Map<Integer, Epic> epics;
  protected final Map<Integer, Subtask> subtasks;
  protected HistoryManager historyManager = Managers.getDefaultHistoryManager();
  protected TreeSet<Task> prioritizedTasks = new TreeSet<>(
      Comparator.comparing(Task::getStartTime));

  /**
   * Конструктор для создания менеджера задач с указанным менеджером истории.
   *
   * @param historyManager менеджер истории для отслеживания просмотренных задач.
   */
  public InMemoryTaskManager(HistoryManager historyManager) {
    id = 0;
    this.historyManager = historyManager;
    tasks = new HashMap<>();
    subtasks = new HashMap<>();
    epics = new HashMap<>();
  }

  /**
   * Конструктор для создания менеджера задач с менеджером истории по умолчанию.
   */
  public InMemoryTaskManager() {
    tasks = new HashMap<>();
    subtasks = new HashMap<>();
    epics = new HashMap<>();
  }

  /**
   * Проверяет и обновляет статус эпика на основе статусов его подзадач.
   *
   * @param epic эпик, статус которого нужно проверить и обновить.
   */
  private void checkEpicStatus(Epic epic) {
    if (epic.getSubtaskIds().isEmpty()) {
      epic.setStatus(Status.NEW);
      return;
    }

    boolean allNew = true;
    boolean allDone = true;

    for (Integer subtaskId : epic.getSubtaskIds()) {
      Status subtaskStatus = subtasks.get(subtaskId).getStatus();
      if (subtaskStatus != Status.NEW) {
        allNew = false;
      }
      if (subtaskStatus != Status.DONE) {
        allDone = false;
      }
    }

    if (allNew) {
      epic.setStatus(Status.NEW);
    } else if (allDone) {
      epic.setStatus(Status.DONE);
    } else {
      epic.setStatus(Status.IN_PROGRESS);
    }
  }

  /**
   * Генерирует уникальный идентификатор для задачи.
   *
   * @return уникальный идентификатор.
   */
  private int idGenerate() {
    return ++id;
  }

  /**
   * Проверяет, пересекается ли временной интервал новой задачи с существующими задачами.
   *
   * @param newTask новая задача, которую нужно проверить.
   * @return true, если есть пересечение, иначе false.
   */
  private boolean hasTimeOverlap(Task newTask) {
    return prioritizedTasks.stream().noneMatch(existingTask ->
        !existingTask.getEndTime().isBefore(newTask.getStartTime()) &&
            !existingTask.getStartTime().isAfter(newTask.getEndTime())
    );
  }

  /**
   * Пересчитывает время начала эпика на основе времени начала его подзадач.
   *
   * @param epic эпик, время начала которого нужно пересчитать.
   */
  private void recalculateEpicStartTime(Epic epic) {
    epic.setStartTime(
        epic.getSubtaskIds().stream()
            .map(subtasks::get)
            .map(Subtask::getStartTime)
            .filter(Objects::nonNull)
            .min(Instant::compareTo)
            .orElse(null)
    );
  }

  /**
   * Вычисляет общую продолжительность эпика на основе продолжительности его подзадач.
   *
   * @param epic эпик, продолжительность которого нужно вычислить.
   */
  private void calculateEpicTotalDuration(Epic epic) {
    Duration duration = epic.getSubtaskIds().stream()
        .map(subtasks::get)
        .map(Subtask::getDuration)
        .filter(Objects::nonNull)
        .reduce(Duration.ofMinutes(0), Duration::plus);

    epic.setDuration(duration);
  }

  /**
   * Обновляет временные параметры эпика (время начала, продолжительность и время окончания).
   *
   * @param epic эпик, временные параметры которого нужно обновить.
   */
  private void refreshEpicTiming(Epic epic) {
    recalculateEpicStartTime(epic);
    calculateEpicTotalDuration(epic);

    epic.setEndTime(
        epic.getStartTime() != null && epic.getDuration() != null
            ? epic.getStartTime().plus(epic.getDuration())
            : null
    );
  }

  @Override
  public List<Task> getHistory() {
    return historyManager.getHistory();
  }

  @Override
  public Task createTask(Task task) {
    task.setId(idGenerate());
    Task put = tasks.put(task.getId(), task);
    if (task.getStartTime() != null && hasTimeOverlap(task)) {
      tasks.put(task.getId(), task);
      prioritizedTasks.add(task);
    }
    return put;
  }

  @Override
  public Epic createEpic(Epic epic) {
    epic.setId(idGenerate());
    return epics.put(epic.getId(), epic);
  }

  @Override
  public Subtask createSubtask(Subtask subtask) {
    Epic epic = epics.get(subtask.getEpicId());
    if (epic == null) {
      return null;
    }

    subtask.setId(idGenerate());
    epic.getSubtaskIds().add(subtask.getId());
    Subtask createdSubtask = subtasks.put(subtask.getId(), subtask);
    checkEpicStatus(epic);
    prioritizedTasks.add(subtask);
    return createdSubtask;
  }

  @Override
  public Collection<Task> findAllTasks() {
    return tasks.values();
  }

  @Override
  public Collection<Epic> findAllEpics() {
    return epics.values();
  }

  @Override
  public Collection<Subtask> findAllSubtasks() {
    return subtasks.values();
  }

  @Override
  public Collection<Subtask> findAllSubtasksByEpic(int epicId) {
    Epic epic = epics.get(epicId);
    Collection<Integer> subtasksIds = epic.getSubtaskIds();
    List<Subtask> epicSubtask = new ArrayList<>();
    for (Integer subtasksId : subtasksIds) {
      epicSubtask.add(subtasks.get(subtasksId));
    }
    return epicSubtask;
  }

  @Override
  public Task findTaskById(Integer id) {
    Task task = tasks.get(id);
    historyManager.addHistory(task);
    return tasks.get(id);
  }

  @Override
  public Epic findEpicById(Integer id) {
    Epic epic = epics.get(id);
    historyManager.addHistory(epic);
    return epic;
  }

  @Override
  public Subtask findSubtaskById(Integer id) {
    Subtask subtask = subtasks.get(id);
    historyManager.addHistory(subtask);
    return subtask;
  }

  @Override
  public Task updateTask(Task task) {
    if (!tasks.containsKey(task.getId())) {
      return null;
    }
    if (task.getStartTime() != null && hasTimeOverlap(task)) {
      tasks.put(task.getId(), task);
      prioritizedTasks.add(task);
    }
    return tasks.get(task.getId());
  }

  @Override
  public Epic updateEpic(Epic epic) {
    if (!epics.containsKey(epic.getId())) {
      return null;
    }
    Epic oldEpic = epics.put(epic.getId(), epic);
    Epic updatedEpic = epics.get(epic.getId());
    assert oldEpic != null;
    updatedEpic.getSubtaskIds().addAll(oldEpic.getSubtaskIds());
    return updatedEpic;
  }

  @Override
  public Subtask updateSubtask(Subtask subtask) {
    if (!subtasks.containsKey(subtask.getId())) {
      return null;
    }
    subtasks.put(subtask.getId(), subtask);
    Subtask updatedSubtask = subtasks.get(subtask.getId());
    Epic epic = epics.get(updatedSubtask.getEpicId());
    checkEpicStatus(epic);
    prioritizedTasks.add(subtask);
    return updatedSubtask;
  }

  @Override
  public Task deleteTaskById(int id) {
    prioritizedTasks.remove(tasks.remove(id));
    historyManager.remove(id);
    return tasks.remove(id);
  }

  @Override
  public Epic deleteEpicById(int id) {
    Epic epic = epics.get(id);
    if (epic == null) {
      return null;
    }
    Collection<Integer> subtaskIds = epic.getSubtaskIds();
    for (Integer subtaskId : subtaskIds) {
      subtasks.remove(subtaskId);
      historyManager.remove(subtaskId);
    }
    historyManager.remove(id);
    return epics.remove(id);
  }

  @Override
  public Subtask deleteSubtaskById(int id) {
    Subtask deletedSubtask = subtasks.remove(id);
    Epic epic = epics.get(deletedSubtask.getEpicId());
    epic.deleteSubtask(deletedSubtask.getId());
    prioritizedTasks.remove(deletedSubtask);
    checkEpicStatus(epic);
    refreshEpicTiming(epic);
    historyManager.remove(id);
    return deletedSubtask;
  }

  @Override
  public void deleteAllTasks() {
    tasks.values().forEach(prioritizedTasks::remove);
    tasks.keySet().forEach(historyManager::remove);
    tasks.clear();
  }

  @Override
  public void deleteAllEpics() {
    tasks.values().forEach(prioritizedTasks::remove);
    epics.keySet().forEach(epic -> {
      historyManager.remove(epic);
    });
    subtasks.clear();
    epics.clear();
  }

  @Override
  public void deleteAllSubtasks() {
    for (Epic epic : epics.values()) {
      epic.clearSubtaskIds();
      checkEpicStatus(epic);
    }
    for (Integer subtask : subtasks.keySet()) {
      historyManager.remove(subtask);
    }
    subtasks.values().forEach(prioritizedTasks::remove);
    subtasks.clear();
  }

  @Override
  public TreeSet<Task> getPrioritizedTasks() {
    return new TreeSet<>(prioritizedTasks);
  }
}