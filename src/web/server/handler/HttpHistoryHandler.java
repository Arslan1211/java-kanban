package web.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import model.Task;
import service.TaskManager;

public class HttpHistoryHandler extends BaseHttpHandler {

  private final TaskManager taskManager;
  private final Gson jsonMapper;

  public HttpHistoryHandler(TaskManager taskManager, Gson jsonMapper) {
    this.taskManager = taskManager;
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath();
    String[] urlParts = path.split("/");

    if (method.equals("GET") && urlParts.length == 2) {
      List<Task> history = taskManager.getHistory();
      String jsonText = jsonMapper.toJson(history);
      sendText(exchange, jsonText, 200);
    }
  }
}
