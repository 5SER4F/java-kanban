package managers;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File storage;

    public FileBackedTasksManager(File storage) {
        this.storage = storage;
        loadHistory();
    }

    public void setStorage(String newPath) throws IOException{
        storage = new File(newPath);
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void getSubtasksOfEpic(int epicId) {
        super.getSubtasksOfEpic(epicId);
        save();
    }

    @Override
    public void getSubtasksOfEpic(Epic epic) {
        super.getSubtasksOfEpic(epic);
        save();
    }

     protected void save() throws ManagerSaveException {
        if (getPrioritizedTasks().isEmpty() && getInMemoryHistoryManager().getHistory().isEmpty()) {
            try {
                storage.createNewFile();
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storage, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            for (Task task : super.getAllId()) {
                writer.write(taskToSave(task));
                writer.newLine();
            }
            writer.newLine();//разделяет таски и историю
            writer.write(HistoryManager.historyToString(super.getInMemoryHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private String taskToSave(Task task) {
        String[] splitedTask = task.toString().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String field : splitedTask) {
            stringBuilder.append(field.substring(field.indexOf('=') + 1) + ",");
        }
        return stringBuilder.toString();
    }

    private void loadHistory() {
        if (!storage.canRead() || storage.length() <= 40)
            return;
        try {
            String content = Files.readString(Path.of(storage.getPath()), StandardCharsets.UTF_8);
            String[] lines = content.split(System.lineSeparator());

            for ( int i = 1; i < lines.length - 2; i++) {
                addTaskFromString(lines[i]);
            }
            if (lines[lines.length - 2].isBlank()) {
                for (String id : HistoryManager.historyFromString(lines[lines.length - 1]))
                    super.addToHistory(Integer.parseInt(id));
            }

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private void addTaskFromString(String taskAsString) {
        String[] fields = taskAsString.split(",");
        if (fields[1].equals(TaskType.TASK.toString())) {
            super.addTask(new Task(fields[2], fields[4], Status.valueOf(fields[3]), Integer.parseInt(fields[5]),
                    Instant.parse(fields[6])));
        }
        if (fields[1].equals(TaskType.EPIC.toString())) {
            super.addEpic(new Epic(fields[2], fields[4]));
        }
        if (fields[1].equals(TaskType.SUBTASK.toString())) {
            super.addSubtask(new Subtask(fields[2], fields[4], Status.valueOf(fields[3]), Integer.parseInt(fields[7]),
                    Integer.parseInt(fields[5]), Instant.parse(fields[6])));
        }
    }


}
