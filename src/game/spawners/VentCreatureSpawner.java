package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.actors.Parasite;
import game.actors.Slime;

import java.util.Arrays;

/**
 * A creature spawner for vents in 20-overflow.
 * Vents spawn Parasites and Slimes.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class VentCreatureSpawner implements CreatureSpawner {

    /**
     * The table of creatures this vent can spawn.
     */
    private final SpawnTable spawnTable;

    /**
     * Creates a vent spawner.
     */
    public VentCreatureSpawner() {
        this(new SpawnTable(Arrays.asList(Parasite::new, Slime::new)));
    }

    /**
     * Creates a vent spawner with a custom spawn table.
     *
     * @param spawnTable spawn table
     */
    private VentCreatureSpawner(SpawnTable spawnTable) {
        this.spawnTable = spawnTable;
    }

    /**
     * Spawns a random creature from this vent's spawn table.
     *
     * @return the spawned creature
     */
    @Override
    public Actor spawnCreature() {
        return spawnTable.createCreature();
    }

    /**
     * Copies this spawner with the same spawn capabilities.
     *
     * @return copied vent spawner
     */
    @Override
    public CreatureSpawner copy() {
        return new VentCreatureSpawner(new SpawnTable(spawnTable.entries()));
    }
}
