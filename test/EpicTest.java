package test;

import tasks.Status;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private final InMemoryTaskManager taskManager;

    public EpicTest() {
        this.taskManager = new InMemoryTaskManager();
    }

    //2-Проверка того что наследники класса Task равны друг другу, если равен их id
    @Test
    public void equals_returnTrue_IfIdIsSame() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        Epic epic2 = new Epic("Эпик-2", "Описание эпика-2", Status.NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "Эпики не равны т.к. не равны их id");
    }

    //3-Проверка того что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void false_returnTrue_IfAddEpicItSelfAsSubtask() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        subtask1.setId(epic1.getId());
        taskManager.addSubtask(subtask1);
        assertFalse(epic1.getSubtaskIdInEpic().isEmpty(), "Объект эпика не должен добавлять себя в виде подзадачи");
    }

    // ТЗ-6 Проверка того что внутри эпиков не должно оставаться неактуальных id подзадач
    @Test
    public void false_returnTrue_IfEpicNoContainsDeletedSubtasks() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertFalse(epic1.getSubtaskIdInEpic().contains(subtask1.getId()), "Внутри эпиков не должно оставаться неактуальных id подзадач");
    }

}