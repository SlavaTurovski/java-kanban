package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int LIMIT_TASKS = 10;
    private final List<Task> historyTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            System.out.println("Задача не найдена!");
            return;
        }
        if (historyTasks.size() >= LIMIT_TASKS) {
            historyTasks.removeFirst();
        }
        historyTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }
}
