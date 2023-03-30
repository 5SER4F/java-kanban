package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class SubtaskHandler implements HttpHandler {
    final TaskManager manager;
    Gson gson = new Gson();

    public SubtaskHandler(TaskManager manager) {
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
        String query = exchange.getRequestURI().getQuery();
        if (query.substring(query.indexOf("?") + 1).length() <= "id=".length()) {
            exchange.sendResponseHeaders(404, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("GET запрос должен содержать id.".getBytes());
            }
            return;
        }

        int id = Integer.parseInt(query.substring(query.indexOf("=") + 1));
        exchange.sendResponseHeaders(201, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(gson.toJson(manager.getSubtask(id), Subtask.class).getBytes());
        }
    }

    private void handlePOST(HttpExchange exchange) throws IOException{
        JsonElement taskAsJE = JsonParser.parseString(new String(exchange.getRequestBody().readAllBytes(),
                Charset.defaultCharset()));
        if (!taskAsJE.isJsonObject()) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
            return;
        }
        Subtask task = gson.fromJson(taskAsJE.getAsJsonObject(), Subtask.class);
        exchange.sendResponseHeaders(201, 0);
        if (manager.containSubtask(task.getId())) {
            manager.updateSubtask(task);
        } else {
            manager.addSubtask(task);
        }
    }

    private void handleDELET(HttpExchange exchange) throws IOException{
        String query = exchange.getRequestURI().getQuery();
        try (OutputStream os = exchange.getResponseBody()) {
            if (query.substring(query.indexOf("?") + 1).length() <= "id=".length()) {
                exchange.sendResponseHeaders(404, 0);
                os.write("DELET запрос должен содержать id.".getBytes());
                return;
            }
            int id = Integer.parseInt(query.substring(query.indexOf("=") + 1));
            if (!manager.containSubtask(id)) {
                exchange.sendResponseHeaders(404, 0);
                os.write("Такого объекта не существует.".getBytes());
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            manager.removeSubtask(id);
        }
    }


}
