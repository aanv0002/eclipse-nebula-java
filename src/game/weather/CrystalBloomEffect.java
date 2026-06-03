package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SolarCrystal;

import java.util.List;

/**
 * Effect that activates newly spawned solar crystals.
 *
 * The spreading itself is handled by SolarCrystal on later ground ticks,
 * so crystal spreading happens after the weather event turn instead of
 * immediately on the same turn.
 *
 * @author Tan Hong Sze
 * @version 2.1
 */
public class CrystalBloomEffect implements WeatherEffect<SolarCrystal> {

    @Override
    public String apply(GameMap map, List<Location> locations, List<SolarCrystal> crystals) {
        return "Solar crystals begin pulsing and may spread on the next turns.";
    }
}