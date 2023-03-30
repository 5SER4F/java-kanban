package server;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import server.handlers.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int port = 8080;
    private HttpServer httpServer;
    TaskManager manager;

    public HttpTaskServer() {
        manager = Managers.loadFromFile(new File("history.csv"));
    }

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }


    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext("/tasks/task", new TaskHandler(manager));
            httpServer.createContext("/tasks/subtask", new SubtaskHandler(manager));
            httpServer.createContext("/tasks/epic", new EpicHandler(manager));
            httpServer.createContext("/tasks/", new AllTaskHandler(manager));
            httpServer.createContext("/tasks/history", new historyHandler(manager));
            httpServer.start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить Http сервер");
        }
    }

    public void stop() {
        httpServer.stop(0);
    }


}
