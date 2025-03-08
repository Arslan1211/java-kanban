package adapter;

/*
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InstantTypeAdapter extends TypeAdapter<Instant> {

  protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss/dd.MM.yyyy");

  @Override
  public void write(JsonWriter out, Instant value) throws IOException {
    if (value != null) {
      String formattedDate = OffsetDateTime.ofInstant(value, ZoneOffset.UTC).format(formatter);
      out.value(formattedDate);
    } else {
      out.nullValue();
    }
  }

  @Override
  public Instant read(JsonReader in) throws IOException {
    String dateTimeString = in.nextString();
    try {
      OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString, formatter);
      return offsetDateTime.toInstant();
    } catch (DateTimeParseException e) {
      throw  new IOException("Ошибка при парсинге даты: " + dateTimeString, e);
    }
  }
}*/

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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

      // Проверка, что дата не в прошлом
      LocalDateTime now = LocalDateTime.now();
    /*  if (dateTime.isBefore(now)) {
        System.out.println("Дата находится в прошлом: " + dateTime);
      } else {
        System.out.println("Строка является корректной датой и не в прошлом: " + dateTime);
      }*/
      Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
      return instant;
    } catch (
        DateTimeParseException e) {
      //System.out.println("Строка НЕ является корректной датой: " + e.getMessage());
    }
    return null;
  }
}