package game.items;

import game.actors.ContractedWorker;
import game.inventory.WeightLimitedInventory;
import game.statuses.RootStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MoldedApple in REQ3 Eclipse Weather System.
 *
 * Tests consumption behaviour and RootStatus application.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
class MoldedAppleTest {

    /**
     * Creates a test worker.
     *
     * @return test worker
     */
    private ContractedWorker createWorker() {
        return new ContractedWorker("#1 Test Worker", 'ඞ', 10,
                new WeightLimitedInventory(50));
    }

    /**
     * Tests that MoldedApple can be consumed before use.
     */
    @Test
    void moldedAppleCanBeConsumedBeforeUse() {
        MoldedApple moldedApple = new MoldedApple();

        assertTrue(moldedApple.canConsume());
    }

    /**
     * Tests that MoldedApple applies RootStatus when consumed.
     */
    @Test
    void moldedAppleAppliesRootStatusWhenConsumed() {
        ContractedWorker worker = createWorker();
        MoldedApple moldedApple = new MoldedApple();

        moldedApple.consume(worker);

        assertFalse(worker.statusesOf(RootStatus.class).isEmpty());
    }

    /**
     * Tests that MoldedApple becomes consumed after use.
     */
    @Test
    void moldedAppleBecomesConsumedAfterUse() {
        ContractedWorker worker = createWorker();
        MoldedApple moldedApple = new MoldedApple();

        String result = moldedApple.consume(worker);

        assertTrue(result.contains("rooted"));
        assertTrue(moldedApple.isConsumedUp());
        assertFalse(moldedApple.canConsume());
    }
}