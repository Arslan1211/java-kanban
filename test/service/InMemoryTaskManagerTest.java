package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDeafultManager();

        task1 = new Task("Построить дом","Нанять строителей", Status.NEW);
        task2 = new Task("Заварить чай","Налить кипяток", Status.DONE);

        epic1 = new Epic("Подарить подарок","Купить подарок");
        epic2 = new Epic("Убрать квартиру","Помыть полы");
    }

    @Test
    void getHistory() {
        final int quantityViewTaskHistory = 8;
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(), "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(epic2.getId(), "Сдать диплом", "Подготовить презентацию", Status.DONE);
        Subtask subtask4 = new Subtask(epic2.getId(), "Сдать зачет", "Повторить теорию", Status.NEW);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        taskManager.findTaskById(1);
        taskManager.findTaskById(2);
        taskManager.findEpicById(3);
        taskManager.findEpicById(4);
        taskManager.findSubtaskById(5);
        taskManager.findSubtaskById(6);
        taskManager.findSubtaskById(7);
        taskManager.findSubtaskById(8);

        Assertions.assertEquals(quantityViewTaskHistory, taskManager.getHistory().size());
        String expected =
                "[Task ID: 1, название: 'Построить дом', описание: 'Нанять строителей', статус: NEW, " +
                 "Task ID: 2, название: 'Заварить чай', описание: 'Налить кипяток', статус: DONE, " +
                 "Epic ID: 3, название: 'Подарить подарок', описание: 'Купить подарок', статус: IN_PROGRESS, " +
                 "Epic ID: 4, название: 'Убрать квартиру', описание: 'Помыть полы', статус: IN_PROGRESS, " +
                 "Subtask ID: 5, название: 'Написать сочинение', описание: 'Выбрать тему', статус: NEW, " +
                 "Subtask ID: 6, название: 'Написать курсовую', описание: 'Выбрать тему', статус: IN_PROGRESS, " +
                 "Subtask ID: 7, название: 'Сдать диплом', описание: 'Подготовить презентацию', статус: DONE, " +
                 "Subtask ID: 8, название: 'Сдать зачет', описание: 'Повторить теорию', статус: NEW]";
        String actually = taskManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);

    }

    @Test
    void createTask() {
        int id = 1;
        String title = "Построить дом";
        String description = "Нанять строителей";
        String status = "NEW";

        taskManager.createTask(task1);

        String format = "[Task ID: %d, название: '%s', описание: '%s', статус: %s]";
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findAllTasks().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void createEpic() {
        int id = 1;
        String title = "Подарить подарок";
        String description = "Купить подарок";
        String status = "NEW";

        taskManager.createEpic(epic1);

        String format = "[Epic ID: %d, название: '%s', описание: '%s', статус: %s]";
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findAllEpics().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void createSubtask() {
        String title = "Написать сочинение";
        String description = "Выбрать тему";
        String status = "NEW";

        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        taskManager.createSubtask(subtask1);

        String format = "[Subtask ID: %d, название: '%s', описание: '%s', статус: %s]";
        String expected = String.format(format, subtask1.getId(), title, description, status);

        String actually = taskManager.findAllSubtasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void findAllTasks() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        String expected =
                "[Task ID: 1, название: 'Построить дом', описание: 'Нанять строителей', статус: NEW, " +
                "Task ID: 2, название: 'Заварить чай', описание: 'Налить кипяток', статус: DONE]";
        String actually = taskManager.findAllTasks().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void findAllEpics() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        String expected =
                "[Epic ID: 1, название: 'Подарить подарок', описание: 'Купить подарок', статус: NEW, " +
                "Epic ID: 2, название: 'Убрать квартиру', описание: 'Помыть полы', статус: NEW]";
        String actually = taskManager.findAllEpics().toString();

        Assertions.assertEquals(expected, actually);


    }

    @Test
    void findAllSubtasks() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(), "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(epic1.getId(), "Сдать диплом", "Подготовить презентацию", Status.DONE);
        Subtask subtask4 = new Subtask(epic1.getId(), "Сдать зачет", "Повторить теорию", Status.NEW);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        String expected =
                "[Subtask ID: 3, название: 'Написать сочинение', описание: 'Выбрать тему', статус: NEW, " +
                 "Subtask ID: 4, название: 'Написать курсовую', описание: 'Выбрать тему', статус: IN_PROGRESS, " +
                 "Subtask ID: 5, название: 'Сдать диплом', описание: 'Подготовить презентацию', статус: DONE, " +
                 "Subtask ID: 6, название: 'Сдать зачет', описание: 'Повторить теорию', статус: NEW]";
        String actually = taskManager.findAllSubtasks().toString();

        Assertions.assertEquals(expected, actually);

    }

    @Test
    void findTaskById() {
        int id = 1;
        String title = "Построить дом";
        String description = "Нанять строителей";
        String status = "NEW";

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        String format = "Task ID: %d, название: '%s', описание: '%s', статус: %s";
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findTaskById(1).toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void findEpicById() {
        int id = 1;
        String title = "Подарить подарок";
        String description = "Купить подарок";
        String status = "NEW";

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        String format = "Epic ID: %d, название: '%s', описание: '%s', статус: %s";
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findEpicById(1).toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void findSubtaskById() {
        String title = "Написать сочинение";
        String description = "Выбрать тему";
        String status = "NEW";

        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        taskManager.createSubtask(subtask1);
        String format = "Subtask ID: %d, название: '%s', описание: '%s', статус: %s";

        String expected = String.format(format, subtask1.getId(), title, description, status);
        String actually = taskManager.findSubtaskById(2).toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void updateTask() {
        int id = 1;
        String title = "Построить дом";
        String description = "Нанять строителей";
        String status = "DONE";

        taskManager.createTask(task1);

        String format = "Task ID: %d, название: '%s', описание: '%s', статус: %s";
        Task task = new Task(id, title, description, Status.DONE);
        taskManager.updateTask(task);
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findTaskById(id).toString();

        Assertions.assertEquals(expected, actually);

    }

    @Test
    void updateEpic() {
        int id = 1;
        String title = "Подарить еще один подарок";
        String description = "Купил второй подарок";
        String status = "NEW";

        taskManager.createEpic(epic1);
        Epic epic = taskManager.findEpicById(id);
        epic.setTitle(title);
        epic.setDescription(description);

        String format = "[Epic ID: %d, название: '%s', описание: '%s', статус: %s]";
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findAllEpics().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void updateSubtask() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(), "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(epic1.getId(), "Сдать диплом", "Подготовить презентацию", Status.DONE);
        Subtask subtask4 = new Subtask(epic1.getId(), "Сдать зачет", "Повторить теорию", Status.NEW);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        subtask1 = taskManager.findSubtaskById(subtask1.getId());
        subtask2 = taskManager.findSubtaskById(subtask2.getId());
        subtask3 = taskManager.findSubtaskById(subtask3.getId());
        subtask4 = taskManager.findSubtaskById(subtask4.getId());

        subtask1.setTitle("Обновляем описание");
        subtask2.setTitle("Обновляем описание");
        subtask3.setTitle("Обновляем описание");
        subtask4.setTitle("Обновляем описание");

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);
        taskManager.updateSubtask(subtask4);

        String expected =
                "[Subtask ID: 3, название: 'Обновляем описание', описание: 'Выбрать тему', статус: NEW, " +
                 "Subtask ID: 4, название: 'Обновляем описание', описание: 'Выбрать тему', статус: IN_PROGRESS, " +
                 "Subtask ID: 5, название: 'Обновляем описание', описание: 'Подготовить презентацию', статус: DONE, " +
                 "Subtask ID: 6, название: 'Обновляем описание', описание: 'Повторить теорию', статус: NEW]";
        String actually = taskManager.findAllSubtasks().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void deleteTaskById() {
        int id = 1;
        String title = "Построить дом";
        String description = "Нанять строителей";
        String status = "DONE";

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteTaskById(task2.getId());


        String format = "[Task ID: %d, название: '%s', описание: '%s', статус: %s]";
        Task task = new Task(id, title, description, Status.DONE);
        taskManager.updateTask(task);
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findAllTasks().toString();

        Assertions.assertEquals(expected, actually);

    }

    @Test
    void deleteEpicById() {
        int id = 1;
        String title = "Подарить подарок";
        String description = "Купить подарок";
        String status = "NEW";

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.deleteEpicById(epic2.getId());

        String format = "[Epic ID: %d, название: '%s', описание: '%s', статус: %s]";
        String expected = String.format(format, id, title, description, status);
        String actually = taskManager.findAllEpics().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void deleteSubtaskById() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(), "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(epic1.getId(), "Сдать диплом", "Подготовить презентацию", Status.DONE);
        Subtask subtask4 = new Subtask(epic1.getId(), "Сдать зачет", "Повторить теорию", Status.NEW);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        taskManager.deleteSubtaskById(subtask1.getId());

        String expected =
                "[Subtask ID: 4, название: 'Написать курсовую', описание: 'Выбрать тему', статус: IN_PROGRESS, " +
                 "Subtask ID: 5, название: 'Сдать диплом', описание: 'Подготовить презентацию', статус: DONE, " +
                 "Subtask ID: 6, название: 'Сдать зачет', описание: 'Повторить теорию', статус: NEW]";
        String actually = taskManager.findAllSubtasks().toString();

        Assertions.assertEquals(expected, actually);
    }

    @Test
    void deleteAllTasks() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteAllTasks();

        Assertions.assertTrue(taskManager.findAllTasks().isEmpty());
    }

    @Test
    void deleteAllEpics() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.deleteAllEpics();

        Assertions.assertTrue(taskManager.findAllEpics().isEmpty());
    }

    @Test
    void deleteAllSubtasks() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getId(), "Написать сочинение", "Выбрать тему", Status.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(), "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(epic1.getId(), "Сдать диплом", "Подготовить презентацию", Status.DONE);
        Subtask subtask4 = new Subtask(epic1.getId(), "Сдать зачет", "Повторить теорию", Status.NEW);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        taskManager.deleteAllSubtasks();

        Assertions.assertTrue(taskManager.findAllSubtasks().isEmpty());


    }
}