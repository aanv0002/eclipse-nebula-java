package game.world;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actions.UnlockDoorAction;
import game.alarm.AlarmSystem;
import game.actors.AnomalousMannequin;
import game.actors.ContractedWorker;
import game.actors.Undead;
import game.grounds.*;
import game.grounds.flora.*;
import game.inventory.WeightLimitedInventory;
import game.items.*;
import game.spawners.HoleCreatureSpawner;
import game.teleport.TeleportDestination;
import game.teleport.Teleportable;
import game.spawners.VentCreatureSpawner;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the creation of the Eclipse Nebula world.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @author Shrabonti Majumder
 * @author Amra Anver
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
     * Initialises maps, actors, grounds, items, teleporters, doors, and REQ5 test area.
     *
     * @throws Exception if setup fails
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();

        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('_', Floor::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('S', Supercomputer::new);
        groundCreator.registerGround('≈', ToxicWaste::new);
        groundCreator.registerGround('=', AluminiumDoor::new);
        groundCreator.registerGround('N', IronDoor::new);
        groundCreator.registerGround('M', TitaniumDoor::new);
        groundCreator.registerGround('o', () -> new Hole(HoleCreatureSpawner.forDeprecatedMoon()));
        groundCreator.registerGround('V', () -> new Vent(new VentCreatureSpawner()));
        groundCreator.registerGround('!', () -> new AlarmTile(alarmSystem));
        groundCreator.registerGround('Φ', TeleportationTube::new);
        groundCreator.registerGround('◎', MagicCircle::new);
        groundCreator.registerGround('y', FleshySprout::new);
        groundCreator.registerGround('v', FleshySapling::new);
        groundCreator.registerGround('Y', FleshyMatureTree::new);
        groundCreator.registerGround('w', WarperSapling::new);
        groundCreator.registerGround('W', WarperMatureTree::new);

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

        configureOverflowSpawners(map20, moon20Overflow);

        this.addGameMap(map99);
        this.addGameMap(map20);

        placeUniqueShipItems(map99);
        placeFacilityItems99(map99);
        placeFacilityItems20(map20);
        placeFlora(map99);
        placeFlora(map20);

        configureTeleportationTubes(map99, map20);
        configureMagicCircles(map20);

        placeReq5TestingArea(map99);

        addWorkers(map99);
    }

    private void placeUniqueShipItems(GameMap map) {
        map.at(6, 2).addItem(new AccessCardLevel1());
        map.at(7, 2).addItem(new FirstAidKit());
        map.at(8, 2).addItem(new SterilisationBox());
        map.at(7, 3).setGround(new Supercomputer());
    }

    private void placeFacilityItems99(GameMap map) {
        map.at(11, 3).addItem(new Apple());
        map.at(12, 3).addItem(new Cookies());
        map.at(13, 3).addItem(new Lantern());
        map.at(14, 3).addItem(new FloppyDisk());
        map.at(15, 3).addItem(new CRTMonitor());
    }

    private void placeFacilityItems20(GameMap map) {
        map.at(45, 3).addItem(new AlienCube());
        map.at(43, 14).addItem(new AlienCube());
        map.at(32, 5).addItem(new AlienCube());
        map.at(33, 5).setGround(new Supercomputer());

        map.at(7, 3).addItem(new AccessCardLevel2());
        map.at(8, 3).addItem(new AccessCardLevel3());
    }

    private void placeFlora(GameMap map){
        map.at(9, 11).setGround(new FleshySprout());
        map.at(12, 10).setGround(new WarperSapling());
    }

    /**
     * Places REQ5 testing objects close to the starting area.
     *
     * Testing guide:
     * - The mannequin starts at (10, 3).
     * - Items near it help trigger ActiveState.
     * - Moving close to it helps trigger StalkingState.
     * - Standing adjacent to it helps trigger BerserkState.
     *
     * @param map test map
     * @throws Exception if the mannequin cannot be added
     */
    private void placeReq5TestingArea(GameMap map) throws Exception {
        map.addActor(new AnomalousMannequin(), map.at(14, 7));

        map.at(12, 7).addItem(new Apple());
        map.at(13, 7).addItem(new Cookies   ());
    }

    private void configureTeleportationTubes(GameMap map99, GameMap map20) {
        Teleportable tube99 = map99.at(6, 3).getGroundAs(Teleportable.class);
        Teleportable tube20 = map20.at(6, 3).getGroundAs(Teleportable.class);

        if (tube99 != null) {
            tube99.addDestination(new TeleportDestination(map99, map99.at(18, 2), "99-Deprecated facility entrance"));
            tube99.addDestination(new TeleportDestination(map20, map20.at(6, 3), "20-Overflow armoured ship"));
            tube99.addDestination(new TeleportDestination(map20, map20.at(31, 5), "20-Overflow factory floor"));

        }

        if (tube20 != null) {
            tube20.addDestination(new TeleportDestination(map20, map20.at(31, 5), "20-Overflow factory floor"));
            tube20.addDestination(new TeleportDestination(map99, map99.at(6, 3), "99-Deprecated armoured ship"));
        }
    }

    private void configureMagicCircles(GameMap map20) {
        List<TeleportDestination> destinations = new java.util.ArrayList<>();
        List<Teleportable> magicCircles = new java.util.ArrayList<>();

        for (int x : map20.getXRange()) {
            for (int y : map20.getYRange()) {
                Teleportable teleportable = map20.at(x, y).getGroundAs(Teleportable.class);

                if (teleportable != null && "the magic circle".equals(teleportable.getTeleportName())) {
                    destinations.add(new TeleportDestination(
                            map20,
                            map20.at(x, y),
                            "Magic Circle at (" + x + ", " + y + ")"
                    ));
                    magicCircles.add(teleportable);
                }
            }
        }

        for (Teleportable magicCircle : magicCircles) {
            magicCircle.setDestinations(destinations);
        }
    }

    /**
     * Configures 20-Overflow-only spawners.
     * Holes in 20-Overflow spawn Undead and Parasites, while vents spawn
     * Parasites and Slimes.
     *
     * @param map20 the 20-Overflow map
     * @param moon20Overflow source map strings
     */
    private void configureOverflowSpawners(GameMap map20, List<String> moon20Overflow) {
        for (int y = 0; y < moon20Overflow.size(); y++) {
            String row = moon20Overflow.get(y);
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == 'o') {
                    map20.at(x, y).setGround(new Hole(HoleCreatureSpawner.forOverflowMoon()));
                }
            }
        }

        map20.at(12, 6).setGround(new Vent(new VentCreatureSpawner()));
        map20.at(18, 12).setGround(new Vent(new VentCreatureSpawner()));
    }


    /**
     * Adds workers to the starting map.
     *
     * @param map starting map
     * @throws Exception if workers cannot be added
     */
    private void addWorkers(GameMap map) throws Exception {
       ContractedWorker contractedWorker1 = new ContractedWorker("#1 Bob", 'ඞ', 10, createStartingInventory());
     //  ContractedWorker contractedWorker2 = new ContractedWorker("#2 Tom", 'ඞ', 10, createStartingInventory());
    //   ContractedWorker contractedWorker3 = new ContractedWorker("#3 Sarah", 'ඞ', 10, createStartingInventory());
    //   ContractedWorker contractedWorker4 = new ContractedWorker("#4 Julie", 'ඞ', 10, createStartingInventory());
     //  ContractedWorker contractedWorker5 = new ContractedWorker("#5 Rick", 'ඞ', 10, createStartingInventory());

        this.addPlayer(contractedWorker1, map.at(5, 3));
      //  this.addPlayer(contractedWorker2, map.at(13, 2));
      //  this.addPlayer(contractedWorker3, map.at(8, 2));
      //  this.addPlayer(contractedWorker4, map.at(6, 4));
      //  this.addPlayer(contractedWorker5, map.at(8, 4));

    }

    private Inventory createStartingInventory() {
        Inventory inventory = new WeightLimitedInventory(WORKER_WEIGHT_LIMIT);
        inventory.add(new Flask());
        return inventory;
    }

    @Override
    protected void gameLoop() throws edu.monash.fit2099.engine.GameEngineException {
        alarmSystem.tick();
        super.gameLoop();
    }
}