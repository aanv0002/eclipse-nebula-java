package game.grounds.flora;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Amra Anver
 * @version 1.0
 */
class FleshyTreeTest {
    /**
     * Sets up a smaller test map to place creature, actors and set grounds to observe their behaviour.
     *
     * @throws Exception if map or actor setup fails
     **/
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap("REQ2 Test Map", groundCreator, Arrays.asList(
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
     * Tests if an Overflow Fleshy Sprout spawns a Slime when a worker is on one of its surrounding tiles
     */
    @Test
    void overflowSproutSpawnsSlimeWhenWorkerNearby() throws Exception {

        GameMap map = createTestMap();
        FleshySprout sprout = new FleshySprout(FleshyTreeType.OVERFLOW);

        map.at(3,3).setGround(sprout);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));

        map.addActor(worker, map.at(3,4));
        sprout.tick(map.at(3,3));

        boolean slimeExists = false;
        for (int x = 2; x <= 4; x++) {
            for (int y = 2; y <= 4; y++) {

                if (map.at(x,y).containsAnActor()) {

                    String actorName =
                            map.at(x,y).getActor().toString();

                    if (actorName.contains("Slime")) {
                        slimeExists = true;
                    }
                }
            }
        }

        assertTrue(slimeExists);
    }

    /**
     * Tests if a Deprecated Fleshy Sprout spawns an Undead when a worker is within its surrounding tiles.
     *
     */
    @Test
    void deprecatedSproutSpawnsUndeadWhenWorkerNearby() throws Exception {

        GameMap map = createTestMap();

        FleshySprout sprout = new FleshySprout(FleshyTreeType.DEPRECATED);
        map.at(3,3).setGround(sprout);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));
        map.addActor(worker, map.at(3,4));
        sprout.tick(map.at(3,3));

        boolean undeadExists = false;

        for (int x = 2; x <= 4; x++) {
            for (int y = 2; y <= 4; y++) {

                if (map.at(x,y).containsAnActor()) {

                    String actorName =
                            map.at(x,y).getActor().toString();

                    if (actorName.contains("Undead")) {
                        undeadExists = true;
                    }
                }
            }
        }

        assertTrue(undeadExists);
    }

    /**
     * Tests if a Deprecated Mature Tree can spawn a Scrap Snatcher
     *  when activated by a nearby worker.
     */
    @Test
    void deprecatedMatureTreeSpawnsScrapSnatcher() throws Exception {

        GameMap map = createTestMap();

        FleshyMatureTree tree = new FleshyMatureTree(FleshyTreeType.DEPRECATED);
        map.at(3,3).setGround(tree);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));

        map.addActor(worker, map.at(3,4));
        tree.tick(map.at(3,3));

        boolean snatcherExists = false;

        for (int x = 2; x <= 4; x++) {
            for (int y = 2; y <= 4; y++) {

                if (map.at(x,y).containsAnActor()) {

                    String actorName =
                            map.at(x,y).getActor().toString();

                    if (actorName.contains("Scrap Snatcher")) {
                        snatcherExists = true;
                    }
                }
            }
        }

        assertTrue(snatcherExists);
    }

    /**
     * Tests whether an Overflow Mature Tree spawns an Undead when a worker
     *  is nearby.
     */
    @Test
    void overflowMatureTreeSpawnsUndead() throws Exception {

        GameMap map = createTestMap();

        FleshyMatureTree tree = new FleshyMatureTree(FleshyTreeType.OVERFLOW);
        map.at(3,3).setGround(tree);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));

        map.addActor(worker, map.at(3,4));
        tree.tick(map.at(3,3));

        boolean undeadExists = false;

        for (int x = 2; x <= 4; x++) {
            for (int y = 2; y <= 4; y++) {

                if (map.at(x,y).containsAnActor()) {

                    String actorName =
                            map.at(x,y).getActor().toString();

                    if (actorName.contains("Undead")) {
                        undeadExists = true;
                    }
                }
            }
        }

        assertTrue(undeadExists);
    }

    /**
     * Basic functionality check
     * Tests that a Fleshy Tree cannot be entered by actors.
     */
    @Test
    void fleshyTreeCannotBeEntered() {

        FleshySprout tree = new FleshySprout(FleshyTreeType.OVERFLOW);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));

        assertFalse(tree.canActorEnter(worker));
    }
    /**
     * Tests that an Overflow Fleshy Sprout eventually grows into a
     * Fleshy Sapling.
     * *
     * Since growth is chance based, the test performs many ticks and
     * verifies that the growth stage changes.
     */
    @Test
    void overflowSproutEventuallyGrowsIntoSapling() throws Exception {

        GameMap map = createTestMap();

        map.at(3, 3).setGround(
                new FleshySprout(FleshyTreeType.OVERFLOW)
        );

        for (int i = 0; i < 1000; i++) {

            map.at(3, 3).getGround().tick(map.at(3, 3));

            if (map.at(3, 3)
                    .getGround()
                    .toString()
                    .contains("Sapling")) {
                break;
            }
        }

        assertTrue(
                map.at(3, 3)
                        .getGround()
                        .toString()
                        .contains("Sapling")
        );
    }

    /**
     * Tests that an Overflow Sapling eventually grows into a Fleshy Mature Tree.
     * *
     * The test performs repeated ticks because growth occurs
     * probabilistically.
     */
    @Test
    void overflowSaplingEventuallyGrowsIntoMatureTree() throws Exception {

        GameMap map = createTestMap();

        map.at(3, 3).setGround(
                new FleshySapling(FleshyTreeType.OVERFLOW)
        );

        for (int i = 0; i < 1500; i++) {

            map.at(3, 3).getGround().tick(map.at(3, 3));

            if (map.at(3, 3).getGround().toString().contains("Mature")) {
                break;
            }
        }

        assertTrue(
                map.at(3, 3).getGround().toString().contains("Mature")
        );
    }

    /**
     * Tests that an Overflow Mature Tree does not grow into a Fleshy Monolith.
     * *
     * Overflow trees are intended to remain at the mature stage.
     */
    @Test
    void overflowMatureTreeDoesNotGrowIntoMonolith() throws Exception {

        GameMap map = createTestMap();

        map.at(3, 3).setGround(new FleshyMatureTree(FleshyTreeType.OVERFLOW));

        for (int i = 0; i < 1000; i++) {
            map.at(3, 3).getGround().tick(map.at(3, 3));
        }

        assertFalse(map.at(3, 3).getGround().toString().contains("Monolith"));
    }

    /**
     * Tests that a Fleshy Monolith warps a nearby worker away from
     * its original location.
     * *
     * Verifies that the worker no longer occupies the adjacent tile
     * after the monolith performs its special action.
     */
    @Test
    void monolithWarpsNearbyWorker() throws Exception {

        GameMap map = createTestMap();

        FleshyMonolith monolith = new FleshyMonolith(FleshyTreeType.DEPRECATED);

        map.at(3, 3).setGround(monolith);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));

        map.addActor(worker, map.at(3, 4));

        monolith.tick(map.at(3, 3));

        assertFalse(
                map.at(3, 4).containsAnActor()
        );
    }
    /**
     *  Tests that a Deprecated Mature Tree eventually grows into a Fleshy Monolith after sufficient ticking.
     *  Because growth may involve random chance, the test performs many ticks and verifies the resulting ground state.
     */

    @Test
    void deprecatedMatureTreeEventuallyGrowsIntoMonolith()
            throws Exception {

        GameMap map = createTestMap();

        map.at(3,3).setGround(new FleshyMatureTree(FleshyTreeType.DEPRECATED));

        for (int i = 0; i < 1000; i++) {
            map.at(3,3).getGround().tick(map.at(3,3));

            if (map.at(3,3).getGround().toString().contains("Monolith")) {
                break;
            }
        }

        assertTrue(map.at(3,3).getGround().toString().contains("Monolith")
        );
    }

}