package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;

/**
 * A gravity anomaly that pulls nearby actors and loose items.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class GravityAnchor extends Ground {

    /**
     * Number of turns the anchor remains active.
     */
    private static final int DURATION = 5;

    /**
     * Map containing this anchor.
     */
    private final GameMap map;

    /**
     * Ground that existed before the anchor appeared.
     */
    private final Ground previousGround;

    /**
     * Remaining active turns.
     */
    private int remainingTurns;

    /**
     * Creates a gravity anchor.
     *
     * @param map map containing this anchor
     * @param previousGround ground replaced by the anchor
     */
    public GravityAnchor(GameMap map, Ground previousGround) {
        super('֍', "Gravity Anchor");
        this.map = map;
        this.previousGround = previousGround;
        this.remainingTurns = DURATION;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return previousGround.canActorEnter(actor);
    }

    @Override
    public void tick(Location location) {
        pullAdjacentActors(location);
        pullAdjacentItems(location);

        remainingTurns--;

        if (remainingTurns <= 0) {
            location.setGround(previousGround);
        }
    }

    /**
     * Pulls one adjacent actor onto the anchor location.
     *
     * @param anchor anchor location
     */
    private void pullAdjacentActors(Location anchor) {
        if (anchor.containsAnActor()) {
            return;
        }

        for (Exit exit : anchor.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.containsAnActor()) {
                map.moveActor(adjacent.getActor(), anchor);
                return;
            }
        }
    }

    /**
     * Pulls all adjacent loose items onto the anchor.
     *
     * @param anchor anchor location
     */
    private void pullAdjacentItems(Location anchor) {
        for (Exit exit : anchor.getExits()) {
            Location adjacent = exit.getDestination();

            for (Item item : new ArrayList<>(adjacent.getItems())) {
                adjacent.removeItem(item);
                anchor.addItem(item);
            }
        }
    }
}