package game.items;

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
 * Unit tests for Wallet.
 *
 * Tests cover credit management including:
 * - Adding credits normally and at cap boundary
 * - Deducting credits normally and at boundary
 * - Insufficient credit handling
 * - Zero credit edge cases
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class WalletTest {

    private GameMap map;
    private ContractedWorker worker;
    private Wallet wallet;

    /**
     * Sets up a fresh test map, worker and wallet before each test.
     *
     * @throws Exception if map or actor setup fails
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

        worker = new ContractedWorker("#1 Test Worker", 'ඞ', 100,
                new WeightLimitedInventory(50));
        map.addActor(worker, map.at(3, 3));

        wallet = worker.asCapability(WalletCarrier.class)
                .map(WalletCarrier::getWallet)
                .orElseThrow();
    }

    /**
     * Normal case: wallet adds credits correctly.
     */
    @Test
    void walletAddsCreditsCorrectly() {
        wallet.addCredits(100);
        assertEquals(100, wallet.getCredits());
    }

    /**
     * Boundary case: wallet caps at exactly 1000 credits.
     */
    @Test
    void walletCapsAtMaximumOf1000Credits() {
        wallet.addCredits(999);
        wallet.addCredits(999);
        assertEquals(1000, wallet.getCredits());
    }

    /**
     * Edge case: wallet adding zero credits changes nothing.
     */
    @Test
    void walletAddingZeroCreditsChangesNothing() {
        wallet.addCredits(0);
        assertEquals(0, wallet.getCredits());
    }

    /**
     * Normal case: wallet deducts credits correctly.
     */
    @Test
    void walletDeductsCreditsCorrectly() {
        wallet.addCredits(500);
        assertTrue(wallet.deductCredits(200));
        assertEquals(300, wallet.getCredits());
    }

    /**
     * Boundary case: wallet deducts exactly all credits.
     */
    @Test
    void walletDeductsExactlyAllCredits() {
        wallet.addCredits(100);
        assertTrue(wallet.deductCredits(100));
        assertEquals(0, wallet.getCredits());
    }

    /**
     * Edge case: wallet returns false when insufficient credits.
     */
    @Test
    void walletReturnsFalseWhenInsufficientCredits() {
        wallet.addCredits(50);
        assertFalse(wallet.deductCredits(100));
        assertEquals(50, wallet.getCredits());
    }

    /**
     * Edge case: wallet deducting zero credits always succeeds.
     */
    @Test
    void walletDeductingZeroCreditsSucceeds() {
        wallet.addCredits(100);
        assertTrue(wallet.deductCredits(0));
        assertEquals(100, wallet.getCredits());
    }
}