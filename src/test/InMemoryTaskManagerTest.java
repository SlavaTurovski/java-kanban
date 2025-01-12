package test;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;
    Task task;
    Task task1;
    Epic epic;
    Subtask subtask;

    //Каждую проверку создаем TASK EPIC SUBTASK
    @BeforeEach
    public void setTaskEpicAndSubtask() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        task = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task);
        task1 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        taskManager.addTask(task1);
        epic = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - TASK
    @Test
    public void InMemoryTaskManagerMustAddTask() {
        Task taskActual = taskManager.getTaskById(task.getId());
        assertEquals(task, taskActual);
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - EPIC
    @Test
    public void InMemoryTaskManagerMustAddEpic() {
        Epic epicActual = taskManager.getEpicById(epic.getId());
        assertEquals(epic, epicActual);
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - SUBTASK
    @Test
    public void InMemoryTaskManagerMustAddSubTask() {
        Subtask subtaskActual = taskManager.getSubtaskById(subtask.getId());
        assertEquals(subtask, subtaskActual);
    }

    //7-Проверка того что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    void tasksNotConflict() {
        task.setId(1);
        taskManager.addTask(task);
        assertNotEquals(task.getId(), task1.getId());
    }
}