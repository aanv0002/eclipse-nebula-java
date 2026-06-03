package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SporeNest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Weather event that creates fungal spore nests near puddles.
 *
 * The puddle-adjacent target locations are shuffled so nests do not always
 * appear beside the same puddle.
 *
 * @author Tan Hong Sze
 * @version 2.1
 */
public class FungalBloomWeather implements WeatherEvent {

    /**
     * Minimum number of SporeNests to attempt to spawn.
     */
    private static final int MIN_NESTS = 2;

    /**
     * Maximum number of SporeNests to attempt to spawn.
     */
    private static final int MAX_NESTS = 3;

    private final Random random = new Random();

    /**
     * Weather effect activated after SporeNests are created.
     */
    private final WeatherEffect<SporeNest> effect;

    public FungalBloomWeather() {
        this.effect = new FungalBloomEffect();
    }

    /**
     * Triggers the Fungal Bloom weather event on the given map.
     *
     * This method finds valid locations near puddles, randomly selects how many
     * SporeNests to spawn, places SporeNest grounds on the selected locations,
     * and then activates the corresponding FungalBloomEffect.
     *
     * @param map map affected by the weather event
     * @return message describing the spawned SporeNests and effect result
     */
    @Override
    public String trigger(GameMap map) {
        List<Location> candidates = findCandidateLocations(map);
        Collections.shuffle(candidates);

        int nestsToSpawn = getRandomNestCount();
        int actualCount = Math.min(nestsToSpawn, candidates.size());

        List<Location> targets = candidates.subList(0, actualCount);
        List<SporeNest> nests = new ArrayList<>();

        for (Location location : targets) {
            SporeNest nest = new SporeNest(map, location.getGround());
            location.setGround(nest);
            nests.add(nest);
        }

        return "Fungal Bloom Weather attempts to spawn "
                + nestsToSpawn + " SporeNest ground(s). "
                + targets.size() + " SporeNest ground(s) appear.\n"
                + effect.apply(map, targets, nests);
    }

    /**
     * Randomly chooses how many SporeNests this weather event attempts to spawn.
     *
     * @return random nest count between MIN_NESTS and MAX_NESTS
     */
    private int getRandomNestCount() {
        return MIN_NESTS + random.nextInt(MAX_NESTS - MIN_NESTS + 1);
    }

    /**
     * Finds valid candidate locations for SporeNest spawning.
     *
     * Candidate locations are empty tiles adjacent to puddles. All candidates
     * are collected before selection so the weather does not always spawn nests
     * beside the first puddle found during map scanning.
     *
     * @param map map to scan
     * @return list of valid candidate locations
     */
    private List<Location> findCandidateLocations(GameMap map) {
        List<Location> candidates = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);

                if (!"Puddle".equals(location.getGround().toString())) {
                    continue;
                }

                for (Exit exit : location.getExits()) {
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
     * Checks whether a location can receive a SporeNest.
     *
     * The location must not contain an actor or loose items so the SporeNest
     * symbol remains visible and does not overwrite existing items.
     *
     * @param location location to check
     * @return true if a SporeNest can spawn here
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
        return "Fungal Bloom Weather";
    }
}