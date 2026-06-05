package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.GravityAnchor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Weather event that creates one visible gravity anchor near loose items.
 *
 * @author Tan Hong Sze
 * @version 2.2
 */
public class GravitySurgeWeather implements WeatherEvent {

    /**
     * Maximum number of GravityAnchors spawned per Gravity Surge event.
     */
    private static final int MAX_ANCHORS = 1;

    /**
     * Weather effect activated after GravityAnchors are created.
     */
    private final WeatherEffect<GravityAnchor> effect;

    /**
     * Creates a Gravity Surge weather event with its corresponding effect.
     */
    public GravitySurgeWeather() {
        this.effect = new GravitySurgeEffect();
    }

    /**
     * Triggers the Gravity Surge weather event on the given map.
     *
     * This method scans for loose ground items, selects a nearby empty location
     * as the gravity centre, spawns one visible GravityAnchor, and activates the
     * corresponding GravitySurgeEffect.
     *
     * @param map map affected by the weather event
     * @return message describing the spawned GravityAnchor and effect result
     */
    @Override
    public String trigger(GameMap map) {
        List<Location> candidates = findCandidateLocations(map);
        Collections.shuffle(candidates);

        int actualCount = Math.min(MAX_ANCHORS, candidates.size());
        List<Location> targets = candidates.subList(0, actualCount);
        List<GravityAnchor> anchors = new ArrayList<>();

        for (Location location : targets) {
            GravityAnchor anchor = new GravityAnchor(map, location.getGround());
            location.setGround(anchor);
            anchors.add(anchor);
        }

        String coordinateMessage = buildCoordinateMessage(targets);

        return "Gravity Surge Weather bends around loose objects. "
                + targets.size() + " GravityAnchor ground(s) appear"
                + coordinateMessage + ".\n"
                + effect.apply(map, targets, anchors);
    }

    /**
     * Builds a coordinate message for the spawned GravityAnchor.
     *
     * @param targets selected anchor locations
     * @return formatted coordinate message, or an empty string if no target exists
     */
    private String buildCoordinateMessage(List<Location> targets) {
        if (targets.isEmpty()) {
            return "";
        }

        Location location = targets.get(0);
        return " at (" + location.x() + ", " + location.y() + ")";
    }

    /**
     * Finds valid candidate locations for GravityAnchor spawning.
     *
     * The method scans for loose item locations, then checks nearby tiles so the
     * anchor spawns close to items instead of directly on top of them.
     *
     * @param map map to scan
     * @return list of valid candidate locations
     */
    private List<Location> findCandidateLocations(GameMap map) {
        List<Location> candidates = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location itemLocation = map.at(x, y);

                if (itemLocation.getItems().isEmpty()) {
                    continue;
                }

                for (Exit exit : itemLocation.getExits()) {
                    Location nearby = exit.getDestination();

                    if (isValidSpawnLocation(nearby) && !candidates.contains(nearby)) {
                        candidates.add(nearby);
                    }
                }
            }
        }

        return candidates;
    }

    /**
     * Checks whether a location can receive a gravity anchor.
     *
     * The anchor must not spawn on actors or loose items so that the ֍ icon
     * remains visible for one round.
     *
     * @param location location to check
     * @return true if the anchor can spawn here
     */
    private boolean isValidSpawnLocation(Location location) {
        return !location.containsAnActor()
                && location.getItems().isEmpty();
    }

    /**
     * Returns the display name of this weather event.
     *
     * @return name of the weather event
     */
    @Override
    public String getName() {
        return "Gravity Surge Weather";
    }
}