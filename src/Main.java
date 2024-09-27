import taskmanager.servise.TaskManager;
import taskmanager.servise.impl.FileBackedTaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Task;
import taskmanager.tasktypes.Subtask;
import taskmanager.utility.Managers;
import taskmanager.utility.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        int cutWidth = 80;

//        System.out.print("-".repeat(cutWidth) + "создание задач" + "-".repeat(cutWidth));
//        Task task1 = new Task("task1", "taskTesting1");
//        Task task2 = new Task("task2", "taskTesting2");
//        taskManager.addTask(task1);
//        taskManager.addTask(task2);
//
//        Epic epic1 = new Epic("epic1", "epicTesting1");
//        Epic epic2 = new Epic("epic2", "epicTesting2");
//        taskManager.addEpic(epic1);
//        taskManager.addEpic(epic2);
//
//        Subtask subtask1 = new Subtask("subtask1", "subtaskTesting1", epic1.getId());
//        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic1.getId());
//        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3", epic2.getId());
//        taskManager.addSubtask(subtask1);
//        taskManager.addSubtask(subtask2);
//        taskManager.addSubtask(subtask3);
//
//        printTasks(taskManager);
//        System.out.println("-".repeat(cutWidth) + "создание задач" + "-".repeat(cutWidth));
//        System.out.println();
//
//
//        System.out.print("-".repeat(cutWidth) + "обновление задач" + "-".repeat(cutWidth));
//        Task updatedTask = new Task("updatedTask", "updatedTaskTesting");
//        updatedTask.setId(task1.getId());
//        updatedTask.setStatus(Status.DONE);
//        taskManager.updateTask(updatedTask);
//
//        Epic updatedEpic = new Epic("updatedEpic", "updatedEpicTesting");
//        updatedEpic.setId(epic1.getId());
//        updatedEpic.setSubtasksIds(epic1.getSubtasksIds());
//        taskManager.updateEpic(updatedEpic);
//
//        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic1.getId());
//        updatedSubtask.setId(subtask1.getId());
//        updatedSubtask.setStatus(Status.DONE);
//        Subtask updatedSubtask3 = new Subtask("updatedSubtask3", "updatedSubtaskTesting3", epic2.getId());
//        updatedSubtask3.setId(subtask3.getId());
//        updatedSubtask3.setStatus(Status.DONE);
//        taskManager.updateSubtask(updatedSubtask);
//        taskManager.updateSubtask(updatedSubtask3);
//
//        printTasks(taskManager);
//        System.out.println("-".repeat(cutWidth) + "обновление задач" + "-".repeat(cutWidth));
//        System.out.println();
//
//
//        System.out.println("-".repeat(cutWidth) + "поиск задач" + "-".repeat(cutWidth));
//        System.out.println("taskManager.findTask(task1.getId()) = " + taskManager.findTask(task1.getId()));
//        System.out.println("taskManager.findEpic(epic1.getId()) = " + taskManager.findEpic(epic1.getId()));
//        System.out.println("taskManager.findSubtask(subtask1.getId()) = " + taskManager.findSubtask(subtask1.getId()));
//        System.out.println("taskManager.findSubtask(subtask3.getId()) = " + taskManager.findSubtask(subtask3.getId()));
//        System.out.println();
//        System.out.println("taskManager.getEpicSubtasksIds(epic1.getId()) = " + taskManager.getEpicSubtasksIds(epic1.getId()));
//        System.out.println();
//        System.out.println("taskManager.getHistory() = " + taskManager.getHistory());
//        System.out.println("-".repeat(cutWidth) + "поиск задач" + "-".repeat(cutWidth));
//        System.out.println();
//
//        System.out.println("-".repeat(cutWidth) + "удаление задач" + "-".repeat(cutWidth));
//        System.out.println("taskManager.removeTask(task2.getId()) = " + taskManager.removeTask(task2.getId()));
//        System.out.println("taskManager.removeEpic(epic2.getId()) = " + taskManager.removeEpic(epic2.getId()));
//        System.out.println("taskManager.removeSubtask(subtask1.getId()) = " + taskManager.removeSubtask(subtask1.getId()));
//        System.out.println();
//        System.out.println("taskManager.getHistory() = " + taskManager.getHistory());
//
//        printTasks(taskManager);
//        System.out.println("-".repeat(cutWidth) + "удаление задач" + "-".repeat(cutWidth));
//        System.out.println();
//
//
//        System.out.println("-".repeat(cutWidth) + "очистка задач" + "-".repeat(cutWidth));
//        System.out.println("taskManager.clearTasks() = " + taskManager.clearTasks());
//        System.out.println("taskManager.clearEpics() = " + taskManager.clearEpics());
//        System.out.println("taskManager.clearSubtasks() = " + taskManager.clearSubtasks());
//        System.out.println();
//        System.out.println("taskManager.getHistory() = " + taskManager.getHistory());
//        printTasks(taskManager);
//        System.out.println("-".repeat(cutWidth) + "очистка задач" + "-".repeat(cutWidth));
//        System.out.println();
//
//
//        System.out.println("-".repeat(cutWidth) + "проверка истории на удаление и дубликаты" + "-".repeat(cutWidth));
//        task1 = new Task("task1", "taskTesting1");
//        task2 = new Task("task2", "taskTesting2");
//        epic1 = new Epic("epic1", "epicTesting1");
//        epic2 = new Epic("epic2", "epicTesting2");
//
//        taskManager.addTask(task1);
//        taskManager.addTask(task2);
//        taskManager.addEpic(epic1);
//        taskManager.addEpic(epic2);
//        subtask1 = new Subtask("subtask1", "subtaskTesting1", epic1.getId());
//        subtask2 = new Subtask("subtask2", "subtaskTesting2", epic1.getId());
//        subtask3 = new Subtask("subtask3", "subtaskTesting3", epic1.getId());
//        taskManager.addSubtask(subtask1);
//        taskManager.addSubtask(subtask2);
//        taskManager.addSubtask(subtask3);
//
//        taskManager.findTask(task1.getId());
//        taskManager.findTask(task2.getId());
//        taskManager.findEpic(epic1.getId());
//        taskManager.findEpic(epic2.getId());
//        taskManager.findSubtask(subtask1.getId());
//        taskManager.findSubtask(subtask2.getId());
//        taskManager.findSubtask(subtask3.getId());
//        taskManager.findTask(task1.getId());
//        taskManager.findSubtask(subtask1.getId());
//        taskManager.findSubtask(subtask3.getId());
//        taskManager.findEpic(epic1.getId());
//
//        System.out.println("taskManager.getHistory() = " + taskManager.getHistory());
//        System.out.println();
//
//        taskManager.removeTask(task2.getId());
//        taskManager.removeEpic(epic1.getId());
//
//        System.out.println("taskManager.getHistory() = " + taskManager.getHistory());
//        System.out.println();
//        printTasks(taskManager);
//
//        System.out.println("-".repeat(cutWidth) + "проверка истории на удаление и дубликаты" + "-".repeat(cutWidth));
//        System.out.println();
//
//
//        System.out.print("-".repeat(cutWidth) + "проверка чтения задач из файла" + "-".repeat(cutWidth));
//        taskManager.clearTasks();
//        taskManager.clearEpics();
//        taskManager.clearSubtasks();
//
//        task1 = new Task("task1", "taskTesting1");
//        task2 = new Task("task2", "taskTesting2");
//        epic1 = new Epic("epic1", "epicTesting1");
//        epic2 = new Epic("epic2", "epicTesting2");
//
//        taskManager.addTask(task1);
//        taskManager.addTask(task2);
//        taskManager.addEpic(epic1);
//        taskManager.addEpic(epic2);
//        subtask1 = new Subtask("subtask1", "subtaskTesting1", epic1.getId());
//        subtask2 = new Subtask("subtask2", "subtaskTesting2", epic1.getId());
//        subtask3 = new Subtask("subtask3", "subtaskTesting3", epic2.getId());
//        subtask1.setStatus(Status.DONE);
//        subtask3.setStatus(Status.DONE);
//        taskManager.addSubtask(subtask1);
//        taskManager.addSubtask(subtask2);
//        taskManager.addSubtask(subtask3);
//
//        printTasks(taskManager);
//
//        TaskManager newTaskManager = FileBackedTaskManager.loadFromFile(Managers.CSV_FILE);
//        printTasks(newTaskManager);
//        System.out.println("-".repeat(cutWidth) + "проверка чтения задач из файла" + "-".repeat(cutWidth));

        System.out.println("-".repeat(cutWidth) + "проверка времени" + "-".repeat(cutWidth));
        Task task1 = new Task("task1", "taskTesting1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofHours(1));

        Epic epic1 = new Epic("epic1", "epicTesting1");
        Epic epic2 = new Epic("epic2", "epicTesting2");

        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("subtask1", "subtaskTesting1");
        subtask1.setStartTime(LocalDateTime.now().plusHours(3));
        subtask1.setDuration(Duration.ofHours(4));
        subtask1.setContainingEpicId(epic1.getId());

        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask2.setStartTime(LocalDateTime.now().plusHours(4));
        subtask2.setDuration(Duration.ofHours(5));
        subtask2.setContainingEpicId(epic1.getId());

        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3");
        subtask3.setStartTime(LocalDateTime.now().plusHours(5));
        subtask3.setDuration(Duration.ofHours(6));
        subtask3.setContainingEpicId(epic2.getId());

        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4");
        subtask4.setStartTime(LocalDateTime.now().plusHours(6));
        subtask4.setDuration(Duration.ofHours(7));
        subtask4.setContainingEpicId(epic2.getId());

        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask3);

        printTasks(taskManager);

        System.out.println();
        System.out.println("taskManager.getPrioritizedTasks() = " + taskManager.getPrioritizedTasks());

        Subtask updatedSubtask1 = new Subtask("updatedSubtask1", "updatedSubtaskTesting1");
        updatedSubtask1.setStartTime(LocalDateTime.of(2024, Month.SEPTEMBER, 25, 0,0,0));
        updatedSubtask1.setId(subtask1.getId());
        updatedSubtask1.setContainingEpicId(epic1.getId());
        updatedSubtask1.setDuration(Duration.ofMinutes(5));
        taskManager.updateSubtask(updatedSubtask1);
        taskManager.removeSubtask(subtask4.getId());

        System.out.println();
        System.out.println("taskManager.getPrioritizedTasks() = " + taskManager.getPrioritizedTasks());

        printTasks(taskManager);
        TaskManager newTaskManager = FileBackedTaskManager.loadFromFile(Managers.CSV_FILE);
        printTasks(newTaskManager);

        System.out.println();
        System.out.println("taskManager.getPrioritizedTasks() = " + taskManager.getPrioritizedTasks());

        System.out.println("-".repeat(cutWidth) + "проверка времени" + "-".repeat(cutWidth));
        System.out.println();
    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println();
        System.out.println("taskManager.getTasks() = " + taskManager.getTasks());
        System.out.println("taskManager.getEpics() = " + taskManager.getEpics());
        System.out.println("taskManager.getSubtasks() = " + taskManager.getSubtasks());
    }
}
