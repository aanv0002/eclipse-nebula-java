package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SporeNest;

import java.util.List;

/**
 * Effect that activates newly spawned SporeNest grounds.
 *
 * This effect does not release spores immediately. The SporeNest handles its
 * own incubation timer and releases Spores after several ground ticks.
 *
 * @author Tan Hong Sze
 * @version 2.1
 */
public class FungalBloomEffect implements WeatherEffect<SporeNest> {

    @Override
    public String apply(GameMap map, List<Location> locations, List<SporeNest> nests) {
        return "SporeNest ground(s) begin incubating. "
                + "They will release Spores after a few turns.";
    }
}