package test;

import tasks.Status;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    private final InMemoryTaskManager taskManager;

    public InMemoryTaskManagerTest() {
        this.taskManager = new InMemoryTaskManager();
    }

    //7-Проверка того что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void equals_returnTrue_IfTasksWithGivenIdAndGeneratedIdNotConflict() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        taskManager.addTask(task2);
        task2.setId(1);
        taskManager.addTask(task2);
        assertNotEquals(task1.getId(), task2.getId(), "Задачи с заданным id и сгенерированным id не должны " +
                "конфликтовать");
    }

}