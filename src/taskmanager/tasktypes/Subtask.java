package taskmanager.tasktypes;

import taskmanager.utility.Type;

import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    protected int containingEpicId;

    public Subtask(String name, String description) {
        super(name, description);
        type = Type.SUBTASK;
    }

    public Subtask(String name, String description, int containingEpicId) {
        super(name, description);
        this.containingEpicId = containingEpicId;
        type = Type.SUBTASK;
    }

    public int getContainingEpicId() {
        return containingEpicId;
    }

    public void setContainingEpicId(int containingEpicId) {
        this.containingEpicId = containingEpicId;
    }

    @Override
    public String toString() {
        return String.format("Subtask{name='%s', description='%s', id=%d, status=%s, " +
                        "containingEpicId=%d, startTime='%s', endTime='%s', duration='%s'}",
                name, description, id, status, containingEpicId,
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
