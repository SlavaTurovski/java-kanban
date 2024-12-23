import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.util.List;

public class Main {

    static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {

        System.out.println("Создать задачу");
        taskManager.addTask(new Task("Задача-1", "Описание задачи-1"));
        taskManager.addTask(new Task("Задача-2", "Описание задачи-2"));
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        System.out.println("Получить все задачи");
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        System.out.println("Получить задачу по id-2");
        Task task = taskManager.getTaskById(2);
        System.out.println(task);
        System.out.println();

        System.out.println("Обновить полученную задачу");
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        System.out.println(task);
        System.out.println();

        System.out.println("Создать эпик");
        taskManager.addEpic(new Epic("Эпик-1", "Описание эпика-1"));
        taskManager.addEpic(new Epic("Эпик-2", "Описание эпика-2"));
        System.out.println(taskManager.getAllEpics());
        System.out.println();

        System.out.println("Получить все эпики");
        System.out.println(taskManager.getAllEpics());
        System.out.println();

        System.out.println("Получить эпик по id-3");
        Epic epic = taskManager.getEpicById(3);
        System.out.println(epic);
        System.out.println();

        System.out.println("Обновить полученный эпик");
        taskManager.updateEpic(taskManager.getEpicById(3));
        epic.setStatus(Status.IN_PROGRESS);
        System.out.println(epic);
        System.out.println();

        System.out.println("Создать подзадачу");
        taskManager.addSubtask(new Subtask("Подзадача-1", "Описание подзадачи-1", 3));
        taskManager.addSubtask(new Subtask("Подзадача-2", "Описание подзадачи-2", 3));
        taskManager.addSubtask(new Subtask("Подзадача-3", "Описание подзадачи-3", 4));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Получить все подзадачи по эпику c id-3");
        List<Subtask> subtasksByEpicId = taskManager.getSubtasksByEpicId(3);
        System.out.println(subtasksByEpicId);
        System.out.println();

        System.out.println("Получить все подзадачи");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Получить подзадачу c id-5");
        Subtask subtask = taskManager.getSubtaskById(5);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Обновить полученную подзадачу");
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Удалить задачу c id-3 и показать оставшиеся задачи");
        taskManager.deleteTaskById(3);
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        System.out.println("Удаление всех задач");
        taskManager.deleteAllTasks();
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        System.out.println("Удалить подзадачу c id-5 и показать оставшиеся подзадачи");
        taskManager.deleteSubtaskById(5);
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Удалить все подзадачи");
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Удалить эпик c id-3 и показать оставшиеся эпики");
        taskManager.deleteEpicById(3);
        System.out.println(taskManager.getAllEpics());
        System.out.println();

        System.out.println("Удалить все эпики");
        taskManager.deleteAllEpics();
        System.out.println(taskManager.getAllEpics());
        System.out.println();
    }
}