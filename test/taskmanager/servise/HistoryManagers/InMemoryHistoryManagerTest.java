package taskmanager.servise.HistoryManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.servise.impl.InMemoryHistoryManager;
import taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.Arrays;

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
    public void shouldNotAddTaskToHistoryWhenSizeIs10() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task("name test", "description test");
            task.setId(i);
            inMemoryHistoryManager.add(task);
        }
        assertEquals(10, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void arrayShouldRemoveLastTaskWhenTryingToAddMoreThan10Tasks() {
        for (int i = 0; i < 10; i++) {
            Task task = new Task("name test", "description test");
            task.setId(i);
            inMemoryHistoryManager.add(task);
        }
        ArrayList<Task> expected = new ArrayList<>(inMemoryHistoryManager.getHistory());
        for (int i = 11; i < 15; i++) {
            Task task = new Task("name test", "description test");
            task.setId(i);
            inMemoryHistoryManager.add(task);
            expected.addFirst(task);
            expected.removeLast();
        }
        ArrayList<Task> actual = inMemoryHistoryManager.getHistory();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldRemoveTaskFromHistory() {
        Task task = new Task("name test", "description test");
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.remove(task);
        assertEquals(0, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void shouldReturnHistory() {
        Task task = new Task("name test", "description test");
        inMemoryHistoryManager.add(task);
        Task task2 = new Task("name test2", "description test2");
        inMemoryHistoryManager.add(task2);
        ArrayList<Task> expected = new ArrayList<>(Arrays.asList(task2, task));
        ArrayList<Task> actual = inMemoryHistoryManager.getHistory();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}