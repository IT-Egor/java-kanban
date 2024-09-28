package taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import taskmanager.utility.Status;

import java.time.Duration;
import java.time.LocalDateTime;

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
        subtask.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        subtask.setDuration(Duration.ZERO);
        String expected = "Subtask{name='Subtask Name', description='Subtask Description', id=10, status=DONE, " +
                "containingEpicId=11, startTime='01-01-0001 00:00:00', endTime='01-01-0001 00:00:00', duration='0m'}";
        assertEquals(expected, subtask.toString());
    }

}