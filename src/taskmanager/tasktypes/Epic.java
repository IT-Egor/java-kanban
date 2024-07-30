package taskmanager.tasktypes;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtasksIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtasksIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtasks.size=" + subtasksIds.size() +
                '}';
    }
}
