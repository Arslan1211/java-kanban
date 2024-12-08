package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Epic extends Task {
    private final Collection<Integer> subtaskIds;

    public Epic(String title, String description) {
        super(title, description);
        subtaskIds = new ArrayList<>();
    }

    public Collection<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void deleteSubtask(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        String format = "Epic ID: %d, название: '%s', описание: '%s', статус: %s \n";
        return String.format(format, super.getId(), super.getTitle(), super.getDescription(), super.getStatus());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }
}