package game.grounds;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.inventory.WeightLimitedInventory;
import game.statuses.StunnedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ChargedFloorTest {

    private GameMap map;
    private ContractedWorker worker;

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

        worker = new ContractedWorker("Worker", '@', 100, new WeightLimitedInventory(50));
        map.addActor(worker, map.at(3, 3));
    }

    @Test
    void tick() {
        // Test 1: actor standing on ChargedFloor must receive StunnedStatus after tick()
        map.at(3, 3).setGround(new ChargedFloor());

        map.at(3, 3).getGround().tick(map.at(3, 3));

        assertTrue(worker.hasStatus(StunnedStatus.class),
                "ChargedFloor.tick() must apply StunnedStatus to an actor standing on it");

        // Test 2: tick() on an empty ChargedFloor tile must not throw
        map.at(2, 2).setGround(new ChargedFloor());

        assertDoesNotThrow(() -> map.at(2, 2).getGround().tick(map.at(2, 2)),
                "ChargedFloor.tick() must not throw when no actor is on the tile");

        // Test 3: an actor already stunned must not receive a duplicate StunnedStatus on the same tick
        // (the code checks hasStatus before adding — so count should remain 1)
        long statusCountBefore = worker.statusesOf(StunnedStatus.class).size();
        map.at(3, 3).getGround().tick(map.at(3, 3)); // second tick while already stunned

        long statusCountAfter = worker.statusesOf(StunnedStatus.class).size();
        assertEquals(statusCountBefore, statusCountAfter,
                "ChargedFloor.tick() must not stack duplicate StunnedStatus on an already-stunned actor");
    }

    @Test
    void purge() {
        // Test 1: purge() must replace ChargedFloor with a clean Floor
        map.at(2, 2).setGround(new ChargedFloor());
        ChargedFloor charged = (ChargedFloor) map.at(2, 2).getGround();
        charged.purge(map.at(2, 2));

        assertInstanceOf(Floor.class, map.at(2, 2).getGround(),
                "ChargedFloor.purge() must replace the tile with a clean Floor");

        // Test 2: after purge(), the tile must no longer be a ChargedFloor
        map.at(4, 4).setGround(new ChargedFloor());
        ChargedFloor charged2 = (ChargedFloor) map.at(4, 4).getGround();
        charged2.purge(map.at(4, 4));

        assertFalse(map.at(4, 4).getGround() instanceof ChargedFloor,
                "After purge(), the tile must no longer be a ChargedFloor");

        // Test 3: purge() must not throw even when an actor is standing on the tile
        map.at(3, 3).setGround(new ChargedFloor());
        ChargedFloor charged3 = (ChargedFloor) map.at(3, 3).getGround();

        assertDoesNotThrow(() -> charged3.purge(map.at(3, 3)),
                "ChargedFloor.purge() must not throw when an actor is standing on the tile");
    }
}