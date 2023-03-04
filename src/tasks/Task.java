package tasks;

import java.util.Objects;

public class Task {
    protected int id;
    private TaskType type = TaskType.TASK;
    protected String name;
    protected Status status;
    protected String description;




    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        id = Objects.hash(name, description);
    }

    public Task(String name, String description, Status status, TaskType type) {
        this.name = name;
        this.description = description;
        this.status = status;
        id = Objects.hash(name, description);
        this.type = type;
    }

    public Task(Task o) {
        this.name = o.name;
        this.description = o.description;
        this.id = o.id;
        this.status = o.status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return  "id=" + id + ", type=" + type + ", name=" + name +  ", status=" + status + ", description="
                + description;
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

