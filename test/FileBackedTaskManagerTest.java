package test;

import interfaces.TaskManager;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Test
    void save_saveToEmptyFile() throws IOException {
        File file = File.createTempFile("tempFile", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        taskManager.save();
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManagerFromFile.getAllTasks().size(), 0, "Количество задач не равно 0");
        assertEquals(taskManagerFromFile.getHistory().size(), 0, "Количество задач в истории не равно 0");
    }

    @Test
    void loadFromFile_loadFromEmptyFile() throws IOException {
        File file = File.createTempFile("tempFile", ".csv");
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.getAllTasks().size(), 0, "Количество задач не равно 0");
        assertEquals(taskManager.getHistory().size(), 0, "Количество задач в истории не равно 0");
    }

    @Test
    public void loadFromFile_saveAndLoadData() throws IOException {
        File file = File.createTempFile("tempFile", ".csv");
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusDays(1));
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().minusMinutes(20));
        Task task3 = new Task("Задача-3", "Описание задачи-3", Status.NEW, Duration.ofMinutes(40), LocalDateTime.now().minusDays(22));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        Epic epic2 = new Epic("Эпик-2", "Описание эпика-2", Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(25), LocalDateTime.now().minusDays(12), epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, Duration.ofMinutes(40), LocalDateTime.now().minusDays(10), epic2.getId());
        Subtask subtask3 = new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic2.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        assertEquals(3, taskManager.getAllTasks().size(), "Количество задач в менеджерах не равно");
        assertEquals(2, taskManager.getAllEpics().size(), "Количество задач в менеджерах не равно");
        assertEquals(3, taskManager.getAllSubtasks().size(), "Количество задач в менеджерах не равно");
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertEquals(3, taskManagerFromFile.getAllTasks().size(), "Количество задач в менеджерах не равно");
        assertEquals(2, taskManagerFromFile.getAllEpics().size(), "Количество задач в менеджерах не равно");
        assertEquals(3, taskManagerFromFile.getAllSubtasks().size(), "Количество задач в менеджерах не равно");
    }

    @Override
    TaskManager initTaskManager() {
        try {
            File file = File.createTempFile("tempFile", ".csv");
            return  FileBackedTaskManager.loadFromFile(file);
        } catch (IOException ignored) {
        }
        return null;
    }

}