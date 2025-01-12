package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    //5-Проверка того что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void initManagerGetDefault() {
        InMemoryTaskManager def = (InMemoryTaskManager) Managers.getDefault();
        Assertions.assertNotNull(def);
    }

    //5-Проверка того что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void initManagerGetDefaultHistory() {
        InMemoryHistoryManager defHistory = (InMemoryHistoryManager) Managers.getDefaultHistory();
        Assertions.assertNotNull(defHistory);
    }

}