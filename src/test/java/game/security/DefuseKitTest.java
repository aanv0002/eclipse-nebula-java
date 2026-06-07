package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
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

class DefuseKitTest {

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
        // Test 1: active SpikeDrone adjacent — must offer DefuseSecurityAction
        DefuseKit kit = new DefuseKit();
        worker.getInventory().add(kit);

        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(3, 4));

        ActionList actions = kit.allowableActions(worker, map);
        boolean hasDefuse = actions.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.DefuseSecurityAction);

        assertTrue(hasDefuse,
                "DefuseKit must offer DefuseSecurityAction when an active SpikeDrone is adjacent");

        // Test 2: no hazard adjacent — must return empty list
        map.removeActor(drone);

        DefuseKit kit2 = new DefuseKit();
        worker.getInventory().add(kit2);

        ActionList actions2 = kit2.allowableActions(worker, map);
        boolean hasDefuse2 = actions2.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.DefuseSecurityAction);

        assertFalse(hasDefuse2,
                "DefuseKit must not offer DefuseSecurityAction when no hazard is adjacent");

        // Test 3: adjacent SpikeDrone already disarmed — must NOT offer DefuseSecurityAction
        SpikeDrone drone2 = new SpikeDrone();
        map.addActor(drone2, map.at(3, 4));
        drone2.disarm(map.at(3, 4));

        DefuseKit kit3 = new DefuseKit();
        worker.getInventory().add(kit3);

        ActionList actions3 = kit3.allowableActions(worker, map);
        boolean hasDefuse3 = actions3.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.DefuseSecurityAction);

        assertFalse(hasDefuse3,
                "DefuseKit must not offer DefuseSecurityAction for an already-disarmed hazard");
    }

    @Test
    void getCountermeasureType() {
        DefuseKit kit = new DefuseKit();

        assertEquals("Defuse", kit.getCountermeasureType(),
                "DefuseKit countermeasure type must be 'Defuse'");

        assertNotNull(kit.getCountermeasureType(),
                "DefuseKit countermeasure type must not be null");

        assertFalse(kit.getCountermeasureType().isBlank(),
                "DefuseKit countermeasure type must not be blank");
    }
}