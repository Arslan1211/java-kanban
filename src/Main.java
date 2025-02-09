import java.io.File;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

  public static void main(String[] args) {

    File file = new File("fileForSavingTasks.csv");

    TaskManager taskManager = new InMemoryTaskManager();

    FileBackedTaskManager fileBackedTaskManager;
    fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
    checkTaskManager(fileBackedTaskManager);

    FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);
    loadFromFile.deleteAllTasks();
    loadFromFile.deleteAllEpics();
    loadFromFile.deleteAllSubtasks();
    checkTaskManager(loadFromFile);
  }

  // Реализовано для удобного чтения вывода в консоли
  public static void printTask(TaskManager taskManager) {
    for (Task task : taskManager.getHistory()) {
      System.out.println(task);
    }
  }

  public static void checkTaskManager(TaskManager manager) {
    // Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
    Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
    Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
    manager.createTask(task1);
    manager.createTask(task2);

    Epic epic1 = new Epic("Эпик 1", "У меня есть подзадачи");
    manager.createEpic(epic1);
    Subtask subtask1 = new Subtask(epic1.getId(), "Подзадача 1", "Связана с эпик 1",
        Status.NEW);
    Subtask subtask2 = new Subtask(epic1.getId(), "Подзадача 2", "Связана с эпик 1",
        Status.NEW);
    Subtask subtask3 = new Subtask(epic1.getId(), "Подзадача 3", "Связана с эпик 1",
        Status.NEW);
    manager.createSubtask(subtask1);
    manager.createSubtask(subtask2);
    manager.createSubtask(subtask3);

    Epic epic2 = new Epic("Эпик 2", "У меня нет подзадачи");
    manager.createEpic(epic2);

    System.out.println("************************* ЗАДАЧИ *************************");
    System.out.println("Запросите созданные задачи несколько раз в разном порядке.");
    manager.findTaskById(task1.getId());
    manager.findTaskById(task2.getId());

    manager.findTaskById(task2.getId());
    manager.findTaskById(task1.getId());

    System.out.println(
        "После каждого запроса выведите историю и убедитесь, что в ней нет повторов.");
    System.out.println("История просмотров задач");
    printTask(manager);
    System.out.println();
    System.out.println(
        "Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.");
    manager.deleteTaskById(task1.getId());
    System.out.println("История просмотров после удаления задачи 1");
    printTask(manager);
    System.out.println();

    System.out.println("*********************** ПОДЗАДАЧИ ************************");
    System.out.println("Запросите созданные задачи несколько раз в разном порядке.");
    manager.findSubtaskById(subtask1.getId());
    manager.findSubtaskById(subtask2.getId());
    manager.findSubtaskById(subtask3.getId());

    manager.findSubtaskById(subtask3.getId());
    manager.findSubtaskById(subtask2.getId());
    manager.findSubtaskById(subtask1.getId());

    manager.findSubtaskById(subtask2.getId());
    manager.findSubtaskById(subtask3.getId());
    manager.findSubtaskById(subtask1.getId());

    System.out.println(
        "После каждого запроса выведите историю и убедитесь, что в ней нет повторов.");
    System.out.println("История просмотров подзадач");
    printTask(manager);
    System.out.println();
    System.out.println(
        "Удалите подзадачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.");
    manager.deleteSubtaskById(subtask1.getId());
    System.out.println("История просмотров после удаления подзадачи 1");
    printTask(manager);
    System.out.println();

    System.out.println("************************* ЭПИКИ **************************");
    System.out.println("Запросите созданные задачи несколько раз в разном порядке.");
    manager.findEpicById(epic1.getId());
    manager.findEpicById(epic2.getId());

    manager.findEpicById(epic2.getId());
    manager.findEpicById(epic1.getId());

    System.out.println(
        "После каждого запроса выведите историю и убедитесь, что в ней нет повторов.");
    System.out.println("История просмотров эпиков");
    printTask(manager);
    System.out.println();
    System.out.println(
        "Удалите эпик, который есть в истории, и проверьте, что при печати она не будет выводиться.");
    manager.deleteEpicById(epic2.getId());
    System.out.println("История просмотров после удаления эпика 2");
    printTask(manager);
    System.out.println();
    System.out.println(
        "Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.");
    manager.deleteEpicById(epic1.getId());
    System.out.println(
        "История просмотров после удаления эпика 1-го эпика с тремя подзадачами");
    printTask(manager);
  }
}