package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ContractedWorker;

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
        ContractedWorker worker = currEntity.asCapability(ContractedWorker.class).orElse(null);
        if (worker == null) return;

        if (!applied) {
            originalLimit = worker.applyWeightPenalty(30);
            applied = true;
            new Display().println(worker + "'s suit is heavily corroded! Max weight limited to 30 units.");
        }

        remainingTurns--;

        if (remainingTurns <= 0) {
            worker.removeWeightPenalty(originalLimit);
            new Display().println(worker + "'s suit corrosion has faded. Weight limit safely restored.");
        }
    }

    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}