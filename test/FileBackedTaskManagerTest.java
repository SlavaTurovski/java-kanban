package test;

import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static File tempFile;
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        tempFile = new File("task.csv");
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
    }

    @AfterEach
    public void afterEach() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();
    }

    //Проверка того что задачи загружаются из файла
    @Test
    public void equals_returnTrue_IfLoadFromFile() {
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    //Проверка того что задачи записываются в файл
    @Test
    public void equals_returnTrue_IfSaveFromData() {
        taskManager.addEpic(new Epic("Эпик-1", "Описание эпика-1", Status.NEW));
        assertEquals(1, taskManager.getAllEpics().size());
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(1, taskManager.getAllEpics().size());
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Эпики должны быть удалены!");
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(0, taskManager.getAllEpics().size());
    }

    //Проверка того что 50 задач записываются в файл и загружаются из него
    @Test
    public void equals_returnTrue_IfSaveAndLoadBigData() {
        for (int i = 0; i < 50; i++) {
            taskManager.addEpic(new Epic("Эпик-" + i, "Описание эпика-1", Status.NEW));
            taskManager.addTask(new Task("Задача-" + i, "Описание задачи-1", Status.NEW));
        }
        assertEquals(50, taskManager.getAllEpics().size());
        assertEquals(50, taskManager.getAllTasks().size());
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(50, taskManager.getAllEpics().size());
        assertEquals(50, taskManager.getAllTasks().size());
    }

}