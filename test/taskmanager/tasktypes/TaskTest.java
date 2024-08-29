package taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import taskmanager.utility.Status;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void taskNameGetterAndSetter() {
        Task task = new Task("Task Name", "Task Description");
        String expected = "test";
        task.setName(expected);
        assertEquals(expected, task.getName());
    }

    @Test
    public void taskDescriptionGetterAndSetter() {
        Task task = new Task("Task Name", "Task Description");
        String expected = "test";
        task.setDescription(expected);
        assertEquals(expected, task.getDescription());
    }

    @Test
    public void taskIdGetterAndSetter() {
        Task task = new Task("Task Name", "Task Description");
        int expected = 10;
        task.setId(expected);
        assertEquals(expected, task.getId());
    }

    @Test
    public void taskStatusGetterAndSetter() {
        Task task = new Task("Task Name", "Task Description");
        Status expected = Status.DONE;
        task.setStatus(expected);
        assertEquals(expected, task.getStatus());
    }

    @Test
    public void newTaskStatusIsNEW() {
        Task task = new Task("Task Name", "Task Description");
        Status expected = Status.NEW;
        Status actual = task.getStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void sameTaskButDifferentObjectsShouldBeEqual() {
        Task task1 = new Task("Task Name", "Task Description");
        Task task2 = new Task("Task2 Name", "Task2 Description");
        task1.setId(10);
        task2.setId(10);
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);
        assertEquals(task1, task2);
    }

    @Test
    public void tasksWithDifferentIdShouldNotBeEqual() {
        Task task1 = new Task("Task Name", "Task Description");
        Task task2 = new Task("Task Name", "Task Description");
        task1.setId(10);
        assertNotEquals(task1, task2);
    }

    @Test
    public void hashCodesOfSameTasksShouldBeEqual() {
        Task task1 = new Task("Task Name", "Task Description");
        Task task2 = new Task("Task2 Name", "Task2 Description");
        task1.setId(10);
        task2.setId(10);
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void hashCodesOfDifferentTasksShouldNotBeEqual() {
        Task task1 = new Task("Task Name", "Task Description");
        Task task2 = new Task("Task Name", "Task Description");
        task1.setId(10);
        task2.setStatus(Status.DONE);
        assertNotEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void toStringOfTask() {
        Task task = new Task("Task Name", "Task Description");
        task.setStatus(Status.DONE);
        task.setId(10);
        String expected = "Task{name='Task Name', description='Task Description', id=10, status=DONE}";
        assertEquals(expected, task.toString());
    }
}