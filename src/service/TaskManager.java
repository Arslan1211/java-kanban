package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Collection;
import java.util.List;

public interface TaskManager {

  List<Task> getHistory();

  Task createTask(Task task);

  Epic createEpic(Epic epic);

  Subtask createSubtask(Subtask subtask);

  Collection<Task> findAllTasks();

  Collection<Epic> findAllEpics();

  Collection<Subtask> findAllSubtasks();

  Collection<Subtask> findAllSubtasksByEpic(int epicId);

  Task findTaskById(Integer id);

  Epic findEpicById(Integer id);

  Subtask findSubtaskById(Integer id);

  Task updateTask(Task task);

  Epic updateEpic(Epic epic);

  Subtask updateSubtask(Subtask subtask);

  Task deleteTaskById(Integer id);

  Epic deleteEpicById(Integer id);

  Subtask deleteSubtaskById(Integer id);

  void deleteAllTasks();

  void deleteAllEpics();

  void deleteAllSubtasks();

  List<Task> getPrioritizedTasks();
}