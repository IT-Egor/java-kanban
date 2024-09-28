package taskmanager.tasktypes;

import taskmanager.utility.Type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("Epic{name='%s', description='%s', id=%d, status=%s, subtasks.size=%d," +
                " startTime='%s', endTime='%s', duration='%s'}",
                name, description, id, status, subtasksIds.size(),
                getStartTime()
                        .map(startTime -> startTime.format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm:ss")))
                        .orElse("null"),
                getEndTime()
                        .map(endTime -> endTime.format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm:ss")))
                        .orElse("null"),
                getDuration().map(duration1 -> duration.toMinutes() + "m").orElse("null"));
    }
}
