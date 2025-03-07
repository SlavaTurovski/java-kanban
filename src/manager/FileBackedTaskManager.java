package manager;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    private FileBackedTaskManager(File file) {
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

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                Task task = CSVTaskFormat.fromString(line);

                if (task.getId() > manager.id) {
                    manager.id = task.getId() + 1;
                }

                switch (task.getTaskType()) {
                    case EPIC:
                        manager.epics.put(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        manager.subtasks.put(task.getId(), (Subtask) task);
                        break;
                    default:
                        manager.tasks.put(task.getId(), task);
                        break;
                }
            }

            for (Subtask subtask : manager.subtasks.values()) {
                Epic epic = manager.epics.get(subtask.getEpicId());
                epic.getSubtaskIdInEpic().add(subtask.getId());
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

        File file = new File("task.csv");
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);

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
        Subtask subtask3 = new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, epic2.getId());
        fileBackedTaskManager1.addSubtask(subtask1);
        fileBackedTaskManager1.addSubtask(subtask3);

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);

        Epic epic3 = new Epic("Эпик-3", "Описание эпика-3", Status.NEW);
        Epic epic4 = new Epic("Эпик-4", "Описание эпика-4", Status.NEW);
        fileBackedTaskManager2.addEpic(epic3);
        fileBackedTaskManager2.addEpic(epic4);

        Subtask subtask4 = new Subtask("Подзадача-4", "Описание подзадачи-4", Status.NEW, epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача-6", "Описание подзадачи-6", Status.NEW, epic4.getId());
        fileBackedTaskManager2.addSubtask(subtask4);
        fileBackedTaskManager2.addSubtask(subtask6);

    }
}