package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;

/**
 * A temporary gravity anomaly created by Gravity Surge Weather.
 *
 * The GravityAnchor stays visible for one round after spawning. On its next
 * tick, it pulls adjacent loose items and one adjacent actor directly onto its
 * location, then restores the previous ground.
 *
 * @author Tan Hong Sze
 * @version 2.6
 */
public class GravityAnchor extends Ground {

    /**
     * Map containing this anchor.
     */
    private final GameMap map;

    /**
     * Ground that existed before the anchor appeared.
     */
    private final Ground previousGround;

    /**
     * Whether the anchor has already stayed visible for one round.
     */
    private boolean hasStabilised;

    /**
     * Creates a gravity anchor.
     *
     * @param map map containing this anchor
     * @param previousGround ground replaced by this anchor
     */
    public GravityAnchor(GameMap map, Ground previousGround) {
        super('֍', "Gravity Anchor");
        this.map = map;
        this.previousGround = previousGround;
        this.hasStabilised = false;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return previousGround.canActorEnter(actor);
    }

    @Override
    public void tick(Location location) {
        if (!hasStabilised) {
            hasStabilised = true;
            return;
        }

        int itemsPulled = pullNearbyItems(location);
        int actorsPulled = pullNearbyActors(location);

        new Display().println("Gravity Anchor at ("
                + location.x() + "," + location.y()
                + ") pulls "
                + itemsPulled + " item(s) and "
                + actorsPulled + " actor(s), then disappears.");

        location.setGround(previousGround);
    }

    /**
     * Pulls all loose items within a 3-tile radius onto the anchor location.
     *
     * This uses Chebyshev distance, meaning diagonal tiles are counted as part
     * of the radius. Any loose item within three tiles of the GravityAnchor is
     * removed from its original location and added to the anchor location.
     *
     * @param anchorLocation location of the GravityAnchor
     * @return number of items pulled
     */
    private int pullNearbyItems(Location anchorLocation) {
        int count = 0;
        int radius = 3;

        int anchorX = anchorLocation.x();
        int anchorY = anchorLocation.y();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);

                if (location == anchorLocation) {
                    continue;
                }

                int distance = Math.max(
                        Math.abs(anchorX - x),
                        Math.abs(anchorY - y)
                );

                if (distance > radius) {
                    continue;
                }

                for (Item item : new ArrayList<>(location.getItems())) {
                    location.removeItem(item);
                    anchorLocation.addItem(item);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Pulls one actor within a 3-tile radius directly onto the anchor location.
     *
     * This uses Chebyshev distance, meaning diagonal tiles are counted as part
     * of the radius. Only one actor can be pulled because a Location can contain
     * at most one actor. The closest valid actor is selected.
     *
     * @param anchorLocation location of the GravityAnchor
     * @return number of actors pulled
     */
    private int pullNearbyActors(Location anchorLocation) {
        if (anchorLocation.containsAnActor()) {
            return 0;
        }

        int radius = 3;
        int anchorX = anchorLocation.x();
        int anchorY = anchorLocation.y();

        Location closestActorLocation = null;
        int closestDistance = Integer.MAX_VALUE;

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);

                if (!location.containsAnActor()) {
                    continue;
                }

                int distance = Math.max(
                        Math.abs(anchorX - x),
                        Math.abs(anchorY - y)
                );

                if (distance > radius) {
                    continue;
                }

                if (distance < closestDistance) {
                    closestActorLocation = location;
                    closestDistance = distance;
                }
            }
        }

        if (closestActorLocation == null) {
            return 0;
        }

        Actor actor = closestActorLocation.getActor();

        if (!anchorLocation.canActorEnter(actor)) {
            return 0;
        }

        map.moveActor(actor, anchorLocation);
        return 1;
    }
}