import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Task;
import taskmanager.tasktypes.Subtask;
import taskmanager.utility.Status;

public class Main {

    public static void main(String[] args) {

        System.out.print("-----------------------------------------добавление и поиск-----------------------------------------------------------");
        TaskManager taskManager = new TaskManager();
        Task task = new Task("task1", "taskTesting");
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask, 10);

        printTasks(taskManager);
        System.out.println("taskManager.findTask(10) = " + taskManager.findTask(10));
        System.out.println("taskManager.findEpic(10) = " + taskManager.findEpic(10));
        System.out.println("taskManager.findSubtask(10) = " + taskManager.findSubtask(10));
        System.out.println("taskManager.findTask(task.getId()) = " + taskManager.findTask(task.getId()));
        System.out.println("taskManager.findEpic(epic.getId()) = " + taskManager.findEpic(epic.getId()));
        System.out.println("taskManager.findSubtask(subtask.getId()) = " + taskManager.findSubtask(subtask.getId()));

        taskManager.addSubtask(subtask, epic.getId());

        printTasks(taskManager);
        System.out.println("taskManager.findSubtask(subtask.getId()) = " + taskManager.findSubtask(subtask.getId()));;
        System.out.println("-----------------------------------------добавление и поиск-----------------------------------------------------------\n");

        System.out.print("-------------------------------------------------очистка--------------------------------------------------------------");
        System.out.println();
        System.out.println("taskManager.clearTasks() = " + taskManager.clearTasks());
        System.out.println("taskManager.clearEpics() = " + taskManager.clearEpics());
        System.out.println("taskManager.clearSubtasks() = " + taskManager.clearSubtasks());

        printTasks(taskManager);
        System.out.println("-------------------------------------------------очистка--------------------------------------------------------------\n");

        System.out.print("----------------------------------------------очистка подзадач--------------------------------------------------------");
        Epic epic2 = new Epic("epic2", "epicTesting");
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting");
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting");
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask2, epic2.getId());
        taskManager.addSubtask(subtask3, epic2.getId());

        printTasks(taskManager);
        System.out.println("epic2.getSubtasksIds() = " + epic2.getSubtasksIds());

        System.out.println();
        System.out.println("taskManager.clearSubtasks() = " + taskManager.clearSubtasks());
        System.out.println("epic2.getSubtasksIds() = " + epic2.getSubtasksIds());

        printTasks(taskManager);
        System.out.println("----------------------------------------------очистка подзадач--------------------------------------------------------\n");

        taskManager.clearEpics();

        System.out.print("--------------------------------------------------обновление----------------------------------------------------------");
        task = new Task("task1", "taskTesting");
        epic = new Epic("epic1", "epicTesting");
        subtask = new Subtask("subtask1", "subtaskTesting");
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask, epic.getId());

        printTasks(taskManager);

        task.setName("updatedTask"); task.setStatus(Status.IN_PROGRESS);
        epic.setName("updatedEpic"); epic.setStatus(Status.IN_PROGRESS);
        subtask.setName("updatedSubtask"); subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        taskManager.updateEpic(epic);
        taskManager.updateSubtask(subtask);

        printTasks(taskManager);

        task.setId(100);
        epic.setId(100);
        subtask.setId(100);

        System.out.println();
        System.out.println("taskManager.updateTask(task) = " + taskManager.updateTask(task));
        System.out.println("taskManager.updateEpic(epic) = " + taskManager.updateEpic(epic));
        System.out.println("taskManager.updateSubtask(subtask) = " + taskManager.updateSubtask(subtask));

        epic.setId(8);
        subtask.setId(9);

        epic = new Epic("updatedEpic", "epicTesting");
        subtask = new Subtask("updatedSubtask", "subtaskTesting");
        epic.setId(8);
        subtask.setId(9);
        epic.addSubtaskId(100);
        subtask.setContainingEpicId(101);

        System.out.println();
        System.out.println(epic);
        System.out.println(subtask);

        System.out.println();
        System.out.println("taskManager.updateEpic(epic) = " + taskManager.updateEpic(epic));
        System.out.println("taskManager.updateSubtask(subtask) = " + taskManager.updateSubtask(subtask));

        printTasks(taskManager);
        System.out.println("Здесь у задачи выводится id=100 из-за небезопасного обновления id, в дебаггере все работает корректно, на работе методов это никак не сказывается");
        System.out.println("--------------------------------------------------обновление----------------------------------------------------------\n");

        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();

        System.out.print("------------------------------------------------удаление по id--------------------------------------------------------");
        task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        epic = new Epic("epic1", "epicTesting");
        epic2 = new Epic("epic2", "epicTesting2");
        subtask = new Subtask("subtask1", "subtaskTesting");
        subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask3 = new Subtask("subtask3", "subtaskTesting3");

        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask, epic.getId());
        taskManager.addSubtask(subtask2, epic2.getId());
        taskManager.addSubtask(subtask3, epic2.getId());

        printTasks(taskManager);

        taskManager.removeTask(task.getId());
        taskManager.removeEpic(epic.getId());
        taskManager.removeSubtask(subtask2.getId());

        printTasks(taskManager);
        System.out.println("taskManager.removeSubtask(subtask.getId()) = " + taskManager.removeSubtask(subtask.getId()));
        System.out.println("------------------------------------------------удаление по id--------------------------------------------------------\n");

        System.out.println("-------------------------------------------получение подзадач эпика---------------------------------------------------");
        System.out.println("taskManager.getEpicSubtasksIds(13) = " + taskManager.getEpicSubtasksIds(13));
        System.out.println("taskManager.getEpicSubtasksIds(100) = " + taskManager.getEpicSubtasksIds(100));
        System.out.println("-------------------------------------------получение подзадач эпика---------------------------------------------------\n");

        System.out.println("----------------------------------------------установка статусов------------------------------------------------------");
        System.out.println("taskManager.setTaskStatus(100, Status.IN_PROGRESS) = " + taskManager.setTaskStatus(100, Status.IN_PROGRESS));
        System.out.println("taskManager.setSubtaskStatus(100, Status.IN_PROGRESS) = " + taskManager.setSubtaskStatus(100, Status.IN_PROGRESS));
        System.out.println("taskManager.setTaskStatus(11, Status.IN_PROGRESS) = " + taskManager.setTaskStatus(11, Status.IN_PROGRESS));
        System.out.println("taskManager.setSubtaskStatus(16, Status.IN_PROGRESS) = " + taskManager.setSubtaskStatus(16, Status.IN_PROGRESS));

        printTasks(taskManager);

        task2 = new Task("task2", "taskTesting2");
        task2.setId(11);
        subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask2.setId(16);
        subtask2.setContainingEpicId(13);
        epic2 = new Epic("epic2", "epicTesting2");
        epic2.setId(13);
        epic2.setStatus(Status.IN_PROGRESS);

        System.out.println();
        System.out.println("taskManager.updateTask(task2) = " + taskManager.updateTask(task2));
        System.out.println("taskManager.updateEpic(epic2) = " + taskManager.updateEpic(epic2));
        System.out.println("taskManager.updateSubtask(subtask2) = " + taskManager.updateSubtask(subtask2));

        printTasks(taskManager);
        System.out.println("----------------------------------------------установка статусов------------------------------------------------------\n");

        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();

        System.out.print("-------------------------------------------обновление статуса эпика---------------------------------------------------");
        epic = new Epic("epic1", "epicTesting");
        epic2 = new Epic("epic2", "epicTesting2");
        subtask = new Subtask("subtask1", "subtaskTesting");
        subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask3 = new Subtask("subtask3", "subtaskTesting3");
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4");
        Subtask subtask5 = new Subtask("subtask5", "subtaskTesting5");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask, epic.getId());
        taskManager.addSubtask(subtask2, epic.getId());
        taskManager.addSubtask(subtask3, epic2.getId());
        taskManager.addSubtask(subtask4, epic2.getId());

        printTasks(taskManager);

        taskManager.setSubtaskStatus(19, Status.NEW);
        taskManager.setSubtaskStatus(20, Status.DONE);
        taskManager.setSubtaskStatus(21, Status.IN_PROGRESS);
        taskManager.setSubtaskStatus(22, Status.IN_PROGRESS);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());

        taskManager.setSubtaskStatus(19, Status.DONE);
        taskManager.setSubtaskStatus(21, Status.NEW);
        taskManager.setSubtaskStatus(22, Status.NEW);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());

        taskManager.setSubtaskStatus(19, Status.NEW);
        taskManager.removeSubtask(20);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());

        taskManager.setSubtaskStatus(19, Status.DONE);
        taskManager.addSubtask(subtask5, epic.getId());

        System.out.println();
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());

        System.out.println();
        System.out.println("taskManager.addSubtask(subtask5, epic.getId()) = " + taskManager.addSubtask(subtask5, epic.getId()));

        printTasks(taskManager);
        System.out.println("-------------------------------------------обновление статуса эпика---------------------------------------------------");
    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println();
        System.out.println("taskManager.getTasks() = " + taskManager.getTasks());
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());
        System.out.println("taskManager.getSubtasks() = " + taskManager.getSubtasks());
    }
}
