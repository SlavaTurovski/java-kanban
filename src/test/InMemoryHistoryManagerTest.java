package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import tasks.Status;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private final InMemoryHistoryManager historyManager;

    public InMemoryHistoryManagerTest() {
        this.historyManager = new InMemoryHistoryManager();
    }

    //9-Проверка того что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
    @Test
    public void equals_returnTrue_IfHistoryManagerSaveAllVersionsOfTasks() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task updatedTask = new Task("Задача-1", "Описание задачи-1(НОВОЕ)", Status.DONE);
        historyManager.add(task1);
        historyManager.add(updatedTask);
        assertEquals(2, historyManager.getHistory().size(), "История должна содержать исходную и " +
                "обновлённую задачу");
        assertEquals("Описание задачи-1", historyManager.getHistory().get(0).getDescription(), "Первая " +
                "версия задачи должна быть сохранена");
        assertEquals("Описание задачи-1(НОВОЕ)", historyManager.getHistory().get(1).getDescription(), "Обновлённая " +
                " версия должна +быть добавлена в историю");
    }

}