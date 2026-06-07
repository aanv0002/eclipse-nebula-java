package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.World;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SporeNest in REQ3 Eclipse Weather System.
 *
 * Tests incubation timing, spore release, and ground restoration.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
class SporeNestTest {

    private static class TestWorld extends World {
        TestWorld() {
            super(new Display());
        }

        @Override
        protected void gameLoop() throws GameEngineException {
        }
    }

    /**
     * Creates a small deterministic test map.
     *
     * @return test map
     * @throws Exception if map setup fails
     */
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap("SporeNest Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        new TestWorld().addGameMap(map);
        return map;
    }

    /**
     * Tests that SporeNest does not release Spores immediately.
     *
     * @throws Exception if setup fails
     */
    @Test
    void sporeNestDoesNotSpawnImmediately() throws Exception {
        GameMap map = createTestMap();

        SporeNest nest = new SporeNest(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(nest);

        nest.tick(map.at(3, 3));

        assertFalse(map.at(3, 3).containsAnActor());
    }

    /**
     * Tests that SporeNest releases one Spores actor after incubation.
     *
     * @throws Exception if setup fails
     */
    @Test
    void sporeNestReleasesSporesAfterIncubation() throws Exception {
        GameMap map = createTestMap();

        SporeNest nest = new SporeNest(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(nest);

        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));

        assertTrue(map.at(3, 3).containsAnActor());
        assertTrue(map.at(3, 3).getActor().toString().contains("Spores"));
    }

    /**
     * Tests that SporeNest restores the previous ground after releasing Spores.
     *
     * @throws Exception if setup fails
     */
    @Test
    void sporeNestRestoresOriginalGroundAfterRelease() throws Exception {
        GameMap map = createTestMap();
        Ground originalGround = map.at(3, 3).getGround();

        SporeNest nest = new SporeNest(map, originalGround);
        map.at(3, 3).setGround(nest);

        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));
        nest.tick(map.at(3, 3));

        assertSame(originalGround, map.at(3, 3).getGround());
    }
}