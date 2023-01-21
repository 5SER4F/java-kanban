import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public void printTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Задач нет.");
        }
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public void printEpics() {
        if (epics.isEmpty()) {
            System.out.println("Эпиков нет.");
        }
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public void printSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("Подзадач нет.");
        }
        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            return null;
        }
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            return null;
        }
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            System.out.println("Такая задача уже есть.");
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            System.out.println("Такой эпик уже есть.");
            return;
        }
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            System.out.println("Такая подзадача уже есть.");
            return;
        }
        epics.get(subtask.getMyEpicId()).addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
    }

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

    public void updateSubtask (Subtask subtask) {
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

    public void removeTask (int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи нет.");
            return;
        }
        tasks.remove(id);
    }

    public void removeEpic (int id) {
        if (!epics.containsKey(id)){
            System.out.println("Такого эпика нет.");
            return;
        }
        ArrayList<Integer> subtasksToRemove = (ArrayList<Integer>) epics.get(id).getMySubtasksId();
        epics.remove(id);
        for (Integer idToRemove : subtasksToRemove) {
            subtasks.remove(idToRemove);
        }
    }

    public void removeSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Такой подзадачи нет.");
            return;
        }

        epics.get(subtasks.get(id).getMyEpicId()).removeSubtask(id);
        checkEpicStatus(subtasks.get(id).getMyEpicId());
        subtasks.remove(id);
    }

    public void getSubtasksOfEpic(int epicId) {
        if (epics.get(epicId).getMySubtasksId().isEmpty()) {
            System.out.println("У эпика с id:" + epicId + " нет подзадач.");
            return;
        }
        System.out.println("В подзадачи эпика с id:" + epicId + " входят следующие подзадачи:");
        for (int subtaskId : epics.get(epicId).getMySubtasksId()){
            System.out.println(subtasks.get(subtaskId));
        }
    }

    public void getSubtasksOfEpic(Epic epic){
        getSubtasksOfEpic(epic.getId());
    }

    private void checkEpicStatus(int epicId) {
        Epic epicToCheck = epics.get(epicId);
        ArrayList<Subtask> subtasksToCheck= new ArrayList<>();
        for (int subtaskId : epicToCheck.getMySubtasksId()) {
            subtasksToCheck.add(subtasks.get(subtaskId));
        }
        epicToCheck.checkStatus(subtasksToCheck);
    }

}
