package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{
    private List<Integer> mySubtasksId;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        mySubtasksId = new ArrayList<>();
    }

    public Epic(Epic o) {
        super(o);
        this.mySubtasksId = o.mySubtasksId;
    }
    public void addSubtask(Subtask subtask){
        mySubtasksId.add(subtask.getId());
        if (status == Status.NEW && subtask.status != Status.NEW && !mySubtasksId.isEmpty()
        || status == Status.DONE && subtask.status != Status.DONE) {
            status = Status.IN_PROGRESS;
        }
    }

    public void removeSubtask(int subtaskId){
        mySubtasksId.remove((Integer) subtaskId);
    }

    public void checkStatus(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            status = Status.NEW;
            return;
        }
        boolean isDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.status == Status.NEW) {
                isDone = false;
                continue;
            }
            if (subtask.status == Status.IN_PROGRESS) {
                status = Status.IN_PROGRESS;
                return;
            }
        }
        if (isDone) {
            status = Status.DONE;
        }
        else {
            status = Status.NEW;
        }
    }

    public List<Integer> getMySubtasksId() {
        return mySubtasksId;
    }

    @Override
    public String toString() {
        return "tasks.Epic{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", id=" + id +", status="
                + status + ", countOfSubtask=" + mySubtasksId.size() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(mySubtasksId, epic.mySubtasksId) && super.equals(epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mySubtasksId);
    }
}
