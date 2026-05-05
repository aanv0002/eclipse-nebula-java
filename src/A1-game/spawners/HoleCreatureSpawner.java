package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.actors.Slime;
import game.actors.Undead;

import java.util.Random;

/**
 * Spawns either an Undead or a Slime with equal probability.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class HoleCreatureSpawner implements CreatureSpawner {

    /**
     * Random generator.
     */
    private final Random random = new Random();

    /**
     * Spawns either an Undead or a Slime.
     *
     * @return the spawned creature
     */
    @Override
    public Actor spawnCreature() {
        if (random.nextBoolean()) {
            return new Undead();
        }
        return new Slime();
    }
}