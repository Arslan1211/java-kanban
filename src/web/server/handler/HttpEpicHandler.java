package web.server.handler;

import static java.util.Objects.isNull;

import com.sun.net.httpserver.HttpExchange;
import exception.EpicNotFoundException;
import exception.ErrorResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import model.Epic;
import service.TaskManager;

public class HttpEpicHandler extends BaseHttpHandler {

  private final TaskManager taskManager;

  public HttpEpicHandler(TaskManager taskManager) {
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
    } catch (EpicNotFoundException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HTTP_NOT_FOUND,
          exchange.getRequestURI().getPath());
      String jsonText = jsonMapper.toJson(errorResponse);
      sendText(exchange, jsonText, HTTP_NOT_FOUND);

    } catch (Exception e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HTTP_INTERNAL_SERVER_ERROR,
          exchange.getRequestURI().getPath());
      String jsonText = jsonMapper.toJson(errorResponse);
      sendText(exchange, jsonText, HTTP_INTERNAL_SERVER_ERROR);
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
      Epic getEpicById = taskManager.findEpicById(id);
      String json = jsonMapper.toJson(getEpicById);
      sendText(exchange, json, HTTP_OK);

    }
    if (urlParts.length == 2) {
      Collection<Epic> allEpics = taskManager.findAllEpics();
      String json = jsonMapper.toJson(allEpics);
      sendText(exchange, json, HTTP_OK);
    }
  }

  private void handlePost(HttpExchange exchange) throws IOException {
    byte[] inputStream = exchange.getRequestBody().readAllBytes();
    String body = new String(inputStream, StandardCharsets.UTF_8);
    Epic epic = jsonMapper.fromJson(body, Epic.class);

    if (isNull(epic.getId())) {
      taskManager.createEpic(epic);
      sendText(exchange, "Эпик создан успешно.", HTTP_CREATED);

    } else {
      taskManager.updateEpic(epic);
      sendText(exchange, "Эпик обновлен успешно.", HTTP_CREATED);
    }
  }

  private void handleDelete(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath();
    String[] urlParts = path.split("/");

    Integer id = Integer.parseInt(urlParts[2]);
    taskManager.deleteEpicById(id);
    sendText(exchange, "Эпик успешно удален.", HTTP_OK);
  }
}
