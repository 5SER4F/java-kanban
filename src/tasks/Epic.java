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
        boolean isHaveDone = false;
        boolean isHaveNew = false;
        boolean isHaveInProgress = false;
        for (Subtask subtask : subtasks) {
            isHaveNew = Status.NEW.equals(subtask.status) ? true : isHaveNew;
            isHaveDone = Status.DONE.equals(subtask.status) ? true : isHaveDone;
            isHaveInProgress = Status.IN_PROGRESS.equals((subtask.status)) ? true : isHaveInProgress;
        }
        if (subtasks.isEmpty() || (isHaveNew & !isHaveInProgress & !isHaveDone)) {
            status = Status.NEW;
        } else if (isHaveInProgress || (isHaveNew & isHaveDone)) {
            status = Status.IN_PROGRESS;
        } else if (isHaveDone) {
            status = Status.DONE;
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
