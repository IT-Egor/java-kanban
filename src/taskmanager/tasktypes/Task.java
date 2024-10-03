package taskmanager.tasktypes;

import taskmanager.utility.Status;
import taskmanager.utility.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected Type type;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description) {
        this.description = description;
        this.name = name;
        status = Status.NEW;
        id = 0;
        type = Type.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(startTime)
                .flatMap(start -> Optional.ofNullable(duration)
                        .map(dur -> start.plus(dur)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s', id=%d, status=%s, " +
                        "startTime='%s', endTime='%s', duration='%s'}",
                name, description, id, status,
                getStartTime()
                        .map(DateTimeFormatter.ISO_LOCAL_DATE_TIME::format)
                        .orElse("null"),
                getEndTime()
                        .map(DateTimeFormatter.ISO_LOCAL_DATE_TIME::format)
                        .orElse("null"),
                getDuration().map(duration1 -> duration.toMinutes() + "m").orElse("null"));
    }
}
