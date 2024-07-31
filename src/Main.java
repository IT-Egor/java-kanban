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
    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println();
        System.out.println("taskManager.getTasks() = " + taskManager.getTasks());
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());
        System.out.println("taskManager.getSubtasks() = " + taskManager.getSubtasks());
    }
}
