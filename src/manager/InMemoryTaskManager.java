package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    int id = 1;

    final Map<Integer, Task> tasks = new HashMap<>();
    final Map<Integer, Epic> epics = new HashMap<>();
    final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager;

    final Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.historyManager = new InMemoryHistoryManager();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    private int generateId() {
        return id++;
    }

    private void updateStatusEpic(int epicId) {

        List<Subtask> subtasks = getSubtasksByEpicId(epicId);

        if (subtasks.isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
            return;
        }

        boolean isDone = subtasks.stream()
                .allMatch(subtask -> subtask.getStatus() == Status.DONE);

        boolean isNew = subtasks.stream()
                .anyMatch(subtask -> subtask.getStatus() != Status.NEW);

        if (isDone) {
            epics.get(epicId).setStatus(Status.DONE);

        } else if (isNew) {
            epics.get(epicId).setStatus(Status.NEW);

        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }

    }

    private void addPrioritizedTask(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private boolean validateTime(Task newTask) {
        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(existing -> isOverlapping(newTask, existing));

        if (hasOverlap) {
            throw new TaskTimeOverlapException("Время выполнения задачи пересекается с существующими задачами!");
        }

        return hasOverlap;

    }

    public boolean isOverlapping(Task newTask, Task exictingTask) {
        return newTask.getStartTime().isBefore(exictingTask.getEndTime()) &&
                newTask.getEndTime().isAfter(exictingTask.getStartTime());
    }

    private void updateTimeForEpic(Epic epic) {

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration totalDuration = Duration.ZERO;

        if (!epic.getSubtaskIdInEpic().isEmpty()) {
            for (int subtaskId : epic.getSubtaskIdInEpic()) {
                Subtask subtask = getSubtaskById(subtaskId);

                totalDuration = totalDuration.plus(subtask.getDuration());

                if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }

                if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }

            epic.setStartTime(startTime);
            epic.setDuration(totalDuration);
            epic.setEndTime(endTime);

        } else {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        }

    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public Task addTask(Task task) {
        int taskId = generateId();
        task.setId(taskId);
        validateTime(task);
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addPrioritizedTask(task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        updateStatusEpic(epic.getId());
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {

        Epic epic = epics.get(subtask.getEpicId());

        if (epic == null) {
            System.out.println("Эпик не создан!");
        }

        validateTime(subtask);
        int subtaskId = generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtaskIdInEpic().add(subtask.getId());
        updateEpic(epic);
        addPrioritizedTask(subtask);
        updateTimeForEpic(epic);
        return subtask;
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
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задача не найдена!");
            return task;
        }

        tasks.put(task.getId(), task);
        prioritizedTasks.remove(task);
        addPrioritizedTask(task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Эпик не найден!");
            return epic;
        }
        epics.put(epic.getId(), epic);
        updateStatusEpic(epic.getId());
        updateTimeForEpic(epic);
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Подзадача не найдена!");
            return null;
        }

        validateTime(subtask);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.remove(subtask);
        addPrioritizedTask(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateStatusEpic(epic.getId());
        updateTimeForEpic(epic);
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().stream()
                .peek(task -> historyManager.remove(task.getId()))
                .forEach(prioritizedTasks::remove);

        tasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    @Override
    public void deleteAllEpics() {
        epics.values().stream()
                .forEach(epic -> historyManager.remove(epic.getId()));

        subtasks.values().stream()
                .forEach(subtask -> historyManager.remove(subtask.getId()));

        subtasks.clear();
        epics.clear();
        System.out.println("Все эпики успешно удалены!");
    }

    @Override
    public void deleteAllSubtasks() {

        subtasks.values().stream()
                .peek(subtask -> historyManager.remove(subtask.getId()))
                .forEach(prioritizedTasks::remove);


        epics.values().stream()
                .peek(epic -> {
                    epic.getSubtaskIdInEpic().clear();
                    updateStatusEpic(epic.getId());
                    updateTimeForEpic(epic);
                });

        subtasks.clear();
        System.out.println("Все подзадачи успешно удалены!");
    }


    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача не найдена!");
            return;
        }

        prioritizedTasks.remove(tasks.remove(id));
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);

        if (epic == null) {
            System.out.println("Эпик не найден!");
            return;
        }

        historyManager.remove(id);

        epic.getSubtaskIdInEpic().stream()
                .forEach(subtasks::remove);

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
        historyManager.remove(id);
        epic.getSubtaskIdInEpic().remove(subtask.getId());
        updateStatusEpic(epic.getId());
        updateTimeForEpic(epic);
        subtasks.remove(id);
        prioritizedTasks.remove(subtask);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        if (!epics.containsKey(id)) {
            return Collections.emptyList();
        }

        Epic epic = epics.get(id);

        return epic.getSubtaskIdInEpic().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) historyManager.getHistory();
    }

}