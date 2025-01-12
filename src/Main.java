import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import enums.Status;

import java.util.List;

public class Main {

    static InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

    public static void main(String[] args) {

        System.out.println("Создать задачи...");
        taskManager.addTask(new Task("Задача-1", "Описание задачи-1", Status.NEW));
        taskManager.addTask(new Task("Задача-2", "Описание задачи-2", Status.NEW));

        System.out.println("Создать эпики...");
        taskManager.addEpic(new Epic("Эпик-1", "Описание эпика-1", Status.NEW));
        taskManager.addEpic(new Epic("Эпик-2", "Описание эпика-2", Status.NEW));

        System.out.println("Создать подзадачи...");
        taskManager.addSubtask(new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 3));
        taskManager.addSubtask(new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 3));
        taskManager.addSubtask(new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, 4));
        taskManager.addSubtask(new Subtask("Подзадача-4", "Описание подзадачи-4", Status.NEW, 4));

        System.out.println("Создать действия для истории...");
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(7);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(7);
        taskManager.getSubtaskById(8);
        taskManager.getSubtaskById(7);
        taskManager.getTaskById(2);
        taskManager.getSubtaskById(8);
        taskManager.getSubtaskById(7);
        taskManager.getTaskById(2);

        System.out.println("Вывести список");
        printAllTasks();

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

    public static void printAllTasks() {

        System.out.println("Задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }

}