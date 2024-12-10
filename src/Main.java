import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача 1", "Я сам по себе");
        Task task2 = new Task("Задача 1", "Я сам по себе");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "У меня есть подзадачи");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Связана с эпик 1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Связана с эпик 1", epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "У меня есть подзадачи");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Связана с эпик 2", epic2.getId());
        taskManager.createSubtask(subtask3);

        // **********************************************************
        // ************************* ЗАДАЧИ *************************
        // **********************************************************
        System.out.println("Задачи");
        System.out.println(taskManager.findAllTasks());
        System.out.println("Поиск задачи по ID");
        System.out.println(taskManager.findTaskById(task1.getId()));
        System.out.println(taskManager.findTaskById(task2.getId()));
        System.out.println("Обновление задачи");
        Task updatedTask = new Task("ЗАДАЧА 1", "Я САМ ПО СЕБЕ");
        updatedTask.setId(task1.getId());
        updatedTask.setStatus(Status.DONE);
        System.out.println(taskManager.updateTask(updatedTask));
        System.out.println("Удаление задачи по ID");
        System.out.println(taskManager.deleteTaskById(task1.getId()));

        // **********************************************************
        // *********************** ПОДЗАДАЧИ ************************
        // **********************************************************
        System.out.println("Подзадачи");
        System.out.println(taskManager.findAllSubtasks());
        System.out.println("Поиск подзадачи по ID");
        System.out.println(taskManager.findSubtaskById(subtask1.getId()));
        System.out.println(taskManager.findSubtaskById(subtask2.getId()));
        System.out.println(taskManager.findSubtaskById(subtask3.getId()));
        System.out.println("Обновление подзадачи");
        Subtask updatedSubtask = new Subtask("ПОДЗАДАЧА 1", "СВЯЗАНА С ЭПИК 1", epic1.getId());
        updatedSubtask.setId(subtask1.getId());
        System.out.println(taskManager.updateSubtask(updatedSubtask));
        System.out.println("Все подзадачи 1-го эпика");
        System.out.println(taskManager.findAllSubtasksByEpic(epic1.getId()));
        System.out.println("Все подзадачи 2-го эпика");
        System.out.println(taskManager.findAllSubtasksByEpic(epic2.getId()));
        System.out.println("Удаление подзадачи по ID");
        System.out.println(taskManager.deleteSubtaskById(subtask3.getId()));
        System.out.println(taskManager.findAllEpics());

        // **********************************************************
        // ************************* ЭПИКИ **************************
        // **********************************************************
        System.out.println("Эпики");
        System.out.println(taskManager.findAllEpics());
        System.out.println("Поиск эпика по ID");
        System.out.println(taskManager.findEpicById(epic1.getId()));
        System.out.println(taskManager.findEpicById(epic2.getId()));
        System.out.println("Обновление эпика");
        Epic updatedEpic = new Epic("ЭПИК 2", "У МЕНЯ ЕСТЬ ПОДЗАДАЧИ");
        updatedEpic.setId(epic2.getId());
        updatedEpic.setStatus(Status.IN_PROGRESS);
        System.out.println(taskManager.updateEpic(updatedEpic));
        System.out.println("Удаление эпика по ID");
        System.out.println(taskManager.deleteEpicById(epic1.getId()));
        System.out.println(taskManager.findAllSubtasks());

        // **********************************************************
        // ************ УДАЛЕНИЕ ЗАДАЧИ ПОДЗАДАЧИ ЭПИКИ *************
        // **********************************************************
        System.out.println("Удаление всех задач");
        taskManager.deleteAllTasks();
        System.out.println(taskManager.findAllTasks());
        System.out.println("Удаление всех подзадач");
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.findAllSubtasks());
        System.out.println("Удаление всех эпиков");
        taskManager.deleteAllEpics();
        System.out.println(taskManager.findAllEpics());
    }
}