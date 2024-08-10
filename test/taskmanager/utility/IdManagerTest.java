package taskmanager.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdManagerTest {

    @Test
    public void firstIdIs1() {
        assertEquals(1, IdManager.generateId());
    }

    @Test
    public void everyNextIdShouldIncrementsTo1() {
        int firstId = IdManager.generateId();
        int secondId = IdManager.generateId();
        int thirdId = IdManager.generateId();
        assertTrue((secondId - firstId == 1 && thirdId - secondId == 1));
    }

}