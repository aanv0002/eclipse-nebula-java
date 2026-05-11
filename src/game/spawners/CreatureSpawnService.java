package game.spawners;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Shared service for putting creatures onto the map and applying spawn reactions.
 */
public class CreatureSpawnService {

    private final List<SpawnReaction> spawnReactions;

    public CreatureSpawnService() {
        this.spawnReactions = Arrays.asList(
                new UndeadSpawnReaction(),
                new SlimeSpawnReaction(),
                new ParasiteSpawnReaction()
        );
    }

    public boolean spawnAt(Location location, CreatureSpawner spawner) {
        return spawnAt(location, spawner.spawnCreature());
    }

    public boolean spawnAt(Location location, Actor creature) {
        if (location.containsAnActor() || !location.canActorEnter(creature)) {
            return false;
        }

        try {
            location.addActor(creature);
            applyReactions(creature, location);
            return true;
        } catch (GameEngineException exception) {
            return false;
        }
    }

    public boolean spawnAdjacent(Location origin, Actor creature) {
        for (Exit exit : origin.getExits()) {
            if (spawnAt(exit.getDestination(), creature)) {
                return true;
            }
        }
        return false;
    }

    private void applyReactions(Actor creature, Location location) {
        for (SpawnReaction reaction : spawnReactions) {
            if (reaction.supports(creature)) {
                reaction.apply(creature, location);
            }
        }
    }
}
