package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Random table of creature factories.
 */
public class SpawnTable {

    private final List<Supplier<Actor>> creatureSuppliers;
    private final Random random;

    public SpawnTable(List<Supplier<Actor>> creatureSuppliers) {
        this.creatureSuppliers = new ArrayList<>(creatureSuppliers);
        this.random = new Random();
    }

    public Actor createCreature() {
        return creatureSuppliers.get(random.nextInt(creatureSuppliers.size())).get();
    }

    public List<Supplier<Actor>> entries() {
        return Collections.unmodifiableList(creatureSuppliers);
    }
}
