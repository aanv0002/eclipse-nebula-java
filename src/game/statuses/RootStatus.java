package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status that prevents an actor from choosing movement actions for three turns.
 *
 * @author Tan Hong Sze
 * @version 1.2
 */
public class RootStatus implements Status {

    private int remainingTurns;

    public RootStatus() {
        this.remainingTurns = 4;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        remainingTurns--;
    }

    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}