package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Slime;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


class ColonyEvolutionTest {
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
     * Tests that colony evolution returns a result message
     * regardless of spawning success.
     *
     * Verifies that player feedback is always produced.
     */
    @Test
    void colonyEvolutionReturnsMessage() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();

        map.addActor(slime, map.at(2, 2));

        ColonyEvolution evolution = new ColonyEvolution();

        String result =
                evolution.evolve(
                        slime,
                        map.at(2, 2),
                        map
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}