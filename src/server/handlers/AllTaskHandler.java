package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.OutputStream;

public class AllTaskHandler implements HttpHandler {
    final TaskManager manager;
    Gson gson = new Gson();

    public AllTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGET(exchange);
                break;
            case "POST":
                handlePOST(exchange);
                break;
            case "DELETE":
                handleDELET(exchange);
                break;
            default:
                exchange.sendResponseHeaders(400, 0);
        }
        exchange.close();
    }

    private void handleGET(HttpExchange exchange) throws IOException{
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(gson.toJson(manager.getAllTasks()).getBytes());
        }
    }

    private void handlePOST(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(400, 0);
    }

    private void handleDELET(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        manager.clearEpics();
        manager.clearTasks();
        manager.clearSubtasks();
    }
}
