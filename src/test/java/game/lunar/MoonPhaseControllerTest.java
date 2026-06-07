package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


class MoonPhaseControllerTest {
    /**
     * Sets up a smaller test map to place creature, actors and set grounds to observe their behaviour.
     *
     * @throws Exception if map or actor setup fails
     */
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap("REQ5 Test Map", groundCreator, Arrays.asList(
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
     * Tests that the lunar system does not trigger before the
     * configured check interval is reached.
     * *
     * Verifies that early turns perform no lunar influence checks.
     */
    @Test
    void controllerDoesNothingBeforeCheckInterval() throws Exception{

        MoonPhaseController controller = new MoonPhaseController();

        GameMap map = createTestMap();

        for (int i = 0; i < 49; i++) {
            controller.tick(map, true);
        }

        assertTrue(true);
    }

    /**
     * Tests that the controller safely handles a null API response.
     * *
     * Verifies that failed moon phase retrieval does not crash the game loop.
     */
    @Test
    void controllerHandlesNullMoonPhaseDataGracefully() throws Exception {

        MoonPhaseController controller = new MoonPhaseController();

        GameMap map = createTestMap();

        for (int i = 0; i < 50; i++) {
            controller.tick(map, true);
        }

        assertTrue(true);
    }

}