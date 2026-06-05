package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Spores;

/**
 * A fungal nest that incubates for a fixed number of turns before releasing one Spores actor.
 *
 * The nest displays a countdown every turn. When it says 1 turn remaining,
 * it does not release immediately in the same tick. It releases on the next tick.
 *
 * @author Tan Hong Sze
 * @version 2.3
 */
public class SporeNest extends Ground {

    private static final int INCUBATION_TURNS = 3;

    private final GameMap map;
    private final Ground previousGround;
    private int remainingTurns;

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
        if (remainingTurns > 0) {
            new Display().println("Spore Nest at ("
                    + location.x() + "," + location.y()
                    + ") is incubating and will release Spores in "
                    + remainingTurns + " turn(s).");

            remainingTurns--;
            return;
        }

        releaseSpores(location);
    }

    /**
     * Releases exactly one Spores actor and restores the previous ground.
     *
     * @param location nest location
     */
    private void releaseSpores(Location location) {
        if (!location.containsAnActor()) {
            try {
                map.addActor(new Spores(), location);
                new Display().println("Spore Nest at ("
                        + location.x() + "," + location.y()
                        + ") releases 1 Spores.");
            } catch (GameEngineException ignored) {
                new Display().println("Spore Nest at ("
                        + location.x() + "," + location.y()
                        + ") tries to release Spores, but the tile is blocked.");
            }
        }

        location.setGround(previousGround);
    }
}