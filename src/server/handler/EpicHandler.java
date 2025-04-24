package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":

                if (pathParts.length == 3) {
                    getEpicById(exchange, pathParts);
                }

                if (pathParts.length == 2) {
                    getAllEpics(exchange);
                }

                if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                    getEpicSubtasksById(exchange, pathParts);
                }
                break;

            case "POST":

                if (pathParts.length == 2) {
                    postEpic(exchange);
                }
                break;

            case "DELETE":
                deleteEpicById(exchange, pathParts);
                break;

            default:
                sendBadRequest(exchange);
                break;
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        try {
            List<Epic> epic = taskManager.getAllEpics();
            String response = gson.toJson(epic);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void getEpicById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpicById(id);
            String response = gson.toJson(epic);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void getEpicSubtasksById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpicById(id);
            List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());
            String response = gson.toJson(subtasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void postEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);

            if (epic.getId() == null || epic.getId() == 0) {
                taskManager.addEpic(epic);
                writeResponse(exchange, "Эпик успешно добавлен! Его id = " + epic.getId(), 201);
            } else {
                sendNotFound(exchange);
            }

        } catch (Exception exp) {
            sendNotFound(exchange);
        }
    }

    private void deleteEpicById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpicById(id);
            taskManager.deleteEpicById(epic.getId());
            writeResponse(exchange, "Эпик c id " + id + " успешно удален!", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

}