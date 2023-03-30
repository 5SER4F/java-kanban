package managers;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    static FileBackedTasksManager setFile() {
        File file = new File("historyTest.csv");
        return Managers.loadFromFile(file);
    }

    FileBackedTasksManagerTest() {
        super(() -> setFile());
    }

    @AfterEach
    public void deletHistoryTest() throws IOException, NoSuchFileException {
        if(Files.exists(Path.of("historyTest.csv")))
            Files.delete(Path.of("historyTest.csv"));
    }

    @Test
    public void emptyManagerAndHistoryMustCreateEmptyFile() throws IOException {
        manager.save();
        assertEquals(0, Files.size(Path.of("historyTest.csv")));
    }

    @Test
    public void loadAndSaveWithoutChangeMustBeSame() throws IOException {
        //manager.setStorage("historyTest0.csv");
        FileBackedTasksManager fileBackedTasksManager = Managers.loadFromFile(new File("historyTest0.csv"));
        fileBackedTasksManager.init();
        fileBackedTasksManager.save();
        fileBackedTasksManager.setStorage("newHistoryTest.csv");
        fileBackedTasksManager.save();


        assertTrue(Files.readString(Path.of("historyTest0.csv")).equals(
                Files.readString(Path.of("newHistoryTest.csv"))));
        Files.delete(Path.of("newHistoryTest.csv"));
    }

    @Test
    public void loadFromFileWithEmptyHistoryMustReturnEmptyHistoryManager() {
        FileBackedTasksManager fileBackedTasksManager = Managers.loadFromFile(
                new File("historyEmptyHistoryTest.csv"));
        assertEquals(0, fileBackedTasksManager.getInMemoryHistoryManager().getHistory().size());
    }
}