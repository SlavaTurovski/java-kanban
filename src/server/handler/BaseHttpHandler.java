package server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    public Gson getGson() {
        return gson;
    }

    protected void writeResponse(HttpExchange exchange, String responseText, int responseCode) throws IOException {
        byte[] response = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Задача не найдена!", 404);
    }

    protected void sendBadRequest(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Неверно введен запрос!", 406);
    }

    protected void sendTimeOverlap(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Задача пересекается по времени с существующими!", 406);
    }

}