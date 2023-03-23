package managers;

import tasks.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Set<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });
    private final InMemoryHistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().collect(Collectors.toList());
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

     void addToHistory(int id) {
        if (tasks.containsKey(id)) {
            inMemoryHistoryManager.add(tasks.get(id));
        }
        if (epics.containsKey(id)){
            inMemoryHistoryManager.add(epics.get(id));
        }
        if (subtasks.containsKey(id)){
            inMemoryHistoryManager.add(subtasks.get(id));
        }
    }

    @Override
    public void addTask(Task task) {
        if (isIntersection(task)) {
            throw new IllegalArgumentException("На это время уже назначена задача");
        }
        if (tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (isIntersection(epic)) {
            throw new IllegalArgumentException("На это время уже назначена задача");
        }
        if (epics.containsKey(epic.getId())) {
            return;
        }
        epics.put(epic.getId(), epic);
        prioritizedTasks.add(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isIntersection(subtask)) {
            throw new IllegalArgumentException("На это время уже назначена задача");
        }
        if (!epics.containsKey(subtask.getMyEpicId())) {
            throw new IllegalStateException("Эпика для этого сабтаска  не существует");
        }
        if (subtasks.containsKey(subtask.getId())) {
            return;
        }
        epics.get(subtask.getMyEpicId()).addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        checkEpicStatus(subtask.getMyEpicId());
    }

    @Override
    public void updateTask(Task task) {
        if (isIntersection(task)) {
            throw new IllegalArgumentException("На это время уже назначена задача");
        }
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Нельзя обновить несуществующую задачу");
            return;
        }
        if (tasks.get(task.getId()).equals(task)) {
            System.out.println("Данная задача уже имеет такие параметры.");
            return;
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.remove(subtasks.get(task.getId()));
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (isIntersection(epic)) {
            throw new IllegalArgumentException("На это время уже назначена задача");
        }
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Нельзя обновить несуществующий эпик.");
            return;
        }
        if (epics.get(epic.getId()).equals(epic)) {
            System.out.println("Данный эпик уже имеет такие параметры.");
            return;
        }
        epics.put(epic.getId(), epic);
        prioritizedTasks.remove(subtasks.get(epic.getId()));
        prioritizedTasks.add(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (isIntersection(subtask)) {
            throw new IllegalArgumentException("На это время уже назначена задача");
        }
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
        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        prioritizedTasks.add(subtask);
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

    List<Task> getAllId() {
        LinkedList<Task> ids = new LinkedList<>();
        ids.addAll(tasks.values());
        ids.addAll(epics.values());
        ids.addAll(subtasks.values());
        return ids;
    }

    private void checkEpicStatus(int epicId) {
        Epic epicToCheck = epics.get(epicId);
        ArrayList<Subtask> subtasksToCheck = new ArrayList<>();
        for (int subtaskId : epicToCheck.getMySubtasksId()) {
            subtasksToCheck.add(subtasks.get(subtaskId));
        }
        epicToCheck.checkStatus(subtasksToCheck);
    }

    protected boolean isIntersection(Task task) {
        return prioritizedTasks.stream().anyMatch(taskToMatch -> task.getStartTime().isAfter(taskToMatch.getStartTime())
        && task.getStartTime().isBefore(taskToMatch.getStartTime()
                .plusSeconds(taskToMatch.getDuration() * 60)));
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }
}
