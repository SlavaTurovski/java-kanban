package managers;

import interfaces.ITaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class TaskManager implements ITaskManager {

    private int id = 0;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();

    private int generateId() {
        return ++id;
    }

    private void  updateStatusEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            if (epic.getSubTaskId().isEmpty()) {
                epic.setStatus(Status.NEW);
            } else {
                List<Subtask> subtasksNew = new ArrayList<>();
                int countDone = 0;
                int countNew = 0;

                for (int i = 0; i < epic.getSubTaskId().size(); i++) {
                    subtasksNew.add(subTasks.get(epic.getSubTaskId().get(i)));
                }

                for (Subtask subtask : subtasksNew) {
                    if (subtask.getStatus().equals(Status.DONE)) {
                        countDone++;
                    }
                    if (subtask.getStatus().equals(Status.NEW)) {
                        countNew++;
                    }
                    if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                    }
                }

                if (countDone == epic.getSubTaskId().size()) {
                    epic.setStatus(Status.DONE);
                } else if (countNew == epic.getSubTaskId().size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        } else {
            System.out.println("Эпик не найден!");
        }
    }

    @Override
    public int addTask(Task task) {
        int taskId = generateId();
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId;
    }

    @Override
    public int addEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateStatusEpic(epic);
        return epicId;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int subtaskId = generateId();
        subtask.setId(subtaskId);
        subTasks.put(subtaskId, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subTasks.put(subtaskId, subtask);
            epic.addSubtaskIds(subtaskId);
            updateEpic(epic);
        } else {
            System.out.println("Эпик не создан!");
        }
        return subtaskId;
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subTasks.get(id);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("Список эпиков пуст!");
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        if (subTasks.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        }
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача не найдена!");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatusEpic(epic);
        } else {
            System.out.println("Эпик не найден!");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateStatusEpic(epic);
        } else {
            System.out.println("Подзадача не найдена!");
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTaskId().clear();
            updateStatusEpic(epic);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача не найдена!");
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubTaskId()) {
                subTasks.remove(subtaskId);
            }
            epics.remove(id);
        } else {
            System.out.println("Эпик не найден!");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subTasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubTaskId().remove((Integer) subtask.getId());
            updateStatusEpic(epic);
            subTasks.remove(id);
        } else {
            System.out.println("Подзадача не найдена!");
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        if (epics.containsKey(id)) {
            List<Subtask> subtasksNew = new ArrayList<>();
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getSubTaskId().size(); i++) {
                subtasksNew.add(subTasks.get(epic.getSubTaskId().get(i)));
            }
            return subtasksNew;
        } else {
            return Collections.emptyList();
        }
    }
}