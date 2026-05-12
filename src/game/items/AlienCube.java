package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.TeleportAction;
import game.actors.Undead;
import game.spawners.CreatureSpawnService;
import game.teleport.CubeCorruptEffect;
import game.teleport.TeleportDestination;
import game.teleport.TeleportEffect;
import game.teleport.Teleportable;
import game.teleport.TeleportService;

import java.util.ArrayList;
import java.util.List;

/**
 * A strange portable cube that can teleport the holder to three fixed random
 * locations within the current map.
 *
 * The cube can also be sold to the Supercomputer for 25 credits. When sold, it
 * spawns an Undead on an empty adjacent tile if possible.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class AlienCube extends Item implements Teleportable, Sellable {

    /**
     * Weight of the alien cube.
     */
    public static final int WEIGHT = 1;

    /**
     * Sell price of the alien cube.
     */
    public static final int SELL_PRICE = 25;

    /**
     * Number of random teleport destinations shown to the worker.
     */
    private static final int RANDOM_DESTINATION_COUNT = 3;

    /**
     * Fixed random destinations generated for the current map.
     */
    private List<TeleportDestination> fixedDestinations;

    /**
     * The map for which fixed destinations were generated.
     */
    private GameMap destinationMap;

    /**
     * Creates an alien cube.
     */
    public AlienCube() {
        super("Alien Cube", '*');
        this.fixedDestinations = new ArrayList<>();
        this.destinationMap = null;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sells the Alien Cube and attempts to spawn an Undead beside the seller.
     *
     * @param actor the actor selling the cube
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(getSellPrice());

        CreatureSpawnService spawnService = new CreatureSpawnService();
        if (spawnService.spawnAdjacent(location, new Undead())) {
            return actor + " sells the Alien Cube for " + getSellPrice()
                    + " credits. An Undead appears nearby!";
        }

        return actor + " sells the Alien Cube for " + getSellPrice()
                + " credits, but there is no empty adjacent tile for an Undead to spawn.";
    }

    /**
     * Returns the sell price.
     *
     * @return sell price in credits
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Returns the sell description.
     *
     * @return sell description
     */
    @Override
    public String getSellDescription() {
        return "Alien Cube";
    }

    /**
     * Returns the cube's teleport destinations.
     *
     * @param actor the actor teleporting
     * @param map the current map
     * @return teleport destinations
     */
    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        if (destinationMap != map || fixedDestinations.isEmpty()) {
            generateFixedDestinations(actor, map);
        }

        return fixedDestinations;
    }

    /**
     * Generates fixed random destinations for the current map.
     *
     * @param actor the actor teleporting
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
     * Returns the teleport effect used by the cube.
     *
     * @return cube corrupt effect
     */
    @Override
    public TeleportEffect getEffect() {
        return new CubeCorruptEffect();
    }

    /**
     * Returns the teleportable name.
     *
     * @return teleportable name
     */
    @Override
    public String getTeleportName() {
        return "the alien cube";
    }

    /**
     * Returns teleport actions while the cube is carried.
     *
     * @param owner the actor carrying this item
     * @param map the current map
     * @return teleport actions enabled by the alien cube
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();

        for (TeleportDestination destination : getDestinations(owner, map)) {
            actions.add(new TeleportAction(this, destination, false, false));
        }

        return actions;
    }
}
