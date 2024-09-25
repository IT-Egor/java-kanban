package taskmanager.tasktypes;

import taskmanager.utility.Type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtasksIds;
    protected LocalDateTime endTime;

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
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

//    @Override
//    public String toString() {
//        return "Epic{" +
//                "name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", id=" + id +
//                ", status=" + status +
//                ", subtasks.size=" + subtasksIds.size() +
//                '}';
//    }

    @Override
    public String toString() {
        String startTimeString;
        String endTimeString;
        String durationString;
        if (startTime == null) {
            startTimeString = "null";
        } else {
            startTimeString = startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }
        if (endTime == null) {
            endTimeString = "null";
        } else {
            endTimeString = endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }
        if (duration == null) {
            durationString = "null";
        } else {
            durationString = Long.toString(duration.toMinutes()) + "m'";
        }

        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtasks.size=" + subtasksIds.size() +
                ", startTime='" + startTimeString + '\'' +
                ", endTime='" + endTimeString + '\'' +
                ", duration='" + durationString +
                '}';
    }
}
