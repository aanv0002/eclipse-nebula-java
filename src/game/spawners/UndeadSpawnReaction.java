package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actors.ActorCapabilities;

/**
 * Spawn reaction for Undead.
 * For every creature adjacent to the Undead at the moment it spawns, the
 * Undead's maximum health is permanently increased by 1.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class UndeadSpawnReaction implements SpawnReaction {

    /**
     * Returns whether this reaction applies to the spawned creature.
     *
     * @param creature the spawned creature
     * @return true if the creature is an Undead
     */
    @Override
    public boolean supports(Actor creature) {
        return creature.hasAbility(ActorCapabilities.UNDEAD);
    }

    /**
     * Applies the Undead spawn reaction.
     *
     * @param creature the spawned Undead
     * @param location the spawn location
     */
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
