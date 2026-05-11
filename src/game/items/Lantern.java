package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.grounds.BurningGround;
import game.grounds.Fire;
import game.infection.Infectable;
import game.infection.LanternInfectionEffect;
import game.statuses.BurningStatus;
import game.statuses.InfectedStatus;

import java.util.Random;

/**
 * An unstable lantern that may leak oil and ignite the ground beneath the actor.
 * Can be sold to the Supercomputer for 5 credits per unit of oil remaining,
 * maxing out at 50 credits for a full lantern.
 * When sold, there is a 50% chance the lantern burns the seller for 2 damage
 * per turn for 3 turns. There is also an independent 25% chance it spawns
 * Fire on all surrounding tiles.
 *
 * The Lantern can also be infected by a Parasite. An infected Lantern loses
 * 1 oil every turn until it runs out.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class Lantern extends Item implements Sellable, Infectable {

    /**
     * Weight of the lantern.
     */
    public static final int WEIGHT = 7;

    /**
     * Starting oil fuel.
     */
    public static final int STARTING_OIL = 10;

    /**
     * Credits earned per unit of oil remaining when sold.
     */
    public static final int CREDITS_PER_OIL = 5;

    /**
     * Chance to leak each carried turn.
     */
    private static final double LEAK_CHANCE = 0.05;

    /**
     * Chance to burn the seller when sold.
     */
    private static final double BURN_CHANCE = 0.5;

    /**
     * Chance to spawn fire on surrounding tiles when sold.
     */
    private static final double FIRE_SPAWN_CHANCE = 0.25;

    /**
     * Number of turns the seller burns when sold.
     */
    private static final int BURN_DURATION = 3;

    /**
     * Damage dealt per turn when seller burns after selling.
     */
    private static final int BURN_DAMAGE = 2;

    /**
     * Remaining oil.
     */
    private int oil;

    /**
     * Random number generator for chance calculations.
     */
    private final Random random = new Random();

    /**
     * Creates a lantern.
     */
    public Lantern() {
        super("Lantern", '&');
        this.oil = STARTING_OIL;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * While carried, the lantern has a 5% chance to leak oil and ignite
     * the ground beneath the actor.
     *
     * @param currentLocation the location of the owner
     * @param actor the actor carrying the item
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (oil <= 0) {
            return;
        }

        if (Math.random() < LEAK_CHANCE) {
            if (currentLocation.getGroundAs(BurningGround.class) == null) {
                currentLocation.setGround(new Fire(currentLocation.getGround(), 5));
            }
            oil--;
        }
    }

    /**
     * Sells the lantern to the Supercomputer.
     *
     * @param actor the actor selling the lantern
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        int creditsEarned = oil * CREDITS_PER_OIL;
        wallet.addCredits(creditsEarned);

        String result = actor + " sells the lantern for " + creditsEarned + " credits.";

        if (random.nextDouble() < BURN_CHANCE) {
            actor.addStatus(new BurningStatus(BURN_DURATION, BURN_DAMAGE));
            result += " The lantern burns the seller for " + BURN_DAMAGE
                    + " damage per turn for " + BURN_DURATION + " turns!";
        }

        if (random.nextDouble() < FIRE_SPAWN_CHANCE) {
            for (Exit exit : location.getExits()) {
                Location surroundingLocation = exit.getDestination();
                if (surroundingLocation.getGroundAs(BurningGround.class) == null) {
                    surroundingLocation.setGround(new Fire(surroundingLocation.getGround(), 5));
                }
            }
            result += " Fire spawns on all surrounding tiles!";
        }

        return result;
    }

    /**
     * Returns the sell price of the lantern based on remaining oil.
     *
     * @return sell price in credits
     */
    @Override
    public int getSellPrice() {
        return oil * CREDITS_PER_OIL;
    }

    /**
     * Returns the menu text fragment for the sell action.
     *
     * @return menu text fragment
     */
    @Override
    public String getSellDescription() {
        return "Lantern (" + oil + " oil remaining)";
    }

    /**
     * Drains lantern oil.
     *
     * @param amount oil amount to drain
     */
    public void drainOil(int amount) {
        oil = Math.max(0, oil - amount);
    }

    /**
     * Returns whether the lantern has oil.
     *
     * @return true if oil remains
     */
    public boolean hasOil() {
        return oil > 0;
    }

    /**
     * Returns whether this Lantern can be infected.
     *
     * @return true if oil remains and no infection is active
     */
    @Override
    public boolean canBeInfected() {
        return hasOil() && !this.hasStatus(InfectedStatus.class);
    }

    /**
     * Applies Parasite infection to the Lantern.
     *
     * @param parasite the Parasite applying infection
     * @param location the Lantern's location
     * @return infection result message
     */
    @Override
    public String infect(Actor parasite, Location location) {
        InfectedStatus infectedStatus = new InfectedStatus(new LanternInfectionEffect(this));
        this.addStatus(infectedStatus);
        return infectedStatus.onInfected(location);
    }
}
