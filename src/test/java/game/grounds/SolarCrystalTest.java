package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.World;
import game.actors.ContractedWorker;
import game.inventory.WeightLimitedInventory;
import game.statuses.BlindStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SolarCrystal in REQ3 Eclipse Weather System.
 *
 * Tests observable behaviour:
 * damage, BlindStatus application, and ground restoration after explosion.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
class SolarCrystalTest {

    /**
     * Test world used only to initialise actor locations for unit tests.
     */
    private static class TestWorld extends World {
        TestWorld() {
            super(new Display());
        }

        @Override
        protected void gameLoop() throws GameEngineException {
        }
    }

    /**
     * Creates a small deterministic test map.
     *
     * @return test map
     * @throws Exception if map setup fails
     */
    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap("SolarCrystal Test Map", groundCreator, Arrays.asList(
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                ".......",
                "......."
        ));

        new TestWorld().addGameMap(map);
        return map;
    }

    /**
     * Tests that SolarCrystal damages a worker standing on it.
     *
     * @throws Exception if setup fails
     */
    @Test
    void solarCrystalDamagesWorker() throws Exception {
        GameMap map = createTestMap();
        ContractedWorker worker = new ContractedWorker("#1 Test Worker", 'ඞ', 10,
                new WeightLimitedInventory(50));

        map.at(3, 3).setGround(new SolarCrystal(map.at(3, 3).getGround()));
        map.addActor(worker, map.at(3, 3));

        map.at(3, 3).getGround().tick(map.at(3, 3));

        assertTrue(worker.toString().contains("6/10"));
    }

    /**
     * Tests that SolarCrystal applies BlindStatus to a worker after explosion.
     *
     * @throws Exception if setup fails
     */
    @Test
    void solarCrystalAppliesBlindStatusToWorker() throws Exception {
        GameMap map = createTestMap();
        ContractedWorker worker = new ContractedWorker("#1 Test Worker", 'ඞ', 10,
                new WeightLimitedInventory(50));

        map.at(3, 3).setGround(new SolarCrystal(map.at(3, 3).getGround()));
        map.addActor(worker, map.at(3, 3));

        map.at(3, 3).getGround().tick(map.at(3, 3));

        assertFalse(worker.statusesOf(BlindStatus.class).isEmpty());
    }

    /**
     * Tests that SolarCrystal restores the original ground after exploding.
     *
     * @throws Exception if setup fails
     */
    @Test
    void solarCrystalRestoresOriginalGroundAfterExplosion() throws Exception {
        GameMap map = createTestMap();
        ContractedWorker worker = new ContractedWorker("#1 Test Worker", 'ඞ', 10,
                new WeightLimitedInventory(50));

        Ground originalGround = map.at(3, 3).getGround();

        map.at(3, 3).setGround(new SolarCrystal(originalGround));
        map.addActor(worker, map.at(3, 3));

        map.at(3, 3).getGround().tick(map.at(3, 3));

        assertSame(originalGround, map.at(3, 3).getGround());
    }
}