import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;

import game.actors.ContractedWorker;
import game.actors.Parasite;
import game.actors.ScrapSnatcher;
import game.grounds.Dirt;
import game.grounds.flora.FleshyMatureTree;
import game.grounds.flora.FleshySprout;
import game.grounds.flora.FleshyTreeType;
import game.inventory.WeightLimitedInventory;
import game.items.AluminiumScrap;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for REQ2: Snap Snatcher
 * These are the criteria, tests are written
 *  - Scrap Snatchers remove depositable resources from their location
 *  - Scrap Snatchers can be infected by Parasites
 *  - Infection changes the Scrap Snatcher's state
 *  - Infected Scrap Snatchers attack adjacent workers
 *  - Infected Scrap Snatchers stop stealing resources
 *  - Infection effects produce the expected outcome over time
 *
 *  - Overflow Fleshy Sprouts spawn Slimes near workers
 *  - Deprecated Fleshy Sprouts spawn Undead near workers
 *  - Overflow Mature Trees spawn Undead
 *  - Deprecated Mature Trees spawn Scrap Snatchers
 *  - Fleshy Trees block actor movement
 *  - Mature Trees eventually grow into Monoliths
 *
 * @author Amra Anver
 * @version 1.0
 */
public class REQ2Test {

    private GameMap createTestMap() throws Exception {
        /**
         * Sets up a smaller test map to place creature, actors and set grounds to observe their behaviour.
         *
         * @throws Exception if map or actor setup fails
         */
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
     * Tests that a Scrap Snatcher removes a depositable resource located on the same tile during its turn.
     */
    @Test
    void scrapSnatcherRemovesDepositableItem() throws Exception {

        GameMap map = createTestMap();

        ScrapSnatcher snatcher = new ScrapSnatcher();

        map.addActor(snatcher, map.at(2,2));

        AluminiumScrap scrap = new AluminiumScrap();
        map.at(2,2).addItem(scrap);

        assertEquals(1, map.at(2,2).getItems().size());

        snatcher.playTurn(
                new ActionList(),
                new DoNothingAction(),
                map,
                new Display()
        );

        assertEquals(0, map.at(2,2).getItems().size());
    }

    /**
     *  Tests that a Scrap Snatcher can be infected only while it is not already infected.
     */
    @Test
    void scrapSnatcherCanBeInfected() {

        ScrapSnatcher snatcher = new ScrapSnatcher();

        assertTrue(snatcher.canBeInfected());

        snatcher.setInfected(true);

        assertFalse(snatcher.canBeInfected());
    }

    /**
     * Tests that applying a Parasite infection marks the Scrap Snatcher as infected.
     *
     * The test also verifies the observable infection state after infection is applied.
     */
    @Test
    void infectMarksScrapSnatcherAsInfected() throws Exception {

        GameMap map = createTestMap();
        ScrapSnatcher snatcher = new ScrapSnatcher();
        Parasite parasite = new Parasite();

        map.addActor(snatcher, map.at(2,2));
        snatcher.infect(parasite, map.at(2,2));
        assertTrue(snatcher.isInfected());
    }

    /**
     * Tests that an infected Scrap Snatcher attacks an adjacent worker instead of behaving like a normal Scrap Snatcher.
     *
     * Checks whether the worker's health decreases after the infected Scrap Snatcher takes a turn.
     */
    @Test
    void infectedScrapSnatcherDamagesAdjacentWorker() throws Exception {

        GameMap map = createTestMap();
        ScrapSnatcher snatcher = new ScrapSnatcher();
        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 20, new WeightLimitedInventory(50));

        map.addActor(snatcher, map.at(2,2));
        map.addActor(worker, map.at(2,3));

        snatcher.setInfected(true);

        int healthBefore = worker.getStatistic(ActorStatistics.HEALTH);

        snatcher.playTurn(
                new ActionList(),
                new DoNothingAction(),
                map,
                new Display()
        );

        int healthAfter = worker.getStatistic(ActorStatistics.HEALTH);

        assertTrue(healthAfter < healthBefore);
    }

    /**
     * Tests that an infected Scrap Snatcher no longer performs its resource-stealing behaviour.
     *
     * The test checks if depositable items remain on the tile after the infected Scrap Snatcher acts.
     */
    @Test
    void infectedScrapSnatcherDoesNotStealItems() throws Exception {

        GameMap map = createTestMap();
        ScrapSnatcher snatcher = new ScrapSnatcher();

        map.addActor(snatcher, map.at(2,2));
        map.at(2,2).addItem(new AluminiumScrap());

        snatcher.setInfected(true);
        snatcher.playTurn(new ActionList(), new DoNothingAction(), map, new Display());

        assertEquals(
                1,
                map.at(2,2).getItems().size()
        );
    }

    /**
     * Tests if an Overflow Fleshy Sprout spawns a Slime when a worker
     *  is within its activation range.
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
     * Tests if a Deprecated Fleshy Sprout spawns an Undead when a worker
     * is within its activation range.
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
     *  Tests that a Deprecated Mature Tree eventually grows into a Fleshy Monolith after sufficient ticking.
     *  Because growth may involve random chance, the test performs
     *  many ticks and verifies the resulting ground state.
     */

    @Test
    void deprecatedMatureTreeEventuallyGrowsIntoMonolith()
            throws Exception {

        GameMap map = createTestMap();

        map.at(3,3).setGround(new FleshyMatureTree(FleshyTreeType.DEPRECATED));

        for (int i = 0; i < 1000; i++) {
            map.at(3,3).getGround().tick(map.at(3,3));

            if (map.at(3,3).getGround()
                    .toString()
                    .contains("Monolith")) {
                break;
            }
        }

        assertTrue(map.at(3,3).getGround().toString().contains("Monolith")
        );
    }
}