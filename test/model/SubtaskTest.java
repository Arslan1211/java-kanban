package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    private Subtask subtask;

    @BeforeEach
    void init() {
        subtask = new Subtask(1, 1, "Вымыть окна", "без разводов", Status.NEW);
    }

    @Test
    void getEpicId() {
        int expected = 1;
        subtask.setEpicId(1);
        int actually = subtask.getEpicId();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testToString() {
        int id = 1;
        String title = "Вымыть окна";
        String description = "без разводов";
        String status = "NEW";

        String format = "Subtask ID: %d, название: '%s', описание: '%s', статус: %s";
        String expected = String.format(format, id, title, description, status);
        String actually = subtask.toString();
        Assertions.assertEquals(expected, actually);
    }
}