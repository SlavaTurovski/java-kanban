package test;

import interfaces.TaskManager;
import manager.Managers;
import manager.TaskTimeOverlapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    TaskManager initTaskManager() {
        return Managers.getDefault();
    }

    @BeforeEach
    public void beforeEach() {
        taskManager = (T) initTaskManager();
    }

    @Test
    public void addSubtask_updateEpic_setStatusDoneIfAllSubtasksInStatusDone() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.DONE, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.updateEpic(epic1);
        assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика должен быть DONE!");
    }

    @Test
    public void addSubtask_updateEpicStatus_setStatusInProgressIfAllSubtasksInStatusInProgress() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.IN_PROGRESS, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.updateEpic(epic1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS!");
    }

    @Test
    public void addSubtask_updateEpicStatus_setStatusNewIfAllSubtasksInStatusNew() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.updateEpic(epic1);
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика должен быть NEW!");
    }

    @Test
    public void addSubtask_updateEpicTime_setStartTimeFromStartTimeOfEarliestSubtask() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.of(2019,1,1,1,1), epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, Duration.ofMinutes(100), LocalDateTime.of(2020,1,1,1,1), epic1.getId());
        taskManager.addSubtask(subtask2);
        taskManager.updateEpic(epic1);
        assertEquals(subtask1.getStartTime(), epic1.getStartTime(), "Время начала задач должно быть одинаковым!");
    }

    @Test
    public void deleteSubtask_updateEpicStatus_setStatusNewIfSubtaskListIsEmpty() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.updateEpic(epic1);
        taskManager.deleteAllSubtasks();
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика должен быть NEW!");
    }

    @Test
    public void getTaskById_addTaskToHistory() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        assertEquals(3, taskManager.getHistory().size(),
                "Метод getHistory в InMemoryTaskManager работает не корректно!");
    }

    //Проверка того что нельзя создать пересекающиеся по времени задачи
    @Test
    public void addTask_shouldNotCreateOverlappingDate() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusMinutes(10));
        Task task2 = new Task("Пересекающаяся задача-2", "Описание задачи-2", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusMinutes(10));
        taskManager.addTask(task1);
        assertThrows(TaskTimeOverlapException.class, () -> taskManager.addTask(task2));
        assertNull(taskManager.getTaskById(task2.getId()));
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - TASK
    @Test
    public void addTask_setIdAndAddToMap() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Task taskActual = taskManager.getTaskById(task1.getId());
        assertEquals(task1, taskActual, "Задача должна добавляться");
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - EPIC
    @Test
    public void addEpic_setIdAndAddToMap() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Epic epicActual = taskManager.getEpicById(epic1.getId());
        assertEquals(epic1, epicActual, "Эпик должен добавляться");
    }

    //6-Проверка того что InMemoryTaskManager действительно добавляет задачи типа - SUBTASK
    @Test
    public void addSubtask_setIdAndAddToMap() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtaskActual = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(subtask1, subtaskActual, "Подзадача должна добавляться");
    }

    //Проверка того что задачу можно получить по id
    @Test
    public void getTaskById_returnTaskById() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    //Проверка того что эпик можно получить по id
    @Test
    public void getEpicById_returnEpicById() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        epic1.setId(1);
        taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(1));
    }

    //Проверка того что подзадачу можно получить по id
    @Test
    public void getSubtasksById_returnSubtaskById() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subTask1);
        assertEquals(subTask1, taskManager.getSubtaskById(subTask1.getId()));
    }

    //Проверка того что задача обновляется
    @Test
    public void updateTask_changeName() {
        Task original = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(original);
        original.setName("Обновленное название задачи");
        taskManager.updateTask(original);
        assertEquals("Обновленное название задачи", taskManager.getTaskById(original.getId()).getName());
    }

    //Проверка того что эпик обновляется
    @Test
    public void updateEpic_changeName() {
        Epic original = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(original);
        original.setName("Обновленное название эпика");
        taskManager.updateEpic(original);
        assertEquals("Обновленное название эпика", taskManager.getEpicById(original.getId()).getName());
    }

    //Проверка того что подзадача обновляется
    @Test
    public void updateSubtask_changeName() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask1.setName("Обновленное название подзадачи");
        assertEquals("Обновленное название подзадачи", taskManager.getSubtaskById(subtask1.getId()).getName());
    }

    //Проверка того что задача удаляется по id
    @Test
    public void deleteTaskById_removeFromTaskManager() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        taskManager.deleteTaskById(task1.getId());
        assertEquals(0, taskManager.getAllTasks().size());
        assertNull(taskManager.getTaskById(task1.getId()));
    }

    //Проверка того что эпик удаляется по id
    @Test
    public void deleteEpicById_removeFromTaskManager() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.deleteEpicById(epic1.getId());
        assertEquals(0, taskManager.getAllEpics().size());
        assertNull(taskManager.getTaskById(epic1.getId()));
    }

    //Проверка того что подзадача удаляется по id
    @Test
    public void deleteSubtaskById_removeFromTaskManager() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subTask1);
        taskManager.deleteSubtaskById(subTask1.getId());
        assertEquals(0, taskManager.getAllSubtasks().size());
        assertNull(taskManager.getTaskById(subTask1.getId()));
    }

    @Test
    void deleteAllTasks_removeFromTaskManager() {
        Task task = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusMinutes(10));
        taskManager.addTask(task);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Задачи не удаляются!");
    }

    @Test
    void deleteAllEpic_removeFromTaskManager() {
        Epic epic = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addTask(epic);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllEpics().size(), "Эпики не удаляются!");
    }

    @Test
    void deleteAllSubtasks_removeFromTaskManager() {
        Epic epic = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size(), "Подзадачи не удаляются!");
    }

    //Проверка того что подзадачу можно получить по id эпика
    @Test
    public void getSubtasksByEpicId_shouldGetSubtasksByEpicId() {
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

    //7-Проверка того что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void setId_tasksWithGeneratedAndAssignedIdsDoNotConflict() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        taskManager.addTask(task2);
        task2.setId(1);
        taskManager.addTask(task2);
        assertNotEquals(task1.getId(), task2.getId(), "Задачи с заданным id и сгенерированным id не должны " +
                "конфликтовать");
    }

    //4-Проверка того что объект Subtask нельзя сделать своим же эпиком
    @Test
    public void getEpicId_subtaskCannotBeItsOwnEpic() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask1.setId(subtask1.getId());
        assertNotEquals(subtask1.getId(), subtask1.getEpicId(), "Подзадача не должна стать своим же эпиком");
    }

    //ТЗ-6 Проверка того что удаляемые подзадачи не должны хранить внутри себя старые id
    @Test
    public void deleteSubtaskById_deleteSubtaskAndCheckForOldIds() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertFalse(epic1.getSubtaskIdInEpic().contains(subtask1.getId()));
        assertNull(taskManager.getSubtaskById(subtask1.getId()), "Подзадачи не должны хранить внутри себя старые id");
    }

    //3-Проверка того что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void getSubtaskIdInEpic_epicCannotAddItselfAsSubtask() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        subtask1.setId(epic1.getId());
        taskManager.addSubtask(subtask1);
        assertFalse(epic1.getSubtaskIdInEpic().isEmpty(), "Объект эпика не должен добавлять себя в виде подзадачи");
    }

    // ТЗ-6 Проверка того что внутри эпиков не должно оставаться неактуальных id подзадач
    @Test
    public void getSubtaskIdInEpic_epicDoesNotContainDeletedSubtaskIds() {
        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(100), LocalDateTime.now().minusDays(9), epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertFalse(epic1.getSubtaskIdInEpic().contains(subtask1.getId()), "Внутри эпиков не должно оставаться неактуальных id подзадач");
    }

    //8-Проверка того что задача неизменна (по всем полям) при добавлении задачи в менеджер
    @Test
    public void get_addTaskAndCheckImmutability() {
        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        taskManager.addTask(task1);
        assertEquals(task1.getName(), taskManager.getTaskById(task1.getId()).getName(), "Поле должно быть неизменно");
        assertEquals(task1.getDescription(), taskManager.getTaskById(task1.getId()).getDescription(), "Поле должно " +
                "быть неизменно");
        assertEquals(task1.getStatus(), taskManager.getTaskById(task1.getId()).getStatus(), "Поле должно быть неизменно");
    }

}