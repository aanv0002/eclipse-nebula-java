package game.statuses;

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

class CorrodedStatusTest {

    private GameMap map;
    private ContractedWorker worker;
    private WeightLimitedInventory inventory;

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

        inventory = new WeightLimitedInventory(50);
        worker = new ContractedWorker("Worker", '@', 100, inventory);
        map.addActor(worker, map.at(3, 3));
    }

    @Test
    void tickStatus() throws Exception {
        // Test 1: first tick must reduce the worker's weight limit from 50 to 30
        assertEquals(50, inventory.getWeightLimit(),
                "Worker must start with weight limit 50");

        CorrodedStatus status = new CorrodedStatus(10);
        worker.addStatus(status);
        status.tickStatus(worker, map.at(3, 3));

        assertEquals(30, inventory.getWeightLimit(),
                "CorrodedStatus must reduce the weight limit to 30 on the first tick");

        // Test 2: after the full duration, the original weight limit must be restored
        CorrodedStatus status2 = new CorrodedStatus(3);
        WeightLimitedInventory inv2 = new WeightLimitedInventory(50);
        ContractedWorker worker2 = new ContractedWorker("Worker2", '@', 100, inv2);
        map.addActor(worker2, map.at(1, 1));
        worker2.addStatus(status2);

        for (int i = 0; i < 3; i++) {
            status2.tickStatus(worker2, map.at(1, 1));
        }

        assertEquals(50, inv2.getWeightLimit(),
                "CorrodedStatus must restore the original weight limit after its duration expires");

        // Test 3: weight limit must remain at 30 while the status is still active
        CorrodedStatus status3 = new CorrodedStatus(5);
        WeightLimitedInventory inv3 = new WeightLimitedInventory(50);
        ContractedWorker worker3 = new ContractedWorker("Worker3", '@', 100, inv3);
        map.addActor(worker3, map.at(5, 5));
        worker3.addStatus(status3);

        status3.tickStatus(worker3, map.at(5, 5));
        status3.tickStatus(worker3, map.at(5, 5));

        assertEquals(30, inv3.getWeightLimit(),
                "Weight limit must remain at 30 while CorrodedStatus is still active mid-duration");

        assertTrue(status3.isStatusActive(),
                "CorrodedStatus must still be active mid-duration");
    }

    @Test
    void isStatusActive() throws Exception {
        // Test 1: newly created status with duration > 0 must be active
        CorrodedStatus status = new CorrodedStatus(10);
        assertTrue(status.isStatusActive(),
                "CorrodedStatus must be active immediately after creation");

        // Test 2: after all ticks consumed, status must be inactive
        CorrodedStatus status2 = new CorrodedStatus(2);
        WeightLimitedInventory inv2 = new WeightLimitedInventory(50);
        ContractedWorker worker2 = new ContractedWorker("Worker2", '@', 100, inv2);
        map.addActor(worker2, map.at(1, 1));
        worker2.addStatus(status2);

        status2.tickStatus(worker2, map.at(1, 1));
        status2.tickStatus(worker2, map.at(1, 1));

        assertFalse(status2.isStatusActive(),
                "CorrodedStatus must be inactive after all turns have been consumed");

        // Test 3: status remains active after only one of two ticks consumed
        CorrodedStatus status3 = new CorrodedStatus(2);
        WeightLimitedInventory inv3 = new WeightLimitedInventory(50);
        ContractedWorker worker3 = new ContractedWorker("Worker3", '@', 100, inv3);
        map.addActor(worker3, map.at(5, 5));
        worker3.addStatus(status3);

        status3.tickStatus(worker3, map.at(5, 5));

        assertTrue(status3.isStatusActive(),
                "CorrodedStatus must still be active after only one of two ticks");
    }
}