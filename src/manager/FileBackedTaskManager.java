package manager;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

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

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                Task task = CSVTaskFormat.fromString(line);

                assert task != null;
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

            manager.subtasks.values().stream()
                    .forEach(subtask -> {
                        Epic epic = manager.epics.get(subtask.getEpicId());
                        epic.getSubtaskIdInEpic().add(subtask.getId());
                    });


        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка загрузки из файла!" + e.getMessage());
        }

        return manager;

    }

    protected void save() {

        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write("id,type,name,status,description,duration,startTime,epic_id");
            writer.newLine();

            getAllTasks().stream()
                    .map(CSVTaskFormat::toString)
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка сохранения задачи!" + e.getMessage());
                        }
                    });

            getAllEpics().stream()
                    .map(CSVTaskFormat::toString)
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка сохранения эпика!" + e.getMessage());
                        }
                    });

            getAllSubtasks().stream()
                    .map(CSVTaskFormat::toString)
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка сохранения подзадачи!" + e.getMessage());
                        }
                    });


        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка сохранения!" + e.getMessage());
        }

    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
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
    public Task updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
        return newTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
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
    public Epic updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask;
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
    public Subtask updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
        return newSubtask;
    }

    public static void main(String[] args) {

        File file = new File("task.csv");
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);

        Task task1 = new Task("Задача-1", "Описание задачи-1", Status.NEW, Duration.ofMinutes(20), LocalDateTime.now().minusDays(1));
        Task task2 = new Task("Задача-2", "Описание задачи-2", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().minusMinutes(20));
        Task task3 = new Task("Задача-3", "Описание задачи-3", Status.NEW, Duration.ofMinutes(40), LocalDateTime.now().minusDays(22));
        fileBackedTaskManager1.addTask(task1);
        fileBackedTaskManager1.addTask(task2);
        fileBackedTaskManager1.addTask(task3);

        Epic epic1 = new Epic("Эпик-1", "Описание эпика-1", Status.NEW);
        Epic epic2 = new Epic("Эпик-2", "Описание эпика-2", Status.NEW);
        fileBackedTaskManager1.addEpic(epic1);
        fileBackedTaskManager1.addEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача-1", "Описание подзадачи-1", Status.NEW, Duration.ofMinutes(25), LocalDateTime.now().minusDays(12), epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача-2", "Описание подзадачи-2", Status.NEW, Duration.ofMinutes(40), LocalDateTime.now().minusDays(10), epic2.getId());
        Subtask subtask3 = new Subtask("Подзадача-3", "Описание подзадачи-3", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().minusDays(9), epic2.getId());
        fileBackedTaskManager1.addSubtask(subtask1);
        fileBackedTaskManager1.addSubtask(subtask2);
        fileBackedTaskManager1.addSubtask(subtask3);

        System.out.println(fileBackedTaskManager1.getAllTasks());
        System.out.println();
        System.out.println(fileBackedTaskManager1.getAllEpics());
        System.out.println();
        System.out.println(fileBackedTaskManager1.getAllSubtasks());
        System.out.println();
        System.out.println(fileBackedTaskManager1.getPrioritizedTasks());

    }
}