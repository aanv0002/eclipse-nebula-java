package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class NewMoonInfluenceTest {
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
     * Tests that New Moon influence safely executes when
     * no creatures are present.
     * *
     * Verifies empty map handling.
     */
    @Test
    void newMoonHandlesEmptyMap() throws Exception{

        GameMap map = createTestMap();

        NewMoonInfluence influence =
                new NewMoonInfluence();

        influence.applyInfluence(
                map,
                new MoonPhaseData(
                        "New Moon",
                        0.0,
                        false
                )
        );

        assertTrue(true);
    }

}