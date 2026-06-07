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
 * Unit tests for IndustrialFan.
 *
 * Tests cover sell and deposit effects including:
 * - Worker credit addition on sell
 * - Descriptive message content on sell
 * - Company credit addition on deposit
 * - Worker healing on deposit
 * - Healing cap at maximum HP
 * - Inventory removal after sell and deposit
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class IndustrialFanTest {

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
     * Normal case: IndustrialFan sell adds 150 worker credits.
     */
    @Test
    void industrialFanSellAdds150WorkerCredits() {
        IndustrialFan fan = new IndustrialFan();
        fan.sell(worker, wallet, map.at(3, 3));
        assertEquals(150, wallet.getCredits());
    }

    /**
     * Normal case: IndustrialFan sell returns descriptive message.
     */
    @Test
    void industrialFanSellReturnsDescriptiveMessage() {
        IndustrialFan fan = new IndustrialFan();
        String result = fan.sell(worker, wallet, map.at(3, 3));
        assertTrue(result.contains("Industrial Fan"));
        assertTrue(result.contains("150"));
    }

    /**
     * Normal case: IndustrialFan deposit adds 10 company credits.
     */
    @Test
    void industrialFanDepositAdds10CompanyCredits() {
        IndustrialFan fan = new IndustrialFan();
        fan.deposit(worker, quotaSystem, map.at(3, 3));
        assertEquals(10, quotaSystem.getCompanyCredits());
    }

    /**
     * Normal case: IndustrialFan deposit heals worker 10 HP.
     */
    @Test
    void industrialFanDepositHealsWorker10HP() {
        worker.hurt(50);
        IndustrialFan fan = new IndustrialFan();
        fan.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(worker.toString().contains("60/100"));
    }

    /**
     * Boundary case: IndustrialFan deposit does not heal above max HP.
     */
    @Test
    void industrialFanDepositDoesNotHealAboveMaxHP() {
        worker.hurt(5);
        IndustrialFan fan = new IndustrialFan();
        fan.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(worker.toString().contains("100/100"));
    }

    /**
     * Edge case: IndustrialFan removes from inventory after sell.
     */
    @Test
    void industrialFanRemovesFromInventoryAfterSell() {
        IndustrialFan fan = new IndustrialFan();
        worker.getInventory().add(fan);
        fan.removeAfterSell(worker);
        assertFalse(worker.getInventory().getItems().contains(fan));
    }

    /**
     * Edge case: IndustrialFan removes from inventory after deposit.
     */
    @Test
    void industrialFanRemovesFromInventoryAfterDeposit() {
        IndustrialFan fan = new IndustrialFan();
        worker.getInventory().add(fan);
        fan.removeAfterDeposit(worker);
        assertFalse(worker.getInventory().getItems().contains(fan));
    }
}