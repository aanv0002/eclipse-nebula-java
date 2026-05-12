package game.spawners;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Hole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A policy that handles the 1% chance for a successful hole spawn to grow a
 * new adjacent hole. The new hole inherits the exact same spawning capabilities
 * as the parent hole.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class HoleGrowthPolicy {

    /**
     * Chance that a successful hole spawn grows the hole.
     */
    private static final double GROWTH_CHANCE = 0.01;

    /**
     * Random generator for growth checks.
     */
    private final Random random;

    /**
     * Creates the hole growth policy.
     */
    public HoleGrowthPolicy() {
        this.random = new Random();
    }

    /**
     * Attempts to convert one adjacent tile into another hole.
     *
     * @param source the parent hole location
     * @param spawner the parent hole spawner
     */
    public void grow(Location source, CreatureSpawner spawner) {
        if (random.nextDouble() >= GROWTH_CHANCE) {
            return;
        }

        List<Location> candidates = new ArrayList<>();
        for (Exit exit : source.getExits()) {
            Location destination = exit.getDestination();
            if (!destination.containsAnActor()) {
                candidates.add(destination);
            }
        }

        if (!candidates.isEmpty()) {
            Location destination = candidates.get(random.nextInt(candidates.size()));
            destination.setGround(new Hole(spawner.copy(), this));
        }
    }
}
