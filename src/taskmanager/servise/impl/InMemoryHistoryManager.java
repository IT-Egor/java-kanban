package taskmanager.servise.impl;

import taskmanager.servise.HistoryManager;
import taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        final int maxSize = 10;
        if (history.size() >= maxSize) {
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
    public LinkedList<Task> getHistory() {
        return history;
    }
}
