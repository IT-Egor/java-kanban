package taskmanager.servise;

import taskmanager.tasktypes.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
