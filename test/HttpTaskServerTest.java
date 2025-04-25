package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Subtask subtask1;
    private Subtask subtask2;
    private HttpTaskServer taskServer;
    private HttpClient client;
    private URI uriTasks;
    private URI uriSubtasks;
    private URI uriEpics;
    private URI uriHistory;
    private URI uriPrioritized;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();
        taskServer.startServer();
        client = HttpClient.newHttpClient();
        uriTasks = URI.create("http://localhost:8080/tasks");
        uriSubtasks = URI.create("http://localhost:8080/subtasks");
        uriEpics = URI.create("http://localhost:8080/epics");
        uriHistory = URI.create("http://localhost:8080/history");
        uriPrioritized = URI.create("http://localhost:8080/prioritized");
        task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2222, 1, 1, 1, 1));
        task2 = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2222, 2, 2, 2, 2));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(20),
                LocalDateTime.of(2222, 3, 3, 3, 3), epic1.getId());
        subtask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, Duration.ofMinutes(20),
                LocalDateTime.of(2222, 4, 4, 4, 4), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    @Test
    public void shouldGetTasksEpicsSubtasks() throws IOException, InterruptedException {
        String jsonTasks = gson.toJson(taskManager.getAllTasks());
        String jsonSubtasks = gson.toJson(taskManager.getAllSubtasks());
        String jsonEpics = gson.toJson(taskManager.getAllEpics());
        HttpRequest taskRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(uriTasks)
                .build();
        HttpRequest subtaskRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(uriSubtasks)
                .build();
        HttpRequest epicRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(uriEpics)
                .build();
        HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> subtaskResponse = client.send(subtaskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> epicResponse = client.send(epicRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, taskResponse.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(200, subtaskResponse.statusCode(), "SubtaskHandler вернул неверный код");
        assertEquals(200, epicResponse.statusCode(), "EpicHandler вернул неверный код");
        assertEquals(jsonTasks, taskResponse.body(), "Не совпадает с фактическим Task");
        assertEquals(jsonSubtasks, subtaskResponse.body(), "Не совпадает с фактическим Subtask");
        assertEquals(jsonEpics, epicResponse.body(), "Не совпадает с фактическим Epic");
    }

    @Test
    public void shouldGetTasksEpicsSubtasksById() throws IOException, InterruptedException {
        String jsonTask = gson.toJson(task1);
        String jsonSubtask = gson.toJson(subtask1);
        String jsonEpic = gson.toJson(epic1);
        URI urlTasks = URI.create("http://localhost:8080/tasks/" + task1.getId());
        URI urlSubtasks = URI.create("http://localhost:8080/subtasks/" + subtask1.getId());
        URI urlEpics = URI.create("http://localhost:8080/epics/" + epic1.getId());
        HttpRequest taskRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(urlTasks)
                .build();
        HttpRequest subtaskRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(urlSubtasks)
                .build();
        HttpRequest epicRequest = HttpRequest
                .newBuilder()
                .GET()
                .uri(urlEpics)
                .build();
        HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> subtaskResponse = client.send(subtaskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> epicResponse = client.send(epicRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, taskResponse.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(200, subtaskResponse.statusCode(), "SubtaskHandler вернул неверный код");
        assertEquals(200, epicResponse.statusCode(), "EpicHandler вернул неверный код");
        assertEquals(jsonTask, taskResponse.body(), "Не совпадает с фактическим Task");
        assertEquals(jsonSubtask, subtaskResponse.body(), "Не совпадает с фактическим Subtask");
        assertEquals(jsonEpic, epicResponse.body(), "Не совпадает с фактическим Epic");
    }

    @Test
    public void shouldGetEpicSubtasks() throws IOException, InterruptedException {
        String jsonSubtasks = gson.toJson(taskManager.getSubtasksByEpicId(epic1.getId()));
        URI url = URI.create("http://localhost:8080/epics/" + epic1.getId() + "/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "EpicHandler вернул неверный код");
        assertEquals(jsonSubtasks, response.body(), "Содержимое не совпадает");
    }

    @Test
    public void shouldPostTask() throws IOException, InterruptedException {
        Task newTask = new Task("Новая задача", "Описание новой задачи", Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(3333, 3, 3, 3, 3));
        String taskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriTasks)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(3, taskManager.getAllTasks().size(), "Некорректное количество задач");
    }


    @Test
    public void shouldPostEpic() throws IOException, InterruptedException {
        Epic newEpic = new Epic("Новый эпик", "Описание нового эпика", Status.NEW);
        String taskJson = gson.toJson(newEpic);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriEpics)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "EpicHandler вернул неверный код");
        assertEquals(2, taskManager.getAllEpics().size(), "Некорректное количество эпиков");
    }

    @Test
    public void shouldPostSubtask() throws IOException, InterruptedException {
        Epic newEpic = new Epic("Новый эпик", "Описание нового эпика", Status.NEW);
        taskManager.addEpic(newEpic);
        Task newSubtask = new Subtask("Новая задача", "Описание новой задачи", Status.NEW, Duration.ofMinutes(40),
                LocalDateTime.of(4444, 4, 4, 4, 4), newEpic.getId());
        String taskJson = gson.toJson(newSubtask);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriSubtasks)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "SubtaskHandler вернул неверный код");
        List<Subtask> tasksFromManager = taskManager.getAllSubtasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(newEpic.getSubtaskIdInEpic().size(), 1, "Вернул неверное кол-во подзадач");
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task newTask = new Task("Новая задача", "Новое описание", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(5555, 5, 5, 5, 5));
        newTask.setId(task1.getId());
        String taskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriTasks)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(2, taskManager.getAllTasks().size(), "Вернул неверное кол-во задач");
        assertEquals(newTask, taskManager.getTaskById(task1.getId()), "Задачи не совпадают");
    }

    @Test
    public void shouldReturnTaskOverlapping() throws IOException, InterruptedException {
        Task newTask = new Task("Новая задача", "Пересечение по времени с task1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2222, 1, 1, 1, 1));
        String taskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriTasks)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals("Задача пересекается по времени с существующими!", response.body());
    }


    @Test
    public void shouldNotAddIncorrectTask() throws IOException, InterruptedException {
        String incorrectTask = "\"description\": \"Новое описание\",\"id\": 4, \"status\": \"NEW\"}";
        String taskJson = gson.toJson(incorrectTask);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriTasks)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals("Задача не найдена!", response.body());
    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        int id = task1.getId();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(1, taskManager.getAllTasks().size(), "Вернул неверное кол-во задач");
    }

    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {
        int id = epic1.getId();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(0, taskManager.getAllEpics().size(), "Вернул неверное кол-во эпиков");
    }

    @Test
    public void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        int id = subtask2.getId();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "TaskHandler вернул неверный код");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Вернул неверное кол-во подзадач");
    }

    @Test
    public void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriPrioritized)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "PrioritizedHandler вернул неверный код");
        String jsonString = gson.toJson(taskManager.getPrioritizedTasks());
        assertEquals(jsonString, response.body(), "PrioritizedHandler не вернул список приоритетных задач");
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uriHistory)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "HistoryHandler вернул неверный код");
        String jsonString = gson.toJson(taskManager.getHistory());
        assertEquals(jsonString, response.body(), "HistoryHandler не вернул историю");
    }

}