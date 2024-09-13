package taskmanager.servise;

import org.junit.jupiter.api.Test;
import taskmanager.servise.impl.FileBackedTaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Managers;
import taskmanager.utility.Status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends AbstractTaskManagerTest<FileBackedTaskManager> {

    private File tempFile;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            tempFile = File.createTempFile("test", ".csv");
            return new FileBackedTaskManager(tempFile, Managers.getDefaultHistoryManager());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void shouldWriteTasksToFile() {
        Task task1 = new Task("task1", "taskTesting1");
        Task task2 = new Task("task2", "taskTesting2");
        Epic epic1 = new Epic("epic1", "epicTesting1");
        Epic epic2 = new Epic("epic2", "epicTesting2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtaskTesting1", epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic1.getId());
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3", epic2.getId());
        subtask1.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        List<String> lines = new ArrayList<>();
        String expected = String.format("type,id,name,description,status,epic\n" +
                "TASK,%d,task1,taskTesting1,NEW,\n" +
                "TASK,%d,task2,taskTesting2,NEW,\n" +
                "EPIC,%d,epic1,epicTesting1,IN_PROGRESS,\n" +
                "EPIC,%d,epic2,epicTesting2,DONE,\n" +
                "SUBTASK,%d,subtask1,subtaskTesting1,DONE,%d,\n" +
                "SUBTASK,%d,subtask2,subtaskTesting2,NEW,%d,\n" +
                "SUBTASK,%d,subtask3,subtaskTesting3,DONE,%d,",
                task1.getId(), task2.getId(),
                epic1.getId(), epic2.getId(),
                subtask1.getId(), subtask1.getContainingEpicId(),
                subtask2.getId(), subtask2.getContainingEpicId(),
                subtask3.getId(), subtask3.getContainingEpicId());
        List<String> expectedLines = new ArrayList<>(List.of(expected.split("\n")));

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            while (reader.ready()) {
                lines.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertArrayEquals(expectedLines.toArray(), lines.toArray());
    }

    @Test
    public void shouldLoadTasksFromFile() {
        Task task1 = new Task("task1", "taskTesting1");
        Task task2 = new Task("task2", "taskTesting2");
        Epic epic1 = new Epic("epic1", "epicTesting1");
        Epic epic2 = new Epic("epic2", "epicTesting2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtaskTesting1", epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic1.getId());
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3", epic2.getId());
        subtask1.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        FileBackedTaskManager newTaskManager = (FileBackedTaskManager) Managers.loadTaskManagerFromFile(tempFile);

        assertArrayEquals(taskManager.getTasks().toArray(), newTaskManager.getTasks().toArray());
        assertArrayEquals(taskManager.getEpics().toArray(), newTaskManager.getEpics().toArray());
        assertArrayEquals(taskManager.getSubtasks().toArray(), newTaskManager.getSubtasks().toArray());
    }

    @Test
    public void shouldBeEmptyWhenLoadingFromEmptyFile() {
        try {
            File tempFile2 = File.createTempFile("emptyTest", ".csv");
            FileBackedTaskManager newTaskManager = (FileBackedTaskManager) Managers.loadTaskManagerFromFile(tempFile2);
            assertTrue(newTaskManager.getTasks().isEmpty());
            assertTrue(newTaskManager.getEpics().isEmpty());
            assertTrue(newTaskManager.getSubtasks().isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}