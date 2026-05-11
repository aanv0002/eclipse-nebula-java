package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

/**
 * Parasite spawn reaction: adjacent workers immediately take damage.
 */
public class ParasiteSpawnReaction implements SpawnReaction {

    private static final int SPAWN_DAMAGE = 2;

    @Override
    public boolean supports(Actor creature) {
        return creature.hasAbility(ActorCapabilities.PARASITE);
    }

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
