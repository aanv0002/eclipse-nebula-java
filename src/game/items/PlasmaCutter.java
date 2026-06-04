package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.CutAction;
import game.statuses.BurningStatus;

/**
 * A plasma cutter that can be purchased from the Supercomputer for 50 worker
 * credits. When purchased, the worker takes 5 damage and is burned for 5 turns
 * dealing 1 damage per turn since the cutter is ejected at searing temperatures.
 * While carried, allows the worker to cut AluminiumDoors, Vents and AlienCubes
 * in their inventory.
 *
 * @author Raima
 * @version 1.0
 */
public class PlasmaCutter extends Item implements Purchasable {

    /**
     * Weight of the plasma cutter.
     */
    public static final int WEIGHT = 7;

    /**
     * Buy price of the plasma cutter in worker credits.
     */
    public static final int BUY_PRICE = 50;

    /**
     * Damage dealt to worker on purchase.
     */
    public static final int PURCHASE_DAMAGE = 5;

    /**
     * Burn duration on purchase.
     */
    public static final int BURN_DURATION = 5;

    /**
     * Burn damage per turn on purchase.
     */
    public static final int BURN_DAMAGE = 1;

    /**
     * Creates a plasma cutter.
     */
    public PlasmaCutter() {
        super("Plasma Cutter", '>');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Purchases the plasma cutter from the Supercomputer for 50 worker credits.
     * Deducts 50 credits, adds the cutter to the worker's inventory,
     * deals 5 damage and applies BurningStatus for 5 turns dealing 1 damage
     * per turn since the cutter is ejected at searing temperatures.
     *
     * @param actor the actor buying the plasma cutter
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor
     * @return the result message of the purchase and side effects
     */
    @Override
    public String buy(Actor actor, Wallet wallet, Location location) {
        if (!wallet.hasEnoughCredits(BUY_PRICE)) {
            return actor + " cannot afford the Plasma Cutter. Costs "
                    + BUY_PRICE + " credits but only has "
                    + wallet.getCredits() + " credits.";
        }

        wallet.deductCredits(BUY_PRICE);
        actor.getInventory().add(this);
        actor.hurt(PURCHASE_DAMAGE);
        actor.addStatus(new BurningStatus(BURN_DURATION, BURN_DAMAGE));

        return actor + " purchases the Plasma Cutter for " + BUY_PRICE
                + " worker credits. The searing cutter deals " + PURCHASE_DAMAGE
                + " damage and burns the worker for " + BURN_DURATION + " turns!";
    }

    /**
     * Returns the buy price of the plasma cutter.
     *
     * @return buy price in worker credits
     */
    @Override
    public int getBuyPrice() {
        return BUY_PRICE;
    }

    /**
     * Returns the menu text fragment for the buy action.
     *
     * @return menu text fragment
     */
    @Override
    public String getBuyDescription() {
        return "Plasma Cutter (cuts doors, vents and alien cubes)";
    }

    /**
     * Returns actions available while the plasma cutter is carried.
     * Adds a CutAction for each adjacent Cuttable ground and for each
     * Cuttable item in the worker's inventory.
     *
     * @param owner the actor carrying the plasma cutter
     * @param map the current map
     * @return cut actions enabled by the plasma cutter
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location currentLocation = map.locationOf(owner);

        // ADDED: add CutAction for each adjacent Cuttable ground
        for (Exit exit : currentLocation.getExits()) {
            Location destination = exit.getDestination();
            Cuttable cuttable = destination.getGroundAs(Cuttable.class);
            if (cuttable != null) {
                actions.add(new CutAction(cuttable, exit.getName()));
            }
        }

        // ADDED: add CutAction for each Cuttable item in inventory
        for (Item item : owner.getInventory().getItems()) {
            item.asCapability(Cuttable.class).ifPresent(cuttable ->
                    actions.add(new CutAction(cuttable, ""))
            );
        }

        return actions;
    }
}