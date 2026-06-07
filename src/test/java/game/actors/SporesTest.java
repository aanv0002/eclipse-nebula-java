package game.actors;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.grounds.Dirt;
import game.items.Apple;
import game.items.Cookies;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Spores in REQ3 Eclipse Weather System.
 *
 * Tests observable mutation behaviour without using instanceof.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
class SporesTest {

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

        GameMap map = new GameMap("Spores Test Map", groundCreator, Arrays.asList(
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
     * Tests that Spores mutate adjacent Apple into Molded Apple.
     *
     * @throws Exception if setup fails
     */
    @Test
    void sporesMutateAdjacentAppleIntoMoldedApple() throws Exception {
        GameMap map = createTestMap();

        Spores spores = new Spores();
        map.addActor(spores, map.at(3, 3));
        map.at(3, 4).addItem(new Apple());

        spores.playTurn(new ActionList(), new DoNothingAction(), map, new Display());

        assertTrue(map.at(3, 4).getItems().stream()
                .anyMatch(item -> "Molded Apple".equals(item.toString())));
        assertFalse(map.at(3, 4).getItems().stream()
                .anyMatch(item -> "Apple".equals(item.toString())));
    }

    /**
     * Tests that Spores mutate adjacent Cookies into Molded Cookies.
     *
     * @throws Exception if setup fails
     */
    @Test
    void sporesMutateAdjacentCookiesIntoMoldedCookies() throws Exception {
        GameMap map = createTestMap();

        Spores spores = new Spores();
        map.addActor(spores, map.at(3, 3));
        map.at(4, 3).addItem(new Cookies());

        spores.playTurn(new ActionList(), new DoNothingAction(), map, new Display());

        assertTrue(map.at(4, 3).getItems().stream()
                .anyMatch(item -> "Molded Cookies".equals(item.toString())));
        assertFalse(map.at(4, 3).getItems().stream()
                .anyMatch(item -> "Cookies".equals(item.toString())));
    }

    /**
     * Tests that Spores disappear after successfully mutating food.
     *
     * @throws Exception if setup fails
     */
    @Test
    void sporesDisappearAfterMutation() throws Exception {
        GameMap map = createTestMap();

        Spores spores = new Spores();
        map.addActor(spores, map.at(3, 3));
        map.at(3, 4).addItem(new Apple());

        spores.playTurn(new ActionList(), new DoNothingAction(), map, new Display());

        assertFalse(map.at(3, 3).containsAnActor());
    }
}