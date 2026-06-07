package game.security;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GravityCoreTest {

    private GameMap map;
    private ContractedWorker worker;
    private GravityCore core;

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

        // Worker at (3,3), core placed at (3,4) — 1 tile apart so tick triggers
        worker = new ContractedWorker("Worker", '@', 100, new WeightLimitedInventory(50));
        map.addActor(worker, map.at(3, 3));

        core = new GravityCore();
        map.at(3, 4).addItem(core);
    }

    @Test
    void tick() {
        // Worker is 1 tile away — tick must trigger implosion
        core.tick(map.at(3, 4));

        assertFalse(core.isActive(),
                "GravityCore must become inactive after imploding when actor is within 1 tile");
        assertFalse(map.at(3, 4).getItems().contains(core),
                "GravityCore must remove itself from the tile after imploding");
    }

    @Test
    void disarm() {
        // Disarming must deactivate the core without triggering implosion
        core.disarm(map.at(3, 4));

        assertFalse(core.isActive(),
                "GravityCore must be inactive after disarm()");

        // Ticking after disarm must NOT remove itself or affect anything
        core.tick(map.at(3, 4));
        // No exception and worker still in original position
        assertTrue(map.at(3, 3).containsAnActor(),
                "Worker must not be moved after disarmed GravityCore tick");
    }

    @Test
    void isActive() {
        // Freshly created core must be active
        assertTrue(core.isActive(),
                "GravityCore must be active on creation");

        // After disarm it must be inactive
        core.disarm(map.at(3, 4));
        assertFalse(core.isActive(),
                "GravityCore must be inactive after disarm()");
    }
}