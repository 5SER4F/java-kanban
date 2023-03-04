package managers;

import tasks.Task;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

    public static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getId() + ",");
        }
        return history.toString();
    }

    public static List<String> historyFromString(String value) {
        List<String> history = Arrays.asList(value.split(","));
        Collections.reverse(history);
        return history;
    }

}
