package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SporeNest;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather event that creates fungal spore nests near puddles.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class FungalBloomWeather implements WeatherEvent {

    private static final int MAX_NESTS = 2;

    private final WeatherEffect<SporeNest> effect;

    public FungalBloomWeather() {
        this.effect = new FungalBloomEffect();
    }

    @Override
    public String trigger(GameMap map) {
        List<Location> targets = selectTargets(map);
        List<SporeNest> nests = new ArrayList<>();

        for (Location location : targets) {
            SporeNest nest = new SporeNest(map, location.getGround());
            location.setGround(nest);
            nests.add(nest);
        }

        return "Fungal Bloom Weather gathers around damp ground. "
                + targets.size() + " SporeNest ground(s) appear.\n"
                + effect.apply(map, targets, nests);
    }

    private List<Location> selectTargets(GameMap map) {
        List<Location> targets = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);

                if (!"Puddle".equals(location.getGround().toString())) {
                    continue;
                }

                for (Exit exit : location.getExits()) {
                    Location nearby = exit.getDestination();

                    if (isValidSpawnLocation(nearby) && !targets.contains(nearby)) {
                        targets.add(nearby);

                        if (targets.size() >= MAX_NESTS) {
                            return targets;
                        }
                    }
                }
            }
        }

        return targets;
    }

    private boolean isValidSpawnLocation(Location location) {
        return !location.containsAnActor()
                && location.getItems().isEmpty();
    }

    @Override
    public String getName() {
        return "Fungal Bloom Weather";
    }
}