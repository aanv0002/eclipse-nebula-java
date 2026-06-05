package game.spawners;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

import java.util.Arrays;
import java.util.List;

/**
 * A shared service for placing creatures onto the map and applying any spawn
 * reactions required by REQ4.
 *
 * @author Shrabonti Majumder
 * @author Amra Anver
 * @version 1.0
 */
public class CreatureSpawnService {

    /**
     * Reactions that may apply after a creature successfully spawns.
     */
    private final List<SpawnReaction> spawnReactions;

    /**
     * Handles the loot explosion effect when a Scrap Snatcher spawns
     */
    private final LootExplosionService lootExplosionService =
            new LootExplosionService();

    /**
     * Creates the default creature spawn service with all REQ4 spawn reactions.
     */
    public CreatureSpawnService() {
        this.spawnReactions = Arrays.asList(
                new UndeadSpawnReaction(),
                new SlimeSpawnReaction(),
                new ParasiteSpawnReaction()
        );
    }

    /**
     * Attempts to place a specific creature at the given location.
     *
     * @param location the target spawn location
     * @param creature the creature to place
     * @return true if the creature was spawned successfully
     */
    public boolean spawnAt(Location location, Actor creature) {
        if (location.containsAnActor() || !location.canActorEnter(creature)) {
            return false;
        }

        try {
            location.addActor(creature);
            if (creature.hasAbility(ActorCapabilities.SCRAP_SNATCHER)) {
                lootExplosionService.explode(location);
            }
            applyReactions(creature, location);
            return true;
        } catch (GameEngineException exception) {
            return false;
        }
    }

    /**
     * Attempts to spawn a creature on one of the empty tiles adjacent to an origin.
     *
     * @param origin the origin location
     * @param creature the creature to spawn
     * @return true if the creature was spawned successfully
     */
    public boolean spawnAdjacent(Location origin, Actor creature) {
        for (Exit exit : origin.getExits()) {
            if (spawnAt(exit.getDestination(), creature)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies all spawn reactions that support the spawned creature.
     *
     * @param creature the spawned creature
     * @param location the spawn location
     */
    private void applyReactions(Actor creature, Location location) {
        for (SpawnReaction reaction : spawnReactions) {
            if (reaction.supports(creature)) {
                reaction.apply(creature, location);
            }
        }
    }
}
