package game.statuses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StunnedStatusTest {

    @Test
    void tickStatus() {
        StunnedStatus status = new StunnedStatus(2);

        assertTrue(status.isStatusActive(),
                "StunnedStatus must be active when created with duration greater than 0");

        status.tickStatus(null, null);

        assertTrue(status.isStatusActive(),
                "StunnedStatus must remain active after only one of two turns has passed");

        status.tickStatus(null, null);

        assertFalse(status.isStatusActive(),
                "StunnedStatus must become inactive after its duration reaches 0");
    }

    @Test
    void isStatusActive() {
        StunnedStatus activeStatus = new StunnedStatus(1);

        assertTrue(activeStatus.isStatusActive(),
                "StunnedStatus with duration 1 must start active");

        activeStatus.tickStatus(null, null);

        assertFalse(activeStatus.isStatusActive(),
                "StunnedStatus must be inactive after one tick when duration is 1");

        StunnedStatus inactiveStatus = new StunnedStatus(0);

        assertFalse(inactiveStatus.isStatusActive(),
                "StunnedStatus with duration 0 must start inactive");
    }
}