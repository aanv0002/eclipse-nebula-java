package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.items.Flask;

/**
 * Spawns a flask beside the destination magic circle.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class SpawnFlaskEffect implements TeleportEffect {

    /**
     * Applies the magic circle arrival effect by spawning one Flask on an empty
     * tile directly next to the destination circle.
     *
     * @param actor the actor teleporting
     * @param source source location
     * @param destination final destination location
     */
    @Override
    public void apply(Actor actor, Location source, Location destination) {
        for (Exit exit : destination.getExits()) {
            Location adjacent = exit.getDestination();

            if (isEmptySpawnTile(adjacent, actor)) {
                adjacent.addItem(new Flask());
                return;
            }
        }
    }

    /**
     * Returns whether the location is a valid empty tile for spawning a Flask.
     *
     * @param location the location to check
     * @param actor the actor used to test whether the tile can be entered
     * @return true if the tile has no actor, no items, and is enterable
     */
    private boolean isEmptySpawnTile(Location location, Actor actor) {
        return !location.containsAnActor()
                && location.getItems().isEmpty()
                && location.canActorEnter(actor);
    }
}