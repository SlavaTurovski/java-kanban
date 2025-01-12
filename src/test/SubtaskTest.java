package test;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

    //2-Проверка того проверьте, что наследники класса Task равны друг другу, если равен их id
    @Test
    public void subtaskIdMustBeEquals() {
        Subtask subtask = new Subtask("1", "1", Status.NEW, 1);
        Subtask subtask1 = new Subtask("2", "1", Status.NEW, 1);
        subtask.setId(1);
        subtask1.setId(1);
        assertEquals(subtask.getId(), subtask1.getId(), "Идентификаторы не равны");
    }

    //4-Проверка того что объект Subtask нельзя сделать своим же эпиком
    @Test
    public void subtaskNotMakeEpic() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask1.setId(subtask1.getId());
        assertNotEquals(subtask1.getId(), subtask1.getEpicId(), "Подзадача не может быть собственным эпиком");
    }

}