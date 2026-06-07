package game.spawners;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.Exit;
import game.grounds.Dirt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amra Anver
 * @version 1.0
 */
class LootExplosionServiceTest {
    /**
     * Sets up a smaller test map to place creature, actors and set grounds to observe their behaviour.
     *
     * @throws Exception if map or actor setup fails
     */
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap("REQ2 Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        game.world.EclipseNebula world =
                new game.world.EclipseNebula(new Display());

        world.addGameMap(map);

        return map;
    }
    /**
     * Tests that a loot explosion places exactly one item on every
     * adjacent location surrounding the centre tile.
     *
     * Verifies that each neighbouring location receives a resource
     * when the loot explosion is triggered.
     */
    @Test
    void lootExplosionPlacesOneItemOnEveryAdjacentTile() throws Exception {

        GameMap map = createTestMap();

        LootExplosionService service = new LootExplosionService();

        Location centre = map.at(3, 3);

        service.explode(centre);

        for (Exit exit : centre.getExits()) {

            assertEquals(1, exit.getDestination().getItems().size()
            );
        }
    }
    /**
     * Tests that a loot explosion does not place resources on
     * the Scrap Snatcher's own tile.
     *
     * Verifies that resources are only distributed to adjacent
     * locations.
     */
    @Test
    void lootExplosionDoesNotPlaceLootOnCentreTile() throws Exception {

        GameMap map = createTestMap();

        LootExplosionService service = new LootExplosionService();

        Location centre = map.at(3, 3);

        service.explode(centre);

        assertEquals(0, centre.getItems().size()
        );
    }
    /**
     * Tests that generated resources are valid depositable resource types.
     * *
     * Verifies that resources originate from the configured
     * resource spawn table.
     */
    @Test
    void lootExplosionGeneratesValidResourceTypes() throws Exception{

        GameMap map = createTestMap();

        LootExplosionService service = new LootExplosionService();

        Location centre = map.at(3, 3);

        service.explode(centre);

        for (Exit exit : centre.getExits()) {

            String itemName =
                    exit.getDestination()
                            .getItems()
                            .get(0)
                            .toString();

            assertTrue(
                    itemName.equals("Aluminium Scrap")
                            || itemName.equals("Industrial Fan")
                            || itemName.equals("Alien Artifact")
            );
        }
    }
    /**
     * Tests that multiple loot explosions stack resources
     * on neighbouring locations.
     * *
     * Verifies that resources are added on every explosion
     * rather than replacing existing resources.
     */
    @Test
    void repeatedLootExplosionsStackResources() throws Exception {

        GameMap map = createTestMap();

        LootExplosionService service = new LootExplosionService();

        Location centre = map.at(3, 3);

        service.explode(centre);
        service.explode(centre);

        for (Exit exit : centre.getExits()) {

            assertEquals(
                    2,
                    exit.getDestination().getItems().size()
            );
        }
    }
}