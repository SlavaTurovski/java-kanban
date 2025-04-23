package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import manager.TaskTimeOverlapException;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        switch (method) {
            case "GET":
                getTasks(exchange, pathParts);
                break;
            case "POST":
                postTask(exchange);
                break;
            case "DELETE":
                deleteTaskById(exchange, pathParts);
                break;
            default:
                sendBadRequest(exchange);
                break;
        }
    }

    public void getTasks(HttpExchange exchange, String[] pathParts) throws IOException {
        try {

            if (pathParts.length == 2) {
                List<Task> tasks = taskManager.getAllTasks();
                String response = gson.toJson(tasks);
                writeResponse(exchange, "Все задачи: " + "\n" + response, 200);

            } else if (pathParts.length >= 3) {
                int id = Integer.parseInt(pathParts[2]);
                Task task = taskManager.getTaskById(id);

                if (task != null) {
                    String response = gson.toJson(task);
                    writeResponse(exchange, "Задача с id = " + id + "\n" + response, 200);
                } else {
                    sendNotFound(exchange);
                }

            } else {
                sendBadRequest(exchange);
            }

        } catch (IllegalArgumentException e) {
            sendBadRequest(exchange);
        }

    }

    public void postTask(HttpExchange exchange) throws IOException {

        InputStream inputStream = exchange.getRequestBody();
        String stringTask = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        if (stringTask.isEmpty() || stringTask.isBlank()) {
            sendNotFound(exchange);
        }

        try {
            Task task = gson.fromJson(stringTask, Task.class);
            try {
                if (task.getId() == 0) {
                    taskManager.addTask(task);
                    writeResponse(exchange, "Задача успешно добавлена! Присвоен id " + task.getId(), 201);
                } else {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача с id  " + task.getId() + " успешно обновлена!", 201);
                }
            } catch (TaskTimeOverlapException e) {
                sendTimeOverlap(exchange);
            } catch (IllegalArgumentException e) {
                sendBadRequest(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange);
        }

    }

    public void deleteTaskById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteTaskById(id);
            writeResponse(exchange, "Задача c id " + id + " успешно удалена!", 200);
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

}