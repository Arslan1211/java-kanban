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

class HttpTaskServerTest {

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
    String actuallyEpicsName = manager.findAllEpics().stream().findFirst().map(Epic::getTitle)
        .get();

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

    String epicId = String.format("http://localhost:8080/epics/%d", epic.getId());
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(epicId))
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
  void testDeleteEpicById() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    String epicId = String.format("http://localhost:8080/epics/%d", epic.getId());
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(epicId))
        .DELETE()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    int expectedEpicsSize = 0;
    int actuallyStatusCode = response.statusCode();
    int actuallyEpicsSize = manager.findAllEpics().size();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedEpicsSize, actuallyEpicsSize, "Менеджере задач не пуст");
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

    String subtaskId = String.format("http://localhost:8080/subtasks/%d", subtask.getId());
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(subtaskId))
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

  @Test
  void testPostSubtask() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "Subtask1",
        "Subtask1 description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createSubtask(subtask);

    String json = jsonMapper.toJson(subtask);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/subtasks"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 201;
    int expectedEpicsSize = 1;
    String expectedSubtaskName = "Subtask1";
    int actuallyStatusCode = response.statusCode();
    int actuallySubtasksSize = manager.findAllSubtasks().size();
    String actuallySubtaskName = manager.findAllSubtasks().stream().findFirst()
        .map(Subtask::getTitle)
        .get();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
    assertEquals(expectedEpicsSize, actuallySubtasksSize,
        "Некорректное количество подзадач");
    assertEquals(expectedSubtaskName, actuallySubtaskName, "Некорректное имя задачи");
  }

  @Test
  void testUpdateSubtask() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "Subtask1",
        "Subtask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createSubtask(subtask);

    Subtask subtaskUpdated = new Subtask(
        subtask.getId(),
        "Subtask1 updated",
        "Subtask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(5)
    );

    String json = jsonMapper.toJson(subtaskUpdated);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/subtasks"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 201;
    int expectedSubTasksSize = 1;
    String expectedSubtasksName = "Subtask1 updated";
    int actuallyStatusCode = response.statusCode();
    int actuallySubTasksSize = manager.findAllSubtasks().size();
    String actuallySubtaskName = manager.findAllSubtasks().stream().findFirst()
        .map(Subtask::getTitle).get();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
    assertEquals(expectedSubTasksSize, actuallySubTasksSize, "Некорректное количество подзадач");
    assertEquals(expectedSubtasksName, actuallySubtaskName, "Некорректное имя подзадачи");
  }

  @Test
  void testDeleteSubtaskById() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "Subtask1",
        "Subtask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createSubtask(subtask);

    String subtaskId = String.format("http://localhost:8080/subtasks/%d", subtask.getId());
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(subtaskId))
        .DELETE()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    int expectedSubTasksSize = 0;
    int actuallyStatusCode = response.statusCode();
    int actuallySubTasksSize = manager.findAllSubtasks().size();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedSubTasksSize, actuallySubTasksSize, "Менеджере задач не пуст");
  }

  @Test
  void testGetHistory() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "Subtask1",
        "Subtask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createSubtask(subtask);

    Task task = new Task(
        "Task1",
        "Task description",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createTask(task);

    manager.findSubtaskById(2);
    manager.findTaskById(3);
    manager.findEpicById(1);
    manager.findSubtaskById(2);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/history"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    int expectedStatusCode = 200;
    String expectedBodyTask = jsonMapper.toJson(task);
    String expectedBodySubtask = jsonMapper.toJson(subtask);
    String expectedBodyEpic = jsonMapper.toJson(epic);
    String expectedBody = String.format("[%s,%s,%s]",
        expectedBodyTask, expectedBodyEpic, expectedBodySubtask);

    int actuallyStatusCode = response.statusCode();
    String actuallyBody = response.body();

    assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
    assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
  }

  @Test
  void testGetPrioritizedTasks() throws IOException, InterruptedException {
    Epic epic = new Epic(
        "Epic1",
        "Epic1 description",
        Instant.now(),
        Duration.ofMinutes(0)
    );

    manager.createEpic(epic);

    Subtask subtask = new Subtask(
        "Subtask1",
        "Subtask description",
        Status.NEW,
        epic.getId(),
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createSubtask(subtask);

    Task task = new Task(
        "Task1",
        "Task description",
        Status.NEW,
        Instant.now(),
        Duration.ofMinutes(5)
    );

    manager.createTask(task);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/prioritized"))
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
}