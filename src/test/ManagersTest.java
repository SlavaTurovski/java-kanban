package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    //5-Проверка того что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void notNull_returnTrue_IfUtilityClassReturnsInitializedManagerCopies_InMemoryTaskManager() {
        InMemoryTaskManager defTask = (InMemoryTaskManager) Managers.getDefault();
        assertNotNull(defTask,"defTask должен быть проинициализирован и готов к работе");
    }

    //5-Проверка того что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void notNull_returnTrue_IfUtilityClassReturnsInitializedManagerCopies_InMemoryHistoryManager() {
        InMemoryHistoryManager defHistory = (InMemoryHistoryManager) Managers.getDefaultHistory();
        assertNotNull(defHistory,"defHistory должен быть проинициализирован и готов к работе");
    }

}