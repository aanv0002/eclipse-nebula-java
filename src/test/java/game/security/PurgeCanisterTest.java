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

class PurgeCanisterTest {

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
    void allowableActions() {
        // Test 1: on a clean map, PurgeCanister must always offer PurgeTerrainAction
        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);

        ActionList actions = canister.allowableActions(worker, map);
        boolean hasPurge = actions.getUnmodifiableActionList().stream()
                .anyMatch(a -> a instanceof game.actions.PurgeTerrainAction);
        assertTrue(hasPurge,
                "PurgeCanister must always offer PurgeTerrainAction");

        // Test 2: must offer exactly one action (it is self-contained, no adjacent detection needed)
        assertEquals(1, actions.getUnmodifiableActionList().size(),
                "PurgeCanister must offer exactly one action");

        // Test 3: action list is not null
        assertNotNull(actions,
                "PurgeCanister.allowableActions() must never return null");
    }

    @Test
    void getCountermeasureType() {
        // Test 1: returns correct type string
        PurgeCanister canister = new PurgeCanister();
        assertEquals("Purge", canister.getCountermeasureType(),
                "PurgeCanister countermeasure type must be 'Purge'");

        // Test 2: type is not null
        assertNotNull(canister.getCountermeasureType(),
                "PurgeCanister countermeasure type must not be null");

        // Test 3: type is not blank
        assertFalse(canister.getCountermeasureType().isBlank(),
                "PurgeCanister countermeasure type must not be blank");
    }
}