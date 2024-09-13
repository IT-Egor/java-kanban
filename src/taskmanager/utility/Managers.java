package taskmanager.utility;

import taskmanager.servise.HistoryManager;
import taskmanager.servise.impl.FileBackedTaskManager;
import taskmanager.servise.impl.InMemoryHistoryManager;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.servise.TaskManager;

import java.io.File;

public class Managers {
    public static int getNextId() {
        return IdManager.generateId();
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileTaskManager(File csvFile) {
        return new FileBackedTaskManager(csvFile, Managers.getDefaultHistoryManager());
    }

    public static TaskManager loadTaskManagerFromFile(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }
}