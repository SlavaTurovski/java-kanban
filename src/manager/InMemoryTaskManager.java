package manager;

import interfaces.HistoryManager;
import tasks.Epic;
import enums.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements interfaces.TaskManager {

    private int id = 1;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        return id++;
    }

    private void updateStatusEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {    // Проверяем наличие эпика
            System.out.println("Эпик не найден!");
            return;
        }
        if (epic.getSubtaskId().isEmpty()) {    // Если у эпика нет подзадач, устанавливаем статус NEW
            epic.setStatus(Status.NEW);
            return;
        }
        List<Subtask> subtasks = getSubtasksForEpic(epic);    // Создаем список подзадач для текущего эпика
        int countDone = countSubtasksWithStatus(subtasks, Status.DONE);    // Считаем количество завершенных и новых подзадач
        int countNew = countSubtasksWithStatus(subtasks, Status.NEW);
        if (countDone == subtasks.size()) {    // Определяем статус эпика
            epic.setStatus(Status.DONE);
        } else if (countNew == subtasks.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private List<Subtask> getSubtasksForEpic(Epic epic) {     // Метод для получения списка подзадач по id
        List<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasks.add(subtask);
        }
        return subtasks;
    }

    // Метод для подсчета подзадач с определенным статусом
    private int countSubtasksWithStatus(List<Subtask> subtasks, Status status) {
        int count = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void addTask(Task task) {
        int taskId = generateId();
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void addEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateStatusEpic(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик не создан!");
            return;
        }
        int subtaskId = generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
        epic.addSubtaskId(subtaskId);
        updateEpic(epic);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задачи с таким id не существует!");
        }
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпика с таким id не существует!");
        }
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадачи с таким id не существует!");
        }
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
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
    public List<Subtask> getAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        }
        return new ArrayList<>(subtasks.values());
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
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateStatusEpic(epic);
        } else {
            System.out.println("Подзадача не найдена!");
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
        }
        subtasks.clear();
        epics.clear();
        System.out.println("Все эпики успешно удалены!");
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateStatusEpic(epic);
        }
        subtasks.clear();
        System.out.println("Все подзадачи успешно удалены!");
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача не найдена!");
            return;
        }
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик не найден!");
            return;
        }
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtaskId().remove(subtask.getId());
        updateStatusEpic(epic);
        subtasks.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        if (!epics.containsKey(id)) {
            return Collections.emptyList();
        }
        Epic epic = epics.get(id);
        List<Subtask> subtasksNew = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasksNew.add(subtasks.get(subtaskId));
        }
        return subtasksNew;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}