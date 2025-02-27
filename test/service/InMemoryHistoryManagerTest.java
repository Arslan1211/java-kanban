package service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
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
    Instant startTime = Instant.parse("2025-02-23T17:05:00Z");
    task1 = new Task(
        1,
        "Построить дом",
        "Нанять строителей",
        Status.NEW,
        startTime,
        Duration.ofMinutes(15)
    );
    task2 = new Task(
        2,
        "Заварить чай",
        "Налить кипяток",
        Status.DONE,
        startTime,
        Duration.ofMinutes(30)
    );

    epic1 = new Epic(
        3,
        "Подарить подарок",
        "Купить подарок",
        Status.NEW,
        startTime,
        Duration.ofMinutes(45)
    );
    epic2 = new Epic(
        4,
        "Убрать квартиру",
        "Помыть полы",
        Status.NEW,
        startTime,
        Duration.ofMinutes(60)
    );

    subtask1 = new Subtask(
        5,
        "Написать сочинение",
        "Выбрать тему",
        Status.NEW,
        epic1.getId(),
        startTime,
        Duration.ofMinutes(75)
    );
    subtask2 = new Subtask(
        6,
        "Написать курсовую",
        "Выбрать тему",
        Status.IN_PROGRESS,
        epic1.getId(),
        startTime,
        Duration.ofMinutes(90)
    );
    subtask3 = new Subtask(
        7,
        "Сдать диплом",
        "Подготовить презентацию",
        Status.DONE,
        epic2.getId(),
        startTime,
        Duration.ofMinutes(105)
    );
    subtask4 = new Subtask(
        8,
        "Сдать зачет",
        "Повторить теорию",
        Status.NEW,
        epic2.getId(),
        startTime,
        Duration.ofMinutes(120)
    );
  }

  private void checkTaskFields(Task task, String title, String description, Status status,
      Instant startTime,
      Duration duration) {
    assertEquals(title, task.getTitle(), "Название задачи не совпадает");
    assertEquals(description, task.getDescription(), "Описание задачи не совпадает");
    assertEquals(status, task.getStatus(), "Статус задачи не совпадает");
    assertEquals(startTime, task.getStartTime(), "Время начала задачи не совпадает");
    assertEquals(duration, task.getDuration(), "Длительность задачи не совпадает");
  }

  @Test
  void addHistory() {
    checkTaskFields(task1, "Построить дом", "Нанять строителей", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(15));
    checkTaskFields(task2, "Заварить чай", "Налить кипяток", Status.DONE,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(30));
    checkTaskFields(epic1, "Подарить подарок", "Купить подарок", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(45));
    checkTaskFields(epic2, "Убрать квартиру", "Помыть полы", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(60));
    checkTaskFields(subtask1, "Написать сочинение", "Выбрать тему", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(75));
    checkTaskFields(subtask2, "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(90));
    checkTaskFields(subtask3, "Сдать диплом", "Подготовить презентацию", Status.DONE,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(105));
    checkTaskFields(subtask4, "Сдать зачет", "Повторить теорию", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(120));

    historyManager.addHistory(task1);
    historyManager.addHistory(task2);
    historyManager.addHistory(epic1);
    historyManager.addHistory(epic2);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(subtask2);
    historyManager.addHistory(subtask3);
    historyManager.addHistory(subtask4);
    List<Task> history = historyManager.getHistory();

    assertEquals(8, history.size(), "История должна содержать 8 задач");
    assertEquals(task1, history.get(0), "Первая задача в истории не совпадает");
    assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает");
    assertEquals(epic1, history.get(2), "Третья задача в истории не совпадает");
    assertEquals(epic2, history.get(3), "Четвертая задача в истории не совпадает");
    assertEquals(subtask1, history.get(4), "Пятая задача в истории не совпадает");
    assertEquals(subtask2, history.get(5), "Шестая задача в истории не совпадает");
    assertEquals(subtask3, history.get(6), "Седьмая задача в истории не совпадает");
    assertEquals(subtask4, history.get(7), "Восьмая задача в истории не совпадает");
  }

  @Test
  void getHistory() {
    checkTaskFields(task1, "Построить дом", "Нанять строителей", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(15));
    checkTaskFields(task2, "Заварить чай", "Налить кипяток", Status.DONE,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(30));
    checkTaskFields(epic1, "Подарить подарок", "Купить подарок", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(45));
    checkTaskFields(epic2, "Убрать квартиру", "Помыть полы", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(60));
    checkTaskFields(subtask1, "Написать сочинение", "Выбрать тему", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(75));
    checkTaskFields(subtask2, "Написать курсовую", "Выбрать тему", Status.IN_PROGRESS,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(90));
    checkTaskFields(subtask3, "Сдать диплом", "Подготовить презентацию", Status.DONE,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(105));
    checkTaskFields(subtask4, "Сдать зачет", "Повторить теорию", Status.NEW,
        Instant.parse("2025-02-23T17:05:00Z"),
        Duration.ofMinutes(120));

    historyManager.addHistory(task1);
    historyManager.addHistory(task2);
    historyManager.addHistory(epic1);
    historyManager.addHistory(epic2);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(subtask2);
    historyManager.addHistory(subtask3);
    historyManager.addHistory(subtask4);
    List<Task> history = historyManager.getHistory();

    assertEquals(8, history.size(), "История должна содержать 8 задач");
    assertEquals(task1, history.get(0), "Первая задача в истории не совпадает");
    assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает");
    assertEquals(epic1, history.get(2), "Третья задача в истории не совпадает");
    assertEquals(epic2, history.get(3), "Четвертая задача в истории не совпадает");
    assertEquals(subtask1, history.get(4), "Пятая задача в истории не совпадает");
    assertEquals(subtask2, history.get(5), "Шестая задача в истории не совпадает");
    assertEquals(subtask3, history.get(6), "Седьмая задача в истории не совпадает");
    assertEquals(subtask4, history.get(7), "Восьмая задача в истории не совпадает");
  }

  @Test
  void removeByID() {

    historyManager.addHistory(task1);
    historyManager.addHistory(task2);
    historyManager.addHistory(epic1);
    historyManager.addHistory(epic2);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(subtask2);
    historyManager.addHistory(subtask3);
    historyManager.addHistory(subtask4);
    historyManager.remove(3);
    historyManager.remove(1);

    assertEquals(historyManager.getHistory().size(), historyManager.getHistory().stream().count());
  }
}