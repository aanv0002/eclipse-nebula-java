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
 * A strange portable cube that can teleport the holder to three fixed random
 * locations within the current map.
 *
 * The three destinations are generated once for the current map and cached so
 * that the menu options remain consistent while the cube is used on that map.
 * The cube can also be sold, which is handled through SellAction.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 1.1
 */
public class AlienCube extends Item implements Teleportable, Sellable {

    /**
     * Weight of the alien cube.
     */
    public static final int WEIGHT = 1;

    /**
     * Number of random teleport destinations presented by the alien cube.
     */
    private static final int RANDOM_DESTINATION_COUNT = 3;

    /**
     * Cached random destinations for the current map.
     */
    private List<TeleportDestination> fixedDestinations;

    /**
     * The map that the cached destinations currently belong to.
     */
    private GameMap destinationMap;

    /**
     * Creates a portable Alien Cube.
     */
    public AlienCube() {
        super("Alien Cube", '◈');
        this.fixedDestinations = new ArrayList<>();
        this.destinationMap = null;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sells the alien cube to the Supercomputer.
     *
     * @param actor the actor selling the cube
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) { // ADDED: sell method
        wallet.addCredits(getSellPrice());
        return actor + " sells the Alien Cube for " + getSellPrice() + " credits.";
    }

    /**
     * Returns the sell price of the alien cube.
     *
     * @return sell price in credits
     */
    @Override
    public int getSellPrice() { // ADDED: getSellPrice method
        return 10; // placeholder until REQ2 defines the price
    }

    /**
     * Returns the menu text fragment for the sell action.
     *
     * @return menu text fragment
     */
    @Override
    public String getSellDescription() { // ADDED: getSellDescription method
        return "Alien Cube";
    }

    /**
     * Removes the alien cube from the actor's inventory after selling.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterSell(Actor actor) {
        actor.getInventory().remove(this); // ADDED: remove alien cube after selling
    }

    /**
     * Returns three fixed random destinations for the current map.
     *
     * If the cube is used on a different map, or if no destinations have been
     * generated yet, a new set of three random destinations is generated.
     *
     * @param actor the actor using the cube
     * @param map the current map
     * @return fixed random destinations for this map
     */
    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        if (destinationMap != map || fixedDestinations.isEmpty()) {
            generateFixedDestinations(actor, map);
        }

        return fixedDestinations;
    }

    /**
     * Generates and caches three random teleport destinations for the given map.
     *
     * @param actor the actor using the cube
     * @param map the map where random destinations are generated
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
     * Returns the corruption effect applied after using the cube.
     *
     * @return cube corruption effect
     */
    @Override
    public TeleportEffect getEffect() {
        return new CubeCorruptEffect();
    }

    /**
     * Returns the name used in teleport menu descriptions.
     *
     * @return teleport device name
     */
    @Override
    public String getTeleportName() {
        return "the alien cube";
    }

    /**
     * Returns actions available while the cube is carried.
     *
     * This includes one teleport action for each fixed random destination and
     * one sell action.
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