package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Slime;
import game.actors.Undead;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class MutationEvolutionTest {
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
     * Tests that mutation removes the original creature from the map.
     * *
     * Verifies that the original actor no longer occupies its
     * previous location after mutation.
     */
    @Test
    void mutationRemovesOriginalCreature() throws Exception {

        GameMap map = createTestMap();

        Undead undead = new Undead();

        map.addActor(undead, map.at(2, 2));

        MutationEvolution evolution = new MutationEvolution();

        evolution.evolve(
                undead,
                map.at(2, 2),
                map
        );

        assertNotEquals(
                undead,
                map.at(2, 2).getActor()
        );
    }

    /**
     * Tests that mutation returns a non-empty message.
     *
     * Verifies that mutation evolution provides user-visible feedback.
     */
    @Test
    void mutationReturnsResultMessage() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();

        map.addActor(slime, map.at(2, 2));

        MutationEvolution evolution = new MutationEvolution();

        String result =
                evolution.evolve(slime, map.at(2, 2), map
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}