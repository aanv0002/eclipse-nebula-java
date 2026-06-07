package game.actions;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.AcidPool;
import game.grounds.ChargedFloor;
import game.grounds.Dirt;
import game.grounds.Floor;
import game.inventory.WeightLimitedInventory;
import game.security.PurgeCanister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PurgeTerrainActionTest {

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
        map.at(3, 4).setGround(new AcidPool());

        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);

        PurgeTerrainAction action = new PurgeTerrainAction(canister);
        action.execute(worker, map);

        assertInstanceOf(Floor.class, map.at(3, 4).getGround(),
                "PurgeTerrainAction must restore an adjacent AcidPool tile back to Floor");

        map.at(4, 3).setGround(new ChargedFloor());

        PurgeCanister canister2 = new PurgeCanister();
        worker.getInventory().add(canister2);

        PurgeTerrainAction action2 = new PurgeTerrainAction(canister2);
        action2.execute(worker, map);

        assertInstanceOf(Floor.class, map.at(4, 3).getGround(),
                "PurgeTerrainAction must restore an adjacent ChargedFloor tile back to Floor");

        PurgeCanister canister3 = new PurgeCanister();
        worker.getInventory().add(canister3);

        PurgeTerrainAction action3 = new PurgeTerrainAction(canister3);
        action3.execute(worker, map);

        assertFalse(worker.getInventory().getItems().contains(canister3),
                "PurgeTerrainAction must consume (remove) the PurgeCanister from the worker's inventory");
    }

    @Test
    void menuDescription() {
        PurgeCanister canister = new PurgeCanister();
        worker.getInventory().add(canister);
        PurgeTerrainAction action = new PurgeTerrainAction(canister);

        assertNotNull(action.menuDescription(worker),
                "menuDescription() must not return null");

        assertFalse(action.menuDescription(worker).isBlank(),
                "menuDescription() must not return a blank string");

        assertTrue(action.menuDescription(worker).contains(worker.toString()),
                "menuDescription() must reference the acting worker");
    }
}