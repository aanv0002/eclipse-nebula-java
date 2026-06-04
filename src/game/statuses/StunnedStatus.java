package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status that skips the affected actor's turn.
 * * @author Shrabonti Majumder
 * @version 1.0
 */
public class StunnedStatus implements Status {

    private int remainingTurns;

    public StunnedStatus(int duration) {
        this.remainingTurns = duration;
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