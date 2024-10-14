package taskmanager.utility;

import taskmanager.exceptions.TaskConversionException;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

public class Converter {
    public static String anyTaskToCSVLine(Task task) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(String.valueOf(task.getType()));
        joiner.add(String.valueOf(task.getId()));
        joiner.add(task.getName());
        joiner.add(task.getDescription());
        joiner.add(String.valueOf(task.getStatus()));

        joiner.add(task.getStartTime()
                .map(DateTimeFormatter.ISO_LOCAL_DATE_TIME::format)
                .orElse("null"));

        joiner.add(task.getDuration()
                .map(duration -> String.valueOf(duration.toMinutes()))
                .orElse("null"));

        if (task.getType() == Type.EPIC) {
            joiner.add(task.getEndTime()
                    .map(DateTimeFormatter.ISO_LOCAL_DATE_TIME::format)
                    .orElse("null"));
        }

        if (task.getType() == Type.SUBTASK) {
            joiner.add(String.format(",%d", ((Subtask) task).getContainingEpicId()));
        }
        joiner.add("\n");

        return joiner.toString();
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
            ((Subtask) task).setContainingEpicId(Integer.parseInt(values[8]));
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
