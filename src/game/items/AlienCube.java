package game.items;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.TeleportAction;
import game.actors.Undead;
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
 * The cube can also be sold to the Supercomputer for 25 credits. When sold, it
 * spawns an Undead on an empty adjacent tile if possible.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class AlienCube extends Item implements Teleportable, Sellable {

    public static final int WEIGHT = 1;

    public static final int SELL_PRICE = 25;

    private static final int RANDOM_DESTINATION_COUNT = 3;

    private List<TeleportDestination> fixedDestinations;

    private GameMap destinationMap;

    public AlienCube() {
        super("Alien Cube", '◈');
        this.fixedDestinations = new ArrayList<>();
        this.destinationMap = null;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sells the Alien Cube to the Supercomputer and attempts to spawn an Undead
     * beside the seller.
     *
     * @param actor the actor selling the cube
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(getSellPrice());

        Undead undead = new Undead();

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (!adjacent.containsAnActor() && adjacent.canActorEnter(undead)) {
                try {
                    location.map().addActor(undead, adjacent);
                    return actor + " sells the Alien Cube for " + getSellPrice()
                            + " credits. An Undead appears nearby!";
                } catch (GameEngineException exception) {
                    return actor + " sells the Alien Cube for " + getSellPrice()
                            + " credits, but the Undead fails to appear.";
                }
            }
        }

        return actor + " sells the Alien Cube for " + getSellPrice()
                + " credits, but there is no empty adjacent tile for an Undead to spawn.";
    }

    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    @Override
    public String getSellDescription() {
        return "Alien Cube";
    }

    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        if (destinationMap != map || fixedDestinations.isEmpty()) {
            generateFixedDestinations(actor, map);
        }

        return fixedDestinations;
    }

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

    @Override
    public TeleportEffect getEffect() {
        return new CubeCorruptEffect();
    }

    @Override
    public String getTeleportName() {
        return "the alien cube";
    }

    /**
     * Returns teleport actions while the cube is carried.
     *
     * Selling is not added here because selling should only be available through
     * the Supercomputer interaction.
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