package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        if (method.equals("GET") && pathParts.length == 2 && pathParts[1].equals("history")) {
            try {
                List<Task> history = taskManager.getHistory();
                if (history == null || history.isEmpty()) {
                    sendNotFound(exchange);
                }
                String response = gson.toJson(history);
                writeResponse(exchange, response, 200);

            } catch (IOException e) {
                sendNotFound(exchange);

            } catch (Exception e) {
                sendBadRequest(exchange);
            }
        }
    }

}