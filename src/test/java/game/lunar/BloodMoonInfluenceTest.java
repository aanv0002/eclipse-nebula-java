package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Slime;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
class BloodMoonInfluenceTest {
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
     * Tests that Blood Moon influence can execute on a map
     * containing no creatures.
     * *
     * Verifies safe handling of empty maps.
     */
    @Test
    void bloodMoonHandlesEmptyMap() throws Exception {

        GameMap map = createTestMap();

        BloodMoonInfluence influence =
                new BloodMoonInfluence();

        influence.applyInfluence(map, new MoonPhaseData("Blood Moon", 1.0, true));

        assertTrue(true);
    }

    /**
     * Tests that isolated creatures are valid mutation targets.
     * *
     * Verifies mutation behaviour can execute without
     * neighbouring creatures.
     */
    @Test
    void bloodMoonMutatesIsolatedCreature() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();

        map.addActor(slime, map.at(2, 2));

        BloodMoonInfluence influence = new BloodMoonInfluence();

        influence.applyInfluence(map, new MoonPhaseData("Blood Moon", 1.0, true));

        assertNotEquals(slime, map.at(2, 2).getActor());
    }

}