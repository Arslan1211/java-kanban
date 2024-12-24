package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public Task createTask(Task task) {
        task.setId(idGenerate());
        return tasks.put(task.getId(), task);
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
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return null;
        }
        Epic oldEpic = epics.put(epic.getId(), epic);
        Epic updatedEpic = epics.get(epic.getId());
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
        return updatedSubtask;
    }

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

    @Override
    public Task deleteTaskById(int id) {
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
        }
        return epics.remove(id);
    }

    @Override
    public Subtask deleteSubtaskById(int id) {
        Subtask deletedSubtask = subtasks.remove(id);
        Epic epic = epics.get(deletedSubtask.getEpicId());
        epic.deleteSubtask(deletedSubtask.getId());
        checkEpicStatus(epic);
        return deletedSubtask;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            checkEpicStatus(epic);
        }
    }

    private int idGenerate() {
        return ++id;
    }
}