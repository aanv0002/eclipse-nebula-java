package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * A random table of creature factories used by creature spawners.
 * Each entry has equal chance of being selected.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class SpawnTable {

    /**
     * Creature factories stored in this spawn table.
     */
    private final List<Supplier<Actor>> creatureSuppliers;

    /**
     * Random generator for selecting a creature factory.
     */
    private final Random random;

    /**
     * Creates a spawn table.
     *
     * @param creatureSuppliers the creature factories available for spawning
     */
    public SpawnTable(List<Supplier<Actor>> creatureSuppliers) {
        this.creatureSuppliers = new ArrayList<>(creatureSuppliers);
        this.random = new Random();
    }

    /**
     * Creates a creature from one randomly selected table entry.
     *
     * @return the spawned creature
     */
    public Actor createCreature() {
        return creatureSuppliers.get(random.nextInt(creatureSuppliers.size())).get();
    }

    /**
     * Returns a read-only view of the creature factories.
     *
     * @return the creature factories
     */
    public List<Supplier<Actor>> entries() {
        return Collections.unmodifiableList(creatureSuppliers);
    }
}
