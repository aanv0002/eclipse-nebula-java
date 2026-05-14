package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.actors.Slime;
import game.actors.Undead;

/**
 * Spawns creatures for fleshy trees when the correct condition is met.
 * @author Amra Anver
 * @version 1.0
 */
public class FleshyTreeCreatureSpawner implements CreatureSpawner {

    /**
     * Service used to spawn creatures and trigger REQ4 spawn reactions
     */
    private final CreatureSpawnService creatureSpawnService;

    /**
     * Creates a fleshy tree creature spawner.
     */
    public FleshyTreeCreatureSpawner() {
        this.creatureSpawnService = new CreatureSpawnService();
    }

    /**
     * Creates a default creature.
     *
     * @return a new Slime
     */
    @Override
    public Actor spawnCreature() {
        return new Slime();
    }

    /**
     * Creates a copy of this creature spawner.
     *
     * @return a copied creature spawner
     */
    @Override
    public CreatureSpawner copy() {
        return new FleshyTreeCreatureSpawner();
    }

    /**
     * Spawns one Slime around the fleshy tree if a worker is beside the tree.
     *
     * @param treeLocation the location of the fleshy tree
     * @return true if a Slime was spawned
     */
    public boolean spawnSlimeIfWorkerNearby(Location treeLocation) {
        if (!hasWorkerNearby(treeLocation)) {
            return false;
        }

        return spawnAroundTree(treeLocation, new Slime());
    }

    /**
     * Spawns one Undead around the fleshy tree if a worker is beside the tree.
     *
     * @param treeLocation the location of the fleshy tree
     * @return true if an Undead was spawned
     */
    public boolean spawnUndeadIfWorkerNearby(Location treeLocation) {
        if (!hasWorkerNearby(treeLocation)) {
            return false;
        }

        return spawnAroundTree(treeLocation, new Undead());
    }

    /**
     * Checks whether a worker is standing in any adjacent tile.
     *
     * @param treeLocation the location of the fleshy tree
     * @return true if a worker is nearby
     */
    private boolean hasWorkerNearby(Location treeLocation) {
        for (Exit exit : treeLocation.getExits()) {
            Location destination = exit.getDestination();
            Actor actor = destination.getActor();

            if (actor != null && actor.hasAbility(ActorCapabilities.WORKER)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Attempts to place the creature in one surrounding tile of the tree.
     *
     * @param treeLocation the location of the fleshy tree
     * @param creature the creature to spawn
     * @return true if the creature was placed successfully
     */
    private boolean spawnAroundTree(Location treeLocation, Actor creature) {
        for (Exit exit : treeLocation.getExits()) {
            Location destination = exit.getDestination();

            if (creatureSpawnService.spawnAt(destination, creature)) {
                return true;
            }
        }

        return false;
    }
}