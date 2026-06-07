package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.World;
import game.actors.ContractedWorker;
import game.inventory.WeightLimitedInventory;
import game.items.Apple;
import game.items.Cookies;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GravityAnchor in REQ3 Eclipse Weather System.
 *
 * Tests delayed activation, item pulling, actor pulling and ground restoration.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
class GravityAnchorTest {

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

        GameMap map = new GameMap("GravityAnchor Test Map", groundCreator, Arrays.asList(
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
     * Tests that GravityAnchor does not pull items on its first stabilising tick.
     *
     * @throws Exception if setup fails
     */
    @Test
    void gravityAnchorDoesNotPullItemsBeforeStabilising() throws Exception {
        GameMap map = createTestMap();

        GravityAnchor anchor = new GravityAnchor(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(anchor);
        map.at(1, 3).addItem(new Apple());

        anchor.tick(map.at(3, 3));

        assertEquals(0, map.at(3, 3).getItems().size());
        assertEquals(1, map.at(1, 3).getItems().size());
    }

    /**
     * Tests that GravityAnchor pulls nearby items after stabilising.
     *
     * @throws Exception if setup fails
     */
    @Test
    void gravityAnchorPullsNearbyItemsOntoAnchorLocation() throws Exception {
        GameMap map = createTestMap();

        GravityAnchor anchor = new GravityAnchor(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(anchor);

        map.at(1, 3).addItem(new Apple());
        map.at(5, 3).addItem(new Cookies());

        anchor.tick(map.at(3, 3));
        anchor.tick(map.at(3, 3));

        assertEquals(2, map.at(3, 3).getItems().size());
        assertTrue(map.at(3, 3).getItems().stream()
                .anyMatch(item -> "Apple".equals(item.toString())));
        assertTrue(map.at(3, 3).getItems().stream()
                .anyMatch(item -> "Cookies".equals(item.toString())));
    }

    /**
     * Tests that GravityAnchor pulls one nearby actor onto the anchor location.
     *
     * @throws Exception if setup fails
     */
    @Test
    void gravityAnchorPullsNearbyActorOntoAnchorLocation() throws Exception {
        GameMap map = createTestMap();

        GravityAnchor anchor = new GravityAnchor(map, map.at(3, 3).getGround());
        map.at(3, 3).setGround(anchor);

        ContractedWorker worker = new ContractedWorker("#1 Test Worker", 'ඞ', 10,
                new WeightLimitedInventory(50));

        map.addActor(worker, map.at(3, 1));

        anchor.tick(map.at(3, 3));
        assertFalse(map.at(3, 3).containsAnActor());

        anchor.tick(map.at(3, 3));

        assertTrue(map.at(3, 3).containsAnActor());
        assertEquals(worker, map.at(3, 3).getActor());
    }

    /**
     * Tests that GravityAnchor restores the original ground after activation.
     *
     * @throws Exception if setup fails
     */
    @Test
    void gravityAnchorRestoresOriginalGroundAfterActivation() throws Exception {
        GameMap map = createTestMap();
        Ground originalGround = map.at(3, 3).getGround();

        GravityAnchor anchor = new GravityAnchor(map, originalGround);
        map.at(3, 3).setGround(anchor);

        anchor.tick(map.at(3, 3));
        anchor.tick(map.at(3, 3));

        assertSame(originalGround, map.at(3, 3).getGround());
    }
}