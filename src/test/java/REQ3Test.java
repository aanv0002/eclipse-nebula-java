import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.actors.Spores;
import game.grounds.Dirt;
import game.grounds.GravityAnchor;
import game.grounds.SolarCrystal;
import game.grounds.SporeNest;
import game.inventory.WeightLimitedInventory;
import game.items.Apple;
import game.items.Cookies;
import game.items.MoldedApple;
import game.statuses.BlindStatus;
import game.statuses.RootStatus;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for REQ3 Eclipse Weather System.
 *
 * These tests check observable functionality instead of concrete object types.
 * They avoid instanceof by testing state changes, returned messages, item names,
 * status presence, and actor/map behaviour.
 *
 * Covered behaviours:
 * - SolarCrystal damages and blinds a worker
 * - SporeNest releases a spore after incubation
 * - Spores mutate moldable food into molded food
 * - MoldedApple applies RootStatus
 * - RootStatus counts down and expires
 * - GravityAnchor pulls nearby items
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
class REQ3Test {

    /**
     * Creates a simple test map and attaches it to a World so actor locations work.
     *
     * @return test map
     * @throws Exception if map setup fails
     */
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap("REQ3 Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        game.world.EclipseNebula world =
                new game.world.EclipseNebula(new Display());

        world.addGameMap(map);

        return map;
    }

    /**
     * Tests that SolarCrystal damages the worker and applies BlindStatus.
     *
     * @throws Exception if map or actor setup fails
     */
    @Test
    void solarCrystalExplodesDamagesWorkerAndAppliesBlindStatus() throws Exception {
        GameMap map = createTestMap();

        ContractedWorker worker = new ContractedWorker(
                "#1 Test Worker",
                'ඞ',
                10,
                new WeightLimitedInventory(50)
        );

        map.at(3, 3).setGround(new SolarCrystal(map.at(3, 3).getGround()));
        map.addActor(worker, map.at(3, 3));

        map.at(3, 3).getGround().tick(map.at(3, 3));

        assertFalse(worker.statusesOf(BlindStatus.class).isEmpty());
        assertTrue(worker.toString().contains("6/10"));
    }

    /**
     * Tests that SporeNest releases an actor after incubation by checking
     * that the location becomes occupied after the countdown.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void sporeNestReleasesActorAfterIncubation() throws Exception {
        GameMap map = createTestMap();

        SporeNest nest = new SporeNest(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(nest);

        nest.tick(map.at(3, 3));
        assertFalse(map.at(3, 3).containsAnActor());

        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));

        assertTrue(map.at(3, 3).containsAnActor());
        assertTrue(map.at(3, 3).getActor().toString().contains("Spores"));
    }

    /**
     * Tests that Spores mutate nearby moldable food into molded food.
     *
     * This checks the resulting item name instead of checking concrete type.
     *
     * @throws Exception if map or actor setup fails
     */
    @Test
    void sporesMutateAdjacentAppleIntoMoldedApple() throws Exception {
        GameMap map = createTestMap();

        Spores spores = new Spores();
        map.addActor(spores, map.at(3, 3));
        map.at(3, 4).addItem(new Apple());

        spores.playTurn(new ActionList(), new DoNothingAction(), map, new Display());

        boolean hasMoldedApple = map.at(3, 4).getItems().stream()
                .anyMatch(item -> "Molded Apple".equals(item.toString()));

        boolean hasOriginalApple = map.at(3, 4).getItems().stream()
                .anyMatch(item -> "Apple".equals(item.toString()));

        assertTrue(hasMoldedApple);
        assertFalse(hasOriginalApple);
    }

    /**
     * Tests that Spores mutate nearby Cookies into Molded Cookies.
     *
     * This provides another input case for moldable item mutation.
     *
     * @throws Exception if map or actor setup fails
     */
    @Test
    void sporesMutateAdjacentCookiesIntoMoldedCookies() throws Exception {
        GameMap map = createTestMap();

        Spores spores = new Spores();
        map.addActor(spores, map.at(3, 3));
        map.at(4, 3).addItem(new Cookies());

        spores.playTurn(new ActionList(), new DoNothingAction(), map, new Display());

        boolean hasMoldedCookies = map.at(4, 3).getItems().stream()
                .anyMatch(item -> "Molded Cookies".equals(item.toString()));

        boolean hasOriginalCookies = map.at(4, 3).getItems().stream()
                .anyMatch(item -> "Cookies".equals(item.toString()));

        assertTrue(hasMoldedCookies);
        assertFalse(hasOriginalCookies);
    }

    /**
     * Tests that MoldedApple applies RootStatus when consumed.
     */
    @Test
    void moldedAppleAppliesRootStatusWhenConsumed() {
        ContractedWorker worker = new ContractedWorker(
                "#1 Test Worker",
                'ඞ',
                10,
                new WeightLimitedInventory(50)
        );

        MoldedApple moldedApple = new MoldedApple();

        String result = moldedApple.consume(worker);

        assertTrue(result.contains("rooted"));
        assertTrue(moldedApple.isConsumedUp());
        assertFalse(worker.statusesOf(RootStatus.class).isEmpty());
    }

    /**
     * Tests RootStatus countdown and expiry behaviour.
     */
    @Test
    void rootStatusCountsDownAndExpires() {
        RootStatus rootStatus = new RootStatus();

        assertTrue(rootStatus.isStatusActive());
        assertEquals(4, rootStatus.getRemainingTurns());

        rootStatus.tickStatus(null, null);
        assertEquals(3, rootStatus.getRemainingTurns());

        rootStatus.tickStatus(null, null);
        assertEquals(2, rootStatus.getRemainingTurns());

        while (rootStatus.getRemainingTurns() > 0) {
            rootStatus.tickStatus(null, null);
        }

        assertFalse(rootStatus.isStatusActive());
    }

    /**
     * Tests that GravityAnchor pulls nearby loose items onto its location
     * after it stabilises.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void gravityAnchorPullsNearbyItemsOntoAnchorLocation() throws Exception {
        GameMap map = createTestMap();

        GravityAnchor anchor = new GravityAnchor(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(anchor);

        map.at(1, 3).addItem(new Apple());
        map.at(5, 3).addItem(new Cookies());

        anchor.tick(map.at(3, 3));
        assertEquals(0, map.at(3, 3).getItems().size());

        anchor.tick(map.at(3, 3));

        assertEquals(2, map.at(3, 3).getItems().size());

        boolean anchorHasApple = map.at(3, 3).getItems().stream()
                .anyMatch(item -> "Apple".equals(item.toString()));

        boolean anchorHasCookies = map.at(3, 3).getItems().stream()
                .anyMatch(item -> "Cookies".equals(item.toString()));

        assertTrue(anchorHasApple);
        assertTrue(anchorHasCookies);
    }

    /**
     * Tests that GravityAnchor pulls one nearby actor onto its location
     * after it stabilises.
     *
     * @throws Exception if map or actor setup fails
     */
    @Test
    void gravityAnchorPullsNearbyActorOntoAnchorLocation() throws Exception {
        GameMap map = createTestMap();

        GravityAnchor anchor = new GravityAnchor(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(anchor);

        ContractedWorker worker = new ContractedWorker(
                "#1 Test Worker",
                'ඞ',
                10,
                new WeightLimitedInventory(50)
        );

        map.addActor(worker, map.at(3, 1));

        anchor.tick(map.at(3, 3));
        assertFalse(map.at(3, 3).containsAnActor());

        anchor.tick(map.at(3, 3));

        assertTrue(map.at(3, 3).containsAnActor());
        assertEquals(worker, map.at(3, 3).getActor());
    }
}