package taskmanager.servise.impl;

import taskmanager.servise.HistoryManager;
import taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() >= MAX_HISTORY_SIZE) {
            history.removeLast();
            history.addFirst(task);
        } else {
            history.addFirst(task);
        }
    }

    @Override
    public void remove(Task task) {
        history.remove(task);
    }

    @Override
    public List<Task> getHistory() {
        // TODO: возвращать копию
        return history;
    }
}
