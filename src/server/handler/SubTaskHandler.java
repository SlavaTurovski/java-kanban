package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import manager.TaskTimeOverlapException;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public SubTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":

                if (pathParts.length == 3) {
                    getSubtaskById(exchange, pathParts);
                }

                if (pathParts.length == 2) {
                    getAllSubtasks(exchange);
                }
                break;

            case "POST":

                if (pathParts.length == 2) {
                    postUpdateSubtasks(exchange);
                }
                break;

            case "DELETE":
                deleteSubtaskById(exchange, pathParts);
                break;

            default:
                sendBadRequest(exchange);
                break;
        }
    }

    private void getAllSubtasks(HttpExchange exchange) throws IOException {
        try {
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            String response = gson.toJson(subtasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void getSubtaskById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Subtask subtask = taskManager.getSubtaskById(id);
            String response = gson.toJson(subtask);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void postUpdateSubtasks(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);

            if (subtask.getId() == null || subtask.getId() == 0) {
                taskManager.addSubtask(subtask);
                writeResponse(exchange, "Эпик успешно добавлен! Его id = " + subtask.getId(),201);
            } else {

                if (taskManager.getEpicById(subtask.getId()) == null) {
                    sendNotFound(exchange);
                } else {
                    taskManager.updateSubtask(subtask);
                    writeResponse(exchange, "Эпик с id = " + subtask.getId() + " успешно обновлен!", 201);
                }
            }

        } catch (TaskTimeOverlapException e) {
            sendTimeOverlap(exchange);

        } catch (Exception exp) {
            sendNotFound(exchange);
        }
    }

    private void deleteSubtaskById(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Subtask subtask= taskManager.getSubtaskById(id);
            taskManager.deleteSubtaskById(subtask.getId());
            writeResponse(exchange, "Подзадача c id " + id + " успешно удалена!", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

}