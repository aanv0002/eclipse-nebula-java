package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls cyclic weather activation.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class WeatherController {

    /**
     * Number of turns between weather events.
     */
    private static final int WEATHER_INTERVAL = 10;

    private final List<WeatherEvent> events;
    private int turnCounter;
    private int eventIndex;

    /**
     * Creates a weather controller with all weather events.
     */
    public WeatherController() {
        this.events = new ArrayList<>();
        this.events.add(new CrystalBloomWeather());
        this.events.add(new FungalBloomWeather());
        this.events.add(new GravitySurgeWeather());
        this.turnCounter = 0;
        this.eventIndex = 0;
    }

    /**
     * Ticks the weather controller.
     *
     * @param map target map
     * @return weather message, or empty string if no weather occurs
     */
    public String tick(GameMap map) {
        turnCounter++;

        if (turnCounter % WEATHER_INTERVAL != 0) {
            return "";
        }

        WeatherEvent event = events.get(eventIndex);
        eventIndex = (eventIndex + 1) % events.size();

        return event.trigger(map);
    }
}