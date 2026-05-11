package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actors.ActorCapabilities;

/**
 * Undead spawn reaction: nearby creatures permanently increase max health.
 */
public class UndeadSpawnReaction implements SpawnReaction {

    @Override
    public boolean supports(Actor creature) {
        return creature.hasAbility(ActorCapabilities.UNDEAD);
    }

    @Override
    public void apply(Actor creature, Location location) {
        int nearbyCreatureCount = 0;

        for (Exit exit : location.getExits()) {
            Actor adjacentActor = exit.getDestination().getActor();
            if (adjacentActor != null && adjacentActor.hasAbility(ActorCapabilities.CREATURE)) {
                nearbyCreatureCount++;
            }
        }

        if (nearbyCreatureCount > 0) {
            creature.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, nearbyCreatureCount);
            creature.heal(nearbyCreatureCount);
        }
    }
}
