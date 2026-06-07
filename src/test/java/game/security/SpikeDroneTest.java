package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.AcidPool;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import game.statuses.CorrodedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SpikeDroneTest {

    private GameMap map;
    private ContractedWorker worker;
    private SpikeDrone drone;

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

        drone = new SpikeDrone();
        map.addActor(drone, map.at(3, 4));
    }

    @Test
    void playTurn() {
        drone.playTurn(new ActionList(), null, map, new Display());

        assertInstanceOf(AcidPool.class, map.at(3, 4).getGround(),
                "SpikeDrone explosion must convert its tile to AcidPool");
        assertTrue(worker.hasStatus(CorrodedStatus.class),
                "Worker adjacent to SpikeDrone explosion must receive CorrodedStatus");
    }

    @Test
    void disarm() {
        drone.disarm(map.at(3, 4));

        assertFalse(drone.isActive(),
                "SpikeDrone must be inactive after disarm()");
        assertFalse(map.at(3, 4).containsAnActor(),
                "Disarmed SpikeDrone must be removed from the map");
    }

    @Test
    void isActive() {
        assertTrue(drone.isActive(),
                "SpikeDrone must be active on creation");

        drone.disarm(map.at(3, 4));
        assertFalse(drone.isActive(),
                "SpikeDrone must be inactive after disarm()");
    }
}