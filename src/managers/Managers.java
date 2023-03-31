package managers;

import server.KVTaskClient;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Managers {

    private Managers() {
        throw new IllegalStateException("Utility class");
    }

    public static TaskManager getDefault(KVTaskClient taskClient, String key) {
        return new HttpTaskManager(taskClient, key);
    }

    public static InMemoryTaskManager getInMemoryMananger() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new LinkedList<>();
        String[] splitedHistory = value.split(",");
        for (String intAsString : splitedHistory) {
            history.add(Integer.parseInt(intAsString));
        }
        return history;
    }

}
