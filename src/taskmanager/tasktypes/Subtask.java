package taskmanager.tasktypes;

import taskmanager.utility.Status;
import taskmanager.utility.Type;

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
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", containingEpicId=" + containingEpicId +
                '}';
    }

    @Override
    public String toCSVLine() {
        return "SUBTASK," + id + "," + name + "," + description + "," + status + "," + containingEpicId + ",";
    }

    public static Subtask fromCSVLine(String csvLine) {
        String[] values = csvLine.split(",");
        Subtask subtask = new Subtask(values[2], values[3]);
        subtask.setId(Integer.parseInt(values[1]));
        subtask.setStatus(Status.valueOf(values[4]));
        subtask.setContainingEpicId(Integer.parseInt(values[5]));
        return subtask;
    }
}
