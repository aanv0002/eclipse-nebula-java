package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.GravityAnchor;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather event that creates gravity anchors near loose items.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class GravitySurgeWeather implements WeatherEvent {

    private static final int MAX_ANCHORS = 2;

    private final WeatherEffect<GravityAnchor> effect;

    public GravitySurgeWeather() {
        this.effect = new GravitySurgeEffect();
    }

    @Override
    public String trigger(GameMap map) {
        List<Location> targets = selectTargets(map);
        List<GravityAnchor> anchors = new ArrayList<>();

        for (Location location : targets) {
            GravityAnchor anchor = new GravityAnchor(map, location.getGround());
            location.setGround(anchor);
            anchors.add(anchor);
        }

        return "Gravity Surge Weather bends around loose objects. "
                + targets.size() + " GravityAnchor ground(s) appear.\n"
                + effect.apply(map, targets, anchors);
    }

    private List<Location> selectTargets(GameMap map) {
        List<Location> targets = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location itemLocation = map.at(x, y);

                if (itemLocation.getItems().isEmpty()) {
                    continue;
                }

                for (Exit exit : itemLocation.getExits()) {
                    Location nearby = exit.getDestination();

                    if (isValidSpawnLocation(nearby) && !targets.contains(nearby)) {
                        targets.add(nearby);

                        if (targets.size() >= MAX_ANCHORS) {
                            return targets;
                        }
                    }
                }
            }
        }

        return targets;
    }

    private boolean isValidSpawnLocation(Location location) {
        return !location.containsAnActor();
    }

    @Override
    public String getName() {
        return "Gravity Surge Weather";
    }
}