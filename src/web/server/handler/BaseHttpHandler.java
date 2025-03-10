package web.server.handler;

import adapter.DurationTypeAdapter;
import adapter.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

public abstract class BaseHttpHandler implements HttpHandler {

  protected static final String METHOD_GET = "GET";
  protected static final String METHOD_POST = "POST";
  protected static final String METHOD_DELETE = "DELETE";
  protected static final int HTTP_OK = 200;
  protected static final int HTTP_CREATED = 201;
  protected static final int HTTP_NOT_FOUND = 404;
  protected static final int HTTP_METHOD_NOT_ALLOWED = 405;
  protected static final int HTTP_INTERNAL_SERVER_ERROR = 500;
  protected static final Gson jsonMapper = new GsonBuilder()
      .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
      .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
      .create();

  protected void sendText(HttpExchange h, String text, Integer code) throws IOException {
    byte[] resp = text.getBytes(StandardCharsets.UTF_8);
    h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
    h.sendResponseHeaders(code, resp.length);
    h.getResponseBody().write(resp);
    h.close();
  }
}
