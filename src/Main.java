import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Task;
import taskmanager.tasktypes.Subtask;
import taskmanager.utility.Managers;
import taskmanager.utility.Status;

public class Main {

    public static void main(String[] args) {

        System.out.print("-----------------------------------------добавление и поиск-----------------------------------------------------------");
        TaskManager inMemoryTaskManager = Managers.getDefaultTaskManager();
        Task task = new Task("task1", "taskTesting");
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", 10);

        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask);

        printTasks(inMemoryTaskManager);
        System.out.println("taskManager.findTask(10) = " + inMemoryTaskManager.findTask(10));
        System.out.println("taskManager.findEpic(10) = " + inMemoryTaskManager.findEpic(10));
        System.out.println("taskManager.findSubtask(10) = " + inMemoryTaskManager.findSubtask(10));
        System.out.println("taskManager.findTask(task.getId()) = " + inMemoryTaskManager.findTask(task.getId()));
        System.out.println("taskManager.findEpic(epic.getId()) = " + inMemoryTaskManager.findEpic(epic.getId()));
        System.out.println("taskManager.findSubtask(subtask.getId()) = " + inMemoryTaskManager.findSubtask(subtask.getId()));

        subtask.setContainingEpicId(2);
        inMemoryTaskManager.addSubtask(subtask);

        printTasks(inMemoryTaskManager);
        System.out.println("taskManager.findSubtask(subtask.getId()) = " + inMemoryTaskManager.findSubtask(subtask.getId()));;
        System.out.println("-----------------------------------------добавление и поиск-----------------------------------------------------------\n");

        System.out.print("-------------------------------------------------очистка--------------------------------------------------------------");
        System.out.println();
        System.out.println("taskManager.clearTasks() = " + inMemoryTaskManager.clearTasks());
        System.out.println("taskManager.clearEpics() = " + inMemoryTaskManager.clearEpics());
        System.out.println("taskManager.clearSubtasks() = " + inMemoryTaskManager.clearSubtasks());

        printTasks(inMemoryTaskManager);
        System.out.println("-------------------------------------------------очистка--------------------------------------------------------------\n");

        System.out.print("----------------------------------------------очистка подзадач--------------------------------------------------------");
        Epic epic2 = new Epic("epic2", "epicTesting");
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting", epic2.getId());
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        printTasks(inMemoryTaskManager);
        System.out.println("epic2.getSubtasksIds() = " + epic2.getSubtasksIds());

        System.out.println();
        System.out.println("taskManager.clearSubtasks() = " + inMemoryTaskManager.clearSubtasks());
        System.out.println("epic2.getSubtasksIds() = " + epic2.getSubtasksIds());

        printTasks(inMemoryTaskManager);
        System.out.println("----------------------------------------------очистка подзадач--------------------------------------------------------\n");

        inMemoryTaskManager.clearEpics();

        System.out.print("--------------------------------------------------обновление----------------------------------------------------------");
        task = new Task("task1", "taskTesting");
        epic = new Epic("epic1", "epicTesting");
        subtask = new Subtask("subtask1", "subtaskTesting");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        inMemoryTaskManager.addSubtask(subtask);

        printTasks(inMemoryTaskManager);

        task.setName("updatedTask"); task.setStatus(Status.IN_PROGRESS);
        epic.setName("updatedEpic"); epic.setStatus(Status.IN_PROGRESS);
        subtask.setName("updatedSubtask"); subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task);
        inMemoryTaskManager.updateEpic(epic);
        inMemoryTaskManager.updateSubtask(subtask);

        printTasks(inMemoryTaskManager);

        task.setId(100);
        epic.setId(100);
        subtask.setId(100);

        System.out.println();
        System.out.println("taskManager.updateTask(task) = " + inMemoryTaskManager.updateTask(task));
        System.out.println("taskManager.updateEpic(epic) = " + inMemoryTaskManager.updateEpic(epic));
        System.out.println("taskManager.updateSubtask(subtask) = " + inMemoryTaskManager.updateSubtask(subtask));

        epic.setId(8);
        subtask.setId(9);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        System.out.println("taskManager.updateEpic(epic) = " + inMemoryTaskManager.updateEpic(epic));
        System.out.println("taskManager.updateSubtask(subtask) = " + inMemoryTaskManager.updateSubtask(subtask));

        printTasks(inMemoryTaskManager);
        System.out.println("Здесь у задачи выводится id=100 из-за небезопасного обновления id, в дебаггере все работает корректно, на работе методов это никак не сказывается");
        System.out.println("--------------------------------------------------обновление----------------------------------------------------------\n");

        inMemoryTaskManager.clearTasks();
        inMemoryTaskManager.clearEpics();
        inMemoryTaskManager.clearSubtasks();

        System.out.print("------------------------------------------------удаление по id--------------------------------------------------------");
        task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        epic = new Epic("epic1", "epicTesting");
        epic2 = new Epic("epic2", "epicTesting2");
        subtask = new Subtask("subtask1", "subtaskTesting");
        subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask3 = new Subtask("subtask3", "subtaskTesting3");

        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic2.getId());
        subtask3.setContainingEpicId(epic2.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        printTasks(inMemoryTaskManager);

        inMemoryTaskManager.removeTask(task.getId());
        inMemoryTaskManager.removeEpic(epic.getId());
        inMemoryTaskManager.removeSubtask(subtask2.getId());

        printTasks(inMemoryTaskManager);
        System.out.println("taskManager.removeSubtask(subtask.getId()) = " + inMemoryTaskManager.removeSubtask(subtask.getId()));
        System.out.println("------------------------------------------------удаление по id--------------------------------------------------------\n");

        System.out.println("-------------------------------------------получение подзадач эпика---------------------------------------------------");
        System.out.println("taskManager.getEpicSubtasksIds(13) = " + inMemoryTaskManager.getEpicSubtasksIds(13));
        System.out.println("taskManager.getEpicSubtasksIds(100) = " + inMemoryTaskManager.getEpicSubtasksIds(100));
        System.out.println("-------------------------------------------получение подзадач эпика---------------------------------------------------\n");

        System.out.println("----------------------------------------------установка статусов------------------------------------------------------");
//        System.out.println("taskManager.setTaskStatus(100, Status.IN_PROGRESS) = " + inMemoryTaskManager.setTaskStatus(100, Status.IN_PROGRESS));
//        System.out.println("taskManager.setSubtaskStatus(100, Status.IN_PROGRESS) = " + inMemoryTaskManager.setSubtaskStatus(100, Status.IN_PROGRESS));
//        System.out.println("taskManager.setTaskStatus(11, Status.IN_PROGRESS) = " + inMemoryTaskManager.setTaskStatus(11, Status.IN_PROGRESS));
//        System.out.println("taskManager.setSubtaskStatus(16, Status.IN_PROGRESS) = " + inMemoryTaskManager.setSubtaskStatus(16, Status.IN_PROGRESS));

        printTasks(inMemoryTaskManager);

        task2 = new Task("task2", "taskTesting2");
        task2.setId(11);
        subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask2.setId(16);
        subtask2.setContainingEpicId(13);
        epic2 = new Epic("epic2", "epicTesting2");
        epic2.setId(13);
        epic2.setStatus(Status.IN_PROGRESS);

        System.out.println();
        System.out.println("taskManager.updateTask(task2) = " + inMemoryTaskManager.updateTask(task2));
        System.out.println("taskManager.updateEpic(epic2) = " + inMemoryTaskManager.updateEpic(epic2));
        System.out.println("taskManager.updateSubtask(subtask2) = " + inMemoryTaskManager.updateSubtask(subtask2));

        printTasks(inMemoryTaskManager);
        System.out.println("----------------------------------------------установка статусов------------------------------------------------------\n");

        inMemoryTaskManager.clearTasks();
        inMemoryTaskManager.clearEpics();
        inMemoryTaskManager.clearSubtasks();

        System.out.print("-------------------------------------------обновление статуса эпика---------------------------------------------------");
        epic = new Epic("epic1", "epicTesting");
        epic2 = new Epic("epic2", "epicTesting2");
        subtask = new Subtask("subtask1", "subtaskTesting");
        subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask3 = new Subtask("subtask3", "subtaskTesting3");
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4");
        Subtask subtask5 = new Subtask("subtask5", "subtaskTesting5");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        subtask3.setContainingEpicId(epic2.getId());
        subtask4.setContainingEpicId(epic2.getId());
        subtask5.setContainingEpicId(epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addSubtask(subtask4);

        printTasks(inMemoryTaskManager);

//        inMemoryTaskManager.setSubtaskStatus(19, Status.NEW);
//        inMemoryTaskManager.setSubtaskStatus(20, Status.DONE);
//        inMemoryTaskManager.setSubtaskStatus(21, Status.IN_PROGRESS);
//        inMemoryTaskManager.setSubtaskStatus(22, Status.IN_PROGRESS);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + inMemoryTaskManager.getEpics());

//        inMemoryTaskManager.setSubtaskStatus(19, Status.DONE);
//        inMemoryTaskManager.setSubtaskStatus(21, Status.NEW);
//        inMemoryTaskManager.setSubtaskStatus(22, Status.NEW);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + inMemoryTaskManager.getEpics());

        //inMemoryTaskManager.setSubtaskStatus(19, Status.NEW);
        inMemoryTaskManager.removeSubtask(20);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + inMemoryTaskManager.getEpics());

        //inMemoryTaskManager.setSubtaskStatus(19, Status.DONE);
        inMemoryTaskManager.addSubtask(subtask5);

        System.out.println();
        System.out.println("taskManager.getEpics() = " + inMemoryTaskManager.getEpics());

        System.out.println();
        System.out.println("taskManager.addSubtask(subtask5, epic.getId()) = " + inMemoryTaskManager.addSubtask(subtask5));

        printTasks(inMemoryTaskManager);
        System.out.println("-------------------------------------------обновление статуса эпика---------------------------------------------------");
    }

    public static void printTasks(TaskManager inMemoryTaskManager) {
        System.out.println();
        System.out.println("taskManager.getTasks() = " + inMemoryTaskManager.getTasks());
        System.out.println("taskManager.getEpics() = " + inMemoryTaskManager.getEpics());
        System.out.println("taskManager.getSubtasks() = " + inMemoryTaskManager.getSubtasks());
    }
}
