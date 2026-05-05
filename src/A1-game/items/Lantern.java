package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.grounds.BurningGround;
import game.grounds.Fire;

/**
 * An unstable lantern that may leak oil and ignite the ground beneath the actor.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class Lantern extends Item {

    /**
     * Weight of the lantern.
     */
    public static final int WEIGHT = 7;

    /**
     * Starting oil fuel.
     */
    public static final int STARTING_OIL = 10;

    /**
     * Chance to leak each carried turn.
     */
    private static final double LEAK_CHANCE = 0.05; //0.05

    /**
     * Remaining oil.
     */
    private int oil;

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
     * While carried, the lantern has a 5% chance to leak oil and ignite the
     * ground beneath the actor.
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
}