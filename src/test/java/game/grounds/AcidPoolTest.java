package game.grounds;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.security.SecuritySalvage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AcidPoolTest {

    private GameMap map;

    @BeforeEach
    void setUp() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        map = new GameMap("Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        game.world.EclipseNebula world = new game.world.EclipseNebula(new Display());
        world.addGameMap(map);
    }

    @Test
    void tick() {
        // Test 1: an item placed on AcidPool must be dissolved (removed) after tick()
        map.at(2, 2).setGround(new AcidPool());
        SecuritySalvage salvage = new SecuritySalvage();
        map.at(2, 2).addItem(salvage);

        map.at(2, 2).getGround().tick(map.at(2, 2));

        assertFalse(map.at(2, 2).getItems().contains(salvage),
                "AcidPool.tick() must dissolve (remove) items on its tile");

        // Test 2: tick() on an empty AcidPool must not throw and must leave ground unchanged
        map.at(3, 3).setGround(new AcidPool());

        assertDoesNotThrow(() -> map.at(3, 3).getGround().tick(map.at(3, 3)),
                "AcidPool.tick() must not throw when the tile is empty");
        assertInstanceOf(AcidPool.class, map.at(3, 3).getGround(),
                "AcidPool must remain as the ground type when no items are dissolved");

        // Test 3: multiple items on the tile must all be dissolved in a single tick
        map.at(4, 4).setGround(new AcidPool());
        SecuritySalvage item1 = new SecuritySalvage();
        SecuritySalvage item2 = new SecuritySalvage();
        map.at(4, 4).addItem(item1);
        map.at(4, 4).addItem(item2);

        map.at(4, 4).getGround().tick(map.at(4, 4));

        assertTrue(map.at(4, 4).getItems().isEmpty(),
                "AcidPool.tick() must dissolve ALL items on its tile in one tick");
    }

    @Test
    void purge() {
        // Test 1: purge() must replace AcidPool with a Floor tile
        map.at(2, 2).setGround(new AcidPool());
        AcidPool pool = (AcidPool) map.at(2, 2).getGround();
        pool.purge(map.at(2, 2));

        assertInstanceOf(Floor.class, map.at(2, 2).getGround(),
                "AcidPool.purge() must replace the tile with a clean Floor");

        // Test 2: after purge(), the tile must no longer be an AcidPool
        map.at(3, 3).setGround(new AcidPool());
        AcidPool pool2 = (AcidPool) map.at(3, 3).getGround();
        pool2.purge(map.at(3, 3));

        assertFalse(map.at(3, 3).getGround() instanceof AcidPool,
                "After purge(), the tile must no longer be an AcidPool");

        // Test 3: purge() must not throw even when the AcidPool tile holds items
        map.at(4, 4).setGround(new AcidPool());
        map.at(4, 4).addItem(new SecuritySalvage());
        AcidPool pool3 = (AcidPool) map.at(4, 4).getGround();

        assertDoesNotThrow(() -> pool3.purge(map.at(4, 4)),
                "AcidPool.purge() must not throw when the tile still contains items");
    }
}