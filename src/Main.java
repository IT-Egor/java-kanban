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
        System.out.println("--------------------------------------------------обновление----------------------------------------------------------\n");
    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println();
        System.out.println("taskManager.getTasks() = " + taskManager.getTasks());
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());
        System.out.println("taskManager.getSubtasks() = " + taskManager.getSubtasks());
    }
}
