package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return FileBackedTaskManager.loadFromFile(new File("tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}