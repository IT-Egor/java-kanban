package taskmanager.servise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Status;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }



    @Test
    public void shouldAddTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        ArrayList<Task> expected = new ArrayList<>(Arrays.asList(task));
        ArrayList<Task> actual = inMemoryTaskManager.getTasks();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldAddEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        ArrayList<Epic> expected = new ArrayList<>(Arrays.asList(epic));
        ArrayList<Epic> actual = inMemoryTaskManager.getEpics();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldNotAddSubtaskToNonexistentEpic() {
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", -1);
        inMemoryTaskManager.addSubtask(subtask);
        ArrayList<Subtask> expected = new ArrayList<>();
        ArrayList<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldAddSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        ArrayList<Subtask> expected = new ArrayList<>(Arrays.asList(subtask));
        ArrayList<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldAddSubtaskToEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        ArrayList<Epic> expected = new ArrayList<>(Arrays.asList(epic));
        ArrayList<Epic> actual = inMemoryTaskManager.getEpics();
        assertArrayEquals(expected.toArray(), actual.toArray());
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
    public void shouldNotAddExistingTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        int expected = -5;
        int actual = inMemoryTaskManager.addTask(task);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotAddExistingEpic() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        int expected = -5;
        int actual = inMemoryTaskManager.addEpic(epic);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotAddExistingSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        int expected = -5;
        int actual = inMemoryTaskManager.addSubtask(subtask);
        assertEquals(expected, actual);
    }




    @Test
    public void shouldClearTasks() {
        Task task = new Task("task1", "taskTesting");
        Task task2 = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.clearTasks();
        ArrayList<Task> expected = new ArrayList<>();
        ArrayList<Task> actual = inMemoryTaskManager.getTasks();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldClearEpics() {
        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.clearEpics();
        ArrayList<Epic> expected = new ArrayList<>();
        ArrayList<Epic> actual = inMemoryTaskManager.getEpics();
        assertArrayEquals(expected.toArray(), actual.toArray());
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
        ArrayList<Subtask> expected = new ArrayList<>();
        ArrayList<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertArrayEquals(expected.toArray(), actual.toArray());
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
        ArrayList<Subtask> expected = new ArrayList<>();
        ArrayList<Subtask> actual = inMemoryTaskManager.getSubtasks();
        assertArrayEquals(expected.toArray(), actual.toArray());
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
    public void shouldReturnMinus3AndNotUpdateTaskWhenStatusesIsDifferent() {
        Task task = new Task("task1", "taskTesting1");
        Task updatedTask = new Task("task2", "taskTesting2");
        inMemoryTaskManager.addTask(task);
        updatedTask.setId(task.getId());
        updatedTask.setStatus(Status.DONE);
        int expected = -3;
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
    public void shouldReturnMinus3AndNotUpdateSubtaskWhenStatusesIsDifferent() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask updatedSubtask = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStatus(Status.DONE);
        int expected = -3;
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
        ArrayList<Subtask> expected = new ArrayList<>(Arrays.asList(subtask));
        ArrayList<Subtask> actual = inMemoryTaskManager.getSubtasks();
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
    public void shouldReturnTaskWithUpdatedStatus() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.setTaskStatus(task.getId(), Status.DONE);
        Task expected = new Task("task1", "taskTesting");
        expected.setStatus(Status.DONE);
        expected.setId(task.getId());
        assertEquals(expected, task);
    }

    @Test
    public void shouldReturnNullWhenTryingToSetStatusToNonexistentTask() {
        Task task = new Task("task1", "taskTesting");
        inMemoryTaskManager.addTask(task);
        assertNull(inMemoryTaskManager.setTaskStatus(-1, Status.DONE));
    }

    @Test
    public void shouldReturnSubtaskWithUpdatedStatus() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask actual = inMemoryTaskManager.setSubtaskStatus(subtask.getId(), Status.DONE);
        Subtask expected = new Subtask("subtask1", "subtaskTesting", epic.getId());
        expected.setStatus(Status.DONE);
        expected.setId(subtask.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullWhenTryingToSetStatusToNonexistentSubtask() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        assertNull(inMemoryTaskManager.setSubtaskStatus(-1, Status.DONE));
    }



    @Test
    public void shouldReturnEpicSubtasksIds() {
        Epic epic = new Epic("epic1", "epicTesting");
        inMemoryTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        inMemoryTaskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        inMemoryTaskManager.addSubtask(subtask2);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(subtask.getId(), subtask2.getId()));
        ArrayList<Integer> actual = inMemoryTaskManager.getEpicSubtasksIds(epic.getId());
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

        inMemoryTaskManager.setSubtaskStatus(subtask.getId(), Status.NEW);
        inMemoryTaskManager.setSubtaskStatus(subtask2.getId(), Status.DONE);
        inMemoryTaskManager.setSubtaskStatus(subtask3.getId(), Status.IN_PROGRESS);
        inMemoryTaskManager.setSubtaskStatus(subtask4.getId(), Status.IN_PROGRESS);

        ArrayList<Status> actualStatusesOfEpics = new ArrayList<>();
        actualStatusesOfEpics.add(epic.getStatus());
        actualStatusesOfEpics.add(epic2.getStatus());
        ArrayList<Status> expectedStatusesOfEpics = new ArrayList<>();
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

        inMemoryTaskManager.setSubtaskStatus(subtask.getId(), Status.DONE);
        inMemoryTaskManager.setSubtaskStatus(subtask2.getId(), Status.DONE);

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

        inMemoryTaskManager.setSubtaskStatus(subtask.getId(), Status.DONE);
        inMemoryTaskManager.setSubtaskStatus(subtask2.getId(), Status.NEW);

        ArrayList<Status> actualStatusesOfEpics = new ArrayList<>();
        actualStatusesOfEpics.add(epic.getStatus());
        ArrayList<Status> expectedStatusesOfEpics = new ArrayList<>();
        expectedStatusesOfEpics.add(Status.IN_PROGRESS);

        inMemoryTaskManager.removeSubtask(subtask.getId());
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.NEW);

        inMemoryTaskManager.setSubtaskStatus(subtask2.getId(), Status.DONE);
        inMemoryTaskManager.setSubtaskStatus(subtask5.getId(), Status.DONE);
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.DONE);

        inMemoryTaskManager.clearSubtasks();
        actualStatusesOfEpics.add(epic.getStatus());
        expectedStatusesOfEpics.add(Status.NEW);

        assertArrayEquals(expectedStatusesOfEpics.toArray(),actualStatusesOfEpics.toArray());
    }
}