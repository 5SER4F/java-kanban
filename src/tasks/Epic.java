package tasks;

import java.time.Instant;
import java.util.*;

public class Epic extends Task{
    private List<Integer> mySubtasksId;
    private Instant endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW, TaskType.EPIC, 0, Instant.MAX);
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
        boolean isDone = true;
        boolean isHaveOneDone = false;

        for (Subtask subtask : subtasks) {
            if (subtasks.isEmpty()) {
                status = Status.NEW;
                break;
            }
            if (subtask.status == Status.NEW) {
                isDone = false;
                continue;
            }
            if (subtask.status == Status.IN_PROGRESS) {
                isDone = false;
                isHaveOneDone = true;
                break;
            }
            if (subtask.status == Status.DONE)
                isHaveOneDone = true;
        }
        if (isDone) {
            status = Status.DONE;
        }
        else {
            if (isHaveOneDone) {
                status = Status.IN_PROGRESS;
            } else {
                status = Status.NEW;
            }
        }

        duration = subtasks.stream().mapToInt(subtask -> subtask.getDuration()).sum();
        OptionalLong optionalStartTimeMili = subtasks.stream()
                .mapToLong(subtaskk -> subtaskk.getStartTime().toEpochMilli())
                .min();
        if (optionalStartTimeMili.isPresent()) {
            startTime = Instant.ofEpochMilli(optionalStartTimeMili.getAsLong());
        }
        endTime = super.getEndTime();
    }

    public List<Integer> getMySubtasksId() {
        return mySubtasksId;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return super.toString() + ", countOfSubtask=" + mySubtasksId.size();
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
