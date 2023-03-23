package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static Epic epic;

    @BeforeEach
    public void newEpic() {
        epic = new Epic("Эпик", "Описание эпика 1");
    }

    @Test
    public void epicWithNoSubTaskMustBeNew() {
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicWithAllNewSubTaskMustBeNew() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1",
                Status.NEW, epic.getId(), 3, Instant.now());
        epic.checkStatus(List.of(subtask));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicWithAllDoneSubTaskMustBeDone() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1",
                Status.DONE, epic.getId(), 3, Instant.now());
        epic.checkStatus(List.of(subtask));
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void epicWithNewAndDoneSubTaskMustInProgress() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",
                Status.NEW, epic.getId(), 3, Instant.now());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",
                Status.DONE, epic.getId(), 3, Instant.now());
        epic.checkStatus(List.of(subtask1, subtask2));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void epicWithInProgressSubTaskMustInProgress() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",
                Status.IN_PROGRESS, epic.getId(), 3, Instant.now());
        epic.checkStatus(List.of(subtask1));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",
                Status.NEW, epic.getId(), 3, Instant.now());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3",
                Status.DONE, epic.getId(), 3, Instant.now());
        epic.checkStatus(List.of(subtask1, subtask2, subtask3));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}