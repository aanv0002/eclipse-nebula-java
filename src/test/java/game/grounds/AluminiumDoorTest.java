package game.grounds;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AluminiumDoor.
 *
 * Tests cover cut effects including:
 * - AluminiumScrap drop at door location after cut
 * - Tile transformation into Floor after cut
 * - Descriptive message content after cut
 * - getCutDescription returns correct text
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class AluminiumDoorTest {

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
     * Normal case: AluminiumDoor cut drops AluminiumScrap at door location.
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
}