package web.server.handler;

import static java.util.Objects.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.ErrorResponse;
import exception.TaskNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import model.Task;
import service.TaskManager;

public class HttpTaskHandler extends BaseHttpHandler {

  private final TaskManager taskManager;
  private final Gson jsonMapper;

  public HttpTaskHandler(TaskManager taskManager, Gson jsonMapper) {
    this.taskManager = taskManager;
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    String method = exchange.getRequestMethod();

    try {
      switch (method) {
        case "GET":
          handleGet(exchange);
          break;
        case "POST":
          handlePost(exchange);
          break;
        case "DELETE":
          handleDelete(exchange);
          break;
        default:
          defaultErrorResponse(exchange, method);
      }
    } catch (TaskNotFoundException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404,
          exchange.getRequestURI().getPath());
      String jsonText = jsonMapper.toJson(errorResponse);
      sendText(exchange, jsonText, 404);

    } catch (Exception e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500,
          exchange.getRequestURI().getPath());
      String jsonText = jsonMapper.toJson(errorResponse);
      sendText(exchange, jsonText, 500);
    } finally {
      exchange.close();
    }
  }

  private void defaultErrorResponse(HttpExchange exchange, String method) throws IOException {
    ErrorResponse errorResponse = new ErrorResponse(
        String.format("Обработка метода %s не предусмотрена", method),
        405,
        exchange.getRequestURI().getPath());
    String jsonText = jsonMapper.toJson(errorResponse);
    sendText(exchange, jsonText, 405);
  }

  private void handleGet(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath();
    String[] urlParts = path.split("/");

    if (urlParts.length == 3) {
      Integer id = Integer.parseInt(urlParts[2]);
      Task getTaskById = taskManager.findTaskById(id);
      String json = jsonMapper.toJson(getTaskById);
      sendText(exchange, json, 200);
    }

    if (urlParts.length == 2) {
      Collection<Task> allTasks = taskManager.findAllTasks();
      String json = jsonMapper.toJson(allTasks);
      sendText(exchange, json, 200);
    }
  }

  private void handlePost(HttpExchange exchange) throws IOException {
    byte[] inputStream = exchange.getRequestBody().readAllBytes();
    String body = new String(inputStream, StandardCharsets.UTF_8);
    Task task = jsonMapper.fromJson(body, Task.class);

    if (isNull(task.getId())) {
      taskManager.createTask(task);
      sendText(exchange, "Задача создана успешно.", 201);

    } else {
      taskManager.updateTask(task);
      sendText(exchange, "Задача обновлена", 201);
    }
  }

  private void handleDelete(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath();
    String[] urlParts = path.split("/");

    if (urlParts.length == 3) {
      Integer id = Integer.parseInt(urlParts[2]);
      taskManager.deleteTaskById(id);
      sendText(exchange, "Задача успешно удаленна.", 200);
    }
  }
}
