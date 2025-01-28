import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();
        // Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        Task task1 = new Task("Задача 1", "Я сам по себе", Status.NEW);
        Task task2 = new Task("Задача 2", "Я сам по себе", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "У меня есть подзадачи");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask(epic1.getId(), "Подзадача 1", "Связана с эпик 1", Status.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(), "Подзадача 2", "Связана с эпик 1", Status.NEW);
        Subtask subtask3 = new Subtask(epic1.getId(), "Подзадача 3", "Связана с эпик 1", Status.NEW);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "У меня нет подзадачи");
        taskManager.createEpic(epic2);

        System.out.println("************************* ЗАДАЧИ *************************");
        System.out.println("Запросите созданные задачи несколько раз в разном порядке.");
        taskManager.findTaskById(task1.getId());
        taskManager.findTaskById(task2.getId());

        taskManager.findTaskById(task2.getId());
        taskManager.findTaskById(task1.getId());

        System.out.println("После каждого запроса выведите историю и убедитесь, что в ней нет повторов.");
        System.out.println("История просмотров задач");
        printTask(taskManager);
        System.out.println();
        System.out.println("Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.");
        taskManager.deleteTaskById(task1.getId());
        System.out.println("История просмотров после удаления задачи 1");
        printTask(taskManager);
        System.out.println();

        System.out.println("*********************** ПОДЗАДАЧИ ************************");
        System.out.println("Запросите созданные задачи несколько раз в разном порядке.");
        taskManager.findSubtaskById(subtask1.getId());
        taskManager.findSubtaskById(subtask2.getId());
        taskManager.findSubtaskById(subtask3.getId());

        taskManager.findSubtaskById(subtask3.getId());
        taskManager.findSubtaskById(subtask2.getId());
        taskManager.findSubtaskById(subtask1.getId());

        taskManager.findSubtaskById(subtask2.getId());
        taskManager.findSubtaskById(subtask3.getId());
        taskManager.findSubtaskById(subtask1.getId());

        System.out.println("После каждого запроса выведите историю и убедитесь, что в ней нет повторов.");
        System.out.println("История просмотров подзадач");
        printTask(taskManager);
        System.out.println();
        System.out.println("Удалите подзадачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.");
        taskManager.deleteSubtaskById(subtask1.getId());
        System.out.println("История просмотров после удаления подзадачи 1");
        printTask(taskManager);
        System.out.println();

        System.out.println("************************* ЭПИКИ **************************");
        System.out.println("Запросите созданные задачи несколько раз в разном порядке.");
        taskManager.findEpicById(epic1.getId());
        taskManager.findEpicById(epic2.getId());

        taskManager.findEpicById(epic2.getId());
        taskManager.findEpicById(epic1.getId());

        System.out.println("После каждого запроса выведите историю и убедитесь, что в ней нет повторов.");
        System.out.println("История просмотров эпиков");
        printTask(taskManager);
        System.out.println();
        System.out.println("Удалите эпик, который есть в истории, и проверьте, что при печати она не будет выводиться.");
        taskManager.deleteEpicById(epic2.getId());
        System.out.println("История просмотров после удаления эпика 2");
        printTask(taskManager);
        System.out.println();
        System.out.println("Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.");
        taskManager.deleteEpicById(epic1.getId());
        System.out.println("История просмотров после удаления эпика 1-го эпика с тремя подзадачами");
        printTask(taskManager);
    }

    // Реализовано для удобного чтения вывода в консоли
    public static void printTask(TaskManager taskManager) {
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}