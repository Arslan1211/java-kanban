package service;

import java.time.Duration;
import java.time.Instant;
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
    Instant startTime = Instant.parse("2025-02-23T14:05:00Z");
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

    String expected = "[Task{id=1, name=Построить дом, description=Нанять строителей, status=NEW, startTime=23.02.2025/17:05, duration=15, endTime=23.02.2025/17:35}, Task{id=2, name=Заварить чай, description=Налить кипяток, status=DONE, startTime=23.02.2025/17:05, duration=30, endTime=23.02.2025/18:05}, Epic{id=3, name=Подарить подарок, description=Купить подарок, status=NEW, startTime=23.02.2025/17:05, duration=45, endTime=23.02.2025/17:50}, Epic{id=4, name=Убрать квартиру, description=Помыть полы, status=NEW, startTime=23.02.2025/17:05, duration=60, endTime=23.02.2025/18:05}, Subtask{id=5, name=Написать сочинение, description=Выбрать тему, status=NEW, epicId=3, startTime=23.02.2025/17:05, duration=75, endTime=23.02.2025/18:20}, Subtask{id=6, name=Написать курсовую, description=Выбрать тему, status=IN_PROGRESS, epicId=3, startTime=23.02.2025/17:05, duration=90, endTime=23.02.2025/18:35}, Subtask{id=7, name=Сдать диплом, description=Подготовить презентацию, status=DONE, epicId=4, startTime=23.02.2025/17:05, duration=105, endTime=23.02.2025/18:50}, Subtask{id=8, name=Сдать зачет, description=Повторить теорию, status=NEW, epicId=4, startTime=23.02.2025/17:05, duration=120, endTime=23.02.2025/19:05}]";
    String actually = historyManager.getHistory().toString();

    Assertions.assertEquals(expected, actually);
  }

  @Test
  void getHistory() {
    historyManager.addHistory(task1);
    historyManager.addHistory(epic1);
    historyManager.addHistory(task2);
    historyManager.addHistory(epic2);
    historyManager.addHistory(subtask2);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(subtask3);
    historyManager.addHistory(subtask4);

    String expected = "[Task{id=1, name=Построить дом, description=Нанять строителей, status=NEW, startTime=23.02.2025/17:05, duration=15, endTime=23.02.2025/17:35}, Epic{id=3, name=Подарить подарок, description=Купить подарок, status=NEW, startTime=23.02.2025/17:05, duration=45, endTime=23.02.2025/17:50}, Task{id=2, name=Заварить чай, description=Налить кипяток, status=DONE, startTime=23.02.2025/17:05, duration=30, endTime=23.02.2025/18:05}, Epic{id=4, name=Убрать квартиру, description=Помыть полы, status=NEW, startTime=23.02.2025/17:05, duration=60, endTime=23.02.2025/18:05}, Subtask{id=6, name=Написать курсовую, description=Выбрать тему, status=IN_PROGRESS, epicId=3, startTime=23.02.2025/17:05, duration=90, endTime=23.02.2025/18:35}, Subtask{id=5, name=Написать сочинение, description=Выбрать тему, status=NEW, epicId=3, startTime=23.02.2025/17:05, duration=75, endTime=23.02.2025/18:20}, Subtask{id=7, name=Сдать диплом, description=Подготовить презентацию, status=DONE, epicId=4, startTime=23.02.2025/17:05, duration=105, endTime=23.02.2025/18:50}, Subtask{id=8, name=Сдать зачет, description=Повторить теорию, status=NEW, epicId=4, startTime=23.02.2025/17:05, duration=120, endTime=23.02.2025/19:05}]";
    String actually = historyManager.getHistory().toString();

    Assertions.assertEquals(expected, actually);
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

    String expected = "[Task{id=2, name=Заварить чай, description=Налить кипяток, status=DONE, startTime=23.02.2025/17:05, duration=30, endTime=23.02.2025/18:05}, Epic{id=4, name=Убрать квартиру, description=Помыть полы, status=NEW, startTime=23.02.2025/17:05, duration=60, endTime=23.02.2025/18:05}, Subtask{id=5, name=Написать сочинение, description=Выбрать тему, status=NEW, epicId=3, startTime=23.02.2025/17:05, duration=75, endTime=23.02.2025/18:20}, Subtask{id=6, name=Написать курсовую, description=Выбрать тему, status=IN_PROGRESS, epicId=3, startTime=23.02.2025/17:05, duration=90, endTime=23.02.2025/18:35}, Subtask{id=7, name=Сдать диплом, description=Подготовить презентацию, status=DONE, epicId=4, startTime=23.02.2025/17:05, duration=105, endTime=23.02.2025/18:50}, Subtask{id=8, name=Сдать зачет, description=Повторить теорию, status=NEW, epicId=4, startTime=23.02.2025/17:05, duration=120, endTime=23.02.2025/19:05}]";
    String actually = historyManager.getHistory().toString();
    Assertions.assertEquals(expected, actually);
  }
}