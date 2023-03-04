package managers;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Managers {

    private Managers() {
        throw new IllegalStateException("Utility class");
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
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
