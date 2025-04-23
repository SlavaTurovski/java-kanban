package server.handler;

import com.google.gson.Gson;
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
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":

                if (pathParts.length == 3) {
                    getTaskById(exchange, pathParts);
                }

                if (pathParts.length == 2) {
                    getAllTasks(exchange);
                }
                break;

            case "POST":

                if (pathParts.length == 2) {
                    postUpdateTask(exchange);
                }
                break;

            case "DELETE":
                deleteTaskById(exchange, pathParts);
                break;

            default:
                sendBadRequest(exchange);
                break;
        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getAllTasks();
            String response = gson.toJson(tasks);
            writeResponse(exchange, "Задачи: " + "\n" + response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void getTaskById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTaskById(id);
            String response = gson.toJson(task);
            writeResponse(exchange, "Задача с id: " + "\n" + response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    public void postUpdateTask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);

            if (task.getId() == null || task.getId() == 0) {
                taskManager.addTask(task);
                writeResponse(exchange, "Задача успешно добавлена! Её id =  " + task.getId(), 201);
            } else {

                if (taskManager.getTaskById(task.getId()) == null) {
                    sendNotFound(exchange);
                } else {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача с id =  " + task.getId() + " успешно обновлена!", 201);
                }
            }

        } catch (TaskTimeOverlapException e) {
            sendTimeOverlap(exchange);

        } catch (Exception e) {
        sendNotFound(exchange);
        }
    }

    public void deleteTaskById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTaskById(id);
            taskManager.deleteTaskById(task.getId());
            writeResponse(exchange, "Задача c id " + id + " успешно удалена!", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

}