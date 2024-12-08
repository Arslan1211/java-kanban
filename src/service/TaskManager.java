package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(idGenerate());
        return tasks.put(task.getId(), task);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(idGenerate());
        return epics.put(epic.getId(), epic);
    }

    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        subtask.setId(idGenerate());
        epic.getSubtaskIds().add(subtask.getId());
        Subtask createdSubtask = subtasks.put(subtask.getId(), subtask);
        checkEpicStatus(epic);
        return createdSubtask;
    }

    public Collection<Task> findAllTasks() {
        return tasks.values();
    }

    public Collection<Epic> findAllEpics() {
        return epics.values();
    }

    public Collection<Subtask> findAllSubtasks() {
        return subtasks.values();
    }

    public Collection<Subtask> findAllSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        Collection<Integer> subtasksIds = epic.getSubtaskIds();
        List<Subtask> epicSubtask = new ArrayList<>();
        for (Integer subtasksId : subtasksIds) {
            epicSubtask.add(subtasks.get(subtasksId));
        }
        return epicSubtask;
    }

    public Task findTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic findEpicById(Integer id) {
        return epics.get(id);
    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return createTask(task);
        }
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return createEpic(epic);
        }
        Epic oldEpic = epics.put(epic.getId(), epic);
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.getSubtaskIds().addAll(oldEpic.getSubtaskIds());
        return updatedEpic;
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            Subtask createdSubtask = createSubtask(subtask);
            Epic epic = epics.get(createdSubtask.getId());
            checkEpicStatus(epic);
            return createdSubtask;
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

    public Task deleteTaskById(int id) {
        return tasks.remove(id);
    }

    public Epic deleteEpicById(int id) {
        Epic epic = epics.get(id);
        Collection<Integer> subtasksIds = new ArrayList<>(epic.getSubtaskIds());
        for (Integer subtasksId : subtasksIds) {
            deleteSubtaskById(subtasksId);
        }
        return epics.remove(id);
    }

    public Subtask deleteSubtaskById(int id) {
        Subtask deletedSubtask = subtasks.remove(id);
        Epic epic = epics.get(deletedSubtask.getEpicId());
        epic.deleteSubtask(deletedSubtask.getId());
        checkEpicStatus(epic);
        return deletedSubtask;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    private int idGenerate() {
        return ++id;
    }
}