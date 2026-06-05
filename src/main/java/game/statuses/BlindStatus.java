package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status that marks an actor as blinded for one turn.
 * The forced random movement is handled by ContractedWorker.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class BlindStatus implements Status {

    private int remainingTurns;

    /**
     * Creates blind status for one turn.
     */
    public BlindStatus() {
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