package adapter;

import java.time.format.DateTimeFormatter;

public class DataTimeFormat {

  public static DateTimeFormatter getDTF() {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm");
  }
}
