package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

/**
 * Spawn reaction for Parasite.
 * When a Parasite spawns, all adjacent workers immediately take 2 damage.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class ParasiteSpawnReaction implements SpawnReaction {

    /**
     * Damage dealt to adjacent workers when a Parasite spawns.
     */
    private static final int SPAWN_DAMAGE = 2;

    /**
     * Returns whether this reaction applies to the spawned creature.
     *
     * @param creature the spawned creature
     * @return true if the creature is a Parasite
     */
    @Override
    public boolean supports(Actor creature) {
        return creature.hasAbility(ActorCapabilities.PARASITE);
    }

    /**
     * Applies the Parasite spawn reaction.
     *
     * @param creature the spawned Parasite
     * @param location the spawn location
     */
    @Override
    public void apply(Actor creature, Location location) {
        for (Exit exit : location.getExits()) {
            Actor actor = exit.getDestination().getActor();
            if (actor != null && actor.hasAbility(ActorCapabilities.WORKER)) {
                actor.hurt(SPAWN_DAMAGE);
            }
        }
    }
}
