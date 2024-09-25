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

//    @Override
//    public String toString() {
//        return "Subtask{" +
//                "name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", id=" + id +
//                ", status=" + status +
//                ", containingEpicId=" + containingEpicId +
//                '}';
//    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", containingEpicId=" + containingEpicId +
                ", startTime='" + startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + '\'' +
                ", endTime='" + getEndTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + '\'' +
                ", duration=" + duration.toMinutes() + "m'" +
                '}';
    }
}
