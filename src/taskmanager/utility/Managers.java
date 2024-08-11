package taskmanager.utility;

import taskmanager.servise.TaskManagers.InMemoryTaskManager;
import taskmanager.servise.TaskManagers.TaskManager;

public class Managers {
    public static int getNextId() {
        return IdManager.generateId();
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }
}