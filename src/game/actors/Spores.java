package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.inventory.BasicInventory;
import game.items.Moldable;

import java.util.*;

/**
 * A spore actor that searches for and mutates nearby moldable organic items.
 *
 * Spores first check their current and adjacent tiles for moldable food.
 * If food is nearby, they mutate it and disappear. If no moldable food is
 * nearby, they move toward the nearest reachable moldable item.
 *
 * @author Tan Hong Sze
 * @version 2.1
 */
public class Spores extends Actor {

    /**
     * Counter used to give each spore a unique numbered name.
     */
    private static int nextId = 1;

    /**
     * Creates spores with a unique numbered name.
     */
    public Spores() {
        super("#" + nextId + " Spores", '웃', 1, new BasicInventory());
        nextId++;
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location current = map.locationOf(this);

        MutationResult result = mutateCurrentOrAdjacent(current);

        if (result.mutated()) {
            display.println(this + " mutates "
                    + result.originalName()
                    + " into "
                    + result.moldedName()
                    + " and disappears.");
            this.unconscious(map);
            return new DoNothingAction();
        }

        Action moveAction = getMoveTowardNearestMoldable(current);

        if (moveAction != null) {
            display.println(this + " follows the scent of moldable food.");
            return moveAction;
        }

        display.println(this + " cannot find any moldable food nearby.");
        return new DoNothingAction();
    }

    /**
     * Attempts to mutate a moldable item on the current tile or adjacent tiles.
     *
     * @param current current location
     * @return mutation result
     */
    private MutationResult mutateCurrentOrAdjacent(Location current) {
        MutationResult currentResult = mutateFirstItemAt(current);

        if (currentResult.mutated()) {
            return currentResult;
        }

        for (Exit exit : current.getExits()) {
            MutationResult adjacentResult = mutateFirstItemAt(exit.getDestination());

            if (adjacentResult.mutated()) {
                return adjacentResult;
            }
        }

        return MutationResult.none();
    }

    /**
     * Mutates the first moldable item at one location.
     *
     * @param location location to scan
     * @return mutation result
     */
    private MutationResult mutateFirstItemAt(Location location) {
        List<Moldable> moldables =
                new ArrayList<>(location.getItemsAs(Moldable.class));

        if (moldables.isEmpty()) {
            return MutationResult.none();
        }

        Moldable moldable = moldables.get(0);

        String originalName = moldable.asItem().toString();
        String moldedName = moldable.createMoldedVersion().toString();

        location.removeItem(moldable.asItem());
        location.addItem(moldable.createMoldedVersion());

        return new MutationResult(true, originalName, moldedName);
    }

    /**
     * Finds a movement action toward the nearest reachable moldable item.
     *
     * @param start current spore location
     * @return movement action, or null if no reachable moldable item exists
     */
    private Action getMoveTowardNearestMoldable(Location start) {
        Queue<Location> queue = new ArrayDeque<>();
        Map<Location, Action> firstMoveMap = new HashMap<>();
        Set<Location> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (!current.equals(start) && hasMoldableItem(current)) {
                return firstMoveMap.get(current);
            }

            for (Exit exit : current.getExits()) {
                Location destination = exit.getDestination();

                if (visited.contains(destination)) {
                    continue;
                }

                Action moveAction = destination.getMoveAction(this, exit.getName(), exit.getHotKey());

                if (moveAction == null) {
                    continue;
                }

                visited.add(destination);
                queue.add(destination);

                if (current.equals(start)) {
                    firstMoveMap.put(destination, moveAction);
                } else {
                    firstMoveMap.put(destination, firstMoveMap.get(current));
                }
            }
        }

        return null;
    }

    /**
     * Checks whether a location contains moldable organic items.
     *
     * @param location location to inspect
     * @return true if moldable items exist
     */
    private boolean hasMoldableItem(Location location) {
        return !location.getItemsAs(Moldable.class).isEmpty();
    }

    /**
     * Stores mutation result details for display output.
     *
     * @param mutated whether a mutation occurred
     * @param originalName original item name
     * @param moldedName molded item name
     */
    private record MutationResult(boolean mutated, String originalName, String moldedName) {

        /**
         * Creates an empty mutation result.
         *
         * @return no-mutation result
         */
        private static MutationResult none() {
            return new MutationResult(false, "", "");
        }
    }
}