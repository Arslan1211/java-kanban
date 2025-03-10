package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import exception.DateDeserializationError;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class InstantTypeAdapter extends TypeAdapter<Instant> {

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm");

  @Override
  public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
    if (instant != null) {
      String formattedDate = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).format(formatter);
      jsonWriter.value(formattedDate);
    } else {
      jsonWriter.nullValue();
    }
  }

  @Override
  public Instant read(JsonReader jsonReader) throws IOException {
    String dateTimeString = jsonReader.nextString();
    try {
      LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
      return dateTime.toInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException e) {
      throw new DateDeserializationError("Ошибка при разборе даты: " + e.getMessage());
    }
  }
}