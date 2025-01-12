import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import enums.Status;

import java.util.List;

public class Main {

    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static void main(String[] args) {

        System.out.println("Создать задачу");
        inMemoryTaskManager.addTask(new Task("Задача-1", "Описание задачи-1", Status.NEW));
        inMemoryTaskManager.addTask(new Task("Задача-2", "Описание задачи-2", Status.NEW));
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println();

        System.out.println("Получить все задачи");
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println();

        System.out.println("Получить задачу по id-2");
        Task task = inMemoryTaskManager.getTaskById(2);
        System.out.println(task);
        System.out.println();

        System.out.println("Обновить полученную задачу");
        task.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task);
        System.out.println(task);
        System.out.println();

        System.out.println("Создать эпик");
        inMemoryTaskManager.addEpic(new Epic("Эпик-1", "Описание эпика-1", Status.NEW));
        inMemoryTaskManager.addEpic(new Epic("Эпик-2", "Описание эпика-2", Status.NEW));
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println();

        System.out.println("Получить все эпики");
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println();

        System.out.println("Получить эпик по id-3");
        Epic epic = inMemoryTaskManager.getEpicById(3);
        System.out.println(epic);
        System.out.println();

        System.out.println("Обновить полученный эпик");
        inMemoryTaskManager.updateEpic(inMemoryTaskManager.getEpicById(3));
        epic.setStatus(Status.IN_PROGRESS);
        System.out.println(epic);
        System.out.println();

        System.out.println("Создать подзадачу");
        inMemoryTaskManager.addSubtask(new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 3));
        inMemoryTaskManager.addSubtask(new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 3));
        inMemoryTaskManager.addSubtask(new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, 4));
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Получить все подзадачи по эпику c id-3");
        List<Subtask> subtasksByEpicId = inMemoryTaskManager.getSubtasksByEpicId(3);
        System.out.println(subtasksByEpicId);
        System.out.println();

        System.out.println("Получить все подзадачи");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Получить подзадачу c id-5");
        Subtask subtask = inMemoryTaskManager.getSubtaskById(5);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Обновить полученную подзадачу");
        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Удалить задачу c id-3 и показать оставшиеся задачи");
        inMemoryTaskManager.deleteTaskById(3);
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println();

        System.out.println("Удаление всех задач");
        inMemoryTaskManager.deleteAllTasks();
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println();

        System.out.println("Удалить подзадачу c id-5 и показать оставшиеся подзадачи");
        inMemoryTaskManager.deleteSubtaskById(5);
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Удалить все подзадачи");
        inMemoryTaskManager.deleteAllSubtasks();
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Удалить эпик c id-3 и показать оставшиеся эпики");
        inMemoryTaskManager.deleteEpicById(3);
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println();

        System.out.println("Удалить все эпики");
        inMemoryTaskManager.deleteAllEpics();
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println();
    }
}