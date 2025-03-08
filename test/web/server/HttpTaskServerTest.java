package web.server;

import static org.junit.jupiter.api.Assertions.*;

import adapter.DurationTypeAdapter;
import adapter.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import service.Managers;

public class HttpTaskServerTest {

  private final TaskManager manager = Managers.getDefaultManager();
  private final HttpTaskServer server = new HttpTaskServer(manager);
  private final Gson jsonMapper = new GsonBuilder()
      .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
      .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
      .create();
  private final HttpClient client = HttpClient.newHttpClient();

  HttpTaskServerTest() throws IOException {
  }

  @BeforeEach
  public void setUp() {
    server.start();
  }

  @AfterEach
  public void shutDown() {
    server.stop();
  }

  @Test
  void testGetTasks() throws IOException, InterruptedException {
    Task task = manager.createTask(new Task(
        "Task1",
        "Task description",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(5)
    ));

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/tasks"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    int expectedStatusCode = 200;
    int actuallyStatusCode = response.statusCode();
    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");

    String expectedBody = String.format("[%s]", jsonMapper.toJson(task));
    String actuallyBody = response.body();
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }

  @Test
  void testGetTaskById() throws IOException, InterruptedException {
    Task task = manager.createTask(new Task(
        "Task1",
        "Task description",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(5)
    ));

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/tasks/1"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    int expectedStatusCode = 200;
    int actuallyStatusCode = response.statusCode();
    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");

    String expectedBody = jsonMapper.toJson(task);
    String actuallyBody = response.body();
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }

  @Test
  void testDeleteTaskById() throws IOException, InterruptedException {
    Task task = manager.createTask(new Task(
        "Task1",
        "Task description",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(5)
    ));

    String json = jsonMapper.toJson(task);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/tasks"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    client.send(request, HttpResponse.BodyHandlers.ofString());

    request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/tasks/1"))
        .DELETE()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    int expectedTasksSize = 0;
    int actuallyStatusCode = response.statusCode();
    int actuallyTasksSize = manager.findAllTasks().size();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedTasksSize, actuallyTasksSize, "Менеджере задач не пуст");
  }

  @Test
  void testPostEpic() throws IOException, InterruptedException {

    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "SubTask1",
        "SubTask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(10)
    );

    manager.createSubtask(subtask);

    String json = jsonMapper.toJson(epic);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/epics"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 201;
    int expectedEpicsSize = manager.findAllEpics().size();
    String expectedEpicName = "Epic1";
    int actuallyStatusCode = response.statusCode();
    int actuallyEpicsSize = manager.findAllEpics().size();
    String actuallyEpicsName = manager.findAllEpics().stream().findFirst().map(Epic::getTitle).get();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
    assertEquals(expectedEpicsSize, actuallyEpicsSize, "Некорректное количество эпиков");
    assertEquals(expectedEpicName, actuallyEpicsName, "Некорректное имя задачи");
  }

  @Test
  void testGetEpics() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/epics"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    String expectedBody = String.format("[%s]", jsonMapper.toJson(epic));
    int actuallyStatusCode = response.statusCode();
    String actuallyBody = response.body();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }

  @Test
  void testGetEpicById() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    //String epicId = String.format("http://localhost:8080/epics/%d", epic.getId());
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/epics/1"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    String expectedBody = jsonMapper.toJson(epic);
    int actuallyStatusCode = response.statusCode();
    String actuallyBody = response.body();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }

  @Test
  void testGetSubtasks() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "SubTask1",
        "SubTask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(10)
    );

    manager.createSubtask(subtask);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/subtasks"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    String expectedBody = String.format("[%s]", jsonMapper.toJson(subtask));
    int actuallyStatusCode = response.statusCode();
    String actuallyBody = response.body();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }

  @Test
  void testGetSubtasksById() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "SubTask1",
        "SubTask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(10)
    );

    manager.createSubtask(subtask);

    //String subtaskId = String.format("http://localhost:8080/subtasks/%d", subtask.getId());
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/subtasks/2"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    String expectedBody = jsonMapper.toJson(subtask);
    int actuallyStatusCode = response.statusCode();
    String actuallyBody = response.body();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }
}