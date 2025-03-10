package web.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;
import web.server.handler.HttpEpicHandler;
import web.server.handler.HttpHistoryHandler;
import web.server.handler.HttpPrioritizedHandler;
import web.server.handler.HttpSubtaskHandler;
import web.server.handler.HttpTaskHandler;

public class HttpTaskServer {

  private final HttpServer httpServer;

  public HttpTaskServer(TaskManager taskManager) throws IOException {

    httpServer = HttpServer.create();
    httpServer.bind(new InetSocketAddress("localhost", 8080), 0);

    httpServer.createContext("/tasks",
        new HttpTaskHandler(taskManager));
    httpServer.createContext("/epics",
        new HttpEpicHandler(taskManager));
    httpServer.createContext("/subtasks",
        new HttpSubtaskHandler(taskManager));
    httpServer.createContext("/history",
        new HttpHistoryHandler(taskManager));
    httpServer.createContext("/prioritized",
        new HttpPrioritizedHandler(taskManager));
  }

  public void start() {
    httpServer.start();
  }

  public void stop() {
    httpServer.stop(1);
  }

  public static void main(String[] args) throws IOException {
    TaskManager taskManager = Managers.getDefaultManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    httpTaskServer.start();

    taskManager.createTask(new Task(
        "название",
        "описание",
        Status.NEW,
        Instant.now().plusSeconds(100_000),
        Duration.ofMinutes(15)));
    taskManager.createEpic(new Epic(
        "название",
        "описание",
        Instant.now().plusSeconds(2_000),
        Duration.ofMinutes(25)));
    taskManager.createSubtask(new Subtask(
        "название",
        "описание",
        Status.NEW,
        2,
        Instant.now().plusSeconds(5_000),
        Duration.ofMinutes(45)));
  }
}
