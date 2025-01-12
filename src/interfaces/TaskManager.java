package interfaces;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int id);

    List<Task> getHistory();

}
