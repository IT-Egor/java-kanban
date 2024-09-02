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

class InMemoryTaskManagerTest {

    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
    }


    @Test
    public void shouldAddTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        List<Task> actual = inMemoryTaskManager.getTasks();
        assertTrue(actual.contains(task));
    }

    @Test
    public void shouldAddEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        List<Epic> actual = inMemoryTaskManager.getEpics();
        assertTrue(actual.contains(epic));
    }

    @Test
    public void shouldNotAddSubtaskToNonexistentEpic() {
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", -1);
        inMemoryTaskManager.addSubtask(subtask);
        List<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldAddSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        List<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertTrue(actual.contains(subtask));
    }

    @Test
    public void shouldAddSubtaskToEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        List<Epic> actual = inMemoryTaskManager.getEpics();
        assertTrue(actual.contains(epic));
    }



    @Test
    public void shouldNotFindNonexistentTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        assertNull(inMemoryTaskManager.findTask(-1));
    }

    @Test
    public void shouldNotFindNonexistentEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        assertNull(inMemoryTaskManager.findEpic(-1));
    }

    @Test
    public void shouldNotFindNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        assertNull(inMemoryTaskManager.findSubtask(-1));
    }

    @Test
    public void shouldFindTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        Task actual = inMemoryTaskManager.findTask(task.getId());
        assertEquals(task, actual);
    }

    @Test
    public void shouldFindEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Epic actual = inMemoryTaskManager.findEpic(epic.getId());
        assertEquals(epic, actual);
    }

    @Test
    public void shouldFindSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask actual = inMemoryTaskManager.findSubtask(subtask.getId());
        assertEquals(subtask, actual);
    }



    @Test
    public void shouldClearTasks() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.clearTasks();
        List<Task> actual = inMemoryTaskManager.getTasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearEpics() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.clearEpics();
        List<Epic> actual = inMemoryTaskManager.getEpics();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearSubtasksWhenClearingEpics() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.clearEpics();
        List<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.clearSubtasks();
        List<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldClearSubtasksInEpicsWhenClearingSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.clearSubtasks();
        int expected = 0;
        int actual = epic.getSubtasksIds().size() + epic2.getSubtasksIds().size();
        assertEquals(expected, actual);
    }



    @Test
    public void shouldUpdateTask() {
        Task task = new Task("task1", "taskTesting1");
        Task updatedTask = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        updatedTask.setId(task.getId());
        int expected = updatedTask.getId();
        int actual = inMemoryTaskManager.updateTask(updatedTask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AAndNotUpdateNonexistentTask() {
        Task task = new Task("task1", "taskTesting1");
        Task updatedTask = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        int expected = -1;
        int actual = inMemoryTaskManager.updateTask(updatedTask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        updatedEpic.setId(epic.getId());
        int expected = updatedEpic.getId();
        int actual = inMemoryTaskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AndNotUpdateNonexistentEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        int expected = -1;
        int actual = inMemoryTaskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus2AndNotUpdateWhenSubtasksIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        updatedEpic.setId(epic.getId());
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        int expected = -2;
        int actual = inMemoryTaskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus3AndNotUpdateEpicWhenStatusesIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic updatedEpic = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        updatedEpic.setId(epic.getId());
        updatedEpic.setStatus(Status.DONE);
        int expected = -3;
        int actual = inMemoryTaskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldUpdateSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        int expected = updatedSubtask.getId();
        int actual = inMemoryTaskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus1AndNotUpdateNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        int expected = -1;
        int actual = inMemoryTaskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMinus2AndNotUpdateSubtaskWhenContainingEpicsIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setContainingEpicId(-1);
        int expected = -2;
        int actual = inMemoryTaskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual);
    }



    @Test
    public void shouldRemoveTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.removeTask(task.getId());
        int expected = 0;
        int actual = inMemoryTaskManager.getTasks().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        Task actual = inMemoryTaskManager.removeTask(task.getId());
        assertEquals(task, actual);
    }

    @Test
    public void shouldRemoveEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.removeEpic(epic.getId());
        int expected = 0;
        int actual = inMemoryTaskManager.getEpics().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Epic actual = inMemoryTaskManager.removeEpic(epic.getId());
        assertEquals(epic, actual);
    }

    @Test
    public void shouldRemoveContainedSubtasksWhenRemovingEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.removeEpic(epic2.getId());
        List<Subtask> expected = new ArrayList<>(Arrays.asList(subtask));
        List<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertArrayEquals(actual.toArray(), expected.toArray());
    }

    @Test
    public void shouldReturnNullAndNotRemoveNonexistentEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        assertNull(inMemoryTaskManager.removeEpic(-1));
    }

    @Test
    public void shouldRemoveSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.removeSubtask(subtask.getId());
        int expected = 0;
        int actual = inMemoryTaskManager.getSubtasks().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRemovedSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask actual = inMemoryTaskManager.removeSubtask(subtask.getId());
        assertEquals(subtask, actual);
    }

    @Test
    public void shouldRemoveSubtaskFromContainingEpicWhenRemovingSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.removeSubtask(subtask.getId());
        int expected = 0;
        int actual = inMemoryTaskManager.findEpic(epic.getId()).getSubtasksIds().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullAndNotRemoveNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        assertNull(inMemoryTaskManager.removeSubtask(-1));
    }



    @Test
    public void shouldReturnIdOfUpdatedTaskWhenUpdatingTaskStatus() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        Task updatedTask = new Task("task2", "taskTesting2");
        updatedTask.setId(task.getId());
        updatedTask.setStatus(Status.DONE);
        int actual = inMemoryTaskManager.updateTask(updatedTask);
        int expected = task.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnUpdatedSubtaskIdWhenUpdatingSubtaskStatus() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        int actual = inMemoryTaskManager.updateSubtask(updatedSubtask);
        int expected = subtask.getId();
        assertEquals(expected, actual);
    }



    @Test
    public void shouldReturnEpicSubtasksIds() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        inMemoryTaskManager.addSubtask(subtask2);
        List<Integer> expected = new ArrayList<>(Arrays.asList(subtask.getId(), subtask2.getId()));
        List<Integer> actual = inMemoryTaskManager.getEpicSubtasksIds(epic.getId());
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldReturnNullWhenTryingToGetNonexistentEpicSubtasksIds() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        inMemoryTaskManager.addSubtask(subtask2);
        assertNull(inMemoryTaskManager.getEpicSubtasksIds(-1));
    }



    @Test void shouldChangeStatusOfEpicToIN_PROGRESSWhenStatusesOfContainedSubtasksIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3");
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4");
        Subtask subtask5 = new Subtask("subtask5", "subtaskTesting5");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        subtask3.setContainingEpicId(epic2.getId());
        subtask4.setContainingEpicId(epic2.getId());
        subtask5.setContainingEpicId(epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addSubtask(subtask4);

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

        inMemoryTaskManager.updateSubtask(updatedSubtask);
        inMemoryTaskManager.updateSubtask(updatedSubtask2);
        inMemoryTaskManager.updateSubtask(updatedSubtask3);
        inMemoryTaskManager.updateSubtask(updatedSubtask4);

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
        inMemoryTaskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.DONE);

        inMemoryTaskManager.updateSubtask(updatedSubtask);
        inMemoryTaskManager.updateSubtask(updatedSubtask2);

        Status actual = epic.getStatus();
        Status expected = Status.DONE;

        assertEquals(expected, actual);
    }

    @Test
    void shouldChangeStatusOfEpicToNEWWhenAllStatusesOfContainedSubtasksIsNEW() {
        Epic epic = new Epic("epic1", "epicTesting");
        Subtask subtask = new Subtask("subtask1", "subtaskTesting");
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2");
        inMemoryTaskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);

        Status actual = epic.getStatus();
        Status expected = Status.NEW;

        assertEquals(expected, actual);
    }

    @Test
    void shouldChangeStatusOfEpicNEWWhenItHaveNotSubtasks() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
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
        inMemoryTaskManager.addEpic(epic);
        subtask.setContainingEpicId(epic.getId());
        subtask2.setContainingEpicId(epic.getId());
        subtask5.setContainingEpicId(epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtaskTesting", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        Subtask updatedSubtask2 = new Subtask("updatedSubtask2", "updatedSubtaskTesting2", epic.getId());
        updatedSubtask2.setId(subtask2.getId());
        updatedSubtask2.setStatus(Status.NEW);

        inMemoryTaskManager.updateSubtask(updatedSubtask);
        inMemoryTaskManager.updateSubtask(updatedSubtask2);

        List<Status> actualStatusesOfEpics = new ArrayList<>();
        actualStatusesOfEpics.add(epic.getStatus());
        List<Status> expectedStatusesOfEpics = new ArrayList<>();
        expectedStatusesOfEpics.add(Status.IN_PROGRESS);

        inMemoryTaskManager.removeSubtask(subtask.getId());
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.NEW);

        Subtask updatedSubtask22 = new Subtask("updatedSubtask22", "updatedSubtaskTesting22", epic.getId());
        updatedSubtask22.setId(subtask2.getId());
        updatedSubtask22.setStatus(Status.DONE);
        Subtask updatedSubtask5 = new Subtask("updatedSubtask5", "updatedSubtaskTesting5", epic.getId());
        updatedSubtask5.setId(subtask5.getId());
        updatedSubtask5.setStatus(Status.DONE);

        inMemoryTaskManager.updateSubtask(updatedSubtask22);
        inMemoryTaskManager.updateSubtask(updatedSubtask5);

        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.DONE);

        inMemoryTaskManager.clearSubtasks();
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.NEW);

        assertArrayEquals(expectedStatusesOfEpics.toArray(),actualStatusesOfEpics.toArray());
    }



    @Test
    public void shouldAddReviewedTasksToViewsHistory() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.findTask(task.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task));
        List<Task> actual = inMemoryTaskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldAddReviewedEpicsToViewsHistory() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.findEpic(epic.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(epic));
        List<Task> actual = inMemoryTaskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldAddReviewedSubtasksToViewsHistory() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.findSubtask(subtask.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(subtask));
        List<Task> actual = inMemoryTaskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldAddReviewedAllTypesOfTasksToView() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.findTask(task.getId());
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.findEpic(epic.getId());
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.findSubtask(subtask.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task, epic, subtask));
        List<Task> actual = inMemoryTaskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldReturnUpdatedHistoryWhenViewingTaskThatAlreadyViewed() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.findTask(task.getId());
        inMemoryTaskManager.findTask(task2.getId());
        inMemoryTaskManager.findTask(task.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task2, task));
        List<Task> actual = inMemoryTaskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }




    @Test
    public void shouldRemoveTaskFromHistoryWhenRemovingTaskFromManager() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        Task task3 = new Task("task3", "taskTesting3");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.findTask(task.getId());
        inMemoryTaskManager.findTask(task2.getId());
        inMemoryTaskManager.findTask(task3.getId());
        inMemoryTaskManager.removeTask(task2.getId());
        List<Task> expected = new ArrayList<>(Arrays.asList(task, task3));
        List<Task> actual = inMemoryTaskManager.getHistory();
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    public void shouldRemoveTaskFromHistoryWhenClearingTaskFromManager() {
        for (int i = 0; i < 10; i++) {
            Task task = new Task("task" + i, "taskTesting" + i);
            inMemoryTaskManager.addTask(task);
            inMemoryTaskManager.findTask(task.getId());
        }
        inMemoryTaskManager.clearTasks();
        assertEquals(0,inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveEpicFromHistoryWhenRemovingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.findEpic(epic.getId());
        inMemoryTaskManager.removeEpic(epic.getId());
        assertEquals(0,inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveEpicFromHistoryWhenClearingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.findEpic(epic.getId());
        inMemoryTaskManager.clearEpics();
        assertEquals(0,inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenRemovingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.findSubtask(subtask.getId());
        inMemoryTaskManager.removeEpic(epic.getId());
        assertEquals(0,inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenClearingEpicFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.findSubtask(subtask.getId());
        inMemoryTaskManager.clearEpics();
        assertEquals(0,inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenRemovingSubtaskFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.findSubtask(subtask.getId());
        inMemoryTaskManager.findSubtask(subtask2.getId());
        inMemoryTaskManager.removeSubtask(subtask.getId());
        assertArrayEquals(new Subtask[]{subtask2}, inMemoryTaskManager.getHistory().toArray());
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryWhenClearingSubtaskFromManager() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.findSubtask(subtask.getId());
        inMemoryTaskManager.findSubtask(subtask2.getId());
        inMemoryTaskManager.clearSubtasks();
        assertEquals(0,inMemoryTaskManager.getHistory().size());
    }
}