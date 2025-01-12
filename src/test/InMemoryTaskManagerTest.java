package test;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - TASK
    @Test
    public void InMemoryTaskManagerMustAddTask() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Task taskActual = taskManager.getTaskById(task1.getId());
        assertEquals(task1, taskActual);
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - EPIC
    @Test
    public void InMemoryTaskManagerMustAddEpic() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Epic epicActual = taskManager.getEpicById(epic1.getId());
        assertEquals(epic1, epicActual);
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - SUBTASK
    @Test
    public void InMemoryTaskManagerMustAddSubTask() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtaskActual = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(subtask1, subtaskActual);
    }

    //7-Проверка того что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    void tasksNotConflict() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        taskManager.addTask(task2);
        task2.setId(1);
        taskManager.addTask(task2);
        assertNotEquals(task1.getId(), task2.getId());
    }
}