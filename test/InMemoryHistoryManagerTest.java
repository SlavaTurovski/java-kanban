package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import tasks.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private final InMemoryHistoryManager historyManager;
    private final InMemoryTaskManager taskManager;

    public InMemoryHistoryManagerTest() {
        this.historyManager = new InMemoryHistoryManager();
        this.taskManager = new InMemoryTaskManager();
    }

    Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
    Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
    Task task3 = new Task("Задача-3", "Описание задачи-3", Status.NEW);
    Task updatedTask = new Task("Задача-1", "Описание задачи-1(НОВОЕ)", Status.DONE);

    @BeforeEach
    public void addTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(updatedTask);
    }

    //9-Проверка того что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
    @Test
    public void add_() {
        historyManager.add(task1);
        taskManager.updateTask(updatedTask);
        historyManager.add(updatedTask);
        assertEquals(2, historyManager.getHistory().size(), "История должна содержать исходную и " +
                "обновлённую задачу");
        assertEquals("Описание задачи-1", historyManager.getHistory().get(0).getDescription(), "Первая " +
                "версия задачи должна быть сохранена");
        assertEquals("Описание задачи-1(НОВОЕ)", historyManager.getHistory().get(1).getDescription(), "Обновлённая " +
                " версия должна быть добавлена в историю");
    }

    //ТЗ-6 Проверка того, что встроенный связный список версий, добавляет задачи
    @Test
    public void equals_returnTrue_IfAddTasksInHistory() {
        taskManager.getTaskById(task1.getId());
        int result = taskManager.getHistory().size();
        assertEquals(1, result, "Cвязный список версий должен добавлять задачи");
    }

    //ТЗ-6 Проверка того, что встроенный связный список версий удаляет задачи
    @Test
    public void equals_returnTrue_IfDeleteTasksFromHistory() {
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.deleteTaskById(task1.getId());
        int result = taskManager.getHistory().size();
        assertEquals(1, result, "Cвязный список версий должен удалять задачи");
    }

    //ТЗ-6 Проверка того, что встроенный связный список версий хранит только уникальные записи
    @Test
    public void equals_returnTrue_IfTasksInHistoryIsUnique() {
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task1.getId());
        int result = taskManager.getHistory().size();
        assertEquals(1, result, "Cвязный список версий должен хранить уникальные записи");
    }

    //ТЗ-6 Проверка на дублирование задач в списке
    @Test
    public void equals_returnTrue_IfTasksInHistoryNotDuplicate() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(historyTask, historyManager.getHistory(), "Не должно быть дублирующихся задач");
    }

    //ТЗ-6 Проверка удаления задач из начала списка
    @Test
    public void delete_deleteFromStarList() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task2);
        historyTask.add(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.deleteTaskById(task1.getId());
        assertEquals(historyTask, taskManager.getHistory(), "Менеджер должен удалять задачи из начал списка");
    }

    //ТЗ-6 Проверка удаления задач из середины списка
    @Test
    public void delete_deleteFromMidList() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task1);
        historyTask.add(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.deleteTaskById(task2.getId());
        assertEquals(historyTask, taskManager.getHistory(), "Менеджер должен удалять задачи из середины списка");
    }

    //ТЗ-6 Проверка удаления задач с конца списка
    @Test
    public void delete_deleteFromEndList() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task1);
        historyTask.add(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.deleteTaskById(task3.getId());
        assertEquals(historyTask, taskManager.getHistory(), "Менеджер должен удалять задачи из конца списка");
    }

}