package tasks;

import java.time.Instant;
import java.util.Objects;

public class Task {
    protected int id;
    private TaskType type = TaskType.TASK;
    protected String name;
    protected Status status;
    protected String description;

    protected int duration;

    Instant startTime;

    public Task(String name, String description, Status status, int duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        id = Objects.hash(name, description);
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, TaskType type, int duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        id = Objects.hash(name, description);
        this.type = type;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Task o) {
        this.name = o.name;
        this.description = o.description;
        this.id = o.id;
        this.status = o.status;
        this.duration = o.duration;
        this.startTime = o.startTime;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getEndTime() {
        return startTime.plusSeconds(duration * 60);
    }

    @Override
    public String toString() {
        return  "id=" + id + ", type=" + type + ", name=" + name +  ", status=" + status + ", description="
                + description + ", duration=" + duration + ", startTime=" + startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}

