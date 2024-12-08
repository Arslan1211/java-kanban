import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task t1 = new Task("Задача 1", "Первая задача");
        Task t2 = new Task("Задача 2", "Вторая задача");
        taskManager.createTask(t1);
        taskManager.createTask(t2);

        Task updatedTask = new Task("Обновленный заголовок задачи 1", "Обновить задачу 1");
        updatedTask.setId(t1.getId());

        Epic ep1 = new Epic("Эпик 1", "Первый эпик");
        Epic ep2 = new Epic("Эпик 2", "Второй эпик");
        Epic ep3 = new Epic("Эпик 3", "Третий эпик");
        taskManager.createEpic(ep1);
        taskManager.createEpic(ep2);
        taskManager.createEpic(ep3);

        Epic updatedEpic = new Epic("Обновленный заголовок эпика 1", "Обновить эпик 1");
        updatedEpic.setId(ep1.getId());

        Subtask sb1 = new Subtask("Подзадача 1", "Первая подзадача", ep1.getId());
        Subtask sb2 = new Subtask("Подзадача 2", "Вторая подзадача", ep2.getId());
        Subtask sb3 = new Subtask("Подзадача 3", "Третья подзадача", ep2.getId());
        Subtask sb4 = new Subtask("Подзадача 4", "Четвертая подзадача", ep2.getId());
        Subtask sb5 = new Subtask("Подзадача 5", "Пятая подзадача", ep3.getId());

        taskManager.createSubtask(sb1);
        taskManager.createSubtask(sb2);
        taskManager.createSubtask(sb3);
        taskManager.createSubtask(sb4);
        taskManager.createSubtask(sb5);

        Subtask updatedSubtask = new Subtask("Обновленный заголовок подзадачи 1", "Обновить подзадачу 1", ep1.getId());
        updatedSubtask.setId(sb1.getId());

        System.out.println("Все задачи");
        System.out.println(taskManager.findAllTasks());
        System.out.println("Все эпики");
        System.out.println(taskManager.findAllEpics());
        System.out.println("Все подзадачи");
        System.out.println(taskManager.findAllSubtasks());

        System.out.println("Все подзадачи 1-го эпика");
        System.out.println(taskManager.findAllSubtasksByEpic(ep1.getId()));
        System.out.println("Все подзадачи 2-го эпика");
        System.out.println(taskManager.findAllSubtasksByEpic(ep2.getId()));
        System.out.println("Все подзадачи 3-го эпика");
        System.out.println(taskManager.findAllSubtasksByEpic(ep3.getId()));

        System.out.println("Поиск задачи по ID");
        System.out.println(taskManager.findTaskById(t1.getId()));
        System.out.println("Поиск эпика по ID");
        System.out.println(taskManager.findEpicById(ep1.getId()));
        System.out.println("Поиск подзадачи по ID");
        System.out.println(taskManager.findSubtaskById(sb1.getId()));

        System.out.println("Обновление задачи");
        System.out.println(taskManager.updateTask(updatedTask));
        System.out.println("Обновление эпика");
        System.out.println(taskManager.updateEpic(updatedEpic));
        System.out.println("Обновление подзадачи");
        System.out.println(taskManager.updateSubtask(updatedSubtask));

        System.out.println("Проверка статуса задачи");
        System.out.println("Статус 1-й задачи " + taskManager.findTaskById(t1.getId()).getStatus());
        Task updateTask1 = new Task("Обновленный заголовок задачи", "Обновленное описание задачи");
        updateTask1.setId(t1.getId());
        updateTask1.setStatus(Status.IN_PROGRESS);
        System.out.println(taskManager.updateTask(updateTask1));

        System.out.println("Проверка статуса эпика");
        System.out.println("Статус 1-го эпика " + taskManager.findEpicById(ep1.getId()).getStatus());
        System.out.print(taskManager.findEpicById(ep1.getId()));
        Epic updateEpic1 = new Epic("Обновленный заголовок эпика", "Обновленное описание эпика");
        updateEpic1.setId(ep1.getId());
        System.out.println(taskManager.updateEpic(updateEpic1));

        System.out.println("Проверка статуса подзадачи");
        System.out.println("Статус 1-й подзадачи " + taskManager.findSubtaskById(sb1.getId()).getStatus());
        System.out.print(taskManager.findSubtaskById(sb1.getId()));
        Subtask updateSubtask1 = new Subtask("Обновленный заголовок подзадачи", "Обновленное описание подзадачи", ep1.getId());
        updateSubtask1.setId(sb1.getId());
        System.out.println(taskManager.updateSubtask(updateSubtask1));

        System.out.println("Удаление задачи по ID");
        System.out.println(taskManager.deleteTaskById(t2.getId()));
        System.out.println("Удаление эпика по ID");
        System.out.println(taskManager.deleteEpicById(ep2.getId()));

        // Проверяем смену статуса у эпика после удаления единственной подзадачи
        sb5.setStatus(Status.DONE);
        taskManager.updateSubtask(sb5);
        System.out.println("СТАТУС ЭПИКА" + taskManager.findEpicById(ep3.getId()));
        System.out.println("Удаление подзадачи по ID");
        System.out.println(taskManager.deleteSubtaskById(sb5.getId()));
        System.out.println(taskManager.findAllEpics());

        System.out.println("Удаление всех задач");
        taskManager.deleteAllTasks();
        System.out.println(taskManager.findAllTasks());
        System.out.println("Удаление всех эпиков");
        taskManager.deleteAllEpics();
        System.out.println(taskManager.findAllEpics());
        System.out.println("Удаление всех подзадач");
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.findAllSubtasks());
    }
}