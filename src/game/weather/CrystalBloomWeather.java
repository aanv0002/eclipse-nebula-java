package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SolarCrystal;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather event that creates solar crystal anomalies near actors.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class CrystalBloomWeather implements WeatherEvent {

    private static final int MAX_CRYSTALS = 3;

    private final WeatherEffect<SolarCrystal> effect;

    public CrystalBloomWeather() {
        this.effect = new CrystalBloomEffect();
    }

    @Override
    public String trigger(GameMap map) {
        List<Location> targets = selectTargets(map);
        List<SolarCrystal> crystals = new ArrayList<>();

        for (Location location : targets) {
            SolarCrystal crystal = new SolarCrystal(location.getGround());
            location.setGround(crystal);
            crystals.add(crystal);
        }

        return "Crystal Bloom Weather strikes near living movement. "
                + targets.size() + " SolarCrystal ground(s) appear.\n"
                + effect.apply(map, targets, crystals);
    }

    private List<Location> selectTargets(GameMap map) {
        List<Location> targets = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location actorLocation = map.at(x, y);

                if (!actorLocation.containsAnActor()) {
                    continue;
                }

                for (Exit exit : actorLocation.getExits()) {
                    Location nearby = exit.getDestination();

                    if (isValidSpawnLocation(nearby) && !targets.contains(nearby)) {
                        targets.add(nearby);

                        if (targets.size() >= MAX_CRYSTALS) {
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
        return "Crystal Bloom Weather";
    }
}