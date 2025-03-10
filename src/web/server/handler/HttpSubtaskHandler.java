package web.server.handler;

import static java.util.Objects.isNull;

import com.sun.net.httpserver.HttpExchange;
import exception.ErrorResponse;
import exception.TaskNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import model.Subtask;
import service.TaskManager;

public class HttpSubtaskHandler extends BaseHttpHandler {

  private final TaskManager taskManager;

  public HttpSubtaskHandler(TaskManager taskManager) {
    this.taskManager = taskManager;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    String method = exchange.getRequestMethod();

    try (exchange) {
      switch (method) {
        case METHOD_GET:
          handleGet(exchange);
          break;
        case METHOD_POST:
          handlePost(exchange);
          break;
        case METHOD_DELETE:
          handleDelete(exchange);
          break;
        default:
          defaultErrorResponse(exchange, method);
      }
    } catch (TaskNotFoundException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HTTP_NOT_FOUND,
          exchange.getRequestURI().getPath());
      String jsonText = jsonMapper.toJson(errorResponse);
      sendText(exchange, jsonText, HTTP_NOT_FOUND);

    } catch (Exception e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HTTP_INTERNAL_SERVER_ERROR,
          exchange.getRequestURI().getPath());
      String jsonText = jsonMapper.toJson(errorResponse);
      sendText(exchange, jsonText, HTTP_INTERNAL_SERVER_ERROR);
    } finally {
      exchange.close();
    }
  }

  private void defaultErrorResponse(HttpExchange exchange, String method) throws IOException {
    ErrorResponse errorResponse = new ErrorResponse(
        String.format("Обработка метода %s не предусмотрена", method),
        HTTP_METHOD_NOT_ALLOWED,
        exchange.getRequestURI().getPath());
    String jsonText = jsonMapper.toJson(errorResponse);
    sendText(exchange, jsonText, HTTP_METHOD_NOT_ALLOWED);
  }

  private void handleGet(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath();
    String[] urlParts = path.split("/");

    if (urlParts.length == 3) {
      Integer id = Integer.parseInt(urlParts[2]);
      Subtask getSubtaskById = taskManager.findSubtaskById(id);
      String json = jsonMapper.toJson(getSubtaskById);
      sendText(exchange, json, HTTP_OK);

    }
    if (urlParts.length == 2) {
      Collection<Subtask> allSubtask = taskManager.findAllSubtasks();
      String json = jsonMapper.toJson(allSubtask);
      sendText(exchange, json, HTTP_OK);
    }
  }

  private void handlePost(HttpExchange exchange) throws IOException {
    byte[] inputStream = exchange.getRequestBody().readAllBytes();
    String body = new String(inputStream, StandardCharsets.UTF_8);
    Subtask subtask = jsonMapper.fromJson(body, Subtask.class);

    if (isNull(subtask.getId())) {
      taskManager.createSubtask(subtask);
      sendText(exchange, "Подзадача создана успешно.", HTTP_CREATED);

    } else {
      taskManager.updateSubtask(subtask);
      sendText(exchange, "Подзадача обновлена", HTTP_CREATED);
    }
  }

  private void handleDelete(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath();
    String[] urlParts = path.split("/");
    Integer id = Integer.parseInt(urlParts[2]);

    Subtask deletedSubtaskById = taskManager.deleteSubtaskById(id);
    String json = jsonMapper.toJson(deletedSubtaskById);
    sendText(exchange, "Подзадача успешно удаленна: " + json, HTTP_OK);
  }
}
