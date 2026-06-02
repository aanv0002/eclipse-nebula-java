package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import java.util.List;

/**
 * Represents the secondary effect caused by a weather event.
 *
 * The generic type represents the anomaly object created by the weather event,
 * such as SolarCrystal, SporeNest, or GravityAnchor.
 *
 * @param <T> the anomaly type created by the weather event
 * @author Tan Hong Sze
 * @version 2.0
 */
public interface WeatherEffect<T> {

    /**
     * Applies this weather effect to selected locations and created anomalies.
     *
     * @param map the affected map
     * @param locations selected locations
     * @param anomalies anomaly objects created at the selected locations
     * @return result message
     */
    String apply(GameMap map, List<Location> locations, List<T> anomalies);
}