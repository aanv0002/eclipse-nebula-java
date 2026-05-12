package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.spawners.CreatureSpawnService;
import game.spawners.CreatureSpawner;
import game.statuses.PoisonStatus;

/**
 * A motion-activated vent in 20-overflow.
 * A vent only spawns when a worker is standing in one of its adjacent tiles.
 * After a successful spawn, it poisons all adjacent actors and the newly spawned
 * creature for 5 turns.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class Vent extends Ground {

    /**
     * Duration of poison applied by the vent.
     */
    private static final int POISON_DURATION = 5;

    /**
     * Damage per turn from vent poison.
     */
    private static final int POISON_DAMAGE = 1;

    /**
     * The spawner used by this vent.
     */
    private final CreatureSpawner creatureSpawner;

    /**
     * Service used to place creatures and apply spawn reactions.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Creates a vent.
     *
     * @param creatureSpawner the creature spawner
     */
    public Vent(CreatureSpawner creatureSpawner) {
        super('V', "Vent");
        this.creatureSpawner = creatureSpawner;
        this.spawnService = new CreatureSpawnService();
    }

    /**
     * Ticks the vent.
     * If a worker is adjacent, it attempts a motion-triggered spawn.
     *
     * @param location the location of this ground
     */
    @Override
    public void tick(Location location) {
        if (!hasAdjacentWorker(location)) {
            return;
        }

        Actor creature = creatureSpawner.spawnCreature();
        if (spawnService.spawnAt(location, creature)) {
            poisonSpawnArea(location);
            new Display().println(creature + " crawls out of a vent at " + location + ".");
        }
    }

    /**
     * Returns whether a worker is adjacent to the vent.
     *
     * @param location the vent location
     * @return true if a worker is adjacent
     */
    private boolean hasAdjacentWorker(Location location) {
        for (Exit exit : location.getExits()) {
            Actor actor = exit.getDestination().getActor();
            if (actor != null && actor.hasAbility(ActorCapabilities.WORKER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Poisons the spawned actor and every actor adjacent to the vent.
     *
     * @param location the vent location
     */
    private void poisonSpawnArea(Location location) {
        poisonActorAt(location);
        for (Exit exit : location.getExits()) {
            poisonActorAt(exit.getDestination());
        }
    }

    /**
     * Poisons the actor at a location if one exists.
     *
     * @param location the location to poison
     */
    private void poisonActorAt(Location location) {
        Actor actor = location.getActor();
        if (actor != null) {
            actor.addStatus(new PoisonStatus(POISON_DURATION, POISON_DAMAGE));
        }
    }
}
