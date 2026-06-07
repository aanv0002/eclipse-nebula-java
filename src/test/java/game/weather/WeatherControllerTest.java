package game.weather;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.grounds.Puddle;
import game.inventory.WeightLimitedInventory;
import game.items.Apple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WeatherController in REQ3 Eclipse Weather System.
 *
 * These tests verify that WeatherController:
 * - does not trigger weather before the interval,
 * - triggers weather on the 10th tick,
 * - continues triggering weather on later intervals.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
class WeatherControllerTest {

    /**
     * Test world used to initialise actor locations for GameMap.
     */
    private static class TestWorld extends World {
        TestWorld() {
            super(new Display());
        }

        @Override
        protected void gameLoop() throws GameEngineException {
        }
    }

    /**
     * Creates a deterministic test map with enough objects for all weather events.
     *
     * @return test map
     * @throws Exception if setup fails
     */
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('~', Puddle::new);

        GameMap map = new GameMap("WeatherController Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                "..~....",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        new TestWorld().addGameMap(map);

        ContractedWorker worker = new ContractedWorker(
                "#1 Test Worker",
                'ඞ',
                10,
                new WeightLimitedInventory(50)
        );

        map.addActor(worker, map.at(3, 3));
        map.at(5, 5).addItem(new Apple());

        return map;
    }

    /**
     * Ticks the controller a fixed number of times.
     *
     * @param controller weather controller
     * @param map test map
     * @param times number of ticks
     * @return result from final tick
     */
    private String tickTimes(WeatherController controller, GameMap map, int times) {
        String result = "";

        for (int i = 0; i < times; i++) {
            result = controller.tick(map);
        }

        return result;
    }

    /**
     * Tests that weather does not trigger before the 10-turn interval.
     *
     * @throws Exception if setup fails
     */
    @Test
    void ninthTickDoesNotTriggerWeather() throws Exception {
        WeatherController controller = new WeatherController();
        GameMap map = createTestMap();

        String result = tickTimes(controller, map, 9);

        assertEquals("", result);
    }

    /**
     * Tests that the first weather event triggers on the 10th tick.
     *
     * @throws Exception if setup fails
     */
    @Test
    void tenthTickTriggersFirstWeatherEvent() throws Exception {
        WeatherController controller = new WeatherController();
        GameMap map = createTestMap();

        String result = tickTimes(controller, map, 10);

        assertFalse(result.isEmpty());
    }

    /**
     * Tests that the second weather event triggers on the 20th tick.
     *
     * @throws Exception if setup fails
     */
    @Test
    void twentiethTickTriggersSecondWeatherEvent() throws Exception {
        WeatherController controller = new WeatherController();
        GameMap map = createTestMap();

        String result = tickTimes(controller, map, 20);

        assertFalse(result.isEmpty());
    }

    /**
     * Tests that the third weather event triggers on the 30th tick.
     *
     * @throws Exception if setup fails
     */
    @Test
    void thirtiethTickTriggersThirdWeatherEvent() throws Exception {
        WeatherController controller = new WeatherController();
        GameMap map = createTestMap();

        String result = tickTimes(controller, map, 30);

        assertFalse(result.isEmpty());
    }
}