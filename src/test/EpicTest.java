package test;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

    //2-Проверка того проверьте, что наследники класса Task равны друг другу, если равен их id
    @Test
    public void epicIdMustBeEquals() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        Epic epic2 = new Epic("Эпик-2", "Описание эпика-2", Status.NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "Эпики равны т.к. равны их id");
    }

    //3-Проверка того что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void epicNotAddInEpic() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic1.getId());
        subtask1.setId(epic1.getId());
        taskManager.addSubtask(subtask1);
        assertFalse(epic1.getSubtaskIdInEpic().isEmpty(), "Объект эпик может добавить себя в виде подзадачи");
    }

}