package service;

public class Managers {

  private Managers() {
  }

  public static TaskManager getDeafultManager() {
    return new InMemoryTaskManager();
  }

  public static HistoryManager getDefaultHistoryManager() {
    return new InMemoryHistoryManager();
  }
}