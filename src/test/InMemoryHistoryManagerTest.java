package test;

import enums.Status;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    //9-Проверка того что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
    @Test
    void mustKeepPrevVersionTaskInHistory() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task updatedTask = new Task("Задача-1", "Описание задачи-1(НОВОЕ)", Status.DONE);
        taskManager.addTask(task1);
        historyManager.add(task1);
        taskManager.updateTask(updatedTask);
        historyManager.add(updatedTask);
        assertEquals(2, historyManager.getHistory().size(), "История должна содержать исходную и обновлённую задачу");
        assertEquals("Описание задачи-1", historyManager.getHistory().get(0).getDescription(), "Первая версия задачи должна быть сохранена");
        assertEquals("Описание задачи-1(НОВОЕ)", historyManager.getHistory().get(1).getDescription(), "Обновлённая версия должна быть добавлена в историю");
    }

}