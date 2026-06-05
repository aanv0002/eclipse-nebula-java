package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * A behaviour that moves the actor toward the closest ground item within 3 tiles
 * and picks it up when standing on the same tile.
 *
 * This behaviour is used by ActiveState. It first checks whether the actor is
 * standing on an item. If so, it picks up the first available item. Otherwise,
 * it uses breadth-first search to find the closest reachable item within 3 tiles
 * and returns the first movement action toward that item.
 *
 * This is one of the two new behaviours for REQ5.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class CollectGroundItemsBehaviour implements Behaviour<Actor, Action> {

    /**
     * Maximum search radius for nearby items.
     */
    private static final int ITEM_SEARCH_RADIUS = 3;

    @Override
    public Action operate(Actor actor, Location location) {
        if (!location.getItems().isEmpty()) {
            Item item = location.getItems().get(0);
            return item.getPickUpAction(actor);
        }

        return getMoveTowardClosestItem(actor, location);
    }

    /**
     * Finds the closest reachable item within 3 tiles and returns the first move
     * toward it.
     *
     * @param actor the actor moving toward the item
     * @param start the actor's current location
     * @return movement action toward the closest item, or null if none exists
     */
    private Action getMoveTowardClosestItem(Actor actor, Location start) {
        Queue<Location> queue = new ArrayDeque<>();
        Set<Location> visited = new HashSet<>();
        Map<Location, Action> firstMoveMap = new HashMap<>();
        Map<Location, Integer> distanceMap = new HashMap<>();

        queue.add(start);
        visited.add(start);
        distanceMap.put(start, 0);

        while (!queue.isEmpty()) {
            Location current = queue.poll();
            int distance = distanceMap.get(current);

            if (distance > 0 && !current.getItems().isEmpty()) {
                return firstMoveMap.get(current);
            }

            if (distance >= ITEM_SEARCH_RADIUS) {
                continue;
            }

            for (Exit exit : current.getExits()) {
                Location destination = exit.getDestination();

                if (visited.contains(destination)) {
                    continue;
                }

                Action moveAction = destination.getMoveAction(actor, exit.getName(), exit.getHotKey());

                if (moveAction == null) {
                    continue;
                }

                visited.add(destination);
                queue.add(destination);
                distanceMap.put(destination, distance + 1);

                if (current.equals(start)) {
                    firstMoveMap.put(destination, moveAction);
                } else {
                    firstMoveMap.put(destination, firstMoveMap.get(current));
                }
            }
        }

        return null;
    }
}