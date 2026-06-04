package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.actors.Parasite;
import game.actors.Slime;
import game.actors.Undead;
import game.actors.ScrapSnatcher;

import java.util.Arrays;

/**
 * A creature spawner for holes.
 * Holes in 99-deprecated spawn Undead, Slimes and ScrapSnatchers, while holes in 20-overflow
 * spawn Undead and Parasites.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @author Amra Anver
 * @version 3.0
 */
public class HoleCreatureSpawner implements CreatureSpawner {

    /**
     * The table of creatures this hole can spawn.
     */
    private final SpawnTable spawnTable;

    /**
     * Creates a hole spawner with a custom spawn table.
     *
     * @param spawnTable spawn table
     */
    public HoleCreatureSpawner(SpawnTable spawnTable) {
        this.spawnTable = spawnTable;
    }

    /**
     * Creates the 99-deprecated hole spawner.
     *
     * @return hole spawner for Undead, Slime and Scrap Snatchers
     */
    public static HoleCreatureSpawner forDeprecatedMoon() {
        return new HoleCreatureSpawner(new SpawnTable(Arrays.asList(Undead::new, Slime::new, ScrapSnatcher::new)));
    }

    /**
     * Creates the 20-overflow hole spawner.
     *
     * @return hole spawner for Undead and Parasite
     */
    public static HoleCreatureSpawner forOverflowMoon() {
        return new HoleCreatureSpawner(new SpawnTable(Arrays.asList(Undead::new, Parasite::new)));
    }

    /**
     * Spawns a random creature from this hole's spawn table.
     *
     * @return the spawned creature
     */
    @Override
    public Actor spawnCreature() {
        return spawnTable.createCreature();
    }

    /**
     * Copies this spawner so a grown hole inherits the same spawn table.
     *
     * @return copied hole spawner
     */
    @Override
    public CreatureSpawner copy() {
        return new HoleCreatureSpawner(new SpawnTable(spawnTable.entries()));
    }
}
