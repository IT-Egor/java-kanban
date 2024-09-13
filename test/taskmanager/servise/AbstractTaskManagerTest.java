package taskmanager.servise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Managers;
import taskmanager.utility.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        taskManager.addSubtask(subtask);
        List<Subtask> actual = taskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldAddSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        taskManager.addSubtask(subtask);
        List<Subtask> actual = taskManager.getSubtasks();
        assertTrue(actual.contains(subtask));
    }

    @Test
    public void shouldAddSubtaskToEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        taskManager.addSubtask(subtask);
        List<Epic> actual = taskManager.getEpics();
        assertTrue(actual.contains(epic));
    }



    @Test
    public void shouldNotFindNonexistentTask() {
        Task task = new Task("task1", "taskTesting");
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
        taskManager.addSubtask(subtask);
        assertNull(taskManager.findSubtask(-1));
    }

    @Test
    public void shouldFindTask() {
        Task task = new Task("task1", "taskTesting");
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
        taskManager.addSubtask(subtask);
        Subtask actual = taskManager.findSubtask(subtask.getId());
        assertEquals(subtask, actual);
    }



    @Test
    public void shouldClearTasks() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
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
        Task updatedTask = new Task("task2", "taskTesting2");
        taskManager.addTask(task);
        updatedTask.setId(task.getId());
        int expected = updatedTask.getId();
        int actual = taskManager.updateTask(updatedTask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AAndNotUpdateNonexistentTask() {
        Task task = new Task("task1", "taskTesting1");
        Task updatedTask = new Task("task2", "taskTesting2");
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
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        int expected = updatedSubtask.getId();
        int actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AndNotUpdateNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        int expected = -1;
        int actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus2AndNotUpdateSubtaskWhenContainingEpicsIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setContainingEpicId(-1);
        int expected = -2;
        int actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }



    @Test
    public void shouldRemoveTask() {
        Task task = new Task("task1", "taskTesting");
        taskManager.addTask(task);
        taskManager.removeTask(task.getId());
        int expected = 0;
        int actual = taskManager.getTasks().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedTask() {
        Task task = new Task("task1", "taskTesting");
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
        taskManager.addSubtask(subtask);
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
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
        taskManager.addSubtask(subtask);
        Subtask actual = taskManager.removeSubtask(subtask.getId());
        assertEquals(subtask, actual);
    }

    @Test
    public void shouldRemoveSubtaskFromContainingEpicWhenRemovingSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
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
        taskManager.addSubtask(subtask);
        assertNull(taskManager.removeSubtask(-1));
    }



    @Test
    public void shouldReturnIdOfUpdatedTaskWhenUpdatingTaskStatus() {
        Task task = new Task("task1", "taskTesting");
        taskManager.addTask(task);
        Task updatedTask = new Task("task2", "taskTesting2");
        updatedTask.setId(task.getId());
        updatedTask.setStatus(Status.DONE);
        int actual = taskManager.updateTask(updatedTask);
        int expected = task.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnUpdatedSubtaskIdWhenUpdatingSubtaskStatus() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        int actual = taskManager.updateSubtask(updatedSubtask);
        int expected = subtask.getId();
        assertEquals(expected, actual);
    }



    @Test
    public void shouldReturnEpicSubtasksIds() {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
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
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
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
        subtask2.setContainingEpicId(epic.getId());
        subtask3.setContainingEpicId(epic2.getId());
        subtask4.setContainingEpicId(epic2.getId());
        subtask5.setContainingEpicId(epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.NEW);
        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.DONE);
        Subtask updatedSubtask3 = new Subtask("updatedSubtask3", "updatedSubtaskTesting3", epic2.getId());
        updatedSubtask3.setId(subtask3.getId());
        updatedSubtask3.setStatus(Status.IN_PROGRESS);
        Subtask updatedSubtask4 = new Subtask("updatedSubtask4", "updatedSubtaskTesting4", epic2.getId());
        updatedSubtask4.setId(subtask4.getId());
        updatedSubtask4.setStatus(Status.IN_PROGRESS);

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
        subtask2.setContainingEpicId(epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.DONE);

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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        Subtask subtask5 = new Subtask("subtask5", "subtaskTesting5");
        taskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        subtask5.setContainingEpicId(epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.NEW);

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
        Subtask updatedSubtask5 = new Subtask("updatedSubtask5", "updatedSubtaskTesting5", epic.getId());
        updatedSubtask5.setId(subtask5.getId());
        updatedSubtask5.setStatus(Status.DONE);

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
        Task task2 = new Task("task2", "taskTesting2");
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
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
        Task task2 = new Task("task2", "taskTesting2");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.findTask(task.getId());
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.findEpic(epic.getId());
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
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
        Task task2 = new Task("task2", "taskTesting2");
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
        Task task2 = new Task("task2", "taskTesting2");
        Task task3 = new Task("task3", "taskTesting3");
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
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
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.findSubtask(subtask.getId());
        taskManager.findSubtask(subtask2.getId());
        taskManager.clearSubtasks();
        assertEquals(0, taskManager.getHistory().size());
    }
}