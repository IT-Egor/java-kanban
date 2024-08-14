package taskmanager.utility;

import taskmanager.servise.HistoryManager;
import taskmanager.servise.impl.InMemoryHistoryManager;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.servise.TaskManager;

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
}