package manager;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

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

    public void save() {

        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            writer.write("id,type,name,status,description,duration,startTime,epic_id");
            writer.newLine();

            Stream.concat(getAllTasks().stream(),
                            Stream.concat(getAllEpics().stream(), getAllSubtasks().stream())
                    ).map(CSVTaskFormat::toString)
                    .forEachOrdered(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка сохранения задачи!" + e.getMessage());
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

}