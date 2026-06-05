package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.quota.QuotaSystem;
import game.statuses.PoisonStatus;
import game.teleport.TeleportService;

import java.util.Random;

/**
 * An alien artifact dropped when an AlienCube is cut with a PlasmaCutter.
 * Can be sold to the Supercomputer for 200 worker credits or deposited
 * for 100 company credits.
 * Selling carries a 50% chance of the worker being poisoned.
 * Depositing teleports the worker to a random valid location on the same map.
 *
 * @author Raima
 * @version 1.0
 */
public class AlienArtifact extends Item implements Sellable, Depositable {

    /**
     * Weight of the alien artifact.
     */
    public static final int WEIGHT = 1;

    /**
     * Sell price of the alien artifact in worker credits.
     */
    public static final int SELL_PRICE = 200;

    /**
     * Company credit value when deposited.
     */
    public static final int DEPOSIT_VALUE = 100;

    /**
     * Poison duration when sold.
     */
    public static final int POISON_TURNS = 5;

    /**
     * Poison damage per turn when sold.
     */
    public static final int POISON_DAMAGE = 1;

    /**
     * Chance of worker being poisoned when sold.
     */
    private static final double POISON_CHANCE = 0.50;

    /**
     * Random number generator for poison chance.
     */
    private final Random random = new Random();

    /**
     * Teleport service for depositing teleportation.
     */
    private final TeleportService teleportService;

    /**
     * Creates an alien artifact.
     */
    public AlienArtifact() {
        super("Alien Artifact", '?');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.teleportService = new TeleportService();
        this.enableAbility(ItemCapabilities.DEPOSITABLE);
    }

    /**
     * Sells the alien artifact to the Supercomputer for 200 worker credits.
     * There is a 50% chance the worker is poisoned due to handling
     * the unstable artifact.
     *
     * @param actor the actor selling the artifact
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message of the sale and any side effects
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(SELL_PRICE);
        String result = actor + " sells the Alien Artifact for "
                + SELL_PRICE + " worker credits.";

        if (random.nextDouble() < POISON_CHANCE) {
            actor.addStatus(new PoisonStatus(POISON_TURNS, POISON_DAMAGE));
            result += " The unstable artifact poisons the worker for "
                    + POISON_TURNS + " turns dealing "
                    + POISON_DAMAGE + " damage per turn!";
        }
        return result;
    }

    /**
     * Returns the sell price of the alien artifact.
     *
     * @return sell price in worker credits
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Returns the menu text fragment for the sell action.
     *
     * @return menu text fragment
     */
    @Override
    public String getSellDescription() {
        return "Alien Artifact (200 worker credits)";
    }

    /**
     * Removes the alien artifact from the actor's inventory after selling.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterSell(Actor actor) {
        actor.getInventory().remove(this);
    }

    /**
     * Deposits the alien artifact to the Supercomputer for 100 company credits.
     * The worker is immediately teleported to a random valid location
     * on the same map to get them back to work faster.
     *
     * @param actor the actor depositing the artifact
     * @param quotaSystem the quota system to add company credits to
     * @param location the location of the actor
     * @return the result message of the deposit and any side effects
     */
    @Override
    public String deposit(Actor actor, QuotaSystem quotaSystem, Location location) {
        quotaSystem.addCompanyCredits(DEPOSIT_VALUE);
        String result = actor + " deposits the Alien Artifact for "
                + DEPOSIT_VALUE + " company credits.";

        Location destination = teleportService.getRandomLocation(location.map(), actor);
        if (destination != null) {
            teleportService.teleport(actor, destination);
            result += " The worker is teleported to a random location on the map!";
        } else {
            result += " No valid teleport destination found!";
        }

        return result;
    }

    /**
     * Returns the company credit value of the alien artifact.
     *
     * @return deposit value in company credits
     */
    @Override
    public int getDepositValue() {
        return DEPOSIT_VALUE;
    }

    /**
     * Returns the menu text fragment for the deposit action.
     *
     * @return menu text fragment
     */
    @Override
    public String getDepositDescription() {
        return "Alien Artifact (100 company credits, teleports worker)";
    }

    /**
     * Removes the alien artifact from the actor's inventory after depositing.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterDeposit(Actor actor) {
        actor.getInventory().remove(this);
    }
}