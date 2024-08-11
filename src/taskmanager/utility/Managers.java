package taskmanager.utility;

import taskmanager.servise.InMemoryTaskManager;
import taskmanager.servise.TaskManager;

public class Managers {
    public static int getNextId() {
        return IdManager.generateId();
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }
}