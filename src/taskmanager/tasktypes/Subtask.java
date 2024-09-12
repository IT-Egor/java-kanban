package taskmanager.tasktypes;

public class Subtask extends Task {
    protected int containingEpicId;

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, int containingEpicId) {
        super(name, description);
        this.containingEpicId = containingEpicId;
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
    public String toCSV() {
        return id + ",SUBTASK," + name + "," + description + "," + status + "," + containingEpicId + ",";
    }
}
