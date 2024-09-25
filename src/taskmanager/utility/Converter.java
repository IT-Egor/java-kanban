package taskmanager.utility;

import taskmanager.exceptions.TaskConversionException;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Converter {
    public static String anyTaskToCSVLine(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getType()).append(",");
        builder.append(task.getId()).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getDescription()).append(",");
        builder.append(task.getStatus()).append(",");
        if (task.getStartTime() != null) {
            builder.append(task.getStartTime().toEpochSecond(ZoneOffset.UTC)).append(",");
        } else {
            builder.append("null,");
        }
        if (task.getDuration() != null) {
            builder.append(task.getDuration().toSeconds()).append(",");
        } else {
            builder.append("null,");
        }
        if (task.getStartTime() != null && task.getDuration() != null) {
            builder.append(task.getEndTime().toEpochSecond(ZoneOffset.UTC)).append(",");
        } else {
            builder.append("null,");
        }
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
            if (values[7].equals("null")) {
                ((Epic) task).setEndTime(null);
            } else {
                ((Epic) task).setEndTime(LocalDateTime.ofEpochSecond(
                        Long.parseLong(values[7]), 0, ZoneOffset.UTC));
            }
        } else if (values[0].equals("SUBTASK")) {
            task = new Subtask(values[2], values[3]);
            ((Subtask) task).setContainingEpicId(Integer.parseInt(values[8]));
        } else {
            throw new TaskConversionException("Unknown task type: " + values[0]);
        }

        task.setId(Integer.parseInt(values[1]));
        task.setStatus(Status.valueOf(values[4]));
        if (values[5].equals("null")) {
            task.setStartTime(null);
        } else {
            task.setStartTime(LocalDateTime.ofEpochSecond(Long.parseLong(values[5]), 0, ZoneOffset.UTC));
        }
        if (values[6].equals("null")) {
            task.setDuration(null);
        } else {
            task.setDuration(Duration.ofSeconds(Long.parseLong(values[6])));
        }
        return task;
    }
}
