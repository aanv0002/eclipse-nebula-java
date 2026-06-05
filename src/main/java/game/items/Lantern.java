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

    public static final int WEIGHT = 7;
    public static final int STARTING_OIL = 10;
    public static final int CREDITS_PER_OIL = 5;

    private static final double LEAK_CHANCE = 0.05;
    private static final double BURN_CHANCE = 0.5;
    private static final double FIRE_SPAWN_CHANCE = 0.25;
    private static final int BURN_DURATION = 3;
    private static final int BURN_DAMAGE = 2;

    private int oil;
    private final Random random = new Random();

    public Lantern() {
        super("Lantern", '&');
        this.oil = STARTING_OIL;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

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

    @Override
    public int getSellPrice() {
        return oil * CREDITS_PER_OIL;
    }

    @Override
    public String getSellDescription() {
        return "Lantern (" + oil + " oil remaining)";
    }

    @Override
    public void removeAfterSell(Actor actor) {
        actor.getInventory().remove(this);
    }

    public void drainOil(int amount) {
        oil = Math.max(0, oil - amount);
    }

    public boolean hasOil() {
        return oil > 0;
    }

    @Override
    public boolean canBeInfected() {
        return hasOil();
    }

    @Override
    public String infect(Actor parasite, Location location) {
        InfectedStatus infectedStatus = new InfectedStatus(new LanternInfectionEffect(this));
        this.addStatus(infectedStatus);
        return infectedStatus.onInfected(location);
    }
}
