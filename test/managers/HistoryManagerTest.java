package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

abstract class HistoryManagerTest <T extends HistoryManager> {

    T historyManager;

    Supplier<T> getInstance;

    HistoryManagerTest(Supplier<T> getInstance) {
        this.getInstance = getInstance;
    }

    @BeforeEach
    public void newHistoryManager() {
        historyManager = getInstance.get();
        ArrayList<Task> lst = new ArrayList<>();

    }

    @Test
    public void emptyHistoryMustReturnEmptyList() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void deletFirst() {
        List<Task> lst = new LinkedList<>();
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        Epic epic = new Epic("Эпик", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);

        lst.add(0, task1);
        lst.add(0, epic);

        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(subtask1.getId());

        assertEquals(lst, historyManager.getHistory());

    }

    @Test
    public void deletMidl() {
        List<Task> lst = new LinkedList<>();
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        Epic epic = new Epic("Эпик", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);

        lst.add(0, task1);
        lst.add(0, subtask1);

        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(epic.getId());
        assertEquals(lst, historyManager.getHistory());
    }

    @Test
    public void deletLast() {
        List<Task> lst = new LinkedList<>();
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        Epic epic = new Epic("Эпик", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);

        lst.add(0, epic);
        lst.add(0, subtask1);

        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(task1.getId());

        assertEquals(lst, historyManager.getHistory());
    }

    @Test
    public void historyMustWorkLikeLIFO() {
        List<Task> lst = new LinkedList<>();
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        Epic epic = new Epic("Эпик", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);

        lst.add(0, task1);
        lst.add(0, epic);
        lst.add(0, subtask1);

        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask1);

        assertEquals(lst, historyManager.getHistory());
    }

    @Test
    public void lastDuplicateMustBeDeleted() {
        List<Task> lst = new LinkedList<>();
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        Epic epic = new Epic("Эпик", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);

        lst.add(0, task1);
        lst.add(0, epic);
        lst.add(0, subtask1);

        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask1);

        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask1);

        assertEquals(lst, historyManager.getHistory());
    }

}