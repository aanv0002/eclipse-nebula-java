package game.quota;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QuotaSystem.
 *
 * Tests cover quota tracking and cycle management including:
 * - Base values on initialisation
 * - Company credit accumulation
 * - Quota detection at boundary conditions
 * - Reset and increase behaviour
 * - Deadline detection at boundary conditions
 * - Turn counter decrement behaviour
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class QuotaSystemTest {

    private GameMap map;
    private QuotaSystem quotaSystem;

    /**
     * Sets up a fresh test map and quota system before each test.
     *
     * @throws Exception if map setup fails
     */
    @BeforeEach
    void setUp() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        map = new GameMap("Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        game.world.EclipseNebula world = new game.world.EclipseNebula(new Display());
        world.addGameMap(map);

        quotaSystem = new QuotaSystem();
    }

    /**
     * Normal case: QuotaSystem starts with correct base values.
     */
    @Test
    void quotaSystemStartsWithCorrectBaseValues() {
        assertEquals(100, quotaSystem.getQuotaTarget());
        assertEquals(0, quotaSystem.getCompanyCredits());
        assertFalse(quotaSystem.isQuotaMet());
        assertFalse(quotaSystem.isDeadlineReached());
    }

    /**
     * Normal case: QuotaSystem correctly accumulates company credits.
     */
    @Test
    void quotaSystemAccumulatesCompanyCredits() {
        quotaSystem.addCompanyCredits(50);
        assertEquals(50, quotaSystem.getCompanyCredits());
        quotaSystem.addCompanyCredits(30);
        assertEquals(80, quotaSystem.getCompanyCredits());
    }

    /**
     * Boundary case: QuotaSystem detects quota met at exactly 100 credits.
     */
    @Test
    void quotaSystemDetectsQuotaMetAtExactly100Credits() {
        quotaSystem.addCompanyCredits(100);
        assertTrue(quotaSystem.isQuotaMet());
    }

    /**
     * Boundary case: QuotaSystem not met at 99 credits.
     */
    @Test
    void quotaSystemNotMetAt99Credits() {
        quotaSystem.addCompanyCredits(99);
        assertFalse(quotaSystem.isQuotaMet());
    }

    /**
     * Normal case: QuotaSystem increases quota by 5% rounded up on reset.
     */
    @Test
    void quotaSystemIncreasesQuotaBy5PercentRoundedUp() {
        quotaSystem.resetAndIncrease();
        assertEquals(105, quotaSystem.getQuotaTarget()); // 100 * 1.05 = 105
    }

    /**
     * Normal case: QuotaSystem increases time limit by 10% rounded up on reset.
     */
    @Test
    void quotaSystemIncreasesTimeLimitBy10PercentRoundedUp() {
        quotaSystem.resetAndIncrease();
        assertEquals(220, quotaSystem.getRemainingTurns()); // 200 * 1.10 = 220
    }

    /**
     * Normal case: QuotaSystem resets company credits to zero after reset.
     */
    @Test
    void quotaSystemResetsCompanyCreditsAfterReset() {
        quotaSystem.addCompanyCredits(100);
        quotaSystem.resetAndIncrease();
        assertEquals(0, quotaSystem.getCompanyCredits());
    }

    /**
     * Normal case: QuotaSystem detects deadline after 200 ticks.
     */
    @Test
    void quotaSystemDetectsDeadlineAfter200Ticks() {
        for (int i = 0; i < 200; i++) {
            quotaSystem.tick();
        }
        assertTrue(quotaSystem.isDeadlineReached());
    }

    /**
     * Boundary case: QuotaSystem not at deadline at 199 ticks.
     */
    @Test
    void quotaSystemNotAtDeadlineAt199Ticks() {
        for (int i = 0; i < 199; i++) {
            quotaSystem.tick();
        }
        assertFalse(quotaSystem.isDeadlineReached());
    }

    /**
     * Edge case: QuotaSystem remaining turns decrements correctly.
     */
    @Test
    void quotaSystemRemainingTurnsDecrementsCorrectly() {
        assertEquals(200, quotaSystem.getRemainingTurns());
        quotaSystem.tick();
        assertEquals(199, quotaSystem.getRemainingTurns());
        quotaSystem.tick();
        assertEquals(198, quotaSystem.getRemainingTurns());
    }
}