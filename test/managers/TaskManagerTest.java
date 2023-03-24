package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.*;

import exceptions.IllegalTaskTimeException;
import java.time.Instant;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    T manager;
    Supplier<T> getInstance;

    TaskManagerTest(Supplier<T> supplier) {
        getInstance = supplier;
    }

    @BeforeEach
    public void instNewManager() {
        manager = getInstance.get();
    }



    @Test
    public void getAnyTaskFromEmptyManagerMustBeNull() {
        assertNull(manager.getTask(0));
        assertNull(manager.getSubtask(0));
        assertNull(manager.getEpic(0));
    }

    @Test
    public void getAnyTaskWithWrongIDMustBeNull() {
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        manager.addTask(task1);

        Epic epic = new Epic("Эпик", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);
        manager.addSubtask(subtask1);

        assertNull(manager.getTask(0));
        assertNull(manager.getSubtask(0));
        assertNull(manager.getEpic(0));
    }

    @Test
    public void getSameTaskAsPut() {
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        manager.addTask(task1);

        Epic epic = new Epic("Эпик", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);
        manager.addSubtask(subtask1);

        assertEquals(task1, manager.getTask(task1.getId()));
        assertEquals(subtask1, manager.getSubtask(subtask1.getId()));
        assertEquals(epic, manager.getEpic(epic.getId()));

    }

    @Test
    public void getSubTaskOfEpic() {
        Instant i = Instant.now();

        Epic epic = new Epic("Эпик", "Описание эпика 1");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);
        manager.addSubtask(subtask1);

        assertEquals(subtask1, manager.getSubtask(epic.getMySubtasksId().get(0)));
    }

    @Test void intersectionThrowIllegalArgumentException() {

        final IllegalTaskTimeException exception = assertThrows(
                IllegalTaskTimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Instant i = Instant.now();
                        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,60 ,i);
                        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW,10 ,i.plusSeconds(20));
                        manager.addTask(task1);
                        manager.addTask(task2);
                    }
                });
    }


}