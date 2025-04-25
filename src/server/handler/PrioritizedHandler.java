package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        if (method.equals("GET") && pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            try {
                Set<Task> tasks = taskManager.getPrioritizedTasks();
                if (tasks == null || tasks.isEmpty()) {
                    sendNotFound(exchange);
                }
                String response = gson.toJson(tasks);
                writeResponse(exchange, response, 200);

            } catch (IOException e) {
                sendNotFound(exchange);

            } catch (Exception exp) {
                sendBadRequest(exchange);
            }
        }
    }

}