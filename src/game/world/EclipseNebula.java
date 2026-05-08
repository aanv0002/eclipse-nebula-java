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
import game.doors.ClearanceLevel;
import game.grounds.*;
import game.inventory.WeightLimitedInventory;
import game.items.*;
import game.spawners.HoleCreatureSpawner;
import game.teleport.TeleportDestination;
import game.teleport.Teleportable;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the creation of the Eclipse Nebula world.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class EclipseNebula extends World {

    public static final int WORKER_WEIGHT_LIMIT = 50;

    private final AlarmSystem alarmSystem;

    public EclipseNebula(Display display) {
        super(display);
        this.alarmSystem = new AlarmSystem();
        Undead.setAlarmSystem(alarmSystem);
        UnlockDoorAction.setAlarmSystem(alarmSystem);
    }

    /**
     * Initialises maps, actors, grounds, items, teleporters, and doors.
     *
     * @throws Exception if setup fails
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();

        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('_', Floor::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('≈', ToxicWaste::new);
        groundCreator.registerGround('=', AluminiumDoor::new);
        groundCreator.registerGround('N', IronDoor::new);
        groundCreator.registerGround('M', TitaniumDoor::new);
        groundCreator.registerGround('o', () -> new Hole(new HoleCreatureSpawner()));
        groundCreator.registerGround('!', () -> new AlarmTile(alarmSystem));
        groundCreator.registerGround('Φ', TeleportationTube::new);
        groundCreator.registerGround('◎', MagicCircle::new);

        List<String> moon99Deprecated = Arrays.asList(
                "....................########################################",
                "...#######.......o..#__________________#________________o__#",
                "...#_____#..........=__________________=___________________#",
                "...#__Φ__=...~......#__________________#___________________#",
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

        List<String> moon20Overflow = Arrays.asList(
                ".....................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                "...#######...........≈≈≈≈≈≈≈≈≈≈≈≈≈≈##################≈≈≈≈≈≈≈",
                "...#_____#...........≈≈≈≈≈≈≈≈≈≈≈≈≈≈#________________#≈≈≈≈≈≈≈",
                "...#__Φ__=NM.........≈≈≈≈≈≈≈≈#######________________#≈≈≈≈≈≈≈",
                "...#_____#...........≈≈≈≈≈≈≈≈#_____=________________#≈≈≈≈≈≈≈",
                "...#######...≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_◎___###########N######≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_____#≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#########N#####≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#_____________#≈≈≈≈≈≈≈≈≈#___◎__#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#______o______#≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈######=########≈≈≈≈≈≈≈≈≈####M###≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈≈≈.≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈≈≈.≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈###############_#######≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈#_____________________________#≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈#_______N_____________≈≈≈≈____#≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈#___◎___#_____________≈≈≈≈≈≈__≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈######################≈≈≈≈≈≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                ".....................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                ".....................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈"
        );

        GameMap map99 = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        GameMap map20 = new GameMap("20-Overflow", groundCreator, moon20Overflow);

        this.addGameMap(map99);
        this.addGameMap(map20);

        placeUniqueShipItems(map99);
        placeFacilityItems99(map99);
        placeFacilityItems20(map20);

        configureTeleportationTubes(map99, map20);
        configureMagicCircles(map20);

        addWorkers(map99);
    }

    /**
     * Places the unique starting ship items in 99-Deprecated.
     *
     * @param map the 99-Deprecated map
     */
    private void placeUniqueShipItems(GameMap map) {
        map.at(6, 2).addItem(new AccessCard(ClearanceLevel.LEVEL_1));
        map.at(7, 2).addItem(new FirstAidKit());
        map.at(8, 2).addItem(new SterilisationBox());
    }

    /**
     * Places items in 99-Deprecated.
     *
     * @param map the 99-Deprecated map
     */
    private void placeFacilityItems99(GameMap map) {
        map.at(11, 3).addItem(new Apple());
        map.at(12, 3).addItem(new Cookies());
        map.at(13, 3).addItem(new Lantern());
        map.at(14, 3).addItem(new FloppyDisk());
        map.at(15, 3).addItem(new CRTMonitor());
    }

    /**
     * Places items in 20-Overflow.
     *
     * @param map the 20-Overflow map
     */
    private void placeFacilityItems20(GameMap map) {
        map.at(45, 3).addItem(new AlienCube());
        map.at(43, 14).addItem(new AlienCube());
        map.at(32, 5).addItem(new AlienCube());

        // Testing cards for higher-level doors.
        map.at(7, 3).addItem(new AccessCard(ClearanceLevel.LEVEL_2));
        map.at(8, 3).addItem(new AccessCard(ClearanceLevel.LEVEL_3));
    }

    /**
     * Configures teleportation tube destinations.
     *
     * @param map99 the 99-Deprecated map
     * @param map20 the 20-Overflow map
     */
    private void configureTeleportationTubes(GameMap map99, GameMap map20) {
        Teleportable tube99Teleportable = map99.at(6, 3).getGroundAs(Teleportable.class);
        Teleportable tube20Teleportable = map20.at(6, 3).getGroundAs(Teleportable.class);

        if (tube99Teleportable instanceof TeleportationTube tube99) {
            tube99.addDestination(new TeleportDestination(
                    map99,
                    map99.at(18, 2),
                    "99-Deprecated facility entrance"
            ));
            tube99.addDestination(new TeleportDestination(
                    map20,
                    map20.at(6, 3),
                    "20-Overflow armoured ship"
            ));
            tube99.addDestination(new TeleportDestination(
                    map20,
                    map20.at(31, 5),
                    "20-Overflow factory floor"
            ));
        }

        if (tube20Teleportable instanceof TeleportationTube tube20) {
            tube20.addDestination(new TeleportDestination(
                    map20,
                    map20.at(31, 5),
                    "20-Overflow factory floor"
            ));
            tube20.addDestination(new TeleportDestination(
                    map99,
                    map99.at(6, 3),
                    "99-Deprecated armoured ship"
            ));
        }
    }

    /**
     * Configures all magic circle destinations in 20-Overflow.
     *
     * @param map20 the 20-Overflow map
     */
    private void configureMagicCircles(GameMap map20) {
        List<TeleportDestination> destinations = new java.util.ArrayList<>();

        for (int x : map20.getXRange()) {
            for (int y : map20.getYRange()) {
                Teleportable teleportable = map20.at(x, y).getGroundAs(Teleportable.class);

                if (teleportable instanceof MagicCircle) {
                    destinations.add(new TeleportDestination(
                            map20,
                            map20.at(x, y),
                            "Magic Circle at (" + x + ", " + y + ")"
                    ));
                }
            }
        }

        for (TeleportDestination destination : destinations) {
            Teleportable teleportable = destination.getLocation().getGroundAs(Teleportable.class);

            if (teleportable instanceof MagicCircle circle) {
                circle.setCircleDestinations(destinations);
            }
        }
    }

    /**
     * Sets magic circle destinations on a location if the ground is a magic circle.
     *
     * @param location magic circle location
     * @param destinations all magic circle destinations
     */
    private void setMagicCircleDestinations(edu.monash.fit2099.engine.positions.Location location,
                                            List<TeleportDestination> destinations) {
        Teleportable teleportable = location.getGroundAs(Teleportable.class);

        if (teleportable instanceof MagicCircle circle) {
            circle.setCircleDestinations(destinations);
        }
    }

    /**
     * Adds workers to the starting map.
     *
     * @param map starting map
     * @throws Exception if workers cannot be added
     */
    private void addWorkers(GameMap map) throws Exception {
        ContractedWorker worker1 = new ContractedWorker("#1 Bob", 'ඞ', 10, createStartingInventory());
     //   ContractedWorker worker2 = new ContractedWorker("#2 Tom", 'ඞ', 10, createStartingInventory());

        this.addPlayer(worker1, map.at(5, 3));
      //  this.addPlayer(worker2, map.at(5, 4));
    }

    /**
     * Creates a starting inventory for each worker.
     *
     * @return starting inventory
     */
    private Inventory createStartingInventory() {
        Inventory inventory = new WeightLimitedInventory(WORKER_WEIGHT_LIMIT);
        inventory.add(new Flask());
        return inventory;
    }

    /**
     * Ticks the alarm system before the normal world loop.
     *
     * @throws edu.monash.fit2099.engine.GameEngineException if the game loop fails
     */
    @Override
    protected void gameLoop() throws edu.monash.fit2099.engine.GameEngineException {
        alarmSystem.tick();
        super.gameLoop();
    }
}