package taskmanager.servise.impl;

import taskmanager.servise.HistoryManager;
import taskmanager.tasktypes.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() + 1 > 10) {
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
    public ArrayList<Task> getHistory() {
        return history;
    }
}
