package game.actions;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ContractedWorker;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import game.security.SecuritySalvage;
import game.security.SpikeDrone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DefuseSecurityActionTest {

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
    void execute() throws Exception {
        // Test 1: executing the action must disarm the hazard
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(3, 4));
        Location hazardLoc = map.at(3, 4);

        DefuseSecurityAction action = new DefuseSecurityAction(drone, hazardLoc);
        action.execute(worker, map);

        assertFalse(drone.isActive(),
                "DefuseSecurityAction.execute() must leave the hazard in an inactive state");

        // Test 2: executing must drop a SecuritySalvage item on the hazard's tile
        SpikeDrone drone2 = new SpikeDrone();
        map.addActor(drone2, map.at(4, 3));
        Location hazardLoc2 = map.at(4, 3);

        DefuseSecurityAction action2 = new DefuseSecurityAction(drone2, hazardLoc2);
        action2.execute(worker, map);

        boolean hasSalvage = hazardLoc2.getItems().stream()
                .anyMatch(i -> i instanceof SecuritySalvage);

        assertTrue(hasSalvage,
                "DefuseSecurityAction.execute() must place a SecuritySalvage on the hazard tile");

        // Test 3: execute() must return a non-null, non-empty descriptive string
        SpikeDrone drone3 = new SpikeDrone();
        map.addActor(drone3, map.at(2, 3));

        DefuseSecurityAction action3 = new DefuseSecurityAction(drone3, map.at(2, 3));
        String result = action3.execute(worker, map);

        assertNotNull(result,
                "DefuseSecurityAction.execute() must not return null");

        assertFalse(result.isBlank(),
                "DefuseSecurityAction.execute() must return a non-empty message");
    }

    @Test
    void menuDescription() throws Exception {
        SpikeDrone drone = new SpikeDrone();
        map.addActor(drone, map.at(3, 4));

        DefuseSecurityAction action = new DefuseSecurityAction(drone, map.at(3, 4));

        assertNotNull(action.menuDescription(worker),
                "menuDescription() must not return null");

        assertFalse(action.menuDescription(worker).isBlank(),
                "menuDescription() must not be blank");

        assertTrue(action.menuDescription(worker).contains(worker.toString()),
                "menuDescription() must reference the acting worker");
    }
}