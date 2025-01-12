package test;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    //1-Проверка того что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void taskIdMustBeEquals() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "ID не равны!");
    }

    //8-Проверка того что задача неизменна (по всем полям) при добавлении задачи в менеджер
    @Test
    public void taskMustBeStable() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
        taskManager.addTask(task1);
        assertEquals(task1.getName(), taskManager.getTaskById(task1.getId()).getName());
        assertEquals(task1.getDescription(), taskManager.getTaskById(task1.getId()).getDescription());
        assertEquals(task1.getStatus(), taskManager.getTaskById(task1.getId()).getStatus());
    }

}