package game.world;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actions.UnlockDoorAction;
import game.alarm.AlarmSystem;
import game.actors.ContractedWorker;
import game.actors.Undead;
import game.grounds.*;
import game.inventory.WeightLimitedInventory;
import game.items.*;
import game.spawners.FleshyTreeSpawner;
import game.spawners.HoleCreatureSpawner;
import game.grounds.flora.*;
import game.spawners.TreeSpawner;
import game.spawners.WarperTreeSpawner;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the creation of the Eclipse Nebula world.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class EclipseNebula extends World {

    /**
     * Inventory weight limit for each worker.
     */
    public static final int WORKER_WEIGHT_LIMIT = 50;

    /**
     * Shared facility alarm system.
     */
    private final AlarmSystem alarmSystem;

    /**
     * Creates the game world.
     *
     * @param display the display object
     */
    public EclipseNebula(Display display) {
        super(display);
        this.alarmSystem = new AlarmSystem();
        Undead.setAlarmSystem(alarmSystem);
        UnlockDoorAction.setAlarmSystem(alarmSystem);
    }

    /**
     * Initialise maps, actors, items, and grounds of the game world.
     *
     * @throws Exception in case if anything goes wrong
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('_', Floor::new);
        groundCreator.registerGround('=', Door::new);
        groundCreator.registerGround('o', () -> new Hole(new HoleCreatureSpawner()));
        groundCreator.registerGround('!', () -> new AlarmTile(alarmSystem));
        groundCreator.registerGround('y', FleshySprout::new);
        groundCreator.registerGround('v', FleshySapling::new);
        groundCreator.registerGround('Y', FleshyMatureTree::new);
        groundCreator.registerGround('w', WarperSapling::new);
        groundCreator.registerGround('W', WarperMatureTree::new);

        List<String> moon99Deprecated = Arrays.asList(
                "....................########################################",
                "...#######.......o..#__________________#________________o__#",
                "...#_____#..........=__________________=___________________#",
                "...#_____=...~......#__________________#___________________#",
                "...#_____#..~!~.....########=#####=#####___#############___#",
                "...#######.~~~~.....#______#_#_________#___#___________#___#",
                ".........~~~~..!....#______#_#_________#####___________#####",
                "....................#______=_#_________#___________________#",
                "......~....o........#______#_#_________#___________________#",
                ".....~~~............#______#_###########___#############___#",
                ".....~..............#______#___________#___#___________#___#",
                "....................=______#___________=___=_____o_____=___#",
                "....o...............#______#############___#############___#",
                ".........~~~~.......#______#___________#####################",
                "........~~~~~~......#______#___________=___________________#",
                ".........~~~~.o.....#______#___________#___________________#",
                "....................#______#############___#############___#",
                "......o.............#______#_____o_____#___#___________#_o_#",
                "..~.................#______=___________=___=___________=___#",
                "....................########################################"
        );

        GameMap moon99DeprecatedMap = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        this.addGameMap(moon99DeprecatedMap);

        placeUniqueShipItems(moon99DeprecatedMap);
        placeFacilityItems(moon99DeprecatedMap);
        addWorkers(moon99DeprecatedMap);
        placeTrees(moon99DeprecatedMap);
    }

    @Override
    protected void gameLoop() throws edu.monash.fit2099.engine.GameEngineException {
        alarmSystem.tick();
        super.gameLoop();
    }

    private void placeUniqueShipItems(GameMap map) {
        map.at(6, 2).addItem(new AccessCard());
        map.at(7, 2).addItem(new FirstAidKit());
        map.at(8, 2).addItem(new SterilisationBox());
        map.at(8, 3).addItem(new AccessCard());
    }

    private void placeFacilityItems(GameMap map) {
        map.at(11, 3).addItem(new Apple());
        map.at(12, 3).addItem(new Cookies());
        map.at(13, 3).addItem(new Lantern());
        map.at(14, 3).addItem(new FloppyDisk());
        map.at(15, 3).addItem(new CRTMonitor());
        map.at(10, 8).addItem(new Cookies());

    }

    private void addWorkers(GameMap map) throws Exception {
        ContractedWorker worker1 = new ContractedWorker("#1 Bob", 'ඞ', 10, createStartingInventory());
        ContractedWorker worker2 = new ContractedWorker("#2 Tom", 'ඞ', 10, createStartingInventory());

        this.addPlayer(worker1, map.at(12, 4));
        this.addPlayer(worker2, map.at(8, 3));
    }

    private Inventory createStartingInventory() {
        Inventory inventory = new WeightLimitedInventory(WORKER_WEIGHT_LIMIT);
        inventory.add(new Flask());
        return inventory;
    }

    /**
     * Places initial flora onto the map
     * @param map game map
     */
    private void placeTrees(GameMap map) {
        TreeSpawner fleshyTreeSpawner = new FleshyTreeSpawner();
        TreeSpawner warperTreeSpawner = new WarperTreeSpawner();
        map.at(10, 5).setGround(fleshyTreeSpawner.spawnTree());
        map.at(20, 8).setGround(warperTreeSpawner.spawnTree());
    }
}