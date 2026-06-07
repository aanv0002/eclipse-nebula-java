package game.security;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.grounds.ChargedFloor;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StaticPylonTest {

    private GameMap map;
    private ContractedWorker worker;
    private StaticPylon pylon;

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

        pylon = new StaticPylon();
        map.at(3, 4).setGround(pylon);
    }

    @Test
    void tick() {
        // Tick 7 times — arc must NOT fire yet
        for (int i = 0; i < 7; i++) {
            pylon.tick(map.at(3, 4));
        }

        boolean anyChargedBefore = false;
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                if (map.at(x, y).getGround() instanceof ChargedFloor) {
                    anyChargedBefore = true;
                    break;
                }
            }
        }
        assertFalse(anyChargedBefore,
                "No ChargedFloor should appear before the 8th tick");

        // 8th tick — arc must fire and convert at least one tile to ChargedFloor
        pylon.tick(map.at(3, 4));

        boolean anyChargedAfter = false;
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                if (map.at(x, y).getGround() instanceof ChargedFloor) {
                    anyChargedAfter = true;
                    break;
                }
            }
        }
        assertTrue(anyChargedAfter,
                "StaticPylon must convert at least one tile to ChargedFloor after the 8th tick");
    }

    @Test
    void disarm() {
        // Disarming must deactivate the pylon
        pylon.disarm(map.at(3, 4));

        assertFalse(pylon.isActive(),
                "StaticPylon must be inactive after disarm()");

        // Ticking 8 times after disarm must NOT fire the arc
        for (int i = 0; i < 8; i++) {
            pylon.tick(map.at(3, 4));
        }

        boolean anyCharged = false;
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                if (map.at(x, y).getGround() instanceof ChargedFloor) {
                    anyCharged = true;
                    break;
                }
            }
        }
        assertFalse(anyCharged,
                "Disarmed StaticPylon must not fire arc even after 8 ticks");
    }

    @Test
    void isActive() {
        // Freshly created pylon must be active
        assertTrue(pylon.isActive(),
                "StaticPylon must be active on creation");

        // After disarm it must be inactive
        pylon.disarm(map.at(3, 4));
        assertFalse(pylon.isActive(),
                "StaticPylon must be inactive after disarm()");
    }
}