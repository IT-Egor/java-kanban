package taskmanager.utility;

import taskmanager.exceptions.TaskTypeDoesNotExistException;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

public class Converter {
    public static String anyTaskToCSVLine(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getType()).append(",");
        builder.append(task.getId()).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getDescription()).append(",");
        builder.append(task.getStatus()).append(",");
        if (task.getType() == Type.SUBTASK) {
            builder.append(((Subtask) task).getContainingEpicId()).append(",");
        }
        builder.append("\n");
        return builder.toString();
    }

    public static Task fromCSVLineToAnyTask(String csvLine) {
        String[] values = csvLine.split(",");
        Task task;
        if (values[0].equals("TASK")) {
            task = new Task(values[2], values[3]);
        } else if (values[0].equals("EPIC")) {
            task = new Epic(values[2], values[3]);
        } else if (values[0].equals("SUBTASK")) {
            task = new Subtask(values[2], values[3]);
            ((Subtask) task).setContainingEpicId(Integer.parseInt(values[5]));
        } else {
            throw new TaskTypeDoesNotExistException("Unknown task type: " + values[0]);
        }

        task.setId(Integer.parseInt(values[1]));
        task.setStatus(Status.valueOf(values[4]));
        return task;
    }
}
