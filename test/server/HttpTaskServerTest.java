package server;

import com.google.gson.Gson;
import managers.HttpTaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    static HttpTaskServer server;
    static HttpClient client;
    static String url = "http://localhost:8080/tasks";
    static Instant i;
    Gson gson = new Gson();

    @BeforeAll
    public static void setEnv() {
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
        i = Instant.now();


    }

    @AfterAll
    public static void clearEnv() {
        server.stop();
    }

    @Test
    public void getTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,1 ,i);
        server.manager.addTask(task1);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/task" + "?id=" +task1.getId()))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
        try {
            HttpResponse<String> response = client.send(request, handler);
            Task loadedTask1 = gson.fromJson(response.body(), Task.class);
            assertEquals(task1, loadedTask1);
        } catch (IOException | InterruptedException e) {
            System.out.println("Не удалось отправить запрос");
            e.printStackTrace();
        }
    }

    @Test
    public void getEpicAndSubtask() {
        Epic epic = new Epic("Эпик", "Описание эпика 1");
        server. manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic.getId(), 3, i);
        server. manager.addSubtask(subtask1);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/epic" + "?id=" +epic.getId()))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest requestSub = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/subtask" + "?id=" +subtask1.getId()))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
        try {
            HttpResponse<String> response = client.send(request, handler);
            HttpResponse<String> responseSub = client.send(requestSub, handler);
            assertEquals(epic, gson.fromJson(response.body(), Epic.class));
            assertEquals(subtask1, gson.fromJson(responseSub.body(), Subtask.class));

        }catch (IOException | InterruptedException e) {
            System.out.println("Не удалось отправить запрос");
            e.printStackTrace();
        }
    }


}