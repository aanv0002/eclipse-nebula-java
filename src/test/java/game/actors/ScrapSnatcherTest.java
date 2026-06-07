package game.actors;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import game.items.AluminiumScrap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Amra Anver
 * @version 1.0
 */
class ScrapSnatcherTest {
    /**
     * Sets up a smaller test map to place creature, actors and set grounds to observe their behaviour.
     *
     * @throws Exception if map or actor setup fails
     */
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
}