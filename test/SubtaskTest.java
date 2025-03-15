package test;

import tasks.Status;
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    //2-Проверка того что наследники класса Task равны друг другу, если равен их id
    @Test
    public void setId_equalsReturnTrue_idIsSame() {
        Subtask subtask = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 1);
        Subtask subtask1 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 1);
        subtask.setId(1);
        subtask1.setId(1);
        assertEquals(subtask.getId(), subtask1.getId(), "Подзадачи должны быть равны по id");
    }

}