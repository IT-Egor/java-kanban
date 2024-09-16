package taskmanager.exceptions;

public class TaskTypeDoesNotExistException extends RuntimeException {
    public TaskTypeDoesNotExistException(String message) {
        super(message);
    }
}
