package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status that prevents an actor from moving for one turn.
 * The movement blocking logic is handled by ContractedWorker.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class RootStatus implements Status {

    private int remainingTurns;

    /**
     * Creates root status for one turn.
     */
    public RootStatus() {
        this.remainingTurns = 1;
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