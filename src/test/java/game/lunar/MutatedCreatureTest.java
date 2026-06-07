package game.lunar;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


class MutatedCreatureTest {
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
     * Tests that a Mutated Creature creates corruption ground
     * on its current tile when taking a turn.
     *
     * Verifies terrain corruption behaviour.
     */
    @Test
    void mutatedCreatureCorruptsCurrentTile() throws Exception {

        GameMap map = createTestMap();

        MutatedCreature mutated =
                new MutatedCreature();

        map.addActor(mutated, map.at(2, 2));

        mutated.playTurn(
                new ActionList(),
                new DoNothingAction(),
                map,
                new Display()
        );

        assertEquals(
                "Corruption Ground",
                map.at(2, 2).getGround().toString()
        );
    }

    /**
     * Tests that corruption is not replaced if the tile
     * is already corrupted.
     * *
     * Verifies repeated turns behave safely.
     */
    @Test
    void mutatedCreatureHandlesExistingCorruptionGround()
            throws Exception {

        GameMap map = createTestMap();

        map.at(2, 2).setGround(
                new CorruptionGround()
        );

        MutatedCreature mutated =
                new MutatedCreature();

        map.addActor(mutated, map.at(2, 2));

        mutated.playTurn(
                new ActionList(),
                new DoNothingAction(),
                map,
                new Display()
        );

        assertEquals(
                "Corruption Ground",
                map.at(2, 2).getGround().toString()
        );
    }

}