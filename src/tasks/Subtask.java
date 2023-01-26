package tasks;

import java.util.Objects;

public class Subtask extends Task{
    private int myEpicId;

    public Subtask(String name, String description, Status status, int myEpicId) {
        super(name, description, status);
        this.myEpicId = myEpicId;
        id += Objects.hash(myEpicId);
    }

    public Subtask(Subtask o) {
        super((Task) o);
        this.myEpicId = o.myEpicId;
    }

    public int getMyEpicId() {
        return myEpicId;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", id=" + id +
                ", status=" + status + ", myEpicId=" + myEpicId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return myEpicId == subtask.myEpicId && super.equals(subtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), myEpicId);
    }
}
