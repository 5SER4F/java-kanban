package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    @Override
    public void printTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Задач нет.");
        }
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Override
    public void printEpics() {
        if (epics.isEmpty()) {
            System.out.println("Эпиков нет.");
        }
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Override
    public void printSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("Подзадач нет.");
        }
        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            return null;
        }
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            return null;
        }
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            System.out.println("Такая задача уже есть.");
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            System.out.println("Такой эпик уже есть.");
            return;
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            System.out.println("Такая подзадача уже есть.");
            return;
        }
        epics.get(subtask.getMyEpicId()).addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Нельзя обновить несуществующую задачу.");
            return;
        }
        if (tasks.get(task.getId()).equals(task)) {
            System.out.println("Данная задача уже имеет такие параметры.");
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Нельзя обновить несуществующий эпик.");
            return;
        }
        if (epics.get(epic.getId()).equals(epic)) {
            System.out.println("Данный эпик уже имеет такие параметры.");
            return;
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Нельзя обновить несуществующую подзадачу.");
            return;
        }
        if (subtasks.get(subtask.getId()).equals(subtask)) {
            System.out.println("Данная подзадача уже имеет такие параметры.");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        checkEpicStatus(subtask.getMyEpicId());
    }

    @Override
    public void removeTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи нет.");
            return;
        }
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Такого эпика нет.");
            return;
        }
        ArrayList<Integer> subtasksToRemove = (ArrayList<Integer>) epics.get(id).getMySubtasksId();
        epics.remove(id);
        inMemoryHistoryManager.remove(id);
        for (Integer idToRemove : subtasksToRemove) {
            subtasks.remove(idToRemove);
            inMemoryHistoryManager.remove(idToRemove);
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Такой подзадачи нет.");
            return;
        }
        epics.get(subtasks.get(id).getMyEpicId()).removeSubtask(id);
        checkEpicStatus(subtasks.get(id).getMyEpicId());
        subtasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void getSubtasksOfEpic(int epicId) {
        if (epics.get(epicId).getMySubtasksId().isEmpty()) {
            System.out.println("У эпика с id:" + epicId + " нет подзадач.");
            return;
        }
        System.out.println("В подзадачи эпика с id:" + epicId + " входят следующие подзадачи:");
        for (int subtaskId : epics.get(epicId).getMySubtasksId()) {
            System.out.println(subtasks.get(subtaskId));
        }
    }

    @Override
    public void getSubtasksOfEpic(Epic epic) {
        getSubtasksOfEpic(epic.getId());
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    private void checkEpicStatus(int epicId) {
        Epic epicToCheck = epics.get(epicId);
        ArrayList<Subtask> subtasksToCheck = new ArrayList<>();
        for (int subtaskId : epicToCheck.getMySubtasksId()) {
            subtasksToCheck.add(subtasks.get(subtaskId));
        }
        epicToCheck.checkStatus(subtasksToCheck);
    }

}
