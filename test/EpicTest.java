package test;

import tasks.Status;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    //2-Проверка того что наследники класса Task равны друг другу, если равен их id
    @Test
    public void setId_returnTrue_idIsSame() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        Epic epic2 = new Epic("Эпик-2", "Описание эпика-2", Status.NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "Эпики не равны т.к. не равны их id");
    }

}