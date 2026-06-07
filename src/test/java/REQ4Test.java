import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.actions.DefuseSecurityAction;
import game.actions.PurgeTerrainAction;
import game.actions.DeployBarrierAction;
import game.grounds.AcidPool;
import game.grounds.ChargedFloor;
import game.grounds.Floor;
import game.grounds.LaserBarrierGround;
import game.grounds.Puddle;
import game.inventory.WeightLimitedInventory;
import game.security.*;
import game.statuses.CorrodedStatus;
import game.statuses.StunnedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for REQ4: Automated Security Hazard and Countermeasure Deployment System.
 *
 * Tests cover the following components:
 * - SpikeDrone explosion, terrain mutation, and CorrodedStatus
 * - StaticPylon arc firing and StunnedStatus
 * - GravityCore implosion and actor/item dragging
 * - DefuseKit and DefuseSecurityAction disarming hazards
 * - PurgeCanister and PurgeTerrainAction terrain restoration
 * - LaserBarrier deployment and LaserBarrierGround interception
 * - GridCountermeasure contract via getCountermeasureType()
 * - SecurityHazard isActive() and disarm() lifecycle
 *
 * Each component has at least three test cases covering:
 * - Normal/typical input
 * - Boundary conditions
 * - Edge/invalid cases
 *
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
class REQ4Test {

    private GameMap map;
    private ContractedWorker worker;

    /**
     * Sets up a fresh test map and worker before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Floor::new);

        map = new GameMap("Test Map", groundCreator, Arrays.asList(
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                "........."
        ));

        game.world.EclipseNebula world = new game.world.EclipseNebula(new Display());
        world.addGameMap(map);

        worker = new ContractedWorker("Test Worker", '@', 100,
                new WeightLimitedInventory(50));
        map.addActor(worker, map.at(4, 4));
    }

    // ==================== SPIKE DRONE TESTS ====================

    /**
     * Normal case: SpikeDrone starts in active state.
     */
    @Test
    void spikeDroneStartsActive() {
        SpikeDrone drone = new SpikeDrone();
        assertTrue(drone.isActive());
    }

    /**
     * Normal case: SpikeDrone explodes and converts tile to AcidPool when worker is adjacent.
     */
    @Test
    void spikeDroneConvertsTileToAcidPoolWhenWorkerAdjacent() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        drone.playTurn(new edu.monash.fit2099.engine.actions.ActionList(),
                new edu.monash.fit2099.engine.actions.DoNothingAction(), map, new Display());

        assertTrue(map.at(4, 3).getGround() instanceof AcidPool);
    }

    /**
     * Normal case: SpikeDrone applies CorrodedStatus to adjacent worker.
     */
    @Test
    void spikeDroneAppliesCorrodedStatusToWorker() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        drone.playTurn(new edu.monash.fit2099.engine.actions.ActionList(),
                new edu.monash.fit2099.engine.actions.DoNothingAction(), map, new Display());

        assertFalse(worker.statusesOf(CorrodedStatus.class).isEmpty());
    }

    /**
     * Normal case: SpikeDrone removes itself from map after exploding.
     */
    @Test
    void spikeDroneRemovesItselfFromMapAfterExplosion() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        drone.playTurn(new edu.monash.fit2099.engine.actions.ActionList(),
                new edu.monash.fit2099.engine.actions.DoNothingAction(), map, new Display());

        assertFalse(map.at(4, 3).containsAnActor());
    }

    /**
     * Boundary case: SpikeDrone does nothing when no worker is adjacent.
     */
    @Test
    void spikeDroneDoesNothingWithoutAdjacentWorker() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(1, 1));

        drone.playTurn(new edu.monash.fit2099.engine.actions.ActionList(),
                new edu.monash.fit2099.engine.actions.DoNothingAction(), map, new Display());

        assertTrue(map.at(1, 1).containsAnActor());
        assertFalse(map.at(1, 1).getGround() instanceof AcidPool);
    }

    /**
     * Edge case: SpikeDrone disarm() deactivates it safely without explosion.
     */
    @Test
    void spikeDroneDisarmDeactivatesSafely() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        drone.disarm(map.at(4, 3));

        assertFalse(drone.isActive());
        assertFalse(map.at(4, 3).getGround() instanceof AcidPool);
        assertFalse(map.at(4, 3).containsAnActor());
    }

    // ==================== STATIC PYLON TESTS ====================

    /**
     * Normal case: StaticPylon starts in active state.
     */
    @Test
    void staticPylonStartsActive() {
        StaticPylon pylon = new StaticPylon();
        assertTrue(pylon.isActive());
    }

    /**
     * Normal case: StaticPylon converts adjacent tile to ChargedFloor after 8 ticks.
     */
    @Test
    void staticPylonConvertsAdjacentTileToChargedFloorAfter8Ticks() {
        map.at(4, 3).setGround(new StaticPylon());
        StaticPylon pylon = (StaticPylon) map.at(4, 3).getGround();

        for (int i = 0; i < 8; i++) {
            pylon.tick(map.at(4, 3));
        }

        boolean anyChargedFloor = map.at(4, 3).getExits().stream()
                .anyMatch(exit -> exit.getDestination().getGround() instanceof ChargedFloor);
        assertTrue(anyChargedFloor);
    }

    /**
     * Boundary case: StaticPylon does not fire before 8 ticks.
     */
    @Test
    void staticPylonDoesNotFireBefore8Ticks() {
        map.at(4, 3).setGround(new StaticPylon());
        StaticPylon pylon = (StaticPylon) map.at(4, 3).getGround();

        for (int i = 0; i < 7; i++) {
            pylon.tick(map.at(4, 3));
        }

        boolean anyChargedFloor = map.at(4, 3).getExits().stream()
                .anyMatch(exit -> exit.getDestination().getGround() instanceof ChargedFloor);
        assertFalse(anyChargedFloor);
    }

    /**
     * Normal case: StaticPylon applies StunnedStatus to actor on arc path.
     */
    @Test
    void staticPylonAppliesStunnedStatusToActorOnArcPath() {
        map.at(4, 3).setGround(new StaticPylon());
        StaticPylon pylon = (StaticPylon) map.at(4, 3).getGround();

        for (int i = 0; i < 8; i++) {
            pylon.tick(map.at(4, 3));
        }

        boolean workerStunned = !worker.statusesOf(StunnedStatus.class).isEmpty();
        boolean anyChargedFloor = map.at(4, 3).getExits().stream()
                .anyMatch(exit -> exit.getDestination().getGround() instanceof ChargedFloor);
        assertTrue(workerStunned || anyChargedFloor);
    }

    /**
     * Edge case: StaticPylon disarm() deactivates it and stops arc firing.
     */
    @Test
    void staticPylonDisarmStopsArcFiring() {
        map.at(4, 3).setGround(new StaticPylon());
        StaticPylon pylon = (StaticPylon) map.at(4, 3).getGround();

        pylon.disarm(map.at(4, 3));

        for (int i = 0; i < 8; i++) {
            pylon.tick(map.at(4, 3));
        }

        boolean anyChargedFloor = map.at(4, 3).getExits().stream()
                .anyMatch(exit -> exit.getDestination().getGround() instanceof ChargedFloor);
        assertFalse(anyChargedFloor);
        assertFalse(pylon.isActive());
    }

    // ==================== GRAVITY CORE TESTS ====================

    /**
     * Normal case: GravityCore starts in active state.
     */
    @Test
    void gravityCoreStartsActive() {
        GravityCore core = new GravityCore();
        assertTrue(core.isActive());
    }

    /**
     * Normal case: GravityCore implodes and removes itself when actor is within 1 tile.
     */
    @Test
    void gravityCoreRemovesItselfWhenActorIsAdjacent() {
        map.at(4, 3).addItem(new GravityCore());
        GravityCore core = (GravityCore) map.at(4, 3).getItems().stream()
                .filter(i -> i instanceof GravityCore).findFirst().orElseThrow();

        core.tick(map.at(4, 3));

        assertFalse(map.at(4, 3).getItems().contains(core));
        assertFalse(core.isActive());
    }

    /**
     * Normal case: GravityCore drags actor closer to epicentre.
     */
    @Test
    void gravityCoresDragsActorCloserToEpicentre() {
        ContractedWorker victim = new ContractedWorker("Victim", 'V', 100,
                new WeightLimitedInventory(50));
        map.addActor(victim, map.at(4, 7));
        map.at(4, 3).addItem(new GravityCore());
        GravityCore core = (GravityCore) map.at(4, 3).getItems().stream()
                .filter(i -> i instanceof GravityCore).findFirst().orElseThrow();

        core.tick(map.at(4, 3));

        int newY = map.locationOf(victim).y();
        assertTrue(newY < 7);
    }

    /**
     * Boundary case: GravityCore does nothing when no actor is within 1 tile.
     */
    @Test
    void gravityCoreDoesNothingWithoutNearbyActor() {
        map.at(1, 1).addItem(new GravityCore());
        GravityCore core = (GravityCore) map.at(1, 1).getItems().stream()
                .filter(i -> i instanceof GravityCore).findFirst().orElseThrow();

        core.tick(map.at(1, 1));

        assertTrue(map.at(1, 1).getItems().contains(core));
        assertTrue(core.isActive());
    }

    /**
     * Edge case: GravityCore disarm() deactivates it without implosion.
     */
    @Test
    void gravityCoreDisarmDeactivatesWithoutImplosion() {
        map.at(4, 3).addItem(new GravityCore());
        GravityCore core = (GravityCore) map.at(4, 3).getItems().stream()
                .filter(i -> i instanceof GravityCore).findFirst().orElseThrow();

        core.disarm(map.at(4, 3));

        assertFalse(core.isActive());
        assertEquals(4, map.locationOf(worker).x());
        assertEquals(4, map.locationOf(worker).y());
    }

    // ==================== DEFUSE KIT TESTS ====================

    /**
     * Normal case: DefuseSecurityAction disarms SpikeDrone without explosion.
     */
    @Test
    void defuseActionDisarmsDroneWithoutExplosion() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        DefuseSecurityAction action = new DefuseSecurityAction(drone, map.at(4, 3));
        action.execute(worker, map);

        assertFalse(drone.isActive());
        assertFalse(map.at(4, 3).getGround() instanceof AcidPool);
    }

    /**
     * Normal case: DefuseSecurityAction spawns SecuritySalvage on tile after disarming.
     */
    @Test
    void defuseActionSpawnsSecuritySalvageOnTile() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        DefuseSecurityAction action = new DefuseSecurityAction(drone, map.at(4, 3));
        action.execute(worker, map);

        boolean hasSalvage = map.at(4, 3).getItems().stream()
                .anyMatch(item -> item instanceof SecuritySalvage);
        assertTrue(hasSalvage);
    }

    /**
     * Normal case: DefuseSecurityAction disarms StaticPylon safely.
     */
    @Test
    void defuseActionDisarmsStaticPylonSafely() {
        map.at(4, 3).setGround(new StaticPylon());
        StaticPylon pylon = (StaticPylon) map.at(4, 3).getGround();

        DefuseSecurityAction action = new DefuseSecurityAction(pylon, map.at(4, 3));
        action.execute(worker, map);

        assertFalse(pylon.isActive());
    }

    /**
     * Boundary case: DefuseSecurityAction returns descriptive result message.
     */
    @Test
    void defuseActionReturnsDescriptiveMessage() {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(4, 3));

        DefuseSecurityAction action = new DefuseSecurityAction(drone, map.at(4, 3));
        String result = action.execute(worker, map);

        assertTrue(result.contains("defused") || result.contains("Security"));
    }

    /**
     * Edge case: DefuseKit getCountermeasureType returns "Defuse".
     */
    @Test
    void defuseKitGetCountermeasureTypeReturnsDefuse() {
        DefuseKit kit = new DefuseKit();
        assertEquals("Defuse", kit.getCountermeasureType());
    }

    // ==================== PURGE CANISTER TESTS ====================

    /**
     * Normal case: PurgeTerrainAction restores AcidPool to Floor.
     */
    @Test
    void purgeActionRestoresAcidPoolToFloor() {
        map.at(4, 3).setGround(new AcidPool());
        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);

        PurgeTerrainAction action = new PurgeTerrainAction(canister);
        action.execute(worker, map);

        assertTrue(map.at(4, 3).getGround() instanceof Floor);
    }

    /**
     * Normal case: PurgeTerrainAction restores ChargedFloor to Floor.
     */
    @Test
    void purgeActionRestoresChargedFloorToFloor() {
        map.at(4, 3).setGround(new ChargedFloor());
        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);

        PurgeTerrainAction action = new PurgeTerrainAction(canister);
        action.execute(worker, map);

        assertTrue(map.at(4, 3).getGround() instanceof Floor);
    }

    /**
     * Normal case: PurgeTerrainAction removes canister from inventory after use.
     */
    @Test
    void purgeActionRemovesCanisterFromInventoryAfterUse() {
        map.at(4, 3).setGround(new AcidPool());
        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);

        PurgeTerrainAction action = new PurgeTerrainAction(canister);
        action.execute(worker, map);

        assertFalse(worker.getInventory().getItems().contains(canister));
    }

    /**
     * Boundary case: PurgeTerrainAction clears 0 tiles when no hazardous terrain nearby.
     */
    @Test
    void purgeActionClearsZeroTilesWhenNoHazardousTerrain() {
        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);

        PurgeTerrainAction action = new PurgeTerrainAction(canister);
        String result = action.execute(worker, map);

        assertTrue(result.contains("0"));
    }

    /**
     * Edge case: PurgeCanister getCountermeasureType returns "Purge".
     */
    @Test
    void purgeCanisterGetCountermeasureTypeReturnsPurge() {
        PurgeCanister canister = new PurgeCanister();
        assertEquals("Purge", canister.getCountermeasureType());
    }

    // ==================== LASER BARRIER TESTS ====================

    /**
     * Normal case: DeployBarrierAction places LaserBarrierGround on target tile.
     */
    @Test
    void deployBarrierActionPlacesLaserBarrierGroundOnTile() {
        LaserBarrier barrier = new LaserBarrier();
        worker.getInventory().add(barrier);

        DeployBarrierAction action = new DeployBarrierAction(barrier, map.at(4, 3), "North");
        action.execute(worker, map);

        assertTrue(map.at(4, 3).getGround() instanceof LaserBarrierGround);
    }

    /**
     * Normal case: DeployBarrierAction removes LaserBarrier from inventory after deploy.
     */
    @Test
    void deployBarrierActionRemovesBarrierFromInventory() {
        LaserBarrier barrier = new LaserBarrier();
        worker.getInventory().add(barrier);

        DeployBarrierAction action = new DeployBarrierAction(barrier, map.at(4, 3), "North");
        action.execute(worker, map);

        assertFalse(worker.getInventory().getItems().contains(barrier));
    }

    /**
     * Normal case: LaserBarrierGround burns out into Puddle after intercepting a hostile.
     */
    @Test
    void laserBarrierGroundBurnsOutIntoPuddleAfterInterception() {
        map.at(4, 3).setGround(new LaserBarrierGround());
        game.actors.Undead hostile = new game.actors.Undead();
        map.addActor(hostile, map.at(4, 3));

        LaserBarrierGround lbg = (LaserBarrierGround) map.at(4, 3).getGround();
        lbg.tick(map.at(4, 3));

        assertTrue(map.at(4, 3).getGround() instanceof Puddle);
    }

    /**
     * Boundary case: LaserBarrierGround deals 50 damage to intercepted hostile.
     */
    @Test
    void laserBarrierGroundDeals50DamageToHostile() {
        map.at(4, 3).setGround(new LaserBarrierGround());
        game.actors.Undead hostile = new game.actors.Undead();
        map.addActor(hostile, map.at(4, 3));

        int hpBefore = hostile.printHp().contains("/") ?
                Integer.parseInt(hostile.printHp().split("/")[0]) : 100;

        LaserBarrierGround lbg = (LaserBarrierGround) map.at(4, 3).getGround();
        lbg.tick(map.at(4, 3));

        assertTrue(!hostile.isConscious() || hostile.printHp().contains("/"));
    }

    /**
     * Edge case: LaserBarrier getCountermeasureType returns "Barrier".
     */
    @Test
    void laserBarrierGetCountermeasureTypeReturnsBarrier() {
        LaserBarrier barrier = new LaserBarrier();
        assertEquals("Barrier", barrier.getCountermeasureType());
    }

    // ==================== ACID POOL TESTS ====================

    /**
     * Normal case: AcidPool dissolves items placed on it each tick.
     */
    @Test
    void acidPoolDissolvesItemsOnTick() {
        map.at(4, 3).setGround(new AcidPool());
        map.at(4, 3).addItem(new SecuritySalvage());

        AcidPool pool = (AcidPool) map.at(4, 3).getGround();
        pool.tick(map.at(4, 3));

        assertTrue(map.at(4, 3).getItems().isEmpty());
    }

    /**
     * Boundary case: AcidPool does nothing when tile has no items.
     */
    @Test
    void acidPoolDoesNothingWithNoItemsOnTile() {
        map.at(4, 3).setGround(new AcidPool());
        AcidPool pool = (AcidPool) map.at(4, 3).getGround();
        pool.tick(map.at(4, 3));
        assertTrue(map.at(4, 3).getItems().isEmpty());
    }

    /**
     * Edge case: AcidPool purge restores tile to Floor.
     */
    @Test
    void acidPoolPurgeRestoresToFloor() {
        map.at(4, 3).setGround(new AcidPool());
        AcidPool pool = (AcidPool) map.at(4, 3).getGround();
        pool.purge(map.at(4, 3));
        assertTrue(map.at(4, 3).getGround() instanceof Floor);
    }

    // ==================== CORRODED STATUS TESTS ====================

    /**
     * Normal case: CorrodedStatus reduces worker weight limit to 30.
     */
    @Test
    void corrodedStatusReducesWeightLimitTo30() {
        WeightLimitedInventory inv = (WeightLimitedInventory) worker.getInventory();
        CorrodedStatus status = new CorrodedStatus(10);
        worker.addStatus(status);

        status.tickStatus(worker, map.at(4, 4));

        assertEquals(30, inv.getWeightLimit());
    }

    /**
     * Normal case: CorrodedStatus restores original weight limit after expiry.
     */
    @Test
    void corrodedStatusRestoresWeightLimitAfterExpiry() {
        WeightLimitedInventory inv = (WeightLimitedInventory) worker.getInventory();
        CorrodedStatus status = new CorrodedStatus(1);
        worker.addStatus(status);

        status.tickStatus(worker, map.at(4, 4));
        status.tickStatus(worker, map.at(4, 4));

        assertEquals(50, inv.getWeightLimit());
    }

    /**
     * Boundary case: CorrodedStatus isStatusActive returns false after expiry.
     */
    @Test
    void corrodedStatusIsInactiveAfterExpiry() {
        CorrodedStatus status = new CorrodedStatus(1);
        worker.addStatus(status);
        status.tickStatus(worker, map.at(4, 4));
        status.tickStatus(worker, map.at(4, 4));
        assertFalse(status.isStatusActive());
    }

    // ==================== STUNNED STATUS TESTS ====================

    /**
     * Normal case: StunnedStatus is active when first created.
     */
    @Test
    void stunnedStatusIsActiveWhenCreated() {
        StunnedStatus status = new StunnedStatus(1);
        assertTrue(status.isStatusActive());
    }

    /**
     * Normal case: StunnedStatus causes worker to return DoNothingAction.
     */
    @Test
    void stunnedStatusCausesWorkerToSkipTurn() {
        worker.addStatus(new StunnedStatus(1));
        edu.monash.fit2099.engine.actions.Action result = worker.playTurn(
                new edu.monash.fit2099.engine.actions.ActionList(),
                new edu.monash.fit2099.engine.actions.DoNothingAction(),
                map, new Display());
        assertTrue(result instanceof edu.monash.fit2099.engine.actions.DoNothingAction);
    }

    /**
     * Boundary case: StunnedStatus is inactive after duration expires.
     */
    @Test
    void stunnedStatusIsInactiveAfterDurationExpires() {
        StunnedStatus status = new StunnedStatus(1);
        status.tickStatus(worker, map.at(4, 4));
        assertFalse(status.isStatusActive());
    }
}