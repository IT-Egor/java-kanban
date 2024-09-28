package taskmanager.exceptions;

public class TasksOverlapsInTimeException extends RuntimeException {
    public TasksOverlapsInTimeException(String message) {
        super(message);
    }
}
