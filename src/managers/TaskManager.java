package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    List<Task> getAllTasks();

    Subtask getSubtask(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    void getSubtasksOfEpic(int epicId);

    void getSubtasksOfEpic(Epic epic);

    boolean containTask(int id);

    boolean containEpic(int id);

    boolean containSubtask(int id);

    List<Task> getHistory();

}
