package game.infection;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Parasite;
import game.spawners.CreatureSpawnService;

/**
 * Infection effect for a worker host.
 * The infected worker takes 1 damage every turn and spawns a Parasite on an
 * adjacent tile every 5 turns.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class WorkerInfectionEffect implements InfectionEffect {

    /**
     * Damage dealt to the infected worker each turn.
     */
    private static final int DAMAGE_PER_TURN = 1;

    /**
     * Number of turns between forced Parasite spawns.
     */
    private static final int SPAWN_INTERVAL = 5;

    /**
     * The infected worker.
     */
    private final Actor worker;

    /**
     * Service used to spawn Parasites and apply spawn reactions.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Number of turns since the last forced spawn.
     */
    private int turnsElapsed;

    /**
     * Creates a worker infection effect.
     *
     * @param worker the infected worker
     */
    public WorkerInfectionEffect(Actor worker) {
        this.worker = worker;
        this.spawnService = new CreatureSpawnService();
        this.turnsElapsed = 0;
    }

    /**
     * Applies the immediate worker infection result.
     *
     * @param location the infected worker's location
     * @return infection result message
     */
    @Override
    public String onInfected(Location location) {
        return worker + " becomes a living hive.";
    }

    /**
     * Damages the worker and spawns a Parasite every 5 turns.
     *
     * @param location the infected worker's location
     */
    @Override
    public void tick(Location location) {
        worker.hurt(DAMAGE_PER_TURN);
        turnsElapsed++;

        if (turnsElapsed >= SPAWN_INTERVAL) {
            spawnService.spawnAdjacent(location, new Parasite());
            turnsElapsed = 0;
        }
    }

    /**
     * Returns whether the infection remains active.
     *
     * @return true while the worker is conscious
     */
    @Override
    public boolean isActive() {
        return worker.isConscious();
    }
}
