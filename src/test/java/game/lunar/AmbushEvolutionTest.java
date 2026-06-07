package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Undead;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
class AmbushEvolutionTest {
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

        game.world.EclipseNebula world = new game.world.EclipseNebula(new Display());

        world.addGameMap(map);

        return map;
    }
    /**
     * Tests that ambush evolution returns a result message.
     * *
     * Verifies user-visible feedback is produced.
     */
    @Test
    void ambushEvolutionReturnsMessage() throws Exception {

        GameMap map = createTestMap();

        Undead undead = new Undead();

        map.addActor(undead, map.at(2, 2));

        AmbushEvolution evolution =
                new AmbushEvolution();

        String result =
                evolution.evolve(
                        undead,
                        map.at(2, 2),
                        map
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}