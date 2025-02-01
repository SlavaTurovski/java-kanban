package test;

import tasks.Status;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private final InMemoryTaskManager taskManager;

    public SubtaskTest() {
        this.taskManager = new InMemoryTaskManager();
    }

    //2-Проверка того что наследники класса Task равны друг другу, если равен их id
    @Test
    public void equals_returnTrue_IfIdIsSame() {
        Subtask subtask = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 1);
        Subtask subtask1 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 1);
        subtask.setId(1);
        subtask1.setId(1);
        assertEquals(subtask.getId(), subtask1.getId(), "Подзадачи должны быть равны по id");
    }

    //4-Проверка того что объект Subtask нельзя сделать своим же эпиком
    @Test
    public void notEquals_returnTrue_IfAddSubtaskAsItsOwnEpic() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask1.setId(subtask1.getId());
        assertNotEquals(subtask1.getId(), subtask1.getEpicId(), "Подзадача не должна стать своим же эпиком");
    }

    //ТЗ-6 Проверка того что удаляемые подзадачи не должны хранить внутри себя старые id
    @Test
    public void FalseAndNull_returnTrue_IfDeletedSubTaskNoContainsOldId() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertFalse(epic1.getSubtaskIdInEpic().contains(subtask1.getId()));
        assertNull(taskManager.getSubtaskById(subtask1.getId()), "Подзадачи не должны хранить внутри себя старые id");
    }
}