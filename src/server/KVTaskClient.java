package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    final String apiToken;
    private final String url;
    private final HttpClient client;


    public KVTaskClient(String url, HttpClient client) {
        this.url = url;
        this.client = client;
        apiToken = reg();
    }

    public void put(String key, String json) {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() - 200 > 99) {
                System.out.println("Ошибка, код ответа = " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время отправки произошла ошибка");
        }
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время получения данных произошла ошибка");
        }
        return "";
    }

    private String reg() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/register"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Не удалось провести регистрацию");
            return "";
        }
    }
}
