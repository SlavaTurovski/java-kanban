package test;

import tasks.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    //1-Проверка того что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void set_returnTrue_idIsSame() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "Задачи должны быть равны по id");
    }

    //ТЗ-6 Проверка на изменение задач сеттерами
    @Test
    public void set_returnTrue_updateTaskFields() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        task1.setName("Новая Задача");
        task1.setDescription("Новое описание задачи");
        task1.setStatus(Status.IN_PROGRESS);
        assertEquals("Новая Задача", task1.getName(), "Сеттер должен изменить имя");
        assertEquals("Новое описание задачи", task1.getDescription(), "Сеттер должен изменить описание");
        assertEquals(Status.IN_PROGRESS, task1.getStatus(), "Сеттер должен изменить статус");
    }

}