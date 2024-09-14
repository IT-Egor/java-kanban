package taskmanager.tasktypes;

import taskmanager.utility.Status;
import taskmanager.utility.Type;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIds = new ArrayList<>();
        type = Type.EPIC;
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(List<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtasksIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtasksIds.remove(subtaskId);
    }

    public List<Integer> clearSubtasksIds() {
        List<Integer> out = new ArrayList<>(subtasksIds);
        subtasksIds.clear();
        return out;
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
