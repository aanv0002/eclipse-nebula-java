package game.items;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import game.statuses.PoisonStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AlienCube.
 *
 * Tests cover cut effects including:
 * - AlienArtifact drop at worker location
 * - Cube removal from inventory after cut
 * - PoisonStatus applied to worker after cut
 * - Descriptive message content
 * - Inventory size reduction after cut
 * - getCutDescription returns correct text
 *
 * Each test covers normal, boundary or edge conditions.
 * Tests are fully deterministic with no reliance on
 * external services, network calls or random state.
 *
 * @author Raima
 * @version 1.0
 */
class AlienCubeTest {

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
        assertFalse(worker.statusesOf(PoisonStatus.class).isEmpty());
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
     * Boundary case: AlienCube cut works when only item in inventory.
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
     * Edge case: AlienCube getCutDescription returns correct text.
     */
    @Test
    void alienCubeGetCutDescriptionReturnsCorrectText() {
        AlienCube cube = new AlienCube();
        assertTrue(cube.getCutDescription().contains("Alien Cube"));
    }
}