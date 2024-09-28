package taskmanager.servise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.exceptions.NullTimesOfTaskException;
import taskmanager.exceptions.TasksOverlapsInTimeException;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractTaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    abstract protected T createTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager = createTaskManager();
    }

    @Test
    public void shouldAddTask() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        taskManager.addTask(task);
        List<Task> actual = taskManager.getTasks();
        assertTrue(actual.contains(task));
    }

    @Test
    public void shouldAddEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        List<Epic> actual = taskManager.getEpics();
        assertTrue(actual.contains(epic));
    }

    @Test
    public void shouldNotAddSubtaskToNonexistentEpic() {
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", -1);
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        List<Subtask> actual = taskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldAddSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        List<Subtask> actual = taskManager.getSubtasks();
        assertTrue(actual.contains(subtask));
    }

    @Test
    public void shouldAddSubtaskToEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        List<Epic> actual = taskManager.getEpics();
        assertTrue(actual.contains(epic));
    }



    @Test
    public void shouldNotFindNonexistentTask() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        taskManager.addTask(task);
        assertNull(taskManager.findTask(-1));
    }

    @Test
    public void shouldNotFindNonexistentEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        assertNull(taskManager.findEpic(-1));
    }

    @Test
    public void shouldNotFindNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);
        assertNull(taskManager.findSubtask(-1));
    }

    @Test
    public void shouldFindTask() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        taskManager.addTask(task);
        Task actual = taskManager.findTask(task.getId());
        assertEquals(task, actual);
    }

    @Test
    public void shouldFindEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Epic actual = taskManager.findEpic(epic.getId());
        assertEquals(epic, actual);
    }

    @Test
    public void shouldFindSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        Subtask actual = taskManager.findSubtask(subtask.getId());
        assertEquals(subtask, actual);
    }



    @Test
    public void shouldClearTasks() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        task2.setDuration(Duration.ofMinutes(2));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.clearTasks();
        List<Task> actual = taskManager.getTasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearEpics() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.clearEpics();
        List<Epic> actual = taskManager.getEpics();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearSubtasksWhenClearingEpics() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        taskManager.clearEpics();
        List<Subtask> actual = taskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        taskManager.clearSubtasks();
        List<Subtask> actual = taskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearSubtasksInEpicsWhenClearingSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        taskManager.clearSubtasks();
        int expected = 0;
        int actual = epic.getSubtasksIds().size() + epic2.getSubtasksIds().size();
        assertEquals(expected, actual);
    }



    @Test
    public void shouldUpdateTask() {
        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task updatedTask = new Task("task2", "taskTesting2");
        updatedTask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedTask.setDuration(Duration.ofMinutes(2));

        taskManager.addTask(task);
        updatedTask.setId(task.getId());
        int expected = updatedTask.getId();
        int actual = taskManager.updateTask(updatedTask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AAndNotUpdateNonexistentTask() {
        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task updatedTask = new Task("task2", "taskTesting2");
        updatedTask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedTask.setDuration(Duration.ofMinutes(2));

        taskManager.addTask(task);
        int expected = -1;
        int actual = taskManager.updateTask(updatedTask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        updatedEpic.setId(epic.getId());
        int expected = updatedEpic.getId();
        int actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AndNotUpdateNonexistentEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        int expected = -1;
        int actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus2AndNotUpdateWhenSubtasksIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        updatedEpic.setId(epic.getId());
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        int expected = -2;
        int actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus3AndNotUpdateEpicWhenStatusesIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        updatedEpic.setId(epic.getId());
        updatedEpic.setStatus(Status.DONE);
        int expected = -3;
        int actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldUpdateSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);

        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(2));

        int expected = updatedSubtask.getId();
        int actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AndNotUpdateNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(2));
        int expected = -1;
        int actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus2AndNotUpdateSubtaskWhenContainingEpicsIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setContainingEpicId(-1);
        updatedSubtask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(2));
        int expected = -2;
        int actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }



    @Test
    public void shouldRemoveTask() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        taskManager.addTask(task);
        taskManager.removeTask(task.getId());
        int expected = 0;
        int actual = taskManager.getTasks().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedTask() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        taskManager.addTask(task);
        Task actual = taskManager.removeTask(task.getId());
        assertEquals(task, actual);
    }

    @Test
    public void shouldRemoveEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        taskManager.removeEpic(epic.getId());
        int expected = 0;
        int actual = taskManager.getEpics().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Epic actual = taskManager.removeEpic(epic.getId());
        assertEquals(epic, actual);
    }

    @Test
    public void shouldRemoveContainedSubtasksWhenRemovingEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);

        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask2);

        taskManager.removeEpic(epic2.getId());
        List<Subtask> expected = new ArrayList<>(Arrays.asList(subtask));
        List<Subtask> actual = taskManager.getSubtasks();
        assertArrayEquals(actual.toArray(), expected.toArray());
    }

    @Test
    public void shouldReturnNullAndNotRemoveNonexistentEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        assertNull(taskManager.removeEpic(-1));
    }

    @Test
    public void shouldRemoveSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        taskManager.removeSubtask(subtask.getId());
        int expected = 0;
        int actual = taskManager.getSubtasks().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        Subtask actual = taskManager.removeSubtask(subtask.getId());
        assertEquals(subtask, actual);
    }

    @Test
    public void shouldRemoveSubtaskFromContainingEpicWhenRemovingSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        taskManager.removeSubtask(subtask.getId());
        int expected = 0;
        int actual = taskManager.findEpic(epic.getId()).getSubtasksIds().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullAndNotRemoveNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        assertNull(taskManager.removeSubtask(-1));
    }



    @Test
    public void shouldReturnIdOfUpdatedTaskWhenUpdatingTaskStatus() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        taskManager.addTask(task);
        Task updatedTask = new Task("task2", "taskTesting2");
        updatedTask.setId(task.getId());
        updatedTask.setStatus(Status.DONE);
        updatedTask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedTask.setDuration(Duration.ofMinutes(2));
        int actual = taskManager.updateTask(updatedTask);
        int expected = task.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnUpdatedSubtaskIdWhenUpdatingSubtaskStatus() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        updatedSubtask.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(2));
        int actual = taskManager.updateSubtask(updatedSubtask);
        int expected = subtask.getId();
        assertEquals(expected, actual);
    }



    @Test
    public void shouldReturnEpicSubtasksIds() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);

        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask2);

        List<Integer> expected = new ArrayList<>(Arrays.asList(subtask.getId(), subtask2.getId()));
        List<Integer> actual = taskManager.getEpicSubtasksIds(epic.getId());
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldReturnNullWhenTryingToGetNonexistentEpicSubtasksIds() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask2);
        assertNull(taskManager.getEpicSubtasksIds(-1));
    }



    @Test void shouldChangeStatusOfEpicToIN_PROGRESSWhenStatusesOfContainedSubtasksIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3");
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4");
        Subtask subtask5 = new Subtask("subtask5", "subtaskTesting5");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        subtask.setContainingEpicId(epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));

        subtask2.setContainingEpicId(epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));

        subtask3.setContainingEpicId(epic2.getId());
        subtask3.setStartTime(LocalDateTime.of(3,1,1, 0,0,0));
        subtask3.setDuration(Duration.ofMinutes(3));

        subtask4.setContainingEpicId(epic2.getId());
        subtask4.setStartTime(LocalDateTime.of(4,1,1, 0,0,0));
        subtask4.setDuration(Duration.ofMinutes(4));

        subtask5.setContainingEpicId(epic.getId());
        subtask5.setStartTime(LocalDateTime.of(5,1,1, 0,0,0));
        subtask5.setDuration(Duration.ofMinutes(5));

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.NEW);
        updatedSubtask.setStartTime(LocalDateTime.of(6,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(6));

        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.DONE);
        updatedSubtask2.setStartTime(LocalDateTime.of(7,1,1, 0,0,0));
        updatedSubtask2.setDuration(Duration.ofMinutes(7));

        Subtask updatedSubtask3 = new Subtask("updatedSubtask3", "updatedSubtaskTesting3", epic2.getId());
        updatedSubtask3.setId(subtask3.getId());
        updatedSubtask3.setStatus(Status.IN_PROGRESS);
        updatedSubtask3.setStartTime(LocalDateTime.of(8,1,1, 0,0,0));
        updatedSubtask3.setDuration(Duration.ofMinutes(8));

        Subtask updatedSubtask4 = new Subtask("updatedSubtask4", "updatedSubtaskTesting4", epic2.getId());
        updatedSubtask4.setId(subtask4.getId());
        updatedSubtask4.setStatus(Status.IN_PROGRESS);
        updatedSubtask4.setStartTime(LocalDateTime.of(9,1,1, 0,0,0));
        updatedSubtask4.setDuration(Duration.ofMinutes(9));

        taskManager.updateSubtask(updatedSubtask);
        taskManager.updateSubtask(updatedSubtask2);
        taskManager.updateSubtask(updatedSubtask3);
        taskManager.updateSubtask(updatedSubtask4);

        List<Status> actualStatusesOfEpics = new ArrayList<>();
        actualStatusesOfEpics.add(epic.getStatus());
        actualStatusesOfEpics.add(epic2.getStatus());
        List<Status> expectedStatusesOfEpics = new ArrayList<>();
        expectedStatusesOfEpics.add(Status.IN_PROGRESS);
        expectedStatusesOfEpics.add(Status.IN_PROGRESS);

        assertArrayEquals(expectedStatusesOfEpics.toArray(),actualStatusesOfEpics.toArray());
    }

    @Test
    void shouldChangeStatusOfEpicToDONEWhenAllStatusesOfContainedSubtasksIsDONE() {
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        taskManager.addEpic(epic);

        subtask.setContainingEpicId(epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        subtask2.setContainingEpicId(epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        updatedSubtask.setStartTime(LocalDateTime.of(3,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(3));
        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.DONE);
        updatedSubtask2.setStartTime(LocalDateTime.of(4,1,1, 0,0,0));
        updatedSubtask2.setDuration(Duration.ofMinutes(4));

        taskManager.updateSubtask(updatedSubtask);
        taskManager.updateSubtask(updatedSubtask2);

        Status actual = epic.getStatus();
        Status expected = Status.DONE;

        assertEquals(expected, actual);
    }

    @Test
    void shouldChangeStatusOfEpicToNEWWhenAllStatusesOfContainedSubtasksIsNEW() {
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));

        taskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        Status actual = epic.getStatus();
        Status expected = Status.NEW;

        assertEquals(expected, actual);
    }

    @Test
    void shouldChangeStatusOfEpicNEWWhenItHaveNotSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Status actual = epic.getStatus();
        Status expected = Status.NEW;
        assertEquals(expected, actual);
    }

    @Test
    void shouldChangeStatusOfEpicNEWWhenRemovingSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        Subtask subtask5 = new Subtask("subtask5", "subtaskTesting5");
        subtask5.setStartTime(LocalDateTime.of(3,1,1, 0,0,0));
        subtask5.setDuration(Duration.ofMinutes(3));

        taskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        subtask5.setContainingEpicId(epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        updatedSubtask.setStartTime(LocalDateTime.of(4,1,1, 0,0,0));
        updatedSubtask.setDuration(Duration.ofMinutes(4));

        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.NEW);
        updatedSubtask2.setStartTime(LocalDateTime.of(5,1,1, 0,0,0));
        updatedSubtask2.setDuration(Duration.ofMinutes(5));

        taskManager.updateSubtask(updatedSubtask);
        taskManager.updateSubtask(updatedSubtask2);

        List<Status> actualStatusesOfEpics = new ArrayList<>();
        actualStatusesOfEpics.add(epic.getStatus());
        List<Status> expectedStatusesOfEpics = new ArrayList<>();
        expectedStatusesOfEpics.add(Status.IN_PROGRESS);

        taskManager.removeSubtask(subtask.getId());
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.NEW);

        Subtask updatedSubtask22 = new Subtask("updatedSubtask22", "updatedSubtaskTesting22", epic.getId());
        updatedSubtask22.setId(subtask2.getId());
        updatedSubtask22.setStatus(Status.DONE);
        updatedSubtask22.setStartTime(LocalDateTime.of(6,1,1, 0,0,0));
        updatedSubtask22.setDuration(Duration.ofMinutes(6));

        Subtask updatedSubtask5 = new Subtask("updatedSubtask5", "updatedSubtaskTesting5", epic.getId());
        updatedSubtask5.setId(subtask5.getId());
        updatedSubtask5.setStatus(Status.DONE);
        updatedSubtask5.setStartTime(LocalDateTime.of(7,1,1, 0,0,0));
        updatedSubtask5.setDuration(Duration.ofMinutes(7));

        taskManager.updateSubtask(updatedSubtask22);
        taskManager.updateSubtask(updatedSubtask5);

        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.DONE);

        taskManager.clearSubtasks();
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.NEW);

        assertArrayEquals(expectedStatusesOfEpics.toArray(),actualStatusesOfEpics.toArray());
    }



    @Test
    public void shouldAddReviewedTasksToViewsHistory() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        task2.setDuration(Duration.ofMinutes(2));

        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.findTask(task.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task));
        List<Task> actual = taskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldAddReviewedEpicsToViewsHistory() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.findEpic(epic.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(epic));
        List<Task> actual = taskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldAddReviewedSubtasksToViewsHistory() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.findSubtask(subtask.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(subtask));
        List<Task> actual = taskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldAddReviewedAllTypesOfTasksToView() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        task2.setDuration(Duration.ofMinutes(2));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.findTask(task.getId());

        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.findEpic(epic.getId());

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(3,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(3));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(4,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(4));

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.findSubtask(subtask.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task, epic, subtask));
        List<Task> actual = taskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldReturnUpdatedHistoryWhenViewingTaskThatAlreadyViewed() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        task2.setDuration(Duration.ofMinutes(2));

        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.findTask(task.getId());
        taskManager.findTask(task2.getId());
        taskManager.findTask(task.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task2, task));
        List<Task> actual = taskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }




    @Test
    public void shouldRemoveTaskFromHistoryWhenRemovingTaskFromManager() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        task2.setDuration(Duration.ofMinutes(2));
        Task task3 = new Task("task3", "taskTesting3");
        task3.setStartTime(LocalDateTime.of(3,1,1, 0,0,0));
        task3.setDuration(Duration.ofMinutes(3));

        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.findTask(task.getId());
        taskManager.findTask(task2.getId());
        taskManager.findTask(task3.getId());
        taskManager.removeTask(task2.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task, task3));
        List<Task> actual = taskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldRemoveTaskFromHistoryWhenClearingTaskFromManager() {
        for (int i = 0; i < 10; i++) {
            Task task = new Task("task" + i, "taskTesting" + i);
            task.setStartTime(LocalDateTime.of(i+1,1,1, 0,0,0));
            task.setDuration(Duration.ofMinutes(i+1));
            taskManager.addTask(task);
            taskManager.findTask(task.getId());
        }
        taskManager.clearTasks();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveEpicFromHistoryWhenRemovingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        taskManager.findEpic(epic.getId());
        taskManager.removeEpic(epic.getId());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveEpicFromHistoryWhenClearingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.findEpic(epic.getId());
        taskManager.clearEpics();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenRemovingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        taskManager.findSubtask(subtask.getId());
        taskManager.removeEpic(epic.getId());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenClearingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        taskManager.addSubtask(subtask);
        taskManager.findSubtask(subtask.getId());
        taskManager.clearEpics();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenRemovingSubtaskFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.findSubtask(subtask.getId());
        taskManager.findSubtask(subtask2.getId());
        taskManager.removeSubtask(subtask.getId());
        assertArrayEquals(new Subtask[]{subtask2}, taskManager.getHistory().toArray());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenClearingSubtaskFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(2));
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.findSubtask(subtask.getId());
        taskManager.findSubtask(subtask2.getId());
        taskManager.clearSubtasks();
        assertEquals(0, taskManager.getHistory().size());
    }



    @Test
    public void shouldThrowNullTimeExceptionWhenAddingTaskWithNullStartTimeOrNullDuration() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.now());
        Task task3 = new Task("task3", "taskTesting3");
        task3.setDuration(Duration.ofHours(1));

        assertThrows(NullTimesOfTaskException.class, () -> {
            taskManager.addTask(task);
        });
        assertThrows(NullTimesOfTaskException.class, () -> {
            taskManager.addTask(task2);
        });
        assertThrows(NullTimesOfTaskException.class, () -> {
            taskManager.addTask(task3);
        });
    }

    @Test
    public void shouldUpdateTimeInTask() {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(1));
        taskManager.addTask(task);
        Task updatedTask = new Task("updatedTask", "updatedTaskTesting");
        updatedTask.setStartTime(LocalDateTime.now().plusHours(1));
        updatedTask.setDuration(Duration.ofHours(10));
        updatedTask.setId(task.getId());
        taskManager.updateTask(updatedTask);
        assertEquals(updatedTask.getStartTime().get(), taskManager.findTask(updatedTask.getId()).getStartTime().get());
    }

    @Test
    public void shouldSetTimesToEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        LocalDateTime start = LocalDateTime.now();

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(start);
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(start.plusHours(1));
        subtask2.setDuration(Duration.ofMinutes(2));
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3", epic.getId());
        subtask3.setStartTime(start.plusHours(2));
        subtask3.setDuration(Duration.ofMinutes(3));
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4", epic.getId());
        subtask4.setStartTime(start.plusHours(3));
        subtask4.setDuration(Duration.ofMinutes(4));

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setStartTime(start.plusHours(4));
        updatedSubtask.setDuration(Duration.ofMinutes(5));
        updatedSubtask.setId(subtask.getId());

        taskManager.removeSubtask(subtask4.getId());
        taskManager.updateSubtask(updatedSubtask);

        assertEquals(epic.getStartTime().get(), start.plusHours(1));
        assertEquals(epic.getEndTime().get(), start.plusHours(4).plusMinutes(5));
        assertEquals(epic.getDuration().get(), Duration.ofMinutes(10));
    }

    @Test
    public void shouldShouldPrioritizeTasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        LocalDateTime start = LocalDateTime.now();

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(start);
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(start.plusHours(1));
        subtask2.setDuration(Duration.ofMinutes(2));
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3", epic.getId());
        subtask3.setStartTime(start.plusHours(2));
        subtask3.setDuration(Duration.ofMinutes(3));
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4", epic.getId());
        subtask4.setStartTime(start.plusHours(3));
        subtask4.setDuration(Duration.ofMinutes(4));

        Task task = new Task("task1", "taskTesting");
        task.setStartTime(start.plusHours(4));
        task.setDuration(Duration.ofMinutes(6));

        taskManager.addTask(task);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask);

        List<Long> actual = taskManager.getPrioritizedTasks().stream()
                .map(currentTask -> currentTask.getStartTime().get().toEpochSecond(ZoneOffset.UTC))
                .toList();

        List<Long> expected = new ArrayList<>(Arrays.asList(
                subtask.getStartTime().get().toEpochSecond(ZoneOffset.UTC),
                subtask2.getStartTime().get().toEpochSecond(ZoneOffset.UTC),
                subtask3.getStartTime().get().toEpochSecond(ZoneOffset.UTC),
                subtask4.getStartTime().get().toEpochSecond(ZoneOffset.UTC),
                task.getStartTime().get().toEpochSecond(ZoneOffset.UTC)));

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldThrowTasksOverlapsInTimeExceptionWhenAddingOverlapingTask() {
        Task task = new Task("task1", "taskTesting");
        LocalDateTime start = LocalDateTime.now();
        task.setStartTime(start);
        task.setDuration(Duration.ofHours(1));
        taskManager.addTask(task);

        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(start.plusMinutes(30));
        task2.setDuration(Duration.ofHours(1));

        Task task3 = new Task("task3", "taskTesting3");
        task3.setStartTime(start.plusHours(2));
        task3.setDuration(Duration.ofHours(1));

        assertThrows(TasksOverlapsInTimeException.class, () -> {
            taskManager.addTask(task2);
        });
        assertDoesNotThrow(() -> {
            taskManager.addTask(task3);
        });
    }
}