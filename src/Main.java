public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.DONE);
//        taskManager.addTask(task1);
//        taskManager.addTask(task2);
//        taskManager.printTasks();
//        taskManager.removeTask(task2.getId());
//        taskManager.printTasks();

        Epic epic = new Epic("Эпик", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask1);

//        taskManager.printEpics();
//        taskManager.getSubtasksOfEpic(epic);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask2);

//        taskManager.printEpics();
//        taskManager.getSubtasksOfEpic(epic);

        taskManager.removeSubtask(subtask2.getId());

        taskManager.printEpics();
        taskManager.getSubtasksOfEpic(epic);

        taskManager.removeEpic(epic.getId());

        taskManager.printEpics();
        taskManager.printSubtasks();

    }
}
