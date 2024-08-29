package taskmanager.servise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.servise.impl.InMemoryHistoryManager;
import taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTaskToHistory() {
        Task task = new Task("name test", "description test");
        inMemoryHistoryManager.add(task);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void shouldRemoveTaskFromHistory() {
        Task task = new Task("name test", "description test");
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.remove(task.getId());
        assertEquals(0, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void shouldReturnHistory() {
        Task task = new Task("name test", "description test");
        inMemoryHistoryManager.add(task);
        Task task2 = new Task("name test2", "description test2");
        task2.setId(1);
        inMemoryHistoryManager.add(task2);
        List<Task> expected = new ArrayList<>(Arrays.asList(task, task2));
        List<Task> actual = inMemoryHistoryManager.getHistory();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldUpdateHistoryWhenTryingToAddTaskThatAlreadyViewed() {
        Task task = new Task("name test", "description test");
        inMemoryHistoryManager.add(task);
        Task task2 = new Task("name test2", "description test2");
        task2.setId(1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task);
        List<Task> expected = new ArrayList<>(Arrays.asList(task2, task));
        List<Task> actual = inMemoryHistoryManager.getHistory();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}