package test;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    private final TaskManager taskManager;
    private final HistoryManager historyManager;

    public ManagersTest() {
        this.taskManager = Managers.getDefault();
        this.historyManager = Managers.getDefaultHistory();
    }

    //5-Проверка того что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void notNullAndInstanceOf_returnTrue_IfUtilityClassReturnsInitializedManagerCopiesInMemoryTaskManager() {
        assertNotNull(taskManager, "taskManager должен быть не null");
        assertInstanceOf(InMemoryTaskManager.class, taskManager, "taskManager должен являться экземпляром" +
                " InMemoryTaskManager");
    }

    //5-Проверка того что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void notNullAndInstanceOf_returnTrue_IfUtilityClassReturnsInitializedManagerCopiesInMemoryHistoryManager() {
        assertNotNull(historyManager, "historyManager должен быть не null");
        assertInstanceOf(InMemoryHistoryManager.class, historyManager, "historyManager должен являться экземпляром" +
                " InMemoryHistoryManager");
    }

}