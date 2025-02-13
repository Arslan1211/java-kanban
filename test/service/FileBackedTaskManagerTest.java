package service;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileBackedTaskManagerTest extends AbstractTaskManagerTest<FileBackedTaskManager> {

  @BeforeEach
  void initManager() {
    Path temp;
    try {
      temp = Files.createTempFile("tmp", ".csv");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    manager = FileBackedTaskManager.loadFromFile(temp.toFile());
  }
}