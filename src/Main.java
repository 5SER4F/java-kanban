import tasks.*;
import managers.*;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.DONE);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());



        Epic epic = new Epic("Эпик", "Описание эпика 1");
        inMemoryTaskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic.getId());
        inMemoryTaskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 2", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.addSubtask(subtask3);

        //обращаемся к здачама в порядке их создания
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubtask(subtask1.getId());
        inMemoryTaskManager.getSubtask(subtask2.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getTask(task2.getId());//еще раз обращаемся к таску2, он выйдет первый в истроии

        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        // удаляем эпик
        inMemoryTaskManager.removeEpic(epic.getId());
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
