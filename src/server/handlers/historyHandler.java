package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

public class historyHandler implements HttpHandler {
    final TaskManager manager;
    Gson gson = new Gson();

    public historyHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            handleGET(exchange);
        } else {
            exchange.sendResponseHeaders(400, 0);
        }
        exchange.close();
    }

    private void handleGET(HttpExchange exchange) throws IOException{
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()){
            os.write(gson.toJson(manager.getHistory()).getBytes());
        }
    }
}
