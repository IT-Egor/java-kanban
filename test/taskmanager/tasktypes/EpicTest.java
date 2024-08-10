package taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import taskmanager.utility.Status;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void epicSubtasksIdGetterAndSetterTest() {
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description");
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description");
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(subtask1.getId(), subtask2.getId()));
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());
        assertArrayEquals(expected.toArray(), epic.getSubtasksIds().toArray());
    }

    @Test
    public void epicSubtasksIdsGetterAndSetterTest() {
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description");
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description");
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(subtask1.getId(), subtask2.getId()));
        epic.setSubtasksIds(expected);
        assertArrayEquals(expected.toArray(), epic.getSubtasksIds().toArray());
    }

    @Test
    public void epicRemoveSubtaskTest() {
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description");
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description");
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());
        epic.removeSubtaskId(subtask2.getId());
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(subtask1.getId()));
        assertArrayEquals(expected.toArray(), epic.getSubtasksIds().toArray());
    }

    @Test
    public void shouldChangeNothingWhenTryingToRemoveNonexistentSubtask() {
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description");
        epic.addSubtaskId(subtask1.getId());
        ArrayList<Integer> expected = epic.getSubtasksIds();
        epic.removeSubtaskId(-1);
        assertArrayEquals(expected.toArray(), epic.getSubtasksIds().toArray());
    }

    @Test
    public void shouldReturnSubtasksIdsWhenClearingSubtasksIds() {
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description");
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description");
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(subtask1.getId(), subtask2.getId()));
        ArrayList<Integer> actual = epic.clearSubtasksIds();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void epicToStringTest() {
        Epic epic = new Epic("Epic Name", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description");
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description");
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());
        epic.setId(10);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        epic.setStatus(Status.DONE);
        String expected = "Epic{name='Epic Name', description='Epic Description', id=10, status=DONE, subtasks.size=2}";
        assertEquals(expected, epic.toString());
    }
}