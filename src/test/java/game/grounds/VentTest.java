package game.grounds;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.inventory.WeightLimitedInventory;
import game.spawners.VentCreatureSpawner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Vent.
 *
 * Tests cover cut effects including:
 * - IndustrialFan drop at vent location after cut
 * - Tile transformation into Floor after cut
 * - Descriptive message content after cut
 * - getCutDescription returns correct text
 * - Undead spawn attempt mentioned in message
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class VentTest {

    private GameMap map;
    private ContractedWorker worker;

    /**
     * Sets up a fresh test map and worker before each test.
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
    }

    /**
     * Normal case: Vent cut drops IndustrialFan at vent location.
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