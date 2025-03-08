package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
  @Override
  public void write(JsonWriter out, Duration value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      // Сериализуем Duration в количество секунд
      out.value(value.toMinutes());
    }
  }

  @Override
  public Duration read(JsonReader in) throws IOException {
    // Десериализуем количество секунд в Duration
    long minutes = in.nextLong();
    return Duration.ofMinutes(minutes);
  }
}
