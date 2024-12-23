package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private Subtask subtask4;

    @BeforeEach
    void init() {
        historyManager = Managers.getDefaultHistoryManager();

        task1 = new Task(1,"Построить дом","Нанять строителей", Status.NEW);
        task2 = new Task(2,"Заварить чай","Налить кипяток", Status.DONE);

        epic1 = new Epic(3,"Подарить подарок","Купить подарок", Status.NEW);
        epic2 = new Epic(4,"Убрать квартиру","Помыть полы",Status.NEW);

        subtask1 = new Subtask(5, epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        subtask2 = new Subtask(6, epic1.getId(), "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS);
        subtask3 = new Subtask(7, epic2.getId(), "Сдать диплом", "Подготовить презентацию", Status.DONE);
        subtask4 = new Subtask(8, epic2.getId(), "Сдать зачет", "Повторить теорию", Status.NEW);
    }

    @Test
    void addHistory() {
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);

        historyManager.addHistory(epic1);
        historyManager.addHistory(epic2);

        historyManager.addHistory(subtask1);
        historyManager.addHistory(subtask2);
        historyManager.addHistory(subtask3);
        historyManager.addHistory(subtask4);

        String expected =
                "[Task ID: 1, название: 'Построить дом', описание: 'Нанять строителей', статус: NEW, " +
                        "Task ID: 2, название: 'Заварить чай', описание: 'Налить кипяток', статус: DONE, " +
                        "Epic ID: 3, название: 'Подарить подарок', описание: 'Купить подарок', статус: NEW, " +
                        "Epic ID: 4, название: 'Убрать квартиру', описание: 'Помыть полы', статус: NEW, " +
                        "Subtask ID: 5, название: 'Написать сочинение', описание: 'Выбрать тему', статус: NEW, " +
                        "Subtask ID: 6, название: 'Написать курсовую', описание: 'Выбрать тему', статус: IN_PROGRESS, " +
                        "Subtask ID: 7, название: 'Сдать диплом', описание: 'Подготовить презентацию', статус: DONE, " +
                        "Subtask ID: 8, название: 'Сдать зачет', описание: 'Повторить теорию', статус: NEW]";
        String actually = historyManager.getHistory().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void getHistory() {
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);

        historyManager.addHistory(epic1);
        historyManager.addHistory(epic2);

        historyManager.addHistory(subtask1);
        historyManager.addHistory(subtask2);
        historyManager.addHistory(subtask3);
        historyManager.addHistory(subtask4);

        String expected =
                "[Task ID: 1, название: 'Построить дом', описание: 'Нанять строителей', статус: NEW, " +
                        "Task ID: 2, название: 'Заварить чай', описание: 'Налить кипяток', статус: DONE, " +
                        "Epic ID: 3, название: 'Подарить подарок', описание: 'Купить подарок', статус: NEW, " +
                        "Epic ID: 4, название: 'Убрать квартиру', описание: 'Помыть полы', статус: NEW, " +
                        "Subtask ID: 5, название: 'Написать сочинение', описание: 'Выбрать тему', статус: NEW, " +
                        "Subtask ID: 6, название: 'Написать курсовую', описание: 'Выбрать тему', статус: IN_PROGRESS, " +
                        "Subtask ID: 7, название: 'Сдать диплом', описание: 'Подготовить презентацию', статус: DONE, " +
                        "Subtask ID: 8, название: 'Сдать зачет', описание: 'Повторить теорию', статус: NEW]";
        String actually = historyManager.getHistory().toString();

        Assertions.assertEquals(expected, actually);
    }
}