package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import server.KVTaskClient;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    public static final String KEY = "key";
    KVServer server;
    HttpTaskManager manager;
    HttpClient client;
    KVTaskClient kvTaskClient;


    @BeforeEach
    public void setEnvironment() {
        try {
            server = new KVServer();
            server.start();
        } catch (IOException e) {
            System.out.println("При запуске тестового сервера поизошла ошибка");
            e.printStackTrace();
        }
        kvTaskClient = new KVTaskClient("http://localhost:8080", HttpClient.newHttpClient());
        manager = new HttpTaskManager(kvTaskClient, KEY);

    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void loadEmpty() {
        manager.save();
        String str = kvTaskClient.load(KEY);
        assertEquals("id,type,name,status,description,epic"+ System.lineSeparator(), str);
    }

    @Test
    public void addTask() {
        Instant i = Instant.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW,1 ,i.plusSeconds(2000));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTask(task1.getId());
        manager.save();
        HttpTaskManager newManager = new HttpTaskManager(kvTaskClient, KEY);
        newManager.init();
        assertEquals(task1, newManager.getTask(task1.getId()));
        assertEquals(task1, newManager.getHistory().get(0));

    }
}