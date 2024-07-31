import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Task;
import taskmanager.tasktypes.Subtask;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("task1", "taskTesting");
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask, 10);

        printTasks(taskManager);

        taskManager.addSubtask(subtask, epic.getId());

        printTasks(taskManager);

        System.out.println("taskManager.clearTasks() = " + taskManager.clearTasks());
        System.out.println("taskManager.clearEpics() = " + taskManager.clearEpics());
        System.out.println("taskManager.clearSubtasks() = " + taskManager.clearSubtasks());

        printTasks(taskManager);

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
    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println();
        System.out.println("taskManager.getTasks() = " + taskManager.getTasks());
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());
        System.out.println("taskManager.getSubtasks() = " + taskManager.getSubtasks());
    }
}
