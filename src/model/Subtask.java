package model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String title, String description, Status status) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, int epicId, String title, String description, Status status) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String format = "Subtask ID: %d, название: '%s', описание: '%s', статус: %s";
        return String.format(format, super.getId(), super.getTitle(), super.getDescription(), super.getStatus());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}