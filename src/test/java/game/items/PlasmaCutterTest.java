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
 * Unit tests for PlasmaCutter.
 *
 * Tests cover purchase effects including:
 * - Worker credit deduction on buy
 * - Inventory addition on buy
 * - Worker damage on buy
 * - Purchase failure at boundary of 49 credits
 * - Purchase success at boundary of exactly 50 credits
 * - Purchase failure when wallet is empty
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class PlasmaCutterTest {

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
     * Normal case: PlasmaCutter buy deducts 50 worker credits.
     */
    @Test
    void plasmaCutterBuyDeducts50WorkerCredits() {
        wallet.addCredits(200);
        PlasmaCutter cutter = new PlasmaCutter();
        cutter.buy(worker, wallet, map.at(3, 3));
        assertEquals(150, wallet.getCredits());
    }

    /**
     * Normal case: PlasmaCutter buy adds cutter to worker inventory.
     */
    @Test
    void plasmaCutterBuyAddsCutterToInventory() {
        wallet.addCredits(200);
        PlasmaCutter cutter = new PlasmaCutter();
        cutter.buy(worker, wallet, map.at(3, 3));
        assertTrue(worker.getInventory().getItems().contains(cutter));
    }

    /**
     * Normal case: PlasmaCutter buy deals 5 damage to worker.
     */
    @Test
    void plasmaCutterBuyDeals5DamageToWorker() {
        wallet.addCredits(200);
        PlasmaCutter cutter = new PlasmaCutter();
        cutter.buy(worker, wallet, map.at(3, 3));
        assertTrue(worker.toString().contains("95/100"));
    }

    /**
     * Boundary case: PlasmaCutter buy fails when credits exactly 49.
     */
    @Test
    void plasmaCutterBuyFailsWhenCreditsExactly49() {
        wallet.addCredits(49);
        PlasmaCutter cutter = new PlasmaCutter();
        String result = cutter.buy(worker, wallet, map.at(3, 3));
        assertTrue(result.contains("cannot afford"));
        assertEquals(49, wallet.getCredits());
    }

    /**
     * Boundary case: PlasmaCutter buy succeeds when credits exactly 50.
     */
    @Test
    void plasmaCutterBuySucceedsWhenCreditsExactly50() {
        wallet.addCredits(50);
        PlasmaCutter cutter = new PlasmaCutter();
        String result = cutter.buy(worker, wallet, map.at(3, 3));
        assertFalse(result.contains("cannot afford"));
        assertEquals(0, wallet.getCredits());
    }

    /**
     * Edge case: PlasmaCutter buy fails when wallet is empty.
     */
    @Test
    void plasmaCutterBuyFailsWhenWalletIsEmpty() {
        PlasmaCutter cutter = new PlasmaCutter();
        String result = cutter.buy(worker, wallet, map.at(3, 3));
        assertTrue(result.contains("cannot afford"));
        assertFalse(worker.getInventory().getItems().contains(cutter));
    }
}