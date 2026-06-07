package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.WeightPenaltyTarget;

/**
 * A status that severely restricts carrying weight by dissolving suit plating.
 *
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class CorrodedStatus implements Status {

    private int remainingTurns;
    private boolean applied;
    private int originalLimit;

    public CorrodedStatus(int duration) {
        this.remainingTurns = duration;
        this.applied = false;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        WeightPenaltyTarget target = currEntity.asCapability(WeightPenaltyTarget.class).orElse(null);
        if (target == null) return;

        if (!applied) {
            originalLimit = target.applyWeightPenalty(30);
            applied = true;
            new Display().println(target + "'s suit is heavily corroded! Max weight limited to 30 units.");
        }

        remainingTurns--;

        if (remainingTurns <= 0) {
            target.removeWeightPenalty(originalLimit);
            new Display().println(target + "'s suit corrosion has faded. Weight limit safely restored.");
        }
    }

    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}