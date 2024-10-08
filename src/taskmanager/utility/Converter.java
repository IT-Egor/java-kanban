package taskmanager.utility;

import taskmanager.exceptions.TaskConversionException;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converter {
    public static String anyTaskToCSVLine(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getType()).append(",");
        builder.append(task.getId()).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getDescription()).append(",");
        builder.append(task.getStatus()).append(",");

        builder.append(task.getStartTime()
                .map(DateTimeFormatter.ISO_LOCAL_DATE_TIME::format)
                .orElse("null")).append(",");

        builder.append(task.getDuration()
                .map(duration -> String.valueOf(duration.toMinutes()))
                .orElse("null")).append(",");

        if (task.getType() == Type.EPIC) {
            builder.append(task.getEndTime()
                    .map(DateTimeFormatter.ISO_LOCAL_DATE_TIME::format)
                    .orElse("null")).append(",");
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
                ((Epic) task).setEndTime(LocalDateTime.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(values[7])));
            }
        } else if (values[0].equals("SUBTASK")) {
            task = new Subtask(values[2], values[3]);
            ((Subtask) task).setContainingEpicId(Integer.parseInt(values[7]));
        } else {
            throw new TaskConversionException("Unknown task type: " + values[0]);
        }

        task.setId(Integer.parseInt(values[1]));
        task.setStatus(Status.valueOf(values[4]));
        if (values[5].equals("null")) {
            task.setStartTime(null);
        } else {
            task.setStartTime(LocalDateTime.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(values[5])));
        }
        if (values[6].equals("null")) {
            task.setDuration(null);
        } else {
            task.setDuration(Duration.ofMinutes(Long.parseLong(values[6])));
        }
        return task;
    }
}
