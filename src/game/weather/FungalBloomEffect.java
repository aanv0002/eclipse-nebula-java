package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SporeNest;

import java.util.List;

/**
 * Effect that accelerates fungal nest incubation and may release spores
 * immediately.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class FungalBloomEffect implements WeatherEffect<SporeNest> {

    @Override
    public String apply(GameMap map, List<Location> locations, List<SporeNest> nests) {
        int accelerated = 0;
        int released = 0;

        for (int i = 0; i < nests.size(); i++) {
            SporeNest nest = nests.get(i);
            Location location = locations.get(i);

            if (nest.accelerateIncubation(location)) {
                released++;
            } else {
                accelerated++;
            }
        }

        return "Fungal energy accelerates "
                + accelerated + " SporeNest ground(s), and "
                + released + " release spores immediately.";
    }
}