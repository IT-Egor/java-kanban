package taskmanager.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void everyNextIdShouldIncrementsTo1() {
        int firstId = Managers.getNextId();
        int secondId = Managers.getNextId();
        int thirdId = Managers.getNextId();
        assertTrue((secondId - firstId == 1 && thirdId - secondId == 1));
    }
}