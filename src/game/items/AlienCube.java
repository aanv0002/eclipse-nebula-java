package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.SellAction;
import game.actions.TeleportAction;
import game.teleport.CubeCorruptEffect;
import game.teleport.Teleportable;
import game.teleport.TeleportDestination;
import game.teleport.TeleportEffect;
import game.teleport.TeleportService;

import java.util.ArrayList;
import java.util.List;

/**
 * A strange cube that can teleport the holder to three fixed random locations
 * within the current map.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class AlienCube extends Item implements Teleportable {

    /**
     * Weight of the alien cube.
     */
    public static final int WEIGHT = 1;

    /**
     * Number of random locations presented by the alien cube.
     */
    private static final int RANDOM_DESTINATION_COUNT = 3;

    /**
     * Cached random destinations for the current map.
     */
    private List<TeleportDestination> fixedDestinations;

    /**
     * The map that the cached destinations belong to.
     */
    private GameMap destinationMap;

    /**
     * Creates an alien cube.
     */
    public AlienCube() {
        super("Alien Cube", '◈');
        this.fixedDestinations = new ArrayList<>();
        this.destinationMap = null;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Returns three fixed random destinations for the current map.
     * The destinations are generated once per map and reused so that random
     * location 1, 2, and 3 remain consistent while the cube is used on that map.
     *
     * @param actor the actor using the cube
     * @param map the current map
     * @return fixed random destinations
     */
    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        if (destinationMap != map || fixedDestinations.isEmpty()) {
            generateFixedDestinations(actor, map);
        }

        return fixedDestinations;
    }

    /**
     * Generates three fixed random locations for the current map.
     *
     * @param actor the actor using the cube
     * @param map the current map
     */
    private void generateFixedDestinations(Actor actor, GameMap map) {
        TeleportService service = new TeleportService();
        List<Location> randomLocations = service.getRandomLocations(map, actor, RANDOM_DESTINATION_COUNT);

        fixedDestinations = new ArrayList<>();
        destinationMap = map;

        int number = 1;
        for (Location location : randomLocations) {
            fixedDestinations.add(new TeleportDestination(
                    map,
                    location,
                    "random location " + number++
            ));
        }
    }

    /**
     * Returns the corruption effect applied when the cube is used.
     *
     * @return cube corruption effect
     */
    @Override
    public TeleportEffect getEffect() {
        return new CubeCorruptEffect();
    }

    /**
     * Returns the teleport device name.
     *
     * @return device name
     */
    @Override
    public String getTeleportName() {
        return "the alien cube";
    }

    /**
     * Returns teleport and sell actions while the cube is carried.
     *
     * @param owner the actor carrying this item
     * @param map the current map
     * @return actions enabled by the alien cube
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();

        for (TeleportDestination destination : getDestinations(owner, map)) {
            actions.add(new TeleportAction(this, destination, false, false));
        }

        actions.add(new SellAction(this));
        return actions;
    }
}