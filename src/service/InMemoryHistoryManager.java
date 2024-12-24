package service;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int MAXIMUM_HISTORY_CAPACITY = 10;
    private final List<Task> history = new LinkedList<>();

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            history.add(task);
            if (history.size() > MAXIMUM_HISTORY_CAPACITY) {
                history.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}