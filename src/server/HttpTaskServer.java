package server;

import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import manager.Managers;
import server.handler.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final Integer PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void startServer() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
    }

    public void stopServer() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    public static TaskManager setUp() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.DONE,
                Duration.ofMinutes(10), LocalDateTime.of(2222, 2, 2, 1, 0), epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.of(2222, 2, 2, 2, 0), epic1.getId());
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2222, 2, 22, 3, 0));
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW,
                Duration.ofMinutes(40), LocalDateTime.of(2222, 2, 22, 4, 0));
        Task task3 = new Task("Задача-3", "Описание задачи-3", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.of(2222, 2, 22, 5, 0));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        return taskManager;
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager1 = setUp();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager1);
        httpTaskServer.startServer();
    }

}