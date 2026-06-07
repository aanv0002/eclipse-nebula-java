package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.grounds.Floor;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LaserBarrierTest {

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
    void allowableActions() throws Exception {
        // Test 1: adjacent Floor tile exists — must offer DeployBarrierAction
        map.at(3, 4).setGround(new Floor());

        LaserBarrier barrier = new LaserBarrier();
        worker.getInventory().add(barrier);

        ActionList actions = barrier.allowableActions(worker, map);
        boolean hasDeploy = actions.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.DeployBarrierAction);

        assertTrue(hasDeploy,
                "LaserBarrier must offer DeployBarrierAction when an adjacent empty Floor tile exists");

        // Test 2: all adjacent tiles are Dirt (not Floor) — must offer no deploy actions
        map.at(3, 4).setGround(new Dirt());

        LaserBarrier barrier2 = new LaserBarrier();
        worker.getInventory().add(barrier2);

        ActionList actions2 = barrier2.allowableActions(worker, map);
        boolean hasDeploy2 = actions2.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.DeployBarrierAction);

        assertFalse(hasDeploy2,
                "LaserBarrier must not offer DeployBarrierAction when no adjacent Floor tile exists");

        // Test 3: adjacent Floor tile is occupied by another actor — must not offer deploy action
        map.at(3, 4).setGround(new Floor());

        ContractedWorker blocker = new ContractedWorker("Blocker", 'X', 50, new WeightLimitedInventory(50));
        map.addActor(blocker, map.at(3, 4));

        LaserBarrier barrier3 = new LaserBarrier();
        worker.getInventory().add(barrier3);

        ActionList actions3 = barrier3.allowableActions(worker, map);
        boolean hasDeploy3 = actions3.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.DeployBarrierAction);

        assertFalse(hasDeploy3,
                "LaserBarrier must not offer DeployBarrierAction when the adjacent Floor tile is occupied");
    }

    @Test
    void getCountermeasureType() {
        LaserBarrier barrier = new LaserBarrier();

        assertEquals("Barrier", barrier.getCountermeasureType(),
                "LaserBarrier countermeasure type must be 'Barrier'");

        assertNotNull(barrier.getCountermeasureType(),
                "LaserBarrier countermeasure type must not be null");

        assertFalse(barrier.getCountermeasureType().isBlank(),
                "LaserBarrier countermeasure type must not be blank");
    }
}