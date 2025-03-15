package test;

import interfaces.TaskManager;
import manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @Override
    TaskManager initTaskManager() {
        return new InMemoryTaskManager();
    }

}