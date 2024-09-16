package taskmanager.servise;

import org.junit.jupiter.api.BeforeEach;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.utility.Managers;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager(Managers.getDefaultHistoryManager());
    }

}