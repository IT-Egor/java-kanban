package taskmanager.utility;

import taskmanager.servise.HistoryManager;
import taskmanager.servise.impl.FileBackedTaskManager;
import taskmanager.servise.impl.InMemoryHistoryManager;
import taskmanager.servise.TaskManager;

import java.io.File;

public class Managers {
    public static final File CSV_FILE = new File("src/taskmanager/resources/test.csv");

    public static int getNextId() {
        return IdManager.generateId();
    }

    public static TaskManager getDefaultTaskManager() {
        return new FileBackedTaskManager(CSV_FILE, getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}