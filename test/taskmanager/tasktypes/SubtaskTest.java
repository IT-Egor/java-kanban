package taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import taskmanager.utility.Status;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void subtaskConstructorWithContainingEpicId() {
        Subtask subtask = new Subtask("Subtask Name", "Subtask Description", 10);
        assertEquals(10, subtask.getContainingEpicId());
    }

    @Test
    public void subtaskContainingIdGetterAndSetterTest() {
        Subtask subtask = new Subtask("Subtask Name", "Subtask Description");
        int expected = 10;
        subtask.setContainingEpicId(expected);
        assertEquals(expected, subtask.getContainingEpicId());
    }

    @Test
    public void subtaskToStringTest() {
        Subtask subtask = new Subtask("Subtask Name", "Subtask Description");
        subtask.setId(10);
        subtask.setContainingEpicId(11);
        subtask.setStatus(Status.DONE);
        String expected = "Subtask{name='Subtask Name', description='Subtask Description', id=10, status=DONE, containingEpicId=11}";
        assertEquals(expected, subtask.toString());
    }

}