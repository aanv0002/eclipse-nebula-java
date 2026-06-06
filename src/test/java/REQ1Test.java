import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.actors.Slime;
import game.grounds.Dirt;
import game.grounds.Vent;
import game.grounds.AluminiumDoor;
import game.inventory.WeightLimitedInventory;
import game.items.*;
import game.quota.QuotaSystem;
import game.spawners.VentCreatureSpawner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for REQ1: Quota System.
 *
 * Tests cover the following components:
 * - Wallet credit management
 * - QuotaSystem tracking and cycle management
 * - AluminiumScrap deposit effects
 * - IndustrialFan sell and deposit effects
 * - AlienArtifact sell and deposit effects
 * - PlasmaCutter purchase effects
 * - AlienCube cut effects
 * - AluminiumDoor cut effects
 * - Vent cut effects
 *
 * Each component has at least three test cases covering:
 * - Normal/typical input
 * - Boundary conditions
 * - Edge/invalid cases
 *
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class REQ1Test {

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

    // WALLET TESTS

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
        assertEquals(50, wallet.getCredits()); // unchanged
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

    // QUOTA SYSTEM TESTS

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

    // ALUMINIUM SCRAP TESTS

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
     * Edge case: AluminiumScrap deposit can meet quota alone.
     */
    @Test
    void twoAluminiumScrapDepositsCanMeetQuota() {
        for (int i = 0; i < 2; i++) {
            new AluminiumScrap().deposit(worker, quotaSystem, map.at(3, 3));
        }
        assertTrue(quotaSystem.isQuotaMet());
    }

    // INDUSTRIAL FAN TESTS

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
        worker.hurt(50); // reduce HP first
        IndustrialFan fan = new IndustrialFan();
        fan.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(worker.toString().contains("60/100"));
    }

    /**
     * Boundary case: IndustrialFan deposit heals worker but not above max HP.
     */
    @Test
    void industrialFanDepositDoesNotHealAboveMaxHP() {
        worker.hurt(5); // only 5 damage taken
        IndustrialFan fan = new IndustrialFan();
        fan.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(worker.toString().contains("100/100")); // capped at max
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

    // ALIEN ARTIFACT TESTS

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
     * Boundary case: AlienArtifact deposit alone meets quota.
     */
    @Test
    void alienArtifactDepositAloneMeetsQuota() {
        AlienArtifact artifact = new AlienArtifact();
        artifact.deposit(worker, quotaSystem, map.at(3, 3));
        assertTrue(quotaSystem.isQuotaMet()); // FIXED: 100 credits meets 100 target exactly
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

    //  PLASMA CUTTER TESTS

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
        assertEquals(49, wallet.getCredits()); // unchanged
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

    //  ALIEN CUBE CUT TESTS

    /**
     * Normal case: AlienCube cut drops AlienArtifact at worker location.
     */
    @Test
    void alienCubeCutDropsAlienArtifactAtWorkerLocation() {
        AlienCube cube = new AlienCube();
        worker.getInventory().add(cube);
        cube.cut(worker, map);
        boolean hasArtifact = map.at(3, 3).getItems().stream()
                .anyMatch(item -> item.toString().contains("Alien Artifact"));
        assertTrue(hasArtifact);
    }

    /**
     * Normal case: AlienCube cut removes cube from inventory.
     */
    @Test
    void alienCubeCutRemovesCubeFromInventory() {
        AlienCube cube = new AlienCube();
        worker.getInventory().add(cube);
        cube.cut(worker, map);
        assertFalse(worker.getInventory().getItems().contains(cube));
    }

    /**
     * Normal case: AlienCube cut poisons worker.
     */
    @Test
    void alienCubeCutPoisonsWorker() {
        AlienCube cube = new AlienCube();
        worker.getInventory().add(cube);
        cube.cut(worker, map);
        assertFalse(worker.statusesOf(game.statuses.PoisonStatus.class).isEmpty());
    }

    /**
     * Normal case: AlienCube cut returns descriptive message.
     */
    @Test
    void alienCubeCutReturnsDescriptiveMessage() {
        AlienCube cube = new AlienCube();
        worker.getInventory().add(cube);
        String result = cube.cut(worker, map);
        assertTrue(result.contains("Alien Cube"));
        assertTrue(result.contains("poisoned"));
    }

    /**
     * Boundary case: AlienCube cut on empty inventory still works.
     */
    @Test
    void alienCubeCutWorksWhenOnlyItemInInventory() {
        AlienCube cube = new AlienCube();
        worker.getInventory().add(cube);
        assertEquals(1, worker.getInventory().getItems().size());
        cube.cut(worker, map);
        assertEquals(0, worker.getInventory().getItems().size());
    }

    /**
     * Edge case: AlienCube getCutDescription returns correct description.
     */
    @Test
    void alienCubeGetCutDescriptionReturnsCorrectText() {
        AlienCube cube = new AlienCube();
        assertTrue(cube.getCutDescription().contains("Alien Cube"));
    }

    // ALUMINIUM DOOR CUT TESTS

    /**
     * Normal case: AluminiumDoor cut drops AluminiumScrap.
     */
    @Test
    void aluminiumDoorCutDropsAluminiumScrap() {
        map.at(3, 2).setGround(new AluminiumDoor());
        AluminiumDoor door = (AluminiumDoor) map.at(3, 2).getGround();
        door.cut(worker, map);
        boolean hasScrap = map.at(3, 2).getItems().stream()
                .anyMatch(item -> item.toString().contains("Aluminium Scrap"));
        assertTrue(hasScrap);
    }

    /**
     * Normal case: AluminiumDoor cut transforms tile into Floor.
     */
    @Test
    void aluminiumDoorCutTransformsTileIntoFloor() {
        map.at(3, 2).setGround(new AluminiumDoor());
        AluminiumDoor door = (AluminiumDoor) map.at(3, 2).getGround();
        door.cut(worker, map);
        assertTrue(map.at(3, 2).getGround().toString().contains("Floor"));
    }

    /**
     * Normal case: AluminiumDoor cut returns descriptive message.
     */
    @Test
    void aluminiumDoorCutReturnsDescriptiveMessage() {
        map.at(3, 2).setGround(new AluminiumDoor());
        AluminiumDoor door = (AluminiumDoor) map.at(3, 2).getGround();
        String result = door.cut(worker, map);
        assertTrue(result.contains("Aluminium Door"));
    }

    /**
     * Boundary case: AluminiumDoor getCutDescription returns correct text.
     */
    @Test
    void aluminiumDoorGetCutDescriptionReturnsCorrectText() {
        AluminiumDoor door = new AluminiumDoor();
        assertEquals("Aluminium Door", door.getCutDescription());
    }

    // VENT CUT TESTS

    /**
     * Normal case: Vent cut drops IndustrialFan.
     */
    @Test
    void ventCutDropsIndustrialFan() {
        map.at(3, 2).setGround(new Vent(new VentCreatureSpawner()));
        Vent vent = (Vent) map.at(3, 2).getGround();
        vent.cut(worker, map);
        boolean hasFan = map.at(3, 2).getItems().stream()
                .anyMatch(item -> item.toString().contains("Industrial Fan"));
        assertTrue(hasFan);
    }

    /**
     * Normal case: Vent cut transforms tile into Floor.
     */
    @Test
    void ventCutTransformsTileIntoFloor() {
        map.at(3, 2).setGround(new Vent(new VentCreatureSpawner()));
        Vent vent = (Vent) map.at(3, 2).getGround();
        vent.cut(worker, map);
        assertTrue(map.at(3, 2).getGround().toString().contains("Floor"));
    }

    /**
     * Normal case: Vent cut returns descriptive message.
     */
    @Test
    void ventCutReturnsDescriptiveMessage() {
        map.at(3, 2).setGround(new Vent(new VentCreatureSpawner()));
        Vent vent = (Vent) map.at(3, 2).getGround();
        String result = vent.cut(worker, map);
        assertTrue(result.contains("Vent"));
        assertTrue(result.contains("Industrial Fan"));
    }

    /**
     * Boundary case: Vent getCutDescription returns correct text.
     */
    @Test
    void ventGetCutDescriptionReturnsCorrectText() {
        Vent vent = new Vent(new VentCreatureSpawner());
        assertEquals("Vent", vent.getCutDescription());
    }

    /**
     * Edge case: Vent cut message mentions Undead spawn attempt.
     */
    @Test
    void ventCutMessageMentionsUndeadSpawn() {
        map.at(3, 2).setGround(new Vent(new VentCreatureSpawner()));
        Vent vent = (Vent) map.at(3, 2).getGround();
        String result = vent.cut(worker, map);
        assertTrue(result.contains("Undead") || result.contains("undead")
                || result.contains("crawl") || result.contains("spawn"));
    }
}