package game.spawners;

import edu.monash.fit2099.engine.items.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * A random table of resource factories used by loot explosions.
 * Each entry has equal chance of being selected.
 *
 * @author Amra Anver
 * @version 1.0
 */
public class ResourceSpawnTable {

    /**
     * Resource factories stored in this spawn table.
     */
    private final List<Supplier<Item>> resourceSuppliers;

    /**
     * Random generator for selecting a resource factory.
     */
    private final Random random;

    /**
     * Creates a resource spawn table.
     *
     * @param resourceSuppliers available resource factories
     */
    public ResourceSpawnTable(List<Supplier<Item>> resourceSuppliers) {
        this.resourceSuppliers = new ArrayList<>(resourceSuppliers);
        this.random = new Random();
    }

    /**
     * Creates a random resource.
     *
     * @return randomly selected resource
     */
    public Item createResource() {
        return resourceSuppliers
                .get(random.nextInt(resourceSuppliers.size()))
                .get();
    }

    /**
     * Returns a read-only view of the resource factories.
     *
     * @return resource factories
     */
    public List<Supplier<Item>> entries() {
        return Collections.unmodifiableList(resourceSuppliers);
    }
}