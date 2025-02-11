package service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {

  @BeforeEach
  void initManager() {
    super.manager = new InMemoryTaskManager();
  }
}