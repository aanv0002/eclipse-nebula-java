package game.spawners;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.items.AlienArtifact;
import game.items.AluminiumScrap;
import game.items.IndustrialFan;

import java.util.Arrays;

/**
 * Creates the loot explosion caused by a Scrap Snatcher.
 *
 * @author Amra Anver
 * @version 1.0
 */
public class LootExplosionService {

    /**
     * Random resource table.
     */
    private final ResourceSpawnTable resourceTable =
            new ResourceSpawnTable(Arrays.asList(
                    AluminiumScrap::new,
                    IndustrialFan::new,
                    AlienArtifact::new
            ));

    /**
     * Drops one random resource on every adjacent tile.
     *
     * @param centre Scrap Snatcher location
     */
    public void explode(Location centre) {

        for (Exit exit : centre.getExits()) {
            exit.getDestination().addItem(
                    resourceTable.createResource()
            );
        }
    }
}