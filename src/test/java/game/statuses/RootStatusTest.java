package game.statuses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RootStatus in REQ3 Eclipse Weather System.
 *
 * Tests countdown and expiry behaviour.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
class RootStatusTest {

    /**
     * Tests that RootStatus starts active with four remaining ticks.
     */
    @Test
    void rootStatusStartsActive() {
        RootStatus rootStatus = new RootStatus();

        assertTrue(rootStatus.isStatusActive());
        assertEquals(4, rootStatus.getRemainingTurns());
    }

    /**
     * Tests that RootStatus decreases after each tick.
     */
    @Test
    void rootStatusCountsDownEachTurn() {
        RootStatus rootStatus = new RootStatus();

        rootStatus.tickStatus(null, null);

        assertEquals(3, rootStatus.getRemainingTurns());
        assertTrue(rootStatus.isStatusActive());
    }

    /**
     * Tests that RootStatus expires after all turns pass.
     */
    @Test
    void rootStatusExpiresAfterFinalTurn() {
        RootStatus rootStatus = new RootStatus();

        while (rootStatus.getRemainingTurns() > 0) {
            rootStatus.tickStatus(null, null);
        }

        assertFalse(rootStatus.isStatusActive());
        assertEquals(0, rootStatus.getRemainingTurns());
    }
}