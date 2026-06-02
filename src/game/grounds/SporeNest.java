package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Spores;

/**
 * A fungal nest that incubates before releasing spores.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class SporeNest extends Ground {

    private static final int INCUBATION_TURNS = 3;
    private static final int ACCELERATION_AMOUNT = 2;

    private final GameMap map;
    private final Ground previousGround;
    private int remainingTurns;

    /**
     * Creates a spore nest.
     *
     * @param map map containing this nest
     * @param previousGround previous ground
     */
    public SporeNest(GameMap map, Ground previousGround) {
        super('Ⓢ', "Spore Nest");
        this.map = map;
        this.previousGround = previousGround;
        this.remainingTurns = INCUBATION_TURNS;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return previousGround.canActorEnter(actor);
    }

    @Override
    public void tick(Location location) {
        remainingTurns--;

        if (remainingTurns <= 0) {
            releaseSpores(location);
        }
    }

    /**
     * Accelerates incubation. If the nest is ready, spores are released.
     *
     * @param location nest location
     * @return true if spores were released
     */
    public boolean accelerateIncubation(Location location) {
        remainingTurns -= ACCELERATION_AMOUNT;

        if (remainingTurns <= 0) {
            releaseSpores(location);
            return true;
        }

        return false;
    }

    /**
     * Releases spores and restores the previous ground.
     *
     * @param location nest location
     */
    private void releaseSpores(Location location) {
        if (!location.containsAnActor()) {
            try {
                map.addActor(new Spores(), location);
            } catch (GameEngineException ignored) {
                // If spawning fails, the nest still disappears.
            }
        }

        location.setGround(previousGround);
    }
}