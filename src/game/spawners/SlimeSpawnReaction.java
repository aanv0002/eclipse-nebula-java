package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

import java.util.ArrayList;

/**
 * Spawn reaction for Slime.
 * When a Slime spawns, all adjacent workers drop every item they are carrying.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class SlimeSpawnReaction implements SpawnReaction {

    /**
     * Returns whether this reaction applies to the spawned creature.
     *
     * @param creature the spawned creature
     * @return true if the creature is a Slime
     */
    @Override
    public boolean supports(Actor creature) {
        return creature.hasAbility(ActorCapabilities.SLIME);
    }

    /**
     * Applies the Slime spawn reaction.
     *
     * @param creature the spawned Slime
     * @param location the spawn location
     */
    @Override
    public void apply(Actor creature, Location location) {
        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            Actor actor = adjacent.getActor();

            if (actor != null && actor.hasAbility(ActorCapabilities.WORKER)) {
                for (Item item : new ArrayList<>(actor.getInventory().getItems())) {
                    actor.getInventory().remove(item);
                    adjacent.addItem(item);
                }
            }
        }
    }
}
