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
 * Unit tests for AluminiumScrap.
 *
 * Tests cover deposit effects including:
 * - Company credit accumulation per deposit
 * - Descriptive message content
 * - Inventory removal after deposit
 * - Multiple deposit accumulation
 * - Quota met through deposits alone
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class AluminiumScrapTest {

    private GameMap map;
    private ContractedWorker worker;
    private QuotaSystem quotaSystem;

    /**
     * Sets up a fresh test map, worker and quota system before each test.
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

        quotaSystem = new QuotaSystem();
    }

    /**
     * Normal case: AluminiumScrap deposit adds 50 company credits.
     */
    @Test
    void aluminiumScrapDepositAdds50CompanyCredits() {
        AluminiumScrap scrap = new AluminiumScrap();
        scrap.deposit(worker, quotaSystem, map.at(3, 3));
        assertEquals(50, quotaSystem.getCompanyCredits());
    }

    /**
     * Normal case: AluminiumScrap deposit returns descriptive message.
     */
    @Test
    void aluminiumScrapDepositReturnsDescriptiveMessage() {
        AluminiumScrap scrap = new AluminiumScrap();
        String result = scrap.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(result.contains("Aluminium Scrap"));
        assertTrue(result.contains("50"));
    }

    /**
     * Normal case: AluminiumScrap removes itself from inventory after deposit.
     */
    @Test
    void aluminiumScrapRemovesFromInventoryAfterDeposit() {
        AluminiumScrap scrap = new AluminiumScrap();
        worker.getInventory().add(scrap);
        assertTrue(worker.getInventory().getItems().contains(scrap));
        scrap.removeAfterDeposit(worker);
        assertFalse(worker.getInventory().getItems().contains(scrap));
    }

    /**
     * Boundary case: Multiple AluminiumScrap deposits accumulate correctly.
     */
    @Test
    void multipleAluminiumScrapDepositsAccumulateCorrectly() {
        AluminiumScrap scrap1 = new AluminiumScrap();
        AluminiumScrap scrap2 = new AluminiumScrap();
        scrap1.deposit(worker, quotaSystem, map.at(3, 3));
        scrap2.deposit(worker, quotaSystem, map.at(3, 3));
        assertEquals(100, quotaSystem.getCompanyCredits());
    }

    /**
     * Edge case: Two AluminiumScrap deposits meet quota exactly.
     */
    @Test
    void twoAluminiumScrapDepositsCanMeetQuota() {
        for (int i = 0; i < 2; i++) {
            new AluminiumScrap().deposit(worker, quotaSystem, map.at(3, 3));
        }
        assertTrue(quotaSystem.isQuotaMet());
    }
}