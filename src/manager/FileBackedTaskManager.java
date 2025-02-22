package manager;

import interfaces.TaskManager;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static manager.CSVTaskFormat.fromString;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Произошла ошибка создания файла!" + e.getMessage());
            }
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);

                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else {
                    manager.addTask(task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка загрузки из файла!" + e.getMessage());
        }
        return manager;

    }

    protected void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            writer.write("id,type,name,status,description,epic_id");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(CSVTaskFormat.toString(epic));
                writer.newLine();
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка сохранения!" + e.getMessage());
        }
    }

    @Override
    public int addTask(Task task) {
        final int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public int addEpic(Epic epic) {
        final int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        final int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public int updateSubtask(Subtask newSubtask) {
        final int result = super.updateSubtask(newSubtask);
        save();
        return result;
    }

    public static void main(String[] args) throws IOException {

        File file = new File("tasks.csv");
        TaskManager fileBackedTaskManager1 = Managers.getDefault();

        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW);
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW);
        Task task3 = new Task("Задача-3", "Описание задачи-3", Status.NEW);
        fileBackedTaskManager1.addTask(task1);
        fileBackedTaskManager1.addTask(task2);
        fileBackedTaskManager1.addTask(task3);

        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        Epic epic2 = new Epic("Эпик-2", "Описание эпика-2", Status.NEW);
        fileBackedTaskManager1.addEpic(epic1);
        fileBackedTaskManager1.addEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.IN_PROGRESS, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, epic2.getId());
        fileBackedTaskManager1.addSubtask(subtask1);
        fileBackedTaskManager1.addSubtask(subtask2);
        fileBackedTaskManager1.addSubtask(subtask3);

        System.out.println(fileBackedTaskManager1.getAllTasks());
        System.out.println(fileBackedTaskManager1.getAllEpics());
        System.out.println(fileBackedTaskManager1.getAllSubtasks());

        System.out.println();

        System.out.println(Files.readString(file.toPath()));
    }
}