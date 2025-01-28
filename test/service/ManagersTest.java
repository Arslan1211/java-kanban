package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

  @Test
  void getDeafultManager() {
    TaskManager actually = Managers.getDeafultManager();
    Assertions.assertInstanceOf(InMemoryTaskManager.class, actually);
  }

  @Test
  void getDefaultHistoryManager() {
    HistoryManager actually = Managers.getDefaultHistoryManager();
    Assertions.assertInstanceOf(InMemoryHistoryManager.class, actually);
  }
}