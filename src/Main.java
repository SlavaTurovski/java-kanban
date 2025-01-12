import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import enums.Status;

import java.util.List;

public class Main {

    static TaskManager manager = new InMemoryTaskManager();

    public static void main(String[] args) {

        System.out.println("Создать задачи...");
        manager.addTask(new Task("Задача-1", "Описание задачи-1", Status.NEW));
        manager.addTask(new Task("Задача-2", "Описание задачи-2", Status.NEW));

        System.out.println("Создать эпики...");
        manager.addEpic(new Epic("Эпик-1", "Описание эпика-1", Status.NEW));
        manager.addEpic(new Epic("Эпик-2", "Описание эпика-2", Status.NEW));

        System.out.println("Создать подзадачи...");
        manager.addSubtask(new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, 3));
        manager.addSubtask(new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, 3));
        manager.addSubtask(new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, 4));
        manager.addSubtask(new Subtask("Подзадача-4", "Описание подзадачи-4", Status.NEW, 4));

        System.out.println("Создать действия для истории...");
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getEpicById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(8);
        manager.getSubtaskById(7);
        manager.getTaskById(2);
        manager.getSubtaskById(8);
        manager.getSubtaskById(7);
        manager.getTaskById(2);

        System.out.println("Вывести список");
        printAllTasks();

        System.out.println("Получить все подзадачи по эпику c id-3");
        List<Subtask> subtasksByEpicId = manager.getSubtasksByEpicId(3);
        System.out.println(subtasksByEpicId);
        System.out.println();

        System.out.println("Получить все подзадачи");
        System.out.println(manager.getAllSubtasks());
        System.out.println();

        System.out.println("Получить подзадачу c id-5");
        Subtask subtask = manager.getSubtaskById(5);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Обновить полученную подзадачу");
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Удалить задачу c id-3 и показать оставшиеся задачи");
        manager.deleteTaskById(3);
        System.out.println(manager.getAllTasks());
        System.out.println();

        System.out.println("Удаление всех задач");
        manager.deleteAllTasks();
        System.out.println(manager.getAllTasks());
        System.out.println();

        System.out.println("Удалить подзадачу c id-5 и показать оставшиеся подзадачи");
        manager.deleteSubtaskById(5);
        System.out.println(manager.getAllSubtasks());
        System.out.println();

        System.out.println("Удалить все подзадачи");
        manager.deleteAllSubtasks();
        System.out.println(manager.getAllSubtasks());
        System.out.println();

        System.out.println("Удалить эпик c id-3 и показать оставшиеся эпики");
        manager.deleteEpicById(3);
        System.out.println(manager.getAllEpics());
        System.out.println();

        System.out.println("Удалить все эпики");
        manager.deleteAllEpics();
        System.out.println(manager.getAllEpics());
        System.out.println();
    }

    public static void printAllTasks() {

        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}