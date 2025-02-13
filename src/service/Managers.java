package service;

public class Managers {

  private Managers() {
  }

  public static TaskManager getDefaultManager() {
    HistoryManager historyManager = getDefaultHistoryManager();
    return new InMemoryTaskManager(historyManager);
  }

  public static HistoryManager getDefaultHistoryManager() {
    return new InMemoryHistoryManager();
  }
}