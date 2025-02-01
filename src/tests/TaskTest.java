package tests;

import tasks.Status;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private final InMemoryTaskManager taskManager;

    public TaskTest() {
        this.taskManager = new InMemoryTaskManager();
    }

    //1-Проверка того что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void equals_returnTrue_IfIdIsSame() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "Задачи должны быть равны по id");
    }

    //8-Проверка того что задача неизменна (по всем полям) при добавлении задачи в менеджер
    @Test
    public void equals_returnTrue_IfAddTaskToTheManager() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        assertEquals(task1.getName(), taskManager.getTaskById(task1.getId()).getName(), "Поле должно быть неизменно");
        assertEquals(task1.getDescription(), taskManager.getTaskById(task1.getId()).getDescription(), "Поле должно " +
                "быть неизменно");
        assertEquals(task1.getStatus(), taskManager.getTaskById(task1.getId()).getStatus(), "Поле должно быть неизменно");
    }

    //ТЗ-6 Проверка на изменение задач сеттерами
    @Test
    public void equals_returnTrue_IfChangeValueBySetters() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        task1.setName("Новая Задача");
        task1.setDescription("Новое описание задачи");
        task1.setStatus(Status.IN_PROGRESS);
        assertEquals("Новая Задача", task1.getName(), "Сеттер должен изменить имя");
        assertEquals("Новое описание задачи", task1.getDescription(), "Сеттер должен изменить описание");
        assertEquals(Status.IN_PROGRESS, task1.getStatus(), "Сеттер должен изменить статус");
    }

}