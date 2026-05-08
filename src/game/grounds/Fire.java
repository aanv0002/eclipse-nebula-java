package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.statuses.BurningStatus;

import java.util.List;

/**
 * A fire ground that lasts for a fixed number of turns and can burn any actor
 * standing on it.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class Fire extends Ground implements BurningGround {

    /**
     * The ground that existed before the fire.
     */
    private final Ground previousGround;

    /**
     * Remaining turns before the fire goes out.
     */
    private int remainingTurns;

    /**
     * Whether the fire has just been created this tick.
     */
    private boolean justCreated;

    /**
     * Creates a fire ground on top of an existing ground.
     *
     * @param previousGround the ground beneath the fire
     * @param duration the number of turns the fire lasts
     */
    public Fire(Ground previousGround, int duration) {
        super('^', "Fire");
        this.previousGround = previousGround;
        this.remainingTurns = duration ;
        this.justCreated = true;
    }

    /**
     * Fire does not block entry unless the underlying ground blocks entry.
     *
     * @param actor the actor attempting to enter
     * @return true if the underlying ground allows entry
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return previousGround.canActorEnter(actor);
    }

    /**
     * Applies burning to any actor standing on this tile if they are not already
     * burning, and decreases the fire duration.
     *
     * The first tick after creation is ignored so that newly spawned fire does
     * not immediately affect the current actor.
     *
     * @param location the location of the fire
     */
    @Override
    public void tick(Location location) {
        if (justCreated) {
            justCreated = false;
            return;
        }

        if (location.containsAnActor()) {
            Actor actor = location.getActor();
            List<BurningStatus> burningStatuses = actor.statusesOf(BurningStatus.class);

            if (burningStatuses.isEmpty()) {
                actor.addStatus(new BurningStatus(5));
            }
        }

        remainingTurns--;

        if (remainingTurns <= 0) {
            location.setGround(previousGround);
        }
    }
}