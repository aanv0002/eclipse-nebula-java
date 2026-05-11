package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawners.CreatureSpawnService;
import game.spawners.CreatureSpawner;
import game.spawners.HoleGrowthPolicy;

/**
 * A hole that attempts to spawn a creature every 20 game turns.
 * Whenever a spawn succeeds, there is a 1% chance that one adjacent tile becomes
 * another hole with the same spawning capabilities.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class Hole extends Ground {

    /**
     * Number of turns between spawn attempts.
     */
    private static final int SPAWN_INTERVAL = 20;

    /**
     * Number of turns elapsed since the last spawn attempt.
     */
    private int turnsElapsed;

    /**
     * The spawner used by this hole.
     */
    private final CreatureSpawner creatureSpawner;

    /**
     * Service used to place creatures and apply spawn reactions.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Growth policy used after successful spawns.
     */
    private final HoleGrowthPolicy growthPolicy;

    /**
     * Creates a hole with the given spawner.
     *
     * @param creatureSpawner the creature spawner
     */
    public Hole(CreatureSpawner creatureSpawner) {
        this(creatureSpawner, new HoleGrowthPolicy());
    }

    /**
     * Creates a hole with the given spawner and growth policy.
     *
     * @param creatureSpawner the creature spawner
     * @param growthPolicy the hole growth policy
     */
    public Hole(CreatureSpawner creatureSpawner, HoleGrowthPolicy growthPolicy) {
        super('o', "Hole");
        this.creatureSpawner = creatureSpawner;
        this.spawnService = new CreatureSpawnService();
        this.growthPolicy = growthPolicy;
        this.turnsElapsed = 0;
    }

    /**
     * Ticks the hole and attempts to spawn every 20 turns.
     *
     * @param location the location of this ground
     */
    @Override
    public void tick(Location location) {
        turnsElapsed++;

        if (turnsElapsed >= SPAWN_INTERVAL) {
            Actor creature = creatureSpawner.spawnCreature();
            if (spawnService.spawnAt(location, creature)) {
                new Display().println(creature + " emerges from a hole at " + location + ".");
                growthPolicy.grow(location, creatureSpawner);
            }
            turnsElapsed = 0;
        }
    }
}
