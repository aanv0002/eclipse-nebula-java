package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawners.CreatureSpawner;

/**
 * A hole that spawns creatures every 20 game turns.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class Hole extends Ground {

    /**
     * Number of turns between spawns.
     */
    private static final int SPAWN_INTERVAL = 20;

    /**
     * Number of turns elapsed since the last spawn.
     */
    private int turnsElapsed;

    /**
     * The spawner used by this hole.
     */
    private final CreatureSpawner creatureSpawner;

    /**
     * Creates a hole with the given spawner.
     *
     * @param creatureSpawner the creature spawner
     */
    public Hole(CreatureSpawner creatureSpawner) {
        super('o', "Hole");
        this.creatureSpawner = creatureSpawner;
        this.turnsElapsed = 0;
    }

    /**
     * Ticks the hole and spawns a creature every 20 turns if the tile is empty.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        turnsElapsed++;

        if (turnsElapsed >= SPAWN_INTERVAL && !location.containsAnActor()) {
            Actor creature = creatureSpawner.spawnCreature();
            try {
                location.addActor(creature);
                new Display().println(creature + " emerges from a hole at " + location + ".");
                turnsElapsed = 0;
            } catch (GameEngineException ignored) {
                // Do nothing if the actor cannot be added.
            }
        }
    }
}