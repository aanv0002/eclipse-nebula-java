package game.items;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import game.quota.QuotaSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AlienArtifact.
 *
 * Tests cover sell and deposit effects including:
 * - Worker credit addition on sell
 * - Descriptive message content on sell and deposit
 * - Company credit addition on deposit
 * - Quota met through single deposit
 * - Inventory removal after sell
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class AlienArtifactTest {

    private GameMap map;
    private ContractedWorker worker;
    private Wallet wallet;
    private QuotaSystem quotaSystem;

    /**
     * Sets up a fresh test map, worker, wallet and quota system before each test.
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

        quotaSystem = new QuotaSystem();
    }

    /**
     * Normal case: AlienArtifact sell adds 200 worker credits.
     */
    @Test
    void alienArtifactSellAdds200WorkerCredits() {
        AlienArtifact artifact = new AlienArtifact();
        artifact.sell(worker, wallet, map.at(3, 3));
        assertEquals(200, wallet.getCredits());
    }

    /**
     * Normal case: AlienArtifact sell returns descriptive message.
     */
    @Test
    void alienArtifactSellReturnsDescriptiveMessage() {
        AlienArtifact artifact = new AlienArtifact();
        String result = artifact.sell(worker, wallet, map.at(3, 3));
        assertTrue(result.contains("Alien Artifact"));
        assertTrue(result.contains("200"));
    }

    /**
     * Normal case: AlienArtifact deposit adds 100 company credits.
     */
    @Test
    void alienArtifactDepositAdds100CompanyCredits() {
        AlienArtifact artifact = new AlienArtifact();
        artifact.deposit(worker, quotaSystem, map.at(3, 3));
        assertEquals(100, quotaSystem.getCompanyCredits());
    }

    /**
     * Boundary case: AlienArtifact deposit alone meets quota exactly.
     */
    @Test
    void alienArtifactDepositAloneMeetsQuota() {
        AlienArtifact artifact = new AlienArtifact();
        artifact.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(quotaSystem.isQuotaMet());
    }

    /**
     * Edge case: AlienArtifact removes from inventory after sell.
     */
    @Test
    void alienArtifactRemovesFromInventoryAfterSell() {
        AlienArtifact artifact = new AlienArtifact();
        worker.getInventory().add(artifact);
        artifact.removeAfterSell(worker);
        assertFalse(worker.getInventory().getItems().contains(artifact));
    }

    /**
     * Edge case: AlienArtifact deposit returns descriptive message.
     */
    @Test
    void alienArtifactDepositReturnsDescriptiveMessage() {
        AlienArtifact artifact = new AlienArtifact();
        String result = artifact.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(result.contains("Alien Artifact"));
        assertTrue(result.contains("100"));
    }
}