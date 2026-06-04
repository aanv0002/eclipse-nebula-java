package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.inventory.WeightLimitedInventory;

/**
 * A status that severely restricts carrying weight by dissolving suit plating.
 * * @author Shrabonti Majumder
 * @version 1.0
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
        Actor actor = currEntity.asCapability(Actor.class).orElse(null);
        if (actor != null && actor.getInventory() instanceof WeightLimitedInventory) {
            WeightLimitedInventory wli = (WeightLimitedInventory) actor.getInventory();

            if (!applied) {
                originalLimit = wli.getWeightLimit();
                wli.setWeightLimit(30);
                applied = true;
                new Display().println(actor + "'s suit is heavily corroded! Max weight limited to 30 units.");
            }

            remainingTurns--;

            if (remainingTurns <= 0) {
                wli.setWeightLimit(originalLimit);
                new Display().println(actor + "'s suit corrosion has faded. Weight limit safely restored.");
            }
        }
    }

    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}