package taskmanager.servise.HistoryManagers;

import taskmanager.tasktypes.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    void remove(Task task);
    ArrayList<Task> getHistory();
}
