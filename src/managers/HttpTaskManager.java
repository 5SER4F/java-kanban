package managers;

import server.KVTaskClient;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient taskClient;
    private String key;

    public HttpTaskManager(KVTaskClient taskClient, String key) {
        this.taskClient = taskClient;
        this.key = key;
    }

    public KVTaskClient getTaskClient() {
        return taskClient;
    }

    public String getKey() {
        return key;
    }

    @Override
    public void init() {
        if (key == null || key.isBlank()) {
            System.out.println("Не установлен ключ для загрузги с сервера.");
        }
        super.loadHistory(taskClient.load(key));
    }

    @Override
    protected void save() {
        StringBuilder data = new StringBuilder();
        data.append("id,type,name,status,description,epic");
        data.append(System.lineSeparator());
        super.getAllTasks().stream().forEach(task -> data.append(taskToSave(task) + System.lineSeparator()));
        data.append(HistoryManager.historyToString(super.getInMemoryHistoryManager()));
        taskClient.put(key, data.toString());
    }

}
