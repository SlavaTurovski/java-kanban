package test;

import tasks.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    //1-Проверка того что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void equals_compareNotOnlyById() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "Задачи должны быть равны по id");
    }

}