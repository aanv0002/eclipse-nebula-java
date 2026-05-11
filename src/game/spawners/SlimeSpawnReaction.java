package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

import java.util.ArrayList;

/**
 * Slime spawn reaction: adjacent workers drop all carried items.
 */
public class SlimeSpawnReaction implements SpawnReaction {

    @Override
    public boolean supports(Actor creature) {
        return creature.hasAbility(ActorCapabilities.SLIME);
    }

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
