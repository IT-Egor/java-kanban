package taskmanager.servise;

import taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public interface HistoryManager {
    void add(Task task);
    void remove(Task task);
    LinkedList<Task> getHistory();
}
