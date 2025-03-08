package exception;

public class DateDeserializationError extends RuntimeException {

  public DateDeserializationError(String message) {
    super(message);
  }
}
