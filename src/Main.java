import tasks.*;
import taskManager.*;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.DONE);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
//        taskManager.printTasks();
//        taskManager.removeTask(task2.getId());
//        taskManager.printTasks();

        Epic epic = new Epic("Эпик", "Описание эпика 1");
        inMemoryTaskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic.getId());
        inMemoryTaskManager.addSubtask(subtask1);

//        taskManager.printEpics();
//        taskManager.getSubtasksOfEpic(epic);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.addSubtask(subtask2);

//        taskManager.printEpics();
//        taskManager.getSubtasksOfEpic(epic);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubtask(subtask1.getId());
        inMemoryTaskManager.getSubtask(subtask2.getId());
        
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

    }
}
