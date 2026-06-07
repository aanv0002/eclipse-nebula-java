package game.actions;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.grounds.Floor;
import game.grounds.LaserBarrierGround;
import game.inventory.WeightLimitedInventory;
import game.security.LaserBarrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeployBarrierActionTest {

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
    void execute() {
        map.at(3, 4).setGround(new Floor());
        LaserBarrier barrier = new LaserBarrier();
        worker.getInventory().add(barrier);

        Location target = map.at(3, 4);
        DeployBarrierAction action = new DeployBarrierAction(barrier, target, "South");
        action.execute(worker, map);

        assertInstanceOf(LaserBarrierGround.class, target.getGround(),
                "DeployBarrierAction must replace the target tile with LaserBarrierGround");

        map.at(4, 3).setGround(new Floor());
        LaserBarrier barrier2 = new LaserBarrier();
        worker.getInventory().add(barrier2);

        DeployBarrierAction action2 = new DeployBarrierAction(barrier2, map.at(4, 3), "East");
        action2.execute(worker, map);

        assertFalse(worker.getInventory().getItems().contains(barrier2),
                "DeployBarrierAction must consume (remove) the LaserBarrier kit from inventory");

        map.at(2, 3).setGround(new Floor());
        LaserBarrier barrier3 = new LaserBarrier();
        worker.getInventory().add(barrier3);

        DeployBarrierAction action3 = new DeployBarrierAction(barrier3, map.at(2, 3), "West");
        String result = action3.execute(worker, map);

        assertNotNull(result,
                "DeployBarrierAction.execute() must not return null");

        assertTrue(result.contains("West"),
                "DeployBarrierAction.execute() must mention the deployment direction in the message");
    }

    @Test
    void menuDescription() {
        map.at(3, 4).setGround(new Floor());
        LaserBarrier barrier = new LaserBarrier();
        worker.getInventory().add(barrier);
        DeployBarrierAction action = new DeployBarrierAction(barrier, map.at(3, 4), "South");

        assertNotNull(action.menuDescription(worker),
                "menuDescription() must not return null");

        assertFalse(action.menuDescription(worker).isBlank(),
                "menuDescription() must not return a blank string");

        assertTrue(action.menuDescription(worker).contains("South"),
                "menuDescription() must reference the deployment direction");
    }
}