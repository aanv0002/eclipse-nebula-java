package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Represents a weather event that can target a map, spawn an anomaly,
 * and trigger a corresponding weather effect.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface WeatherEvent {

    /**
     * Triggers this weather event on the given map.
     *
     * @param map the map affected by weather
     * @return result message
     */
    String trigger(GameMap map);

    /**
     * Returns the display name of this weather event.
     *
     * @return weather name
     */
    String getName();
}