package test;

import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import manager.TaskTimeOverlapException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest<T extends TaskManager> {

    private final InMemoryTaskManager taskManager;

    public TaskManagerTest() {
        this.taskManager = new InMemoryTaskManager();
    }

    //Проверка того что нельзя создать пересекающиеся по времени задачи
    @Test
    public void nullAndEquals_returnTrue_shouldNotCreateOverlappingDate() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusMinutes(10));
        Task task2 = new Task("Пересекающаяся задача-2", "Описание задачи-2", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusMinutes(10));
        taskManager.addTask(task1);
        assertThrows(TaskTimeOverlapException.class, () -> taskManager.addTask(task2));
        assertNull(taskManager.getTaskById(task2.getId()));
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - TASK
    @Test
    public void equals_returnTrue_IfInMemoryTaskManagerReallyAddsTask() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Task taskActual = taskManager.getTaskById(task1.getId());
        assertEquals(task1, taskActual, "Задача должна добавляться");
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - EPIC
    @Test
    public void equals_returnTrue_IfInMemoryTaskManagerReallyAddsEpic() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Epic epicActual = taskManager.getEpicById(epic1.getId());
        assertEquals(epic1, epicActual, "Эпик должен добавляться");
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - SUBTASK
    @Test
    public void equals_returnTrue_IfInMemoryTaskManagerReallyAddsSubTask() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtaskActual = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(subtask1, subtaskActual, "Подзадача должна добавляться");
    }

    //Проверка того что задачу можно получить по id
    @Test
    public void equals_returnTrue_IfTaskGettingByIdIsWell() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    //Проверка того что эпик можно получить по id
    @Test
    public void equals_returnTrue_IfEpicGettingByIdIsWell() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        epic1.setId(1);
        taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(1));
    }

    //Проверка того что подзадачу можно получить по id
    @Test
    public void equals_returnTrue_IfSubtasksGettingByIdIsWell() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subTask1);
        assertEquals(subTask1, taskManager.getSubtaskById(subTask1.getId()));
    }

    //Проверка того что задача обновляется
    @Test
    public void equals_returnTrue_IfUpdateTaskIsWell() {
        Task original = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(original);
        original.setName("Обновленное название задачи");
        taskManager.updateTask(original);
        assertEquals("Обновленное название задачи", taskManager.getTaskById(original.getId()).getName());
    }

    //Проверка того что эпик обновляется
    @Test
    public void equals_returnTrue_IfUpdateEpicIsWell() {
        Epic original = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(original);
        original.setName("Обновленное название эпика");
        taskManager.updateEpic(original);
        assertEquals("Обновленное название эпика", taskManager.getEpicById(original.getId()).getName());
    }

    //Проверка того что подзадача обновляется
    @Test
    public void equals_returnTrue_IfUpdateSubtaskIsWell() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask1.setName("Обновленное название подзадачи");
        assertEquals("Обновленное название подзадачи", taskManager.getSubtaskById(subtask1.getId()).getName());
    }

    //Проверка того что задача удаляется по id
    @Test
    public void nullAndEquals_returnTrue_IfDeleteTaskByIdIsWell() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        taskManager.deleteTaskById(task1.getId());
        assertEquals(0, taskManager.getAllTasks().size());
        assertNull(taskManager.getTaskById(task1.getId()));
    }

    //Проверка того что эпик удаляется по id
    @Test
    public void nullAndEquals_returnTrue_IdDeleteEpicByIdIsWell() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.deleteEpicById(epic1.getId());
        assertEquals(0, taskManager.getAllEpics().size());
        assertNull(taskManager.getTaskById(epic1.getId()));
    }

    //Проверка того что подзадача удаляется по id
    @Test
    public void nullAndEquals_returnTrue_IfDeleteSubTaskByIdIsWell() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subTask1);
        taskManager.deleteSubtaskById(subTask1.getId());
        assertEquals(0, taskManager.getAllSubtasks().size());
        assertNull(taskManager.getTaskById(subTask1.getId()));
    }

    //Проверка того что подзадачу можно получить по id эпика
    @Test
    public void equals_returnTrue_IfSubtasksGettingByEpicIdIsWell() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subTask1);
        Subtask subTask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(19), epic1.getId());
        taskManager.addSubtask(subTask2);
        ArrayList<Subtask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        assertEquals(subTasks, taskManager.getSubtasksByEpicId(epic1.getId()));
    }

}