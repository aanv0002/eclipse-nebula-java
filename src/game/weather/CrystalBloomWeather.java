package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SolarCrystal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Weather event that creates solar crystal anomalies near actors.
 *
 * The number of crystals spawned is random between 1 and 3,
 * depending on available valid target locations.
 *
 * @author Tan Hong Sze
 * @version 2.1
 */
public class CrystalBloomWeather implements WeatherEvent {

    /**
     * Minimum number of crystals to attempt to spawn.
     */
    private static final int MIN_CRYSTALS = 2;

    /**
     * Maximum number of crystals to attempt to spawn.
     */
    private static final int MAX_CRYSTALS = 5;

    /**
     * Random generator for crystal spawn count.
     */
    private final Random random = new Random();

    /**
     * Weather effect activated after crystals are created.
     */
    private final WeatherEffect<SolarCrystal> effect;

    /**
     * Creates crystal bloom weather.
     */
    public CrystalBloomWeather() {
        this.effect = new CrystalBloomEffect();
    }

    /**
     * Triggers the Crystal Bloom weather event on the given map.
     *
     * This method randomly determines how many SolarCrystals should be spawned,
     * selects valid empty locations near actors, places SolarCrystal grounds on
     * those locations, and then activates the corresponding CrystalBloomEffect.
     *
     * @param map map affected by the weather event
     * @return message describing the spawned SolarCrystals and effect result
     */
    @Override
    public String trigger(GameMap map) {
        int crystalsToSpawn = getRandomCrystalCount();
        List<Location> targets = selectTargets(map, crystalsToSpawn);
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

    /**
     * Randomly chooses how many crystals this weather event attempts to spawn.
     *
     * @return random crystal count between MIN_CRYSTALS and MAX_CRYSTALS
     */
    private int getRandomCrystalCount() {
        return MIN_CRYSTALS + random.nextInt(MAX_CRYSTALS - MIN_CRYSTALS + 1);
    }

    /**
     * Selects empty locations near actors.
     *
     * @param map target map
     * @param crystalsToSpawn number of crystals to spawn
     * @return selected crystal locations
     */
    private List<Location> selectTargets(GameMap map, int crystalsToSpawn) {
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

                        if (targets.size() >= crystalsToSpawn) {
                            return targets;
                        }
                    }
                }
            }
        }

        return targets;
    }

    /**
     * Checks whether a location can receive a crystal.
     *
     * @param location location to check
     * @return true if valid
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
        return "Crystal Bloom Weather";
    }
}